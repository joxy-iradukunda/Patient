import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Appointments() {
  const [appointments, setAppointments] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const userEmail = localStorage.getItem('userEmail');

  useEffect(() => {
    if (!userEmail) {
      console.log('No user email found, redirecting to login');
      navigate('/login');
      return;
    }

    const fetchAppointments = async () => {
      try {
        setLoading(true);
        setError('');
        console.log('Fetching appointments for user:', userEmail);
        
        const response = await axios.get('http://localhost:8080/display', {
          withCredentials: true
        });
        
        console.log('Appointments response:', response.data);
        
        if (response.data && Array.isArray(response.data)) {
          setAppointments(response.data);
          console.log('Found appointments:', response.data.length);
        } else {
          console.log('No appointments found in response');
          setAppointments([]);
        }
      } catch (err) {
        console.error('Error details:', {
          message: err.message,
          response: err.response?.data,
          status: err.response?.status
        });
        
        if (err.response?.status === 401) {
          console.log('Session expired, redirecting to login');
          localStorage.removeItem('userEmail');
          localStorage.removeItem('userName');
          navigate('/login');
          return;
        }
        
        setError('Failed to fetch appointments. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchAppointments();
  }, [userEmail, navigate]);

  const handleCancel = async (appId) => {
    try {
      setError('');
      console.log('Canceling appointment:', appId);
      
      const response = await axios.post('http://localhost:8080/cancel', 
        { appId: appId },
        { withCredentials: true }
      );
      
      console.log('Cancel response:', response.data);
      
      // Refresh appointments after cancellation
      const updatedResponse = await axios.get('http://localhost:8080/display', {
        withCredentials: true
      });
      
      if (updatedResponse.data && Array.isArray(updatedResponse.data)) {
        setAppointments(updatedResponse.data);
      }
    } catch (err) {
      console.error('Error canceling appointment:', {
        message: err.message,
        response: err.response?.data,
        status: err.response?.status
      });
      
      if (err.response?.status === 401) {
        console.log('Session expired, redirecting to login');
        localStorage.removeItem('userEmail');
        localStorage.removeItem('userName');
        navigate('/login');
        return;
      }
      
      setError('Failed to cancel appointment. Please try again.');
    }
  };

  if (!userEmail) {
    return (
      <div className="container mt-4">
        <div className="alert alert-warning">
          Please <a href="/login">log in</a> to view your appointments.
        </div>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="container mt-4">
        <div className="text-center">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-body">
          <h2 className="card-title mb-4">My Appointments</h2>
          {error && <div className="alert alert-danger">{error}</div>}
          
          {appointments.length === 0 ? (
            <div className="alert alert-info">
              No appointments found. <a href="/doctors">Book an appointment</a>
            </div>
          ) : (
            <div className="appointment-list">
              {appointments.map((appointment) => (
                <div key={appointment.appId} className="card mb-3">
                  <div className="card-body">
                    <div className="row align-items-center">
                      <div className="col-md-8">
                        <h5 className="card-title">Doctor: {appointment.docName}</h5>
                        <p className="card-text mb-1">
                          <strong>Specialization:</strong> {appointment.docSpecial}
                        </p>
                        <p className="card-text mb-1">
                          <strong>Date:</strong> {appointment.date}
                        </p>
                        <p className="card-text mb-1">
                          <strong>Time:</strong> {appointment.time}
                        </p>
                        <p className="card-text mb-1">
                          <strong>Status:</strong>{' '}
                          <span className={`badge ${
                            appointment.status === 'Active' ? 'bg-success' : 'bg-secondary'
                          }`}>
                            {appointment.status}
                          </span>
                        </p>
                        {appointment.symptoms && (
                          <p className="card-text mb-1">
                            <strong>Symptoms:</strong> {appointment.symptoms}
                          </p>
                        )}
                      </div>
                      <div className="col-md-4 text-end">
                        {appointment.status === 'Active' && (
                          <button
                            className="btn btn-danger"
                            onClick={() => handleCancel(appointment.appId)}
                          >
                            Cancel Appointment
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Appointments;
