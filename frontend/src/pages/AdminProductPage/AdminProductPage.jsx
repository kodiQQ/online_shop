import React, { useEffect, useState } from 'react';
import UserService from '../../services/UserService.js';
import './AdminProductPage.css';

function AdminProductPage() {
    const [products, setProducts] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const token = sessionStorage.getItem('token');
                const allProducts = await UserService.getAllProducts();
                setProducts(allProducts);
            } catch (err) {
                setError('Nie udało się pobrać produktów.');
                console.error(err);
            }
        };

        fetchProducts();
    }, []);

    const handleDeleteProduct = async (productId) => {
        try {
            const token = sessionStorage.getItem('token'); // Pobierz token z localStorage
            await UserService.deleteProduct(productId, token);
            setProducts(products.filter((product) => product.id !== productId)); // Aktualizacja listy produktów
        } catch (err) {
            setError('Nie udało się usunąć produktu.');
            console.error(err);
        }
    };

    return (
        <div className="home-container">
            <div className="top-content-container">
                <h1 className="title">Lista produktów</h1>
                {error && <p className="error-message">{error}</p>}
            </div>

            <div className="productsContainer">
                {products.map((product) => (
                    <div className="product-container" key={product.id}>
                        <div className="user-info">
                            <img className="product-img" src={product.imageUrl} alt={product.name}/>
                            <p><strong>ID: </strong> {product.id}</p>
                            <p><strong>Nazwa: </strong> {product.name}</p>
                            <p><strong>Cena: </strong> {product.price} PLN</p>
                            <p><strong>Kategoria: </strong> {product.category}</p>
                            <p><strong>Opis: </strong> {product.description}</p>
                        </div>
                        <div className="actions">
                        <button className="delete-product-btn" onClick={() => handleDeleteProduct(product.id)}>Usuń produkt</button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default AdminProductPage;