import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import './ProductPage.css';
import UserService from '../../services/UserService.js';

function ProductPage() {
    const { id } = useParams(); // Pobieramy `id` z URL
    const [product, setProduct] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProduct = async () => {
            try {
                // const productData = await UserService.getProductById(id);
                setProduct(productData);
            } catch (err) {
                setError('Nie udało się załadować produktu');
            }
        };

        fetchProduct();
    }, [id]);

    if (error) return <div>{error}</div>;
    if (!product) return <div>Nie znaleziono produktu</div>;

    return (
        <div className="product-page-container">
            <div className="product-main">
                <div className="product-image-section">
                    <h1 className="product-name">{product.name}</h1>
                    <img
                        src={product.imageUrl}
                        alt={product.name}
                        className="product-image"
                    />
                </div>
                <div className="product-purchase-section">
                    <p className="product-price">{product.price} zł</p>
                    <button className="buy-now-button">Kup Teraz</button>
                </div>
            </div>
            <div className="product-description">
                <h2>Opis</h2>
                <p>{product.description}</p>
            </div>
        </div>
    );
}

export default ProductPage;
