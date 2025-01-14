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
                const ordersdata = await UserService.getUserOrders(token); // Czekaj na dane
                // setOrdersData(data); // Zapisz dane w stanie
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
            {/*<div className="productsContainer">*/}
            {/*    {orderList.map((order, index0) => (*/}
            {/*        <div className="order" key={order.id}>*/}
            {/*            <p>Zamówienie numer {index0 + 1}</p>*/}
            {/*            {order.productsAndNumbers.map((item, index) => ( // Użycie map() zamiast forEach()*/}
            {/*                <li key={index}>*/}
            {/*                    <img src={item.product.imageUrl} alt=""/>*/}
            {/*                    {item.product.name}*/}
            {/*                    Liczba: {item.number}*/}

            {/*                </li>*/}
            {/*            ))}*/}

            {/*        </div>*/}
            {/*    ))}*/}
            {/*</div>*/}
            <div className="productsContainer">
                {orderList.map((order, index0) => (
                    <div className="product-container" key={order.id}>
                        {/*{order.productsAndNumbers.map((item, index) => (*/}
                        {/*    <div className="myOrders-productItem-container" key={index}>*/}
                        {/*        <div className="myOrders-productItem">*/}
                        {/*            <img src={item.product.imageUrl} alt=""/>*/}
                        {/*            <p>{item.product.name}</p>*/}
                        {/*            <p>Ilość: {item.number}</p>*/}
                        {/*        </div>*/}
                        {/*    </div>*/}
                        {/*))}*/}


                        <div className="offer-product-container" key={order.id}>
                            <div className="orders-user-details" key={order.id}>
                                <p style={{marginTop: "5px", marginBottom: "5px"}}>Nr zamówienia: {index0 + 1}</p>
                            </div>
                            {order.productsAndNumbers.map((item, index) => (
                                <div className="productItem-container" key={index}>
                                    <div className="productItem">
                                        <img src={item.product.imageUrl} alt=""/>
                                        <p>{item.product.name}</p>
                                        <p>Ilość: {item.number}</p>
                                    </div>
                                </div>
                            ))}
                        </div>

                    </div>
                ))}
            </div>
        </div>
    );
}

export default MyOrders;
