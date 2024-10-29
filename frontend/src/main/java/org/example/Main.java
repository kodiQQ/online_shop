package org.example;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.EndOfFileException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;


public class Main {
    private static final String BASE_URL = "http://localhost:8080";

    public static void main(String[] args) {
        try {
            LineReader lineReader = LineReaderBuilder.builder().build();
            String token = null;

            while (true) {
                System.out.println("\nWybierz opcję:");
                System.out.println("1. Zarejestruj użytkownika");
                System.out.println("2. Zaloguj się");
                System.out.println("3. Dodaj produkt (administrator)");
                System.out.println("4. Pobierz wszystkie produkty (administrator)");
                System.out.println("5. Pobierz wszystkich użytkowników (administrator)");
                System.out.println("6. Pobierz użytkownika po ID (administrator)");
                System.out.println("7. Zaktualizuj użytkownika (administrator)");
                System.out.println("8. Usuń użytkownika (administrator)");
                System.out.println("9. Usuń produkt (administrator)");
                System.out.println("10. Dodaj zamówienie (użytkownik)");
                System.out.println("11. Usuń zamówienie (administrator)");
                System.out.println("0. Wyjdź");

                String input = lineReader.readLine("> ");

                switch (input) {
                    case "1":
                        System.out.print("Podaj dane rejestracji\n ");

                        System.out.println("Podaj email: ");
                        String email = lineReader.readLine();
                        System.out.println("Podaj nazwę: ");
                        String name = lineReader.readLine();
                        System.out.println("Podaj hasło: ");
                        String password = lineReader.readLine();
                        System.out.println("Podaj miasto: ");
                        String city = lineReader.readLine();
                        System.out.println("Podaj rolę: ");
                        String role = lineReader.readLine();

                        String RegisterjsonInput = String.format("{\"email\": \"%s\",\"name\": \"%s\", \"password\": \"%s\", \"city\": \"%s\", \"role\": \"%s\"}", email, name, password, city, role);

                        sendPostRequest("/auth/register", RegisterjsonInput, null);
                        break;

                    case "2":
                        System.out.print("Podaj dane logowania\n ");

                        System.out.println("Podaj email: ");
                        email = lineReader.readLine();
                        System.out.println("Podaj hasło: ");
                        password = lineReader.readLine();

                        String LoginjsonInput = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);
                        String json = sendPostRequest("/auth/login", LoginjsonInput, null);

                        JSONObject jsonObject = new JSONObject(json);
                        token = jsonObject.getString("token");
                        break;

                    case "3":
                        System.out.print("Podaj dane produktu do dodania\n ");

                        System.out.println("Podaj nazwę produktu: ");
                        String productName = lineReader.readLine();
                        System.out.println("Podaj kategorię: ");
                        String category = lineReader.readLine();
                        System.out.println("Podaj cenę: ");
                        String price = lineReader.readLine();

                        String ProductjsonInput = String.format("{\"name\": \"%s\", \"category\": \"%s\", \"price\": \"%s\"}", productName, category, price);

                        String addProductResponse = sendPostRequest("/admin/add-product", ProductjsonInput, token);
                        if (addProductResponse != null) {
                            System.out.println("Odpowiedź z dodawania produktu: " + addProductResponse);
                        }
                        break;

                    case "4":
                        sendGetRequest("/admin/get-all-products", token);
                        break;

                    case "5":
                        sendGetRequest("/admin/get-all-users", token);
                        break;

                    case "6":
                        System.out.print("Podaj ID użytkownika: ");
                        String userIdToFetch = lineReader.readLine();
                        sendGetRequest("/admin/get-users/" + userIdToFetch, token);
                        break;

                    case "7":
                        System.out.print("Podaj ID użytkownika do aktualizacji: ");
                        String userIdToUpdate = lineReader.readLine();
                        System.out.print("Podaj nowe dane użytkownika : ");

                        System.out.println("Podaj email: ");
                        email = lineReader.readLine();
                        System.out.println("Podaj nazwę: ");
                        name = lineReader.readLine();
                        System.out.println("Podaj miasto: ");
                        city = lineReader.readLine();
                        System.out.println("Podaj rolę: ");
                        role = lineReader.readLine();
                        System.out.println("Podaj hasło: ");
                        password = lineReader.readLine();

                        String UpdatejsonInput = String.format("{\"email\": \"%s\",\"name\": \"%s\", \"city\": \"%s\", \"role\": \"%s\", \"password\": \"%s\"}", email, name, city, role, password);

                        // Wydrukuj dane do aktualizacji dla debugowania
                        System.out.println("Wysyłane dane do aktualizacji: " + UpdatejsonInput);
                        sendPutRequest("/admin/update/" + userIdToUpdate, UpdatejsonInput, token);
                        break;

                    case "8":
                        System.out.print("Podaj ID użytkownika do usunięcia: ");
                        String userIdToDelete = lineReader.readLine();
                        sendDeleteRequest("/admin/delete-user/" + userIdToDelete, token);
                        break;

                    case "9":
                        System.out.print("Podaj ID produktu do usunięcia: ");
                        String productIdToDelete = lineReader.readLine();
                        sendDeleteRequest("/admin/delete-product/" + productIdToDelete, token);
                        break;

                    case "10":
//                        System.out.print("Podaj dane zamówienia do dodania (JSON): ");
//                        String addOrderData = lineReader.readLine();
//                        sendPostRequest("/adminuser/add_order", addOrderData, token);
//                        break;

                        System.out.print("Podaj dane zamówienia do dodania\n");

//                        System.out.println("Podaj ID użytkownika: ");
//                        String userId = lineReader.readLine();

//                        System.out.println("Podaj liczbę produktów w zamówieniu: ");
//                        int productCount = Integer.parseInt(lineReader.readLine());

                        List<String> productIds = new ArrayList<String>();
                        int i=0;
                        while(true) {
                            i=i+1;
                            System.out.println("Podaj ID produktu " + (i) + ": \n(wpisz \"x\", aby zakończyć dodawanie)");
                            String productId = lineReader.readLine();
                            if(productId.equals("x")) {
                                break;
                            }
                            productIds.add(productId);  // Dodajemy ID produktu w cudzysłowie dla formatu JSON
                        }
                        String productsJsonArray = "{\"products_id_list\": [";

                        // Tworzymy tablicę JSON ręcznie, aby mieć pełną kontrolę nad formatem
                        for(i=0;i<productIds.size();i++) {
                            productsJsonArray = productsJsonArray + productIds.get(i);
                            if(i!=productIds.size()-1){
                                productsJsonArray = productsJsonArray + ",";
                            }

                        }
                        productsJsonArray = productsJsonArray + "]}";

//                        String orderJsonInput = String.format("{\"userId\": \"%s\", \"productIds\": %s}", userId, productsJsonArray);

                        String addOrderResponse = sendPostRequest("/adminuser/add_order",productsJsonArray, token);
                        if (addOrderResponse != null) {
                            System.out.println("Odpowiedź z dodawania zamówienia: " + addOrderResponse);
                        }
                        break;



                    case "11":
                        System.out.print("Podaj ID zamówienia do usunięcia: ");
                        String orderIdToDelete = lineReader.readLine();
                        sendDeleteRequest("/admin/delete_order/" + orderIdToDelete, token);
                        break;

                    case "0":
                        System.out.println("Do widzenia!");
                        return;

                    default:
                        System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
                }
            }
        } catch (UserInterruptException | EndOfFileException e) {
            System.out.println("\nWyjście z aplikacji.");
        }
    }



    private static void sendGetRequest(String endpoint, String token) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("GET request response code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream responseStream = conn.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream))) {

                    // Pobranie i połączenie odpowiedzi w jeden ciąg
                    String response = reader.lines().collect(Collectors.joining("\n"));

                    // Formatowanie JSON, aby wypisać z wcięciem
                    if (response.startsWith("[")) {
                        JSONArray jsonArray = new JSONArray(response);
                        System.out.println(jsonArray.toString(4)); // 4 oznacza poziom wcięcia
                    } else {
                        JSONObject jsonObject = new JSONObject(response);
                        System.out.println(jsonObject.toString(4)); // 4 oznacza poziom wcięcia
                    }
                }
            } else {
                System.out.println("Error in GET request: " + responseCode);
            }

        } catch (IOException e) {
            System.out.println("Błąd podczas wysyłania GET: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    private static String sendPostRequest(String endpoint, String data, String token) {

        System.out.println(token);
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            // Wysłanie danych JSON
            try (var os = conn.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("POST request response code: " + responseCode);

            // Odczyt odpowiedzi serwera
            StringBuilder responseBuilder = new StringBuilder();
            try (InputStream responseStream = conn.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream))) {
                String responseLine;
                while ((responseLine = reader.readLine()) != null) {
                    responseBuilder.append(responseLine);
                }
            }
            String response = responseBuilder.toString();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                return response;
            } else {
                System.out.println("Error in POST request: " + responseCode + ", response: " + response);
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

    private static void sendPutRequest(String endpoint, String data, String token) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(BASE_URL + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            try (var os = conn.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("PUT request response code: " + responseCode);

            // Odczyt odpowiedzi
            StringBuilder responseBuilder = new StringBuilder();
            try (InputStream responseStream = conn.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream))) {
                String responseLine;
                while ((responseLine = reader.readLine()) != null) {
                    responseBuilder.append(responseLine);
                }
            }
            String response = responseBuilder.toString();
            System.out.println("Odpowiedź z PUT: " + response);

            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
                System.out.println("Błąd w PUT: " + responseCode + ", odpowiedź: " + response);
            }
        } catch (IOException e) {
            System.out.println("Błąd podczas wysyłania PUT: " + e.getMessage());
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
