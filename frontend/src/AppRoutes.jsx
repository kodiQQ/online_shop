import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'

import Navbar from './components/Navbar/Navbar.jsx'
import Footer from './components/Footer/Footer.jsx'
import Home from './pages/Home/Home.jsx'
import Login from './pages/Login/Login.jsx'
import Register from './pages/Register/Register.jsx'
import AddProduct from "./pages/AddProduct/AddProduct.jsx";
import Basket from "./pages/Basket/Basket.jsx";
import UserService from "./services/UserService.js";
import MyOrders from "./pages/MyOrders/MyOrders.jsx";
import AllCustomersOrders from "./pages/AllCustomersOrders/AllCustomersOrders.jsx";
import ProductPage from "./pages/ProductPage/ProductPage.jsx";
import OrderConfirmation from "./pages/OrderConfirmation/OrderConfirmation.jsx"



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
            <Route path="logowanie" element={renderLayout(<Login />)} />
            <Route path="rejestracja" element={renderLayout(<Register />)} />
            <Route path="product/{id}" element={renderLayout(<ProductPage />)} />
            {UserService.isAuthenticated() && (<>
                <Route path="/basket" element={renderLayout(<Basket />)} />
                <Route path="/myOrders" element={renderLayout(<MyOrders />)} />
                <Route path="/order-confirmation" element={renderLayout(<OrderConfirmation />)} />
            </>)}
            {UserService.adminOnly() && (
                <>
                     <Route path="/admin/addProduct" element={renderLayout(<AddProduct />)} />
                    <Route path="/admin/allCustomersOrders" element={renderLayout(<AllCustomersOrders />)} />
                    {/*<Route path="/admin/skin-management" element={<SkinManagementPage />} />*/}
                    {/*<Route path="/admin/user-management" element={<UserManagementPage />} />*/}
                    {/*<Route path="/update-user/:userId" element={<UpdateUser />} />*/}
                </>
            )}
        </Routes>
    </Router>)
}

export default AppRoutes;