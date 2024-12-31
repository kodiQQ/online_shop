import React, { useEffect, useState } from 'react';
import './MyOrders.css';
import UserService from '../../services/UserService.js';

function MyOrders() {
    const [orderList, setOrderList] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const token = sessionStorage.getItem('token');
                const ordersdata = await UserService.getAllUsersOrders(token);
                console.log('ordersData:');
                console.log(ordersdata.ordersEntityList);
                setOrderList(ordersdata.ordersEntityList)

            } catch (error) {
                console.error('Błąd podczas ładowania zamówień:', error);
            }
        };

        fetchOrders();


    }, []);


    // Funkcja pobierająca produkty z serwera


    // Filtrowanie produktów według nazwy


    // Funkcja dodająca produkt do koszyka
    // const handleDeleteFromBasket = () => {
    //
    //
    // };
    //
    //
    // const handleAddOrder = () => {
    //
    // };


    return (
        <div className="home-container">
            <div className="top-content-container">
                <h1 className="title">Moje zamówienia</h1>

                <input
                    type="text"
                    placeholder="Wyszukaj produkt..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="search-bar"
                />
            </div>
            <div className="productsContainer">
                {orderList.map((order,index0) => (
                    <div className="order" key={order.id}>
                        <p>Zamówienie numer {index0+1}</p>
                        {order.productsAndNumbers.map((item, index) => ( // Użycie map() zamiast forEach()
                            <li key={index}>
                                <img src={item.product.imageUrl} alt=""/>
                                {item.product.name}
                                Liczba: {item.number}

                            </li>
                        ))}

                    </div>
                ))}
            </div>
        </div>
    );
}

export default MyOrders;
