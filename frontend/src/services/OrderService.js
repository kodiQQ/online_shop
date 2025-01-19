import axios from "axios";

class OrderService {

    static BASE_URL = "http://localhost:8080"


    


    static async addOrder(formData,token) {

        try {
            const response = await fetch(`${OrderService.BASE_URL}/adminuser/add_order`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`, // Dodanie Bearer Tokena
                    'Content-Type': 'application/json' // Nagłówek JSON
                },
                body: formData // Wysłanie danych jako form-data
            });
            console.log(response);
            if (!response.ok) {
                throw new Error('Błąd podczas dodawania produktu');
            }

            const data = await response.json();
            console.log('Produkt dodany:', data);
            // navigate('/products'); // Przekierowanie po sukcesie

        } catch (err) {
            // setError(err.message);
            console.error('Błąd:', err);
        }
    }

    static async getUserOrders(token) {

        try {
            const response = await fetch(`${OrderService.BASE_URL}/adminuser/orders`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`, // Dodanie Bearer Tokena
                },
            });
            console.log(response);
            if (!response.ok) {
                throw new Error('Błąd podczas dodawania produktu');
            }

            const data = await response.json();
            console.log("Datta");
            console.log(data);
            return data;
            // navigate('/products'); // Przekierowanie po sukcesie

        } catch (err) {
            // setError(err.message);
            console.error('Błąd:', err);
        }
    }


    static async getAllCustomersOrders(token) {

        try {
            const response = await fetch(`${OrderService.BASE_URL}/admin/allOrders`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`, // Dodanie Bearer Tokena
                },
            });
            console.log(response);
            if (!response.ok) {
                throw new Error('Błąd podczas dodawania produktu');
            }

            const data = await response.json();
            console.log("Datta");
            console.log(data);
            return data;
            // navigate('/products'); // Przekierowanie po sukcesie

        } catch (err) {
            // setError(err.message);
            console.error('Błąd:', err);
        }
    }













}

export default OrderService;