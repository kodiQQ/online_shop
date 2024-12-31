import React, { useState, useEffect } from 'react';
import UserService from '../../services/UserService';
import './UserPage.css';

function UserPage() {
    const [users, setUsers] = useState([]);

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const response = await UserService.getAllUsers(token);
            setUsers(response.ourUsersEntityList);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const deleteUser = async (userId) => {
        try {
            const confirmDelete = window.confirm('Are you sure you want to delete this user?');
            const token = sessionStorage.getItem('token');
            if (confirmDelete) {
                await UserService.deleteUser(userId, token);
                fetchUsers();
            }
        } catch (error) {
            console.error('Error deleting user:', error);
        }
    };

    return (
        <div className="home-container">
            <div className="top-content-container">
                <h1 className="title">Panel Użytkowników</h1>
            </div>

            <div className="usersContainer">
                {users.map((user) => (
                    <div className="user-container" key={user.id}>
                        <div className="user-info">
                            <p><strong>ID: </strong> {user.id}</p>
                            <p><strong>Nazwa użytkownika: </strong> {user.name}</p>
                            <p><strong>Email: </strong> {user.email}</p>
                            <p><strong>Miasto: </strong> {user.city}</p>
                            <p><strong>Rola: </strong> {user.role}</p>
                        </div>
                        <div className="actions">
                        <button className="delete-product-btn" onClick={() => deleteUser(user.id)}>Usuń użytkownika</button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default UserPage;