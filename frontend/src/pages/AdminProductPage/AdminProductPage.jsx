import React, { useEffect, useState } from 'react';
import UserService from '../../services/UserService.js';
import ProductService from "../../services/ProductService.js";
import './AdminProductPage.css';

function AdminProductPage() {
    const [products, setProducts] = useState([]);
    const [error, setError] = useState(null);

    // Stan służący do przechowywania aktualnie edytowanego produktu
    const [editingProduct, setEditingProduct] = useState(null);
    // Stan formularza – przechowuje nowe wartości pól podczas edycji
    const [editFormData, setEditFormData] = useState({
        name: '',
        price: '',
        category: '',
        description: '',
        imageUrl: ''
    });

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
            const token = sessionStorage.getItem('token');
            await UserService.deleteProduct(productId, token);
            setProducts(products.filter((product) => product.id !== productId));
        } catch (err) {
            setError('Nie udało się usunąć produktu.');
            console.error(err);
        }
    };

    // Funkcja wywoływana po kliknięciu w „Edytuj produkt”
    const handleEditProduct = (product) => {
        // Ustawiamy aktualnie edytowany produkt w stanie
        setEditingProduct(product);
        // Wypełniamy formularz wartościami produktu
        setEditFormData({
            name: product.name,
            price: product.price,
            category: product.category,
            description: product.description,
            imageUrl: product.imageUrl
        });

    };

    // Funkcja obsługująca zmiany w polach formularza
    const handleChange = (e) => {
        const { name, value } = e.target;
        setEditFormData((prevData) => ({
            ...prevData,
            [name]: value
        }));
    };

    // Funkcja wywoływana po kliknięciu „Zapisz”
    const handleUpdateProduct = async (e) => {
        e.preventDefault();
        try {
            const token = sessionStorage.getItem('token');
            console.log(token);
            console.log(editingProduct.id);
            console.log(editFormData);
            // Wywołanie metody aktualizującej produkt w serwisie
            await ProductService.updateProduct(editingProduct.id, editFormData, token);

            // Aktualizacja listy produktów w stanie – np. możemy pobrać je jeszcze raz,
            // lub zaktualizować tylko jeden produkt w tablicy:
            setProducts((prevProducts) => {
                return prevProducts.map((prod) => {
                    if (prod.id === editingProduct.id) {
                        return { ...prod, ...editFormData };
                    }
                    return prod;
                });
            });

            // Wyczyść stany po zakończeniu edycji
            setEditingProduct(null);
            setEditFormData({
                name: '',
                price: '',
                category: '',
                description: '',
                imageUrl: ''
            });
        } catch (err) {
            setError('Nie udało się zaktualizować produktu.');
            console.error(err);
        }
    };

    // Anulowanie/wyjście z edycji bez zapisywania
    const handleCancelEdit = () => {
        setEditingProduct(null);
        setEditFormData({
            name: '',
            price: '',
            category: '',
            description: '',
            imageUrl: ''
        });
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
                            <img
                                className="product-img"
                                src={product.imageUrl}
                                alt={product.name}
                            />
                            <p><strong>ID:</strong> {product.id}</p>
                            <p><strong>Nazwa:</strong> {product.name}</p>
                            <p><strong>Cena:</strong> {product.price} PLN</p>
                            <p><strong>Kategoria:</strong> {product.category}</p>
                            <p><strong>Opis:</strong> {product.description}</p>
                        </div>
                        <div className="actions">
                            <button
                                className="edit-product-btn"
                                onClick={() => handleEditProduct(product)}
                            >
                                Edytuj produkt
                            </button>
                        </div>
                        <div className="actions">
                            <button
                                className="delete-product-btn"
                                onClick={() => handleDeleteProduct(product.id)}
                            >
                                Usuń produkt
                            </button>
                        </div>
                    </div>
                ))}
            </div>

            {/* Formularz edycji wyświetla się tylko wtedy, gdy editingProduct !== null */}
            {editingProduct && (
                <div className="edit-product-modal">
                    <form onSubmit={handleUpdateProduct} className="editProduct-form">
                        <div className="editProduct-form-group">
                            <input
                                type="text"
                                id="name"
                                value={editFormData.name}
                                onChange={(e) =>
                                    setEditFormData((prev) => ({ ...prev, name: e.target.value }))
                                }
                                placeholder="Nazwa"
                                required
                            />
                        </div>

                        <div className="editProduct-form-group">
                            <input
                                type="text"
                                id="category"
                                value={editFormData.category}
                                onChange={(e) =>
                                    setEditFormData((prev) => ({ ...prev, category: e.target.value }))
                                }
                                placeholder="Kategoria"
                                required
                            />
                        </div>

                        <div className="editProduct-form-group">
                            <input
                                type="number"
                                id="price"
                                value={editFormData.price}
                                onChange={(e) =>
                                    setEditFormData((prev) => ({ ...prev, price: e.target.value }))
                                }
                                placeholder="Cena"
                                required
                            />
                        </div>

                        <div className="editProduct-form-group">
                            <input
                                type="text"
                                id="description"
                                value={editFormData.description}
                                onChange={(e) =>
                                    setEditFormData((prev) => ({
                                        ...prev,
                                        description: e.target.value
                                    }))
                                }
                                placeholder="Opis produktu"
                                required
                            />
                        </div>

                        {/* Jeśli chcesz mieć opcję aktualizacji pliku w trakcie edycji: */}
                        <div className="editProduct-form-group">
                            <input
                                type="file"
                                id="file"
                                accept="file/*"
                                onChange={(e) =>
                                    setEditFormData((prev) => ({
                                        ...prev,
                                        file: e.target.files[0] // np. przechowaj w state plik
                                    }))
                                }
                            />
                        </div>

                        {error && <p className="editProduct-error-message">{error}</p>}

                        <button type="submit" className="editProduct-submit-button">Zapisz</button>
                        <button type="button" onClick={handleCancelEdit} className="editProduct-submit-button">
                            Anuluj
                        </button>
                    </form>
                </div>
            )}

        </div>
    );
}

export default AdminProductPage;
