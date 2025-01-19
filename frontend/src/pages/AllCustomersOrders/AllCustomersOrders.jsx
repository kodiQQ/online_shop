import React, { useEffect, useState } from 'react';
import './AllCustomersOrders.css';
import OrderService from "../../services/OrderService.js";
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
                const ordersdata = await OrderService.getAllCustomersOrders(token); // Czekaj na dane
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

            <div className="top-content-container">
                <h1 className="title">Zamówienia wszystkich klientów</h1>

                <input
                    type="text"
                    placeholder="Wyszukaj produkt..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="search-bar"
                />
            </div>
            <div className="productsContainer">
                {orderList.map((order, index0) => (
                    <div className="product-container" key={order.id}>
                        <div className="offer-product-container" key={order.id}>
                            <div className="orders-user-details" key={order.id}>
                                <p style={{marginTop:"5px",marginBottom:"5px"}}>Nr zamówienia {index0 + 1}</p>
                                <p style={{marginTop:"5px",marginBottom:"5px"}}> Użytkownik {order.ourUser.username}</p>
                                <p style={{marginTop:"5px",marginBottom:"5px"}}>ID użytkownika: {order.ourUser.id}</p>
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

                            {/*<table className="productsTable">*/}
                            {/*    <thead>*/}
                            {/*    <tr>*/}
                            {/*        <th>Obraz</th>*/}
                            {/*        <th>Nazwa produktu</th>*/}
                            {/*        <th>Ilość</th>*/}
                            {/*    </tr>*/}
                            {/*    </thead>*/}
                            {/*    <tbody>*/}
                            {/*    {order.productsAndNumbers.map((item, index) => (*/}
                            {/*        <tr className="productItem-row" key={index}>*/}
                            {/*            <td>*/}
                            {/*                <img src={item.product.imageUrl} alt={item.product.name}/>*/}
                            {/*            </td>*/}
                            {/*            <td>{item.product.name}</td>*/}
                            {/*            <td>{item.number}</td>*/}
                            {/*        </tr>*/}
                            {/*    ))}*/}
                            {/*    </tbody>*/}
                            {/*</table>*/}


                        </div>

                    </div>
                ))}


            </div>
        </div>
    );
}

export default AllCustomersOrders;
