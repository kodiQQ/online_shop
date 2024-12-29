import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Basket.css';
import UserService from '../../services/UserService.js';
import ProductsToOrder from '../../Classes/ProductsToOrder.js';
import {getFirstTokens} from "eslint-plugin-react/lib/util/eslint.js";

function Basket() {
    // Stany komponentu
    const [products, setProducts] = useState([]);
    const [searchTerm, setSearchTerm] = useState(''); // Stan dla wyszukiwania
    const [productsInBasket, setProductsInBasket] = useState([]); // Stan dla koszyka
    const [number, setNumber] = useState(1); // Stan dla koszyka
    const navigate = useNavigate();


    // Pobieranie produktów po załadowaniu komponentu
    // useEffect(() => {
    //
    //     const savedBasket = JSON.parse(localStorage.getItem('basket')) || []; // Pobierz dane lub pustą tablicę
    //     // setBasket(savedBasket);
    //     setProducts(savedBasket)// Ustaw koszyk
    // }, []);

    useEffect(() => {
        const savedBasket = JSON.parse(localStorage.getItem('basket')) || [];
        setProductsInBasket(savedBasket); // Ustaw koszyk
        console.log(savedBasket);
    }, []);


    // Funkcja pobierająca produkty z serwera


    // Filtrowanie produktów według nazwy
    const filteredProducts = productsInBasket.filter(product =>
        product.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    // Funkcja dodająca produkt do koszyka
    const handleDeleteFromBasket = (productToDelete) => {

        setProductsInBasket(prevBasket => {
            // console.log(productToDelete);
            // console.log(productToDelete.id);
            const updatedBasket = prevBasket.filter(product => product.id !== productToDelete.id);
            localStorage.setItem('basket', JSON.stringify(updatedBasket)); // Zapisujemy do localStorage
            // window.location.reload();
            console.log(updatedBasket);
            return updatedBasket; // Zwracamy nowy stan koszyka
        });
    };


    const handleAddOrder = () => {
        // let productsAndNumbersList=[];
        let productsToOrderList=[];
        for (let i = 0; i < productsInBasket.length; i++) {
            console.log(productsInBasket[i]);
            console.log(productsInBasket[i].number);
            let productToOrder= new ProductsToOrder(productsInBasket[i].id,productsInBasket[i].number);
            console.log("productToOrder.toJSON()");
            console.log(productToOrder.toJSON());
            productsToOrderList.push(productToOrder.toJSON());
        }
        console.log("productsToOrderList");
        console.log(productsToOrderList);
        const formattedData = {
            productsAndNumbersList: productsToOrderList.map(item => ({
                productId: String(item.productId),          // Konwersja productId na string
                productNumber: String(item.productNumber)         // Zmiana klucza z "number" na "productNumber"
            }))
        };
        console.log("formattedData");
        console.log(formattedData);
        const resultJson = JSON.stringify(formattedData, null, 2); // Formatowanie JSON z wcięciem
        console.log(resultJson);
        // const jsonProductsToOrderList = JSON.stringify(productsToOrderList);
        UserService.addOrder(resultJson,sessionStorage.getItem('token'));

        navigate('/order-confirmation');
    };

    const handleSetNumber = (product0, number) => {
        setProductsInBasket(prevBasket => {
            // Tworzymy zaktualizowany koszyk
            const updatedBasket = prevBasket.map(product =>
                product.id === product0.id
                    ? { ...product, number: Number(number) || 0 } // Jeśli brak pola number, dodajemy je
                    : product
            );

            // Zapisujemy do localStorage
            console.log(updatedBasket);
            localStorage.setItem('basket', JSON.stringify(updatedBasket));

            return updatedBasket; // Zwracamy zaktualizowany stan koszyka
        });
    };


    return (
        <div className="home-container">
            <div className="top-content-container">

                <h1 className="title">Koszyk</h1>

                {/* Pasek wyszukiwania */}
                <input
                    type="text"
                    placeholder="Wyszukaj produkt..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="search-bar"
                />
            </div>
                {/* Lista produktów */}
                <div className="productsContainer">
                    {filteredProducts.length === 0 ? (
                        <p className="err-container"><span className="sad">:(</span><span className="msg"> Brak produktów</span></p>
                    ) : (
                        filteredProducts.map((product) => (
                            <div className="product-container" key={product.id}>
                                <img className="product-img" src={product.imageUrl} alt={product.name}/>
                                <p className="product-name">{product.name}</p>
                                <p className="product-price">{product.price} zł</p>

                                <form>
                                    <div className="addProduct-form-group">
                                        <input
                                            type="number"
                                            id="number"
                                            value={product.number}
                                            onChange={(e) => handleSetNumber(product, e.target.value)}
                                            placeholder="Ilość"
                                            required
                                        />
                                    </div>

                                    <button className="add-btn" onClick={handleAddOrder}>
                                        Złóż zamówienie
                                    </button>

                                    <button className="delete-product-btn"
                                            onClick={() => handleDeleteFromBasket(product)}>Usuń z koszyka
                                    </button>
                                </form>
                            </div>
                        ))
                    )}

                </div>
            </div>
            );
            }

            export default Basket;
