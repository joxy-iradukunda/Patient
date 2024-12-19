import React from 'react';
import { Link } from 'react-router-dom';

function Home() {
  const isLoggedIn = localStorage.getItem('token');
  const isDoctorLoggedIn = localStorage.getItem('doctorToken');

  return (
    <div className="container">
      <div className="jumbotron text-center my-5">
        <h1 className="display-4">Welcome to Patient Appointment System</h1>
        <p className="lead">
          Schedule appointments with our experienced doctors easily and efficiently
        </p>
        <hr className="my-4" />
        
        <div className="row">
          <div className="col-md-6">
            <h3>For Patients</h3>
            {!isLoggedIn ? (
              <div>
                <p>Please login or register to book appointments</p>
                <Link to="/login" className="btn btn-primary m-2">
                  Patient Login
                </Link>
                <Link to="/register" className="btn btn-secondary m-2">
                  Patient Register
                </Link>
              </div>
            ) : (
              <div>
                <p>Start managing your appointments now</p>
                <Link to="/doctors" className="btn btn-primary m-2">
                  View Doctors
                </Link>
                <Link to="/appointments" className="btn btn-secondary m-2">
                  My Appointments
                </Link>
              </div>
            )}
          </div>
          
          <div className="col-md-6">
            <h3>For Doctors</h3>
            {!isDoctorLoggedIn ? (
              <div>
                <p>Access your patient appointments</p>
                <Link to="/doctor-login" className="btn btn-primary m-2">
                  Doctor Login
                </Link>
                <Link to="/doctor-register" className="btn btn-secondary m-2">
                  Doctor Register
                </Link>
              </div>
            ) : (
              <div>
                <p>Manage your patient appointments</p>
                <Link to="/doctor-dashboard" className="btn btn-primary m-2">
                  Doctor Dashboard
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>

      <div className="row mt-5">
        <div className="col-md-4">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Easy Booking</h5>
              <p className="card-text">
                Book appointments with just a few clicks. Choose your preferred doctor and time slot.
              </p>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Expert Doctors</h5>
              <p className="card-text">
                Access to a wide range of experienced and qualified medical professionals.
              </p>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Manage Appointments</h5>
              <p className="card-text">
                View and manage your appointments easily. Cancel or reschedule as needed.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Home;
