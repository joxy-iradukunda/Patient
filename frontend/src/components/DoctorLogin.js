import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function DoctorLogin() {
  const [formData, setFormData] = useState({
    email: '',
    password: ''
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
      const response = await axios.post('http://localhost:8080/authenticatedoc', 
        {
          email: formData.email.trim().toLowerCase(),
          password: formData.password
        },
        { withCredentials: true }
      );
      
      console.log('Login response:', response.data);
      
      if (response.data && response.data.email) {
        localStorage.setItem('doctorToken', 'true');
        localStorage.setItem('doctorEmail', response.data.email);
        navigate('/doctor-dashboard');
      } else {
        setError('Invalid credentials. Please try again.');
      }
    } catch (err) {
      console.error('Login error:', err.response?.data || err.message);
      setError(err.response?.data || 'Invalid credentials. Please try again.');
    }
  };

  return (
    <div className="container">
      <div className="form-container">
        <h2 className="mb-4">Doctor Login</h2>
        {error && <div className="alert alert-danger">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label htmlFor="email" className="form-label">
              Email
            </label>
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
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <input
              type="password"
              className="form-control"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>
          <button type="submit" className="btn btn-primary w-100">
            Login as Doctor
          </button>
        </form>
      </div>
    </div>
  );
}

export default DoctorLogin;
