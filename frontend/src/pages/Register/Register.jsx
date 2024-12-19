import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../../services/UserService";
import './Register.css';

function Register() {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        confirmPassword: '', // Pole tylko frontendowe,
        city: '',
        role: ''
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (formData.password !== formData.confirmPassword) {
            alert('Hasło i Powtórz hasło muszą być identyczne.');
            return;
        }

        try {
            const token = localStorage.getItem('token');

            // Przygotowanie danych bez confirmPassword
            const { confirmPassword, ...dataToSend } = formData;

            await UserService.register(dataToSend, token);

            setFormData({
                name: '',
                email: '',
                password: '',
                confirmPassword: '',
                city: '',
                role: ''
            });

            alert('Zalogowano pomyślnie!');
            navigate('/strona-glowna');

        } catch (error) {
            console.error('Wystąpił błąd podczas rejestracji: ', error);
            alert('Wystąpił błąd podczas rejestracji');
        }
    };

    return (
        <div className="register-container">
            <h1>Rejestracja</h1>
            <form onSubmit={handleSubmit} className="register-form">
                <div className="register-form-group">
                    <input
                        type="text"
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleInputChange}
                        placeholder="Nazwa użytkownika"
                        required
                    />
                </div>
                <div className="register-form-group">
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleInputChange}
                        placeholder="Adres e-mail"
                        required
                    />
                </div>
                <div className="register-form-group">
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleInputChange}
                        placeholder="Hasło"
                        required
                    />
                </div>
                <div className="register-form-group">
                    <input
                        type="password"
                        id="confirmPassword"
                        name="confirmPassword"
                        value={formData.confirmPassword}
                        onChange={handleInputChange}
                        placeholder="Powtórz hasło"
                        required
                    />
                </div>
                <div className="register-form-group">
                    <input
                        type="city"
                        id="city"
                        name="city"
                        value={formData.city}
                        onChange={handleInputChange}
                        placeholder="Miasto"
                        required
                    />
                </div>
                <div className="register-form-group">
                    <select
                        id="role"
                        name="role"
                        value={formData.role}
                        onChange={handleInputChange}
                        required
                    >
                        <option value="">Wybierz rolę</option>
                        <option value="user">Użytkownik</option>
                        <option value="admin">Administrator</option>
                    </select>
                </div>
                <button type="submit" className="register-submit-button">Zarejestruj się</button>
            </form>
        </div>
    );
}

export default Register;
