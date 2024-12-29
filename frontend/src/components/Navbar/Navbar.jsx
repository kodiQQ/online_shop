import React from 'react';
import './Navbar.css';
import {Link, useNavigate} from "react-router-dom";
import UserService from "../../services/UserService.js"

function Navbar() {
    const navigate = useNavigate();

    const handleLogout=()=>{
        console.log("Logout")
        UserService.logout()
        navigate('/strona-glowna');
    }
    return (
        <nav className="navbar">

            <Link className="logo-link" to="/">
                <div className="navbar-logo">
                    alledrogo
                </div>
            </Link>

            <div className="navbar-links">
                {!UserService.isAuthenticated() && <><Link to="/logowanie">
                    <button className="navbar-btn">Zaloguj się</button>
                </Link>

                    <Link to="/rejestracja">
                        <button className="navbar-btn">Zarejestruj się</button>
                    </Link></>}

                {UserService.isAdmin() && <>
                    <Link to="/admin/allCustomersOrders">
                        <button className="navbar-btn">Zamówienia klientów</button>
                    </Link>

                    <Link to="/admin/addProduct">
                        <button className="navbar-btn">Dodaj produkt</button>
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
