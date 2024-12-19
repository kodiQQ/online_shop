import axios from "axios";

class UserService {

    static BASE_URL = "http://localhost:8080"


    static async login(email, password) {

        const response = await axios.post(`${UserService.BASE_URL}/auth/login`, { email, password });
        return response.data;
    }

    static async register(userData) {

        const response = await axios.post(`${UserService.BASE_URL}/auth/register`, userData, {
        })
        return response.data;
    }

    static async getAllUsers() {
        const response = await axios.get(`${UserService.BASE_URL}/admin/get-all-users`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
        });
        return response.data;
    }

    static async getUserById(userId) {
        const response = await axios.get(`${UserService.BASE_URL}/admin/get-users/${userId}`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
        });
        return response.data;
    }

    static async updateUser(userId, userEntity) {
        const response = await axios.put(`${UserService.BASE_URL}/admin/update/${userId}`, userEntity, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
        });
        return response.data;
    }

    static async deleteUser(userId) {
        await axios.delete(`${UserService.BASE_URL}/admin/delete-user/${userId}`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
        });
    }

    static async getYourProfile(token) {
        const response = await axios.get(`${UserService.BASE_URL}/adminuser/get-profile`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        return response.data;
    }


    /* auth checker */

    static isAuthenticated(){
        const token = localStorage.getItem('token')
        return !!token
    }

    static logout() {
        localStorage.removeItem('token')
        localStorage.removeItem('role')
    }
    static isAdmin(){
        const role = localStorage.getItem('role')
        return role === 'ADMIN'
    }

    static isUser(){
        const role = localStorage.getItem('role')
        return role === 'USER'
    }

    static adminOnly(){
        return this.isAuthenticated() && this.isAdmin();
    }
}

export default UserService;