import axios from "axios";

class UserService {

    static BASE_URL = "http://localhost:8080"


    static async addProduct(formData,token) {

        try {
            const response = await fetch(`${UserService.BASE_URL}/admin/add-product`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}` // Dodanie Bearer Tokena
                },
                body: formData // Wysłanie danych jako form-data
            });

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

    static async updateProduct(productId,formData,token) {
        try {
            const response = await fetch(`${UserService.BASE_URL}/admin/update-product/${productId}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}` // Dodanie Bearer Tokena
                },
                body: formData // Wysłanie danych jako form-data
            });
            console.log('Odpowiedz z backendu');
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


    static async getAllProducts() {
        const response = await axios.get(`${UserService.BASE_URL}/public/get-all-products`);
        return response.data.productsEntityList;
    }

    static async getProductById(productId) {
        const response = await axios.get(`${UserService.BASE_URL}/public/get-product-by-Id/${productId}`);
        return response.data;
    }

}

export default UserService;