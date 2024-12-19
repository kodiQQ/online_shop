import React from 'react';
import './Navbar.css';
import { Link } from "react-router-dom";

function Navbar() {
    return (
        <nav className="navbar">
            <div className="navbar-logo">
                alledrogo
            </div>
            <div className="navbar-links">
                <Link to="/logowanie">
                    <button className="navbar-btn">Zaloguj się</button>
                </Link>

                <Link to="/rejestracja">
                    <button className="navbar-btn">Zarejestruj się</button>
                </Link>
            </div>
        </nav>
    );
}

export default Navbar;
