import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import MovieList from './components/MovieList';
import MovieForm from './components/MovieForm';
import Statistics from './components/Statistics';
import OscarService from './components/OscarService';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          <div className="nav-container">
            <h1 className="nav-title">Movie Management System</h1>
            <ul className="nav-menu">
              <li><Link to="/">Movies</Link></li>
              <li><Link to="/statistics">Statistics</Link></li>
              <li><Link to="/oscar">Oscar Service</Link></li>
            </ul>
          </div>
        </nav>

        <main className="main-content">
          <Routes>
            <Route path="/" element={<MovieList />} />
            <Route path="/movies/new" element={<MovieForm />} />
            <Route path="/movies/edit/:id" element={<MovieForm />} />
            <Route path="/statistics" element={<Statistics />} />
            <Route path="/oscar" element={<OscarService />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;

