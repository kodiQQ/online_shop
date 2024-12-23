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
                {!UserService.isAuthenticated() && <><Link to="/login">
                    <button className="navbar-btn">Zaloguj się</button>
                </Link>

                    <Link to="/register">
                        <button className="navbar-btn">Zarejestruj się</button>
                    </Link></>}

                {UserService.isAdmin() && <>
                    <Link to="/admin/allCustomersOrders">
                        <button className="navbar-btn">Zamówienia klientów</button>
                    </Link>
                </>}

                {UserService.isAuthenticated() && <>
                    <Link to="/basket">
                        <button className="navbar-btn">Koszyk</button>
                    </Link>
                    <Link to="/myOrders">
                        <button className="navbar-btn">Moje Zamówienia</button>
                    </Link>
                        <button onClick={handleLogout} className="navbar-btn">Wyloguj się</button>
                    </>}



            </div>
        </nav>
    );
}

export default Navbar;
