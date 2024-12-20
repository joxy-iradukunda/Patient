import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function DoctorDashboard() {
  const [appointments, setAppointments] = useState([]);
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const doctorEmail = localStorage.getItem('doctorEmail');

  useEffect(() => {
    const checkAuth = async () => {
      const token = localStorage.getItem('doctorToken');
      const email = localStorage.getItem('doctorEmail');
      
      if (!token || !email) {
        navigate('/doctor-login');
        return;
      }

      try {
        // Verify the session is still valid
        const response = await axios.get('http://localhost:8080/doctor-details', {
          withCredentials: true
        });
        
        if (!response.data || !response.data.email) {
          throw new Error('Session expired');
        }
        
        fetchAppointments();
      } catch (err) {
        console.error('Auth check failed:', err);
        localStorage.removeItem('doctorToken');
        localStorage.removeItem('doctorEmail');
        navigate('/doctor-login');
      }
    };

    checkAuth();
  }, [navigate]);

  const fetchAppointments = async () => {
    try {
      const response = await axios.get('http://localhost:8080/patientlist', {
        withCredentials: true,
        headers: {
          'Cache-Control': 'no-cache'
        }
      });
      
      console.log('Appointments response:', response.data);
      
      if (!response.data) {
        setAppointments([]);
        return;
      }

      setAppointments(Array.isArray(response.data) ? response.data : []);
    } catch (err) {
      console.error('Error fetching appointments:', err.response?.data || err.message);
      if (err.response?.status === 401 || err.response?.data === 'Doctor not logged in') {
        navigate('/doctor-login');
      } else {
        setError(err.response?.data || 'Failed to fetch appointments');
      }
    }
  };

  const handleStatusUpdate = async (appointmentId, newStatus) => {
    try {
      await axios.post(`http://localhost:8080/update-appointment`, {
        appId: appointmentId,
        status: newStatus
      }, {
        withCredentials: true
      });
      fetchAppointments();
    } catch (err) {
      console.error('Error updating status:', err.response?.data || err.message);
      setError(err.response?.data || 'Failed to update appointment status');
    }
  };

  const handleLogout = async () => {
    try {
      await axios.post('http://localhost:8080/logout', {}, { withCredentials: true });
    } catch (err) {
      console.error('Error during logout:', err);
    } finally {
      localStorage.removeItem('doctorToken');
      localStorage.removeItem('doctorEmail');
      navigate('/doctor-login');
    }
  };

  return (
    <div className="container">
      <div className="d-flex justify-content-between align-items-center my-4">
        <h2>Doctor Dashboard</h2>
        <button className="btn btn-danger" onClick={handleLogout}>
          Logout
        </button>
      </div>
      
      {error && <div className="alert alert-danger">{error}</div>}
      
      <div className="appointment-list">
        <h3 className="mb-4">Your Appointments</h3>
        {appointments.length === 0 ? (
          <p>No appointments found.</p>
        ) : (
          appointments.map((appointment) => (
            <div key={appointment.appId} className="appointment-item">
              <div className="row align-items-center">
                <div className="col-md-8">
                  <h5>Patient: {appointment.email}</h5>
                  <p className="mb-1">
                    <strong>Date:</strong> {appointment.date}
                  </p>
                  <p className="mb-1">
                    <strong>Time:</strong> {appointment.time}
                  </p>
                  <p className="mb-1">
                    <strong>Symptoms:</strong> {appointment.symptoms}
                  </p>
                  <p className="mb-1">
                    <strong>Status:</strong>{' '}
                    <span className={`badge ${
                      appointment.status === 'Active' ? 'bg-success' : 'bg-secondary'
                    }`}>
                      {appointment.status}
                    </span>
                  </p>
                </div>
                <div className="col-md-4 text-end">
                  <div className="btn-group">
                    <button
                      className="btn btn-success"
                      onClick={() => handleStatusUpdate(appointment.appId, 'Completed')}
                      disabled={appointment.status !== 'Active'}
                    >
                      Mark Complete
                    </button>
                    <button
                      className="btn btn-danger"
                      onClick={() => handleStatusUpdate(appointment.appId, 'Cancelled')}
                      disabled={appointment.status !== 'Active'}
                    >
                      Cancel
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default DoctorDashboard;
