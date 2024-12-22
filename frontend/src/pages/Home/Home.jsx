import React, { useEffect, useState } from 'react';
import './Home.css';
import UserService from '../../services/UserService.js';

function Home() {
    // Stany komponentu
    const [products, setProducts] = useState([]);
    const [searchTerm, setSearchTerm] = useState(''); // Stan dla wyszukiwania
    const [basket, setBasket] = useState([]); // Stan dla koszyka
    const [items_count, setItems_count] = useState(0);

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
            setItems_count(productList.length);
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
            <h1>Ilość przedmiotów - {items_count}</h1>

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
                {filteredProducts.length === 0 ? (
                    <p>Brak produktów</p>
                ) : (
                    filteredProducts.map((product) => (
                        <div className="product-container" key={product.id}>
                            <img className="product-img" src={product.imageUrl} alt={product.name}/>
                            <div className="product-info">
                                <p className="product-name">{product.name}</p>
                            </div>
                            <div className="product-actions">
                                <p className="product-price">{product.price} zł</p>
                                <button className="add-btn" onClick={() => handleAddToBasket(product)}>Dodaj do
                                    koszyka
                                </button>
                            </div>
                        </div>

                    ))
                )}
            </div>
        </div>
    );
}

export default Home;
