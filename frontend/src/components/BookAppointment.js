import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';

function BookAppointment() {
  const location = useLocation();
  const navigate = useNavigate();
  const selectedDoctor = location.state?.doctor;
  const userEmail = localStorage.getItem('userEmail');
  const [error, setError] = useState('');

  useEffect(() => {
    if (!userEmail) {
      setError('Please log in to book an appointment');
      return;
    }

    if (!selectedDoctor) {
      setError('Please select a doctor first');
      return;
    }
  }, [selectedDoctor, userEmail]);

  const [formData, setFormData] = useState({
    docId: selectedDoctor?.email || '',
    docName: selectedDoctor?.name || '',
    docSpecial: selectedDoctor?.specialization || '',
    email: userEmail || '',
    date: '',
    time: '',
    symptoms: '',
    status: 'Active'
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (!selectedDoctor) {
        setError('No doctor selected. Please go back and select a doctor first.');
        return;
      }

      if (!userEmail) {
        setError('Please log in to book an appointment');
        return;
      }

      console.log('Sending appointment data:', formData);
      const response = await axios.post('http://localhost:8080/assignment', formData);
      console.log('Appointment response:', response.data);
      navigate('/appointments');
    } catch (err) {
      console.error('Appointment error:', err.response?.data || err.message);
      setError(err.response?.data || 'Failed to book appointment. Please try again.');
    }
  };

  if (!userEmail) {
    return (
      <div className="container mt-4">
        <div className="alert alert-warning">
          Please <a href="/login">log in</a> to book an appointment.
        </div>
      </div>
    );
  }

  if (!selectedDoctor) {
    return (
      <div className="container mt-4">
        <div className="alert alert-warning">
          Please <a href="/doctors">select a doctor</a> first to book an appointment.
        </div>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-body">
          <h2 className="card-title mb-4">Book Appointment</h2>
          {error && <div className="alert alert-danger">{error}</div>}
          
          <div className="doctor-info mb-4">
            <h5>Selected Doctor</h5>
            <div className="card">
              <div className="card-body">
                <p><strong>Name:</strong> {selectedDoctor.name}</p>
                <p><strong>Specialization:</strong> {selectedDoctor.specialization}</p>
                <p><strong>Experience:</strong> {selectedDoctor.experience} years</p>
                <p><strong>Qualification:</strong> {selectedDoctor.qualification}</p>
              </div>
            </div>
          </div>

          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="date" className="form-label">
                Appointment Date
              </label>
              <input
                type="date"
                className="form-control"
                id="date"
                name="date"
                value={formData.date}
                onChange={handleChange}
                min={new Date().toISOString().split('T')[0]}
                required
              />
            </div>

            <div className="mb-3">
              <label htmlFor="time" className="form-label">
                Preferred Time
              </label>
              <input
                type="time"
                className="form-control"
                id="time"
                name="time"
                value={formData.time}
                onChange={handleChange}
                required
              />
            </div>

            <div className="mb-3">
              <label htmlFor="symptoms" className="form-label">
                Symptoms or Reason for Visit
              </label>
              <textarea
                className="form-control"
                id="symptoms"
                name="symptoms"
                value={formData.symptoms}
                onChange={handleChange}
                rows="3"
                placeholder="Please describe your symptoms or reason for visit"
                required
              />
            </div>

            <button type="submit" className="btn btn-primary w-100">
              Book Appointment
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default BookAppointment;
