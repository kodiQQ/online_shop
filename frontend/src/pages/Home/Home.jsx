import React, { useEffect, useState } from 'react';
import './Home.css';
import UserService from '../../services/UserService.js';

function Home() {
    // Stany komponentu
    const [products, setProducts] = useState([]);
    const [searchTerm, setSearchTerm] = useState(''); // Stan dla wyszukiwania
    const [basket, setBasket] = useState([]); // Stan dla koszyka

    // Pobieranie produktów po załadowaniu komponentu
    useEffect(() => {
        fetchProducts()
        const savedBasket = JSON.parse(localStorage.getItem('basket')) || []; // Pobierz dane lub pustą tablicę
        setBasket(savedBasket); // Ustaw koszyk
    }, []);

    // Funkcja pobierająca produkty z serwera
    const fetchProducts = async () => {
        try {
            const productList = await UserService.getAllProducts();
            setProducts(productList);
        } catch (error) {
            console.error('Error fetching products:', error);
        }
    };

    // Filtrowanie produktów według nazwy
    const filteredProducts = products.filter(product =>
        product.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    // Funkcja dodająca produkt do koszyka
    const handleAddToBasket = (product) => {
        setBasket(prevBasket => {
            const updatedBasket = [...prevBasket, product]; // Tworzymy zaktualizowany koszyk
            localStorage.setItem('basket', JSON.stringify(updatedBasket)); // Zapisujemy do localStorage
            return updatedBasket; // Zwracamy nowy stan koszyka
        });
    };

    return (
        <div className="home-container">
            <h1>Strona główna</h1>

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
                        <button onClick={() => handleAddToBasket(product)}>Dodaj do koszyka</button>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Home;
