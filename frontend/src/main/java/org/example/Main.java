package org.example;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main {
    private static final String BASE_URL = "http://localhost:8080";

    private static ArrayList<Integer> basket = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Screen screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();

        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        Window window = new BasicWindow("Menu aplikacji");

        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(1));

        Label label = new Label("Wybierz opcję z menu:");
        panel.addComponent(label);

        String[] options = {
                "1. Zarejestruj użytkownika",
                "2. Zaloguj się",
                "3. Dodaj produkt (administrator)",
                "4. Pobierz wszystkie produkty",
                "5. Pobierz wszystkich użytkowników (administrator)",
                "6. Pobierz użytkownika po ID (administrator)",
                "7. Zaktualizuj użytkownika (administrator)",
                "8. Usuń użytkownika (administrator)",
                "9. Usuń produkt (administrator)",
                "10. Dodaj zamówienie (użytkownik)",
                "11. Usuń zamówienie (administrator)",
                "12. Dodaj do koszyka",
                "13. Pokaż zawartość koszyka",
                "0. Wyjdź"
        };

        for (String option : options) {
            panel.addComponent(new Button(option, () -> handleOption(option, gui, token)));
        }

        window.setComponent(panel);
        gui.addWindowAndWait(window);
        screen.stopScreen();
    }

    private static void handleOption(String option, MultiWindowTextGUI gui, String token) {
        try {
            switch (option) {
                case "1. Zarejestruj użytkownika":
                    openRegisterWindow(gui);
                    break;

                case "2. Zaloguj się":
                    openLoginWindow(gui);
                    break;

                case "3. Dodaj produkt (administrator)":
                    openAddProductWindow(gui, token);
                    break;

                case "4. Pobierz wszystkie produkty":
                    String productsJson = sendGetRequest("/public/get-all-products", token);  // Pobranie danych z serwera
                    if (productsJson != null) {
                        try {
                            // Debugowanie: Wypisanie odpowiedzi JSON
                            System.out.println("Odpowiedź JSON w Lanternie: " + productsJson);

                            // Parsowanie odpowiedzi jako obiekt JSON
                            JSONObject jsonObject = new JSONObject(productsJson);

                            // Sprawdzamy, czy istnieje pole "productsList" w odpowiedzi
                            if (jsonObject.has("productsList")) {
                                JSONArray products = jsonObject.getJSONArray("productsList");

                                StringBuilder productsList = new StringBuilder("Produkty:\n");

                                // Iterowanie po produktach i formatowanie tekstu
                                for (int i = 0; i < products.length(); i++) {
                                    JSONObject product = products.getJSONObject(i);
                                    int id = product.getInt("id");
                                    String name = product.getString("name");
                                    String category = product.getString("category");
                                    String price = product.getString("price");  // Cena jest jako String, bo może zawierać wartości takie jak "150" bez jednostki

                                    // Dodawanie danych do wyświetlania
                                    productsList.append(String.format("id: %d - nazwa: %s - kategoria: %s - cena: %s\n", id, name, category, price));
                                }

                                // Wyświetlanie produktów w oknie Lanterny
                                showInfoMessage(gui, "Produkty", productsList.toString());
                            } else {
                                showInfoMessage(gui, "Błąd", "Brak danych produktów w odpowiedzi.");
                            }
                        } catch (JSONException e) {
                            // Obsługa sytuacji, gdy nie udało się sparsować odpowiedzi
                            showInfoMessage(gui, "Błąd", "Błąd podczas parsowania odpowiedzi JSON.");
                        }
                    } else {
                        // Obsługa sytuacji, gdy nie udało się pobrać produktów
                        showInfoMessage(gui, "Błąd", "Nie udało się pobrać produktów.");
                    }
                    break;

                case "5. Pobierz wszystkich użytkowników (administrator)":
                    String usersJson = sendGetRequest("/admin/get-all-users", token);  // Pobranie danych użytkowników z serwera
                    if (usersJson != null) {
                        try {
                            // Debugowanie: Wypisanie odpowiedzi JSON w terminalu
                            System.out.println("Odpowiedź JSON w Lanternie: " + usersJson);

                            // Parsowanie odpowiedzi jako obiekt JSON
                            JSONObject jsonObject = new JSONObject(usersJson);

                            // Sprawdzamy, czy istnieje pole "ourUsersList" w odpowiedzi
                            if (jsonObject.has("ourUsersList")) {
                                JSONArray users = jsonObject.getJSONArray("ourUsersList");

                                StringBuilder usersList = new StringBuilder("Użytkownicy:\n");

                                // Iterowanie po użytkownikach i formatowanie tekstu
                                for (int i = 0; i < users.length(); i++) {
                                    JSONObject user = users.getJSONObject(i);
                                    int id = user.getInt("id");
                                    String name = user.getString("name");
                                    String email = user.getString("email");
                                    String city = user.getString("city");  // Miasto

                                    // Dodawanie danych do wyświetlania
                                    usersList.append(String.format("id: %d - nazwa: %s - email: %s - miasto: %s\n", id, name, email, city));
                                }

                                // Wyświetlanie użytkowników w oknie Lanterny
                                showInfoMessage(gui, "Użytkownicy", usersList.toString());
                            } else {
                                // Obsługa sytuacji, gdy nie ma użytkowników w odpowiedzi
                                showInfoMessage(gui, "Błąd", "Brak danych użytkowników w odpowiedzi.");
                            }
                        } catch (JSONException e) {
                            // Obsługa sytuacji, gdy nie udało się sparsować odpowiedzi
                            showInfoMessage(gui, "Błąd", "Błąd podczas parsowania odpowiedzi JSON.");
                        }
                    } else {
                        // Obsługa sytuacji, gdy nie udało się pobrać użytkowników
                        showInfoMessage(gui, "Błąd", "Nie udało się pobrać użytkowników.");
                    }
                    break;

                case "6. Pobierz użytkownika po ID (administrator)":
                    // Okno do wprowadzenia ID użytkownika
                    Window getUserByIdWindow = new BasicWindow("Pobierz użytkownika po ID");

                    Panel getUserPanel = new Panel();
                    getUserPanel.setLayoutManager(new GridLayout(2));

                    TextBox userIdBox = new TextBox();  // Pole tekstowe do wprowadzenia ID użytkownika

                    getUserPanel.addComponent(new Label("Podaj ID użytkownika:"));
                    getUserPanel.addComponent(userIdBox);

                    Button confirmButton = new Button("Pobierz", () -> {
                        String userId = userIdBox.getText();  // Pobranie ID użytkownika z pola tekstowego

                        // Wysyłanie żądania GET na endpoint z ID użytkownika
                        System.out.println("USERID: "+userId);
                        String url="/admin/get-users/"+userId;
                        System.out.println("URL: "+url);
                        System.out.println(token);
                        String userJson = sendGetRequest( url, token);

                        if (userJson != null) {
                            try {
                                // Debugowanie: Wypisanie odpowiedzi JSON w terminalu
                                System.out.println("Odpowiedź JSON w Lanternie: " + userJson);

                                // Parsowanie odpowiedzi jako obiekt JSON
                                JSONObject jsonObject = new JSONObject(userJson);
                                System.out.println(jsonObject);
                                // Sprawdzamy, czy odpowiedź zawiera dane użytkownika
                                if (jsonObject.has("ourUsers")) {
                                    JSONObject user = jsonObject.getJSONObject("ourUsers");
                                    System.out.println(user);
                                    int id = user.getInt("id");
                                    String name = user.getString("name");
                                    String email = user.getString("email");
                                    String city = user.getString("city");  // Miasto

                                    // Wyświetlanie użytkownika w oknie Lanterny
                                    StringBuilder userInfo = new StringBuilder(String.format("ID: %d\nNazwa: %s\nEmail: %s\nMiasto: %s", id, name, email, city));
                                    showInfoMessage(gui, "Użytkownik", userInfo.toString());
                                } else {
                                    showInfoMessage(gui, "Błąd", "Nie znaleziono użytkownika o podanym ID.");
                                }
                            } catch (JSONException e) {
                                // Obsługa błędu parsowania odpowiedzi JSON
                                showInfoMessage(gui, "Błąd", "Błąd podczas parsowania odpowiedzi JSON.");
                            }
                        } else {
                            // Obsługa sytuacji, gdy nie udało się pobrać użytkownika
                            showInfoMessage(gui, "Błąd", "Nie udało się pobrać użytkownika.");
                        }
                        getUserByIdWindow.close();  // Zamknięcie okna po pobraniu użytkownika
                    });

                    Button cancelButton = new Button("Anuluj", getUserByIdWindow::close);

                    getUserPanel.addComponent(confirmButton);
                    getUserPanel.addComponent(cancelButton);

                    getUserByIdWindow.setComponent(getUserPanel);
                    gui.addWindowAndWait(getUserByIdWindow);  // Oczekiwanie na akcję użytkownika
                    break;




                case "7. Zaktualizuj użytkownika (administrator)":
                    showInfoMessage(gui, "Zaktualizuj użytkownika", "Podaj dane do aktualizacji.");
                    openUpdateUserWindow(gui, token);
                    break;

                case "8. Usuń użytkownika (administrator)":
                    showInfoMessage(gui, "Usuń użytkownika", "Podaj ID użytkownika do usunięcia.");
                    openDeleteUserWindow(gui,token);
                    break;

                case "9. Usuń produkt (administrator)":
                    showInfoMessage(gui, "Usuń produkt", "Podaj ID produktu do usunięcia.");
                    openDeleteProductWindow(gui,token);
                    break;

                case "10. Dodaj zamówienie (użytkownik)":
                    openShowBasket(gui);
                    openAddOrderWindow(gui, token);
//                    showInfoMessage(gui, "Dodaj zamówienie", "Logika dodawania zamówienia.");
                    break;

                case "11. Usuń zamówienie (administrator)":
                    showInfoMessage(gui, "Usuń zamówienie", "Podaj ID zamówienia do usunięcia.");
                    break;

                case "12. Dodaj do koszyka":
                    openAddProductToBasketWindow(gui,token);
                    break;

                case "13. Pokaż zawartość koszyka":
                    openShowBasket(gui);
                    break;

                case "0. Wyjdź":
                    System.exit(0);

                default:
                    showInfoMessage(gui, "Błąd", "Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        } catch (Exception e) {
            showInfoMessage(gui, "Błąd", e.getMessage());
        }
    }

    private static boolean checkIfStringIsNumber(String str) {
        try {
            int result = Integer.parseInt(str); // Zły format
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void openShowBasket(MultiWindowTextGUI gui){

                    // Iterowanie po produktach i formatowanie tekstu
        String allProducts="";
                    for (int i = 0; i < basket.size(); i++) {
                        String jsonString=sendGetItemRequest("/public/get-product-by-Id/"+basket.get(i));
                        System.out.println(jsonString);
                        JSONObject json = new JSONObject(jsonString);

                        // Wyciągamy statusCode
                        int statusCode = json.getInt("statusCode");

                        // Wyciągamy obiekt "products"
                        JSONObject products = json.getJSONObject("products");

                        // Wyciągamy dane z obiektu "products"
                        int id = products.getInt("id");
                        String name = products.getString("name");
                        String category = products.getString("category");
                        String price = products.getString("price");

                        allProducts=allProducts+id+","+name+","+category+","+price+"\n";



                    }

        showInfoMessage(gui, "Produkty:", allProducts);


    }

    private static void openRegisterWindow(MultiWindowTextGUI gui) {
        Window registerWindow = new BasicWindow("Rejestracja użytkownika");

        Panel registerPanel = new Panel();
        registerPanel.setLayoutManager(new GridLayout(2));

        TextBox emailBox = new TextBox();
        TextBox nameBox = new TextBox();
        TextBox passwordBox = new TextBox().setMask('*');
        TextBox cityBox = new TextBox();
        TextBox roleBox = new TextBox();

        registerPanel.addComponent(new Label("Email:"));
        registerPanel.addComponent(emailBox);
        registerPanel.addComponent(new Label("Nazwa:"));
        registerPanel.addComponent(nameBox);
        registerPanel.addComponent(new Label("Hasło:"));
        registerPanel.addComponent(passwordBox);
        registerPanel.addComponent(new Label("Miasto:"));
        registerPanel.addComponent(cityBox);
        registerPanel.addComponent(new Label("Rola:"));
        registerPanel.addComponent(roleBox);

        Button confirmButton = new Button("Zarejestruj", () -> {
            String email = emailBox.getText();
            String name = nameBox.getText();
            String password = passwordBox.getText();
            String city = cityBox.getText();
            String role = roleBox.getText();

            String registerJsonInput = String.format(
                    "{\"email\": \"%s\",\"name\": \"%s\", \"password\": \"%s\", \"city\": \"%s\", \"role\": \"%s\"}",
                    email, name, password, city, role);

            sendPostRequest("/auth/register", registerJsonInput, null);
            registerWindow.close();
        });

        Button cancelButton = new Button("Anuluj", registerWindow::close);

        registerPanel.addComponent(confirmButton);
        registerPanel.addComponent(cancelButton);

        registerWindow.setComponent(registerPanel);
        gui.addWindowAndWait(registerWindow);
    }

    private static String token = null;  // Zmienna przechowująca token

    private static void openLoginWindow(MultiWindowTextGUI gui) {
        Window loginWindow = new BasicWindow("Logowanie");

        Panel loginPanel = new Panel();
        loginPanel.setLayoutManager(new GridLayout(2));

        TextBox emailBox = new TextBox();
        TextBox passwordBox = new TextBox().setMask('*');

        loginPanel.addComponent(new Label("Email:"));
        loginPanel.addComponent(emailBox);
        loginPanel.addComponent(new Label("Hasło:"));
        loginPanel.addComponent(passwordBox);

        Button confirmButton = new Button("Zaloguj", () -> {
            String email = emailBox.getText();
            String password = passwordBox.getText();

            String loginJsonInput = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);
            String json = sendPostRequest("/auth/login", loginJsonInput, null);
//            System.out.println(json);
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);

                token = jsonObject.getString("token");  // Przechowujemy token
                System.out.println(token);
                showInfoMessage(gui, "Logowanie", "Zalogowano pomyślnie. Token: " + token);
            }
            loginWindow.close();
        });

        Button cancelButton = new Button("Anuluj", loginWindow::close);

        loginPanel.addComponent(confirmButton);
        loginPanel.addComponent(cancelButton);

        loginWindow.setComponent(loginPanel);
        gui.addWindowAndWait(loginWindow);
    }

    private static void openAddProductWindow(MultiWindowTextGUI gui, String token) {
        Window addProductWindow = new BasicWindow("Dodaj produkt");

        Panel productPanel = new Panel();
        productPanel.setLayoutManager(new GridLayout(2));

        TextBox productNameBox = new TextBox();
        TextBox categoryBox = new TextBox();
        TextBox priceBox = new TextBox();

        productPanel.addComponent(new Label("Nazwa produktu:"));
        productPanel.addComponent(productNameBox);
        productPanel.addComponent(new Label("Kategoria:"));
        productPanel.addComponent(categoryBox);
        productPanel.addComponent(new Label("Cena:"));
        productPanel.addComponent(priceBox);

        Button confirmButton = new Button("Dodaj", () -> {
            String productName = productNameBox.getText();
            String category = categoryBox.getText();
            String price = priceBox.getText();

            // Formatowanie danych do JSON
            String productJsonInput = String.format("{\"name\": \"%s\", \"category\": \"%s\", \"price\": \"%s\"}", productName, category, price);

            // Wysyłanie żądania POST
            String addProductResponse = sendPostRequest("/admin/add-product", productJsonInput, token);

            if (addProductResponse != null) {
                showInfoMessage(gui, "Dodano produkt", "Odpowiedź z serwera: " + addProductResponse);
            } else {
                showInfoMessage(gui, "Błąd", "Nie udało się dodać produktu.");
            }
            addProductWindow.close();
        });

        Button cancelButton = new Button("Anuluj", addProductWindow::close);

        productPanel.addComponent(confirmButton);
        productPanel.addComponent(cancelButton);

        addProductWindow.setComponent(productPanel);
        gui.addWindowAndWait(addProductWindow);
    }


    private static void openAddProductToBasketWindow(MultiWindowTextGUI gui, String token) {
        Window addProductWindow = new BasicWindow("Dodaj produkt do koszyka");

        Panel productPanel = new Panel();
        productPanel.setLayoutManager(new GridLayout(2));

        TextBox productIdBox = new TextBox();
        TextBox quantityBox = new TextBox();

        productPanel.addComponent(new Label("Id Produktu:"));
        productPanel.addComponent(productIdBox);
        productPanel.addComponent(new Label("Liczba:"));
        productPanel.addComponent(quantityBox);



        Button confirmButton = new Button("Dodaj", () -> {
            String productId = productIdBox.getText();
            String quantity = quantityBox.getText();
            System.out.println(productId);
            System.out.println(quantity);
            System.out.println(checkIfStringIsNumber(productId));
            System.out.println(checkIfStringIsNumber(quantity));
            if(!checkIfStringIsNumber(productId)||!checkIfStringIsNumber(quantity)) {
                showInfoMessage(gui, "Błąd!", "Nie wpisałeś liczb");
            }else{
                for(int i=0;i<Integer.parseInt(quantity);i++) {
                    basket.add(Integer.parseInt(productId));
                }
                showInfoMessage(gui, "Sukces!", "Dodano do koszyka");
            }


            addProductWindow.close();
        });

        Button cancelButton = new Button("Anuluj", addProductWindow::close);

        productPanel.addComponent(confirmButton);
        productPanel.addComponent(cancelButton);

        addProductWindow.setComponent(productPanel);
        gui.addWindowAndWait(addProductWindow);
    }


    private static void openAddOrderWindow(MultiWindowTextGUI gui, String token) {
        Window addProductWindow = new BasicWindow("Złóż zamówienie");

        Panel productPanel = new Panel();
        productPanel.setLayoutManager(new GridLayout(2));


        Button confirmButton = new Button("Dodaj", () -> {

            String newJson="{\n" +
                    "  \"products_id_list\": [";

            for(int i=0;i<basket.size();i++) {
                newJson=newJson+basket.get(i)+",";
            }
            //usuniecie ostatniego przecinka:
            newJson=newJson.substring(0, newJson.length()-1);
            newJson=newJson+"]\n}";
            System.out.println(newJson);



            // Wysyłanie żądania POST
            String addProductResponse = sendPostRequest("/adminuser/add_order", newJson, token);

            if (addProductResponse != null) {
                showInfoMessage(gui, "Dodano zamówienie", "Odpowiedź z serwera: " + addProductResponse);
            } else {
                showInfoMessage(gui, "Błąd", "Nie udało się dodać zamówienia.");
            }
            addProductWindow.close();
        });

        Button cancelButton = new Button("Anuluj", addProductWindow::close);

        productPanel.addComponent(confirmButton);
        productPanel.addComponent(cancelButton);

        addProductWindow.setComponent(productPanel);
        gui.addWindowAndWait(addProductWindow);
    }





    private static void showInfoMessage(MultiWindowTextGUI gui, String title, String message) {
        Window infoWindow = new BasicWindow(title);

        Panel infoPanel = new Panel();
        infoPanel.addComponent(new Label(message));
        infoPanel.addComponent(new Button("OK", infoWindow::close));

        infoWindow.setComponent(infoPanel);
        gui.addWindowAndWait(infoWindow);
    }
    private static String sendGetItemRequest(String endpoint) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");



            int responseCode = conn.getResponseCode();
            System.out.println("GET request response code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream responseStream = conn.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream))) {

                    String response = reader.lines().collect(Collectors.joining("\n"));
                    // Wypiszmy odpowiedź w konsoli Lanterny
                    System.out.println("Odpowiedź serwera w Lanternie: " + response);  // Debugowanie

                    return response;
                }
            } else {
                System.out.println("Error in GET request: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Błąd podczas wysyłania GET: " + e.getMessage());
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static String sendGetRequest(String endpoint, String token) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("GET request response code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream responseStream = conn.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream))) {

                    String response = reader.lines().collect(Collectors.joining("\n"));
                    // Wypiszmy odpowiedź w konsoli Lanterny
                    System.out.println("Odpowiedź serwera w Lanternie: " + response);  // Debugowanie

                    return response;
                }
            } else {
                System.out.println("Error in GET request: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Błąd podczas wysyłania GET: " + e.getMessage());
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }



    private static String sendPostRequest(String endpoint, String data, String token) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            try (var os = conn.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("POST request response code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                try (InputStream responseStream = conn.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream))) {

                    return reader.lines().collect(Collectors.joining("\n"));
                }
            }
        } catch (IOException e) {
            System.out.println("Błąd podczas wysyłania POST: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    private static void openUpdateUserWindow(MultiWindowTextGUI gui, String token) {
        Window updateWindow = new BasicWindow("Aktualizuj użytkownika");

        Panel updatePanel = new Panel();
        updatePanel.setLayoutManager(new GridLayout(2));

        // Pola do wprowadzenia danych
        TextBox userIdBox = new TextBox();
        TextBox emailBox = new TextBox();
        TextBox nameBox = new TextBox();
        TextBox passwordBox = new TextBox().setMask('*');
        TextBox cityBox = new TextBox();
        TextBox roleBox = new TextBox();

        // Dodanie komponentów do formularza
        updatePanel.addComponent(new Label("ID użytkownika:"));
        updatePanel.addComponent(userIdBox);
        updatePanel.addComponent(new Label("Email:"));
        updatePanel.addComponent(emailBox);
        updatePanel.addComponent(new Label("Nazwa:"));
        updatePanel.addComponent(nameBox);
        updatePanel.addComponent(new Label("Hasło:"));
        updatePanel.addComponent(passwordBox);
        updatePanel.addComponent(new Label("Miasto:"));
        updatePanel.addComponent(cityBox);
        updatePanel.addComponent(new Label("Rola:"));
        updatePanel.addComponent(roleBox);

        Button confirmButton = new Button("Zaktualizuj", () -> {
            String userId = userIdBox.getText();
            String email = emailBox.getText();
            String name = nameBox.getText();
            String password = passwordBox.getText();
            String city = cityBox.getText();
            String role = roleBox.getText();

            String updateJsonInput = String.format(
                    "{\"email\": \"%s\",\"name\": \"%s\", \"password\": \"%s\", \"city\": \"%s\", \"role\": \"%s\"}",
                    email, name, password, city, role);

            // Wywołanie metody PUT
            sendPutRequest("/users/" + userId, updateJsonInput, token);
            updateWindow.close();
        });

        Button cancelButton = new Button("Anuluj", updateWindow::close);

        updatePanel.addComponent(confirmButton);
        updatePanel.addComponent(cancelButton);

        updateWindow.setComponent(updatePanel);
        gui.addWindowAndWait(updateWindow);
    }


    private static void openDeleteUserWindow(MultiWindowTextGUI gui, String token) {
        Window deleteWindow = new BasicWindow("Usuń użytkownika");

        Panel deletePanel = new Panel();
        deletePanel.setLayoutManager(new GridLayout(2));

        // Pola do wprowadzenia danych
        TextBox userIdBox = new TextBox();


        // Dodanie komponentów do formularza
        deletePanel.addComponent(new Label("ID użytkownika:"));
        deletePanel.addComponent(userIdBox);


        Button confirmButton = new Button("Usuń", () -> {
            String userId = userIdBox.getText();




            // Wywołanie metody PUT
            sendDeleteRequest("/admin/delete-user/" + userId,token);
            deleteWindow.close();
        });

        Button cancelButton = new Button("Anuluj", deleteWindow::close);

        deletePanel.addComponent(confirmButton);
        deletePanel.addComponent(cancelButton);

        deleteWindow.setComponent(deletePanel);
        gui.addWindowAndWait(deleteWindow);
    }


    private static void openDeleteProductWindow(MultiWindowTextGUI gui, String token) {
        Window deleteWindow = new BasicWindow("Usuń użytkownika");

        Panel deletePanel = new Panel();
        deletePanel.setLayoutManager(new GridLayout(2));

        // Pola do wprowadzenia danych
        TextBox userIdBox = new TextBox();


        // Dodanie komponentów do formularza
        deletePanel.addComponent(new Label("ID produktu:"));
        deletePanel.addComponent(userIdBox);


        Button confirmButton = new Button("Usuń", () -> {
            String userId = userIdBox.getText();




            // Wywołanie metody PUT
            sendDeleteRequest("/admin/delete-product/" + userId,token);
            deleteWindow.close();
        });

        Button cancelButton = new Button("Anuluj", deleteWindow::close);

        deletePanel.addComponent(confirmButton);
        deletePanel.addComponent(cancelButton);

        deleteWindow.setComponent(deletePanel);
        gui.addWindowAndWait(deleteWindow);
    }

    private static void sendPutRequest(String endpoint, String data, String token) {
        HttpURLConnection conn = null;
        try {
            // Tworzymy URL z endpointem
            URL url = new URL(BASE_URL + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            // Jeśli token nie jest pusty, dodajemy go do nagłówka Authorization
            if (token != null && !token.isEmpty()) {
                System.out.println("Dodano nagłówek Authorization z tokenem.");
                conn.setRequestProperty("Authorization", "Bearer " + token);
            } else {
                System.out.println("Brak tokena autoryzacji.");
            }

            // Wysyłamy dane w ciele żądania
            try (var os = conn.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Pobieramy kod odpowiedzi
            int responseCode = conn.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);

            // Odczyt odpowiedzi z serwera
            StringBuilder responseBuilder = new StringBuilder();
            try (InputStream responseStream = (responseCode >= 400) ? conn.getErrorStream() : conn.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream))) {
                String responseLine;
                while ((responseLine = reader.readLine()) != null) {
                    responseBuilder.append(responseLine);
                }
            }
            String response = responseBuilder.toString();
            System.out.println("Response: " + response);

            // Sprawdzamy kod odpowiedzi i logujemy
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Aktualizacja zakończona sukcesem.");
            } else {
                System.out.println("Błąd przy próbie aktualizacji: " + responseCode);
                System.out.println("Szczegóły błędu: " + response);
            }
        } catch (IOException e) {
            System.out.println("Błąd podczas wysyłania PUT: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static void sendDeleteRequest(String endpoint, String token) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("DELETE request response code: " + responseCode);
        } catch (IOException e) {
            System.out.println("Błąd podczas wysyłania DELETE: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}