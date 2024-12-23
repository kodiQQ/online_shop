export class ProductsToOrder {
    constructor(productId, number) {
        this._productId = productId; // Prywatna właściwość
        this._number = number; // Prywatna właściwość
    }

    // Getter dla productId
    get productId() {
        return this._productId;
    }

    // Setter dla productId
    set productId(newProductId) {
        // Tutaj możesz dodać jakąś walidację, np. sprawdzenie czy nowy ID jest liczbą
        if (typeof newProductId === 'number') {
            this._productId = newProductId;
        } else {
            console.error('Nieprawidłowy typ danych dla productId');
        }
    }

    // Getter dla number
    get number() {
        return this._number;
    }

    // Setter dla number
    set number(newNumber) {
        // Tutaj możesz dodać jakąś walidację, np. sprawdzenie czy liczba jest dodatnia
        if (typeof newNumber === 'number' && newNumber > 0) {
            this._number = newNumber;
        } else {
            console.error('Nieprawidłowy typ danych lub wartość dla number');
        }
    }

    toJSON() {
        return {
            productId: this._productId, // Zwracamy bez podkreśleń
            productNumber: Number(this._number)
        };
    }

}
export default ProductsToOrder; // Eksport domyślny
