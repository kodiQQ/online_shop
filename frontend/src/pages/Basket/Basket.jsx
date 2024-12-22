import React, { useEffect, useState } from 'react';
import './Basket.css';
import UserService from '../../services/UserService.js';

function Basket() {
    // Stany komponentu
    const [products, setProducts] = useState([]);
    const [searchTerm, setSearchTerm] = useState(''); // Stan dla wyszukiwania
    const [productsInBasket, setProductsInBasket] = useState([]); // Stan dla koszyka
    const [number, setNumber] = useState(1); // Stan dla koszyka


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
        setProductsInBasket(prevBasket => {
            const updatedBasket = prevBasket.filter(product => product.id !== productToDelete.id);
            localStorage.setItem('basket', JSON.stringify(updatedBasket)); // Zapisujemy do localStorage
            window.location.reload();

            return updatedBasket; // Zwracamy nowy stan koszyka

        });
    };

    const handleSetNumber = (product0, number) => {
        setProductsInBasket(prevBasket => {
            // Tworzymy zaktualizowany koszyk
            const updatedBasket = prevBasket.map(product =>
                product.id === product0.id
                    ? { ...product, number: number || 0 } // Jeśli brak pola number, dodajemy je
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
            <h1>Koszyk</h1>

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
                {filteredProducts.map((product) => (
                    <div className="product" key={product.id}>
                        <p>{product.name}</p>
                        <img className="productImg" src={product.imageUrl} alt={product.name} />
                        <p>{product.price}</p>
                        <button onClick={() => handleDeleteFromBasket(product)}>Usuń z koszyka</button>
                        <form>
                            <div className="addProduct-form-group">
                                <input
                                    type="number"
                                    id="number"
                                    value={product.number}
                                    onChange={(e) => handleSetNumber(product,e.target.value)}
                                    placeholder="Ilość"
                                    required
                                />
                            </div>
                            {/*<button type="submit">Zatwierdź</button>*/}
                        </form>
                    </div>
                ))}
            </div>
            <button onClick={handleAddOrder}>Złóż zamówienie</button>
        </div>
    );
}

export default Basket;
