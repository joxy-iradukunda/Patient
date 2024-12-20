import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function DoctorRegister() {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    name: '',
    specialization: '',
    experience: '',
    qualification: '',
    age: '',
    gender: ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      // Validate required fields
      if (!formData.email || !formData.email.includes('@')) {
        setError('Please enter a valid email address');
        return;
      }
      if (!formData.password || formData.password.length < 6) {
        setError('Password must be at least 6 characters long');
        return;
      }
      if (!formData.name.trim()) {
        setError('Name is required');
        return;
      }
      if (!formData.specialization) {
        setError('Please select a specialization');
        return;
      }
      if (!formData.qualification.trim()) {
        setError('Qualification is required');
        return;
      }
      if (!formData.age || parseInt(formData.age) < 1 || parseInt(formData.age) > 120) {
        setError('Please enter a valid age between 1 and 120');
        return;
      }
      if (!formData.gender) {
        setError('Please select your gender');
        return;
      }

      const response = await axios.post('http://localhost:8080/registereddoc', {
        ...formData,
        email: formData.email.toLowerCase().trim(),
        name: formData.name.trim(),
        qualification: formData.qualification.trim(),
        gender: formData.gender.toLowerCase()
      });
      
      console.log('Registration response:', response);
      if (response.data && response.data.email) {
        navigate('/doctor-login');
      } else {
        setError('Registration failed. Please try again.');
      }
    } catch (err) {
      console.error('Registration error:', err);
      const errorMessage = err.response?.data?.message || 
                         err.response?.data?.error || 
                         err.response?.data || 
                         'Registration failed. Please try again.';
      setError(typeof errorMessage === 'string' ? errorMessage : 'Registration failed. Please try again.');
    }
  };

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
              <h2 className="text-center mb-4">Doctor Registration</h2>
              {error && <div className="alert alert-danger">{error}</div>}
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label htmlFor="email" className="form-label">Email</label>
                  <input
                    type="email"
                    className="form-control"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="mb-3">
                  <label htmlFor="password" className="form-label">Password</label>
                  <input
                    type="password"
                    className="form-control"
                    id="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    minLength="6"
                    required
                  />
                </div>
                <div className="mb-3">
                  <label htmlFor="name" className="form-label">Full Name</label>
                  <input
                    type="text"
                    className="form-control"
                    id="name"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="mb-3">
                  <label htmlFor="age" className="form-label">Age</label>
                  <input
                    type="number"
                    className="form-control"
                    id="age"
                    name="age"
                    value={formData.age}
                    onChange={handleChange}
                    min="1"
                    max="120"
                    required
                  />
                </div>
                <div className="mb-3">
                  <label htmlFor="gender" className="form-label">Gender</label>
                  <select
                    className="form-control"
                    id="gender"
                    name="gender"
                    value={formData.gender}
                    onChange={handleChange}
                    required
                  >
                    <option value="">Select Gender</option>
                    <option value="male">Male</option>
                    <option value="female">Female</option>
                    <option value="other">Other</option>
                  </select>
                </div>
                <div className="mb-3">
                  <label htmlFor="specialization" className="form-label">Specialization</label>
                  <select
                    className="form-control"
                    id="specialization"
                    name="specialization"
                    value={formData.specialization}
                    onChange={handleChange}
                    required
                  >
                    <option value="">Select Specialization</option>
                    <option value="Cardiology">Cardiology</option>
                    <option value="Dermatology">Dermatology</option>
                    <option value="Endocrinology">Endocrinology</option>
                    <option value="Gastroenterology">Gastroenterology</option>
                    <option value="Neurology">Neurology</option>
                    <option value="Oncology">Oncology</option>
                    <option value="Pediatrics">Pediatrics</option>
                    <option value="Psychiatry">Psychiatry</option>
                    <option value="General Medicine">General Medicine</option>
                  </select>
                </div>
                <div className="mb-3">
                  <label htmlFor="qualification" className="form-label">Qualification</label>
                  <input
                    type="text"
                    className="form-control"
                    id="qualification"
                    name="qualification"
                    value={formData.qualification}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="mb-3">
                  <label htmlFor="experience" className="form-label">Years of Experience</label>
                  <input
                    type="number"
                    className="form-control"
                    id="experience"
                    name="experience"
                    value={formData.experience}
                    onChange={handleChange}
                    min="0"
                    required
                  />
                </div>
                <button type="submit" className="btn btn-primary w-100">Register</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default DoctorRegister;
