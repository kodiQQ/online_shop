import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'

import Navbar from './components/Navbar/Navbar.jsx'
import Footer from './components/Footer/Footer.jsx'
import Home from './pages/Home/Home.jsx'
import Login from './pages/Login/Login.jsx'
import Register from './pages/Register/Register.jsx'


function AppRoutes() {

    const renderLayout = (component) => (

        <div className="App">

            <div className="navbar">
                <Navbar />
            </div>
            <div className="content-wrapper">
                {component}
            </div>
            <div className="footer">
                <Footer />
            </div>

        </div>

    );

    return (<Router>
        <Routes>
            <Route path="/" element={<Navigate to="strona-glowna" />} />
            <Route path="strona-glowna" element={renderLayout(<Home />)} />
            <Route path="logowanie" element={<Login />} />
            <Route path="rejestracja" element={<Register />} />
        </Routes>
    </Router>)
}

export default AppRoutes;