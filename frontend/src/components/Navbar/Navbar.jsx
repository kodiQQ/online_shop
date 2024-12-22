import React from 'react';
import './Navbar.css';
import { Link } from "react-router-dom";
import UserService from "../../services/UserService.js"

function Navbar() {

    const handleLogout=()=>{
        console.log("Logout")
        UserService.logout()
    }
    return (
        <nav className="navbar">

            <div className="navbar-logo">
                alledrogo
            </div>
            <div className="navbar-links">
                {!UserService.isAuthenticated() && <><Link to="/logowanie">
                    <button className="navbar-btn">Zaloguj się</button>
                </Link>

                    <Link to="/rejestracja">
                        <button className="navbar-btn">Zarejestruj się</button>
                    </Link></>}

                {UserService.isAuthenticated() && <>
                    <Link to="/basket">
                        <button className="navbar-btn">Koszyk</button>
                    </Link>
                        <button onClick={handleLogout} className="navbar-btn">Wyloguj się</button>
                    </>}



            </div>
        </nav>
    );
}

export default Navbar;
