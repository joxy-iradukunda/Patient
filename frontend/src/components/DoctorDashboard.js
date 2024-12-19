import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function DoctorDashboard() {
  const [appointments, setAppointments] = useState([]);
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const doctorEmail = localStorage.getItem('doctorEmail');

  useEffect(() => {
    if (!localStorage.getItem('doctorToken')) {
      navigate('/doctor-login');
      return;
    }
    fetchAppointments();
  }, [navigate]);

  const fetchAppointments = async () => {
    try {
      const response = await axios.get('http://localhost:8080/patientlist');
      setAppointments(response.data.appointments || []);
    } catch (err) {
      setError('Failed to fetch appointments');
    }
  };

  const handleStatusUpdate = async (appointmentId, newStatus) => {
    try {
      await axios.post(`http://localhost:8080/updateAppointment`, {
        appId: appointmentId,
        status: newStatus
      });
      fetchAppointments();
    } catch (err) {
      setError('Failed to update appointment status');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('doctorToken');
    localStorage.removeItem('doctorEmail');
    navigate('/doctor-login');
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
