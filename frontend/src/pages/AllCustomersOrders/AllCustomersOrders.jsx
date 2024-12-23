import React, { useEffect, useState } from 'react';
import './AllCustomersOrders.css';
import UserService from '../../services/UserService.js';
import ProductsToOrder from '../../Classes/ProductsToOrder.js';
import {getFirstTokens} from "eslint-plugin-react/lib/util/eslint.js";

function AllCustomersOrders() {
    // Stany komponentu
    const [orderList, setOrderList] = useState([]);
    const [searchTerm, setSearchTerm] = useState(''); // Stan dla wyszukiwania





    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const token = sessionStorage.getItem('token');
                const ordersdata = await UserService.getAllCustomersOrders(token); // Czekaj na dane
                // setOrdersData(data); // Zapisz dane w stanie
                console.log('ordersData:');
                console.log(ordersdata.ordersEntityList);
                setOrderList(ordersdata.ordersEntityList)

                // Zaloguj dane
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
            <h1>Moja zamówienia</h1>

            {/* Pasek wyszukiwania */}
            <input
                type="text"
                placeholder="Wyszukaj produkt..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="search-bar"
            />

            {/* Lista produktów */}
            <div className="productsContainer">
                {orderList.map((order,index0) => (
                    <div className="order" key={order.id}>
                        <p>Zamówienie numer {index0+1}</p>
                        <p>Zamówienie użytkownika {order.ourUser.username}, ID użytkownika: {order.ourUser.id}</p>
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

export default AllCustomersOrders;
