import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import UserService from '../../services/UserService';
import './AddProduct.css';

function AddProduct() {

    const [name, setName] = useState('');
    const [category, setCategory] = useState('');
    const [price, setPrice] = useState();

    const [error, setError] = useState('');
    const navigate = useNavigate();
    const [file, setFile] = useState(null); // Dodano stan dla obrazka


    useEffect(() => {
        console.log(UserService.isAdmin())
        // if (wholePrice<0){
        //   setWholePrice(0);
        // }
    }, []);

    const handleFileChange = (e) => {
        setFile(e.target.files[0]); // Wczytywanie pliku do stanu
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('name', name);
        formData.append('category', category);
        formData.append('price', price);
        if (file) {
            formData.append('file', file); // Dodanie pliku do żądania
        }

        const token = sessionStorage.getItem('token'); // Pobieranie tokena z lokalnego magazynu

        UserService.addProduct(formData, token);

        alert('Pomyślnie dodano nowy produkt!');
        navigate('/strona-glowna');
    };

    return (
        <div className="addProduct-container">
            <h1>Dodaj produkt</h1>
            <form onSubmit={handleSubmit} className="addProduct-form">
                <div className="addProduct-form-group">
                    <input
                        type="name"
                        id="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="Nazwa"
                        required
                    />
                </div>
                <div className="addProduct-form-group">
                    <input
                        type="category"
                        id="category"
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        placeholder="Kategoria"
                        required
                    />
                </div>
                <div className="addProduct-form-group">
                    <input
                        type="price"
                        id="price"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        placeholder="Cena"
                        required
                    />
                </div>

                <div className="addProduct-form-group">
                    <input
                        type="file"
                        id="file"
                        accept="file/*"
                        onChange={handleFileChange} // Obsługa zmiany obrazka
                    />
                </div>

                {error && <p className="addProduct-error-message">{error}</p>}
                <button type="submit" className="addProduct-submit-button">Zatwierdź</button>
            </form>
        </div>
    );
}

export default AddProduct;
