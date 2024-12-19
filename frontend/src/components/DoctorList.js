import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

function DoctorList() {
  const [doctors, setDoctors] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchDoctors = async () => {
      try {
        const response = await axios.get('http://localhost:8080/docdetails');
        // The response is now directly the array of doctors
        setDoctors(response.data || []);
      } catch (err) {
        console.error('Error fetching doctors:', err);
        setError('Failed to fetch doctors. Please try again later.');
      }
    };

    fetchDoctors();
  }, []);

  if (doctors.length === 0 && !error) {
    return (
      <div className="container">
        <h2 className="my-4">Available Doctors</h2>
        <div className="alert alert-info">No doctors available at the moment.</div>
      </div>
    );
  }

  return (
    <div className="container">
      <h2 className="my-4">Available Doctors</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="row">
        {doctors.map((doctor) => (
          <div key={doctor.email} className="col-md-4 mb-4">
            <div className="card doctor-card">
              <div className="card-body">
                <h5 className="card-title">{doctor.name}</h5>
                <p className="card-text">
                  <strong>Specialization:</strong> {doctor.specialization}
                </p>
                <p className="card-text">
                  <strong>Experience:</strong> {doctor.experience} years
                </p>
                <p className="card-text">
                  <strong>Qualification:</strong> {doctor.qualification}
                </p>
                <Link
                  to="/book-appointment"
                  state={{ doctor }}
                  className="btn btn-primary"
                >
                  Book Appointment
                </Link>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default DoctorList;
