import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './components/Home';
import Login from './components/Login';
import Register from './components/Register';
import DoctorLogin from './components/DoctorLogin';
import DoctorRegister from './components/DoctorRegister';
import DoctorDashboard from './components/DoctorDashboard';
import DoctorList from './components/DoctorList';
import Appointments from './components/Appointments';
import BookAppointment from './components/BookAppointment';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/doctor-login" element={<DoctorLogin />} />
          <Route path="/doctor-register" element={<DoctorRegister />} />
          <Route path="/doctor-dashboard" element={<DoctorDashboard />} />
          <Route path="/doctors" element={<DoctorList />} />
          <Route path="/appointments" element={<Appointments />} />
          <Route path="/book-appointment" element={<BookAppointment />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
