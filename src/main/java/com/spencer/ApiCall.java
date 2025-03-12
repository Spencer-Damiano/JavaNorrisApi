package com.spencer;


import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApiCall {
    /*
     * Table: Summary of Key Components
     * | Component                  | Description                                      | Example Usage                          |
     * |----------------------------|--------------------------------------------------|----------------------------------------|
     * | CloseableHttpClient        | Manages HTTP connections                        | HttpClients.createDefault()            |
     * | HttpGet                    | Represents GET request                          | new HttpGet("http://targethost")       |
     * | execute                    | Sends request and gets response                 | httpclient.execute(httpGet)            |
     * | getStatusLine              | Gets HTTP status line                           | response1.getStatusLine()              |
     * | getEntity                  | Gets response body                              | response1.getEntity()                  |
     * | EntityUtils.toString       | Converts entity to string                       | EntityUtils.toString(entity1)          |
     * | EntityUtils.consume        | Releases entity resources                       | EntityUtils.consume(entity1)           |
     * | close                      | Closes response to free resources               | response1.close()                      |
     */

    public static Map<Integer, String> categoryMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        getCategories();
        getJoke();
        getJoke(1);
//        getQuery("9ovbd5b1t66_x92jwrq1yq"); // This was to try and make the query call but I'm not sure how it works.
//        getQuery("ufgjcp0_qrugwggnmqhvdq"); // I'm not going to worry about it right now cause I don't think it's that helpful
    }

    public static void getCategories() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 'CloseableHttpClient' used without 'try'-with-resources statement

            // Chuck Norris Joke Categories
            HttpGet httpGet = new HttpGet("https://api.chucknorris.io/jokes/categories");

            try (ClassicHttpResponse response = httpClient.execute(httpGet)) { // warning on this line: `'execute(org.apache.hc.core5.http.ClassicHttpRequest)' is deprecated`

                if (response.getCode() != 200) {
                    System.out.println(response.getCode() + " " + response.getReasonPhrase());
                }

                String responseBody = EntityUtils.toString(response.getEntity());
//                System.out.println(responseBody);

                if (categoryMap.isEmpty()) {
                    JSONArray jsonArray = new JSONArray(responseBody);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        categoryMap.put(i + 1, jsonArray.getString(i));
                    }
                }

                for (Map.Entry<Integer, String> entry : categoryMap.entrySet()) {
                    System.out.println(entry.getKey() + " " + entry.getValue());
                }

                System.out.println("0 RETURN TO MENU");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void getJoke() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("https://api.chucknorris.io/jokes/random");

            try (ClassicHttpResponse response = httpClient.execute(httpGet)) {
                printJoke(response);
            }
        }
    }

    public static void getJoke(int categoryIndex) throws IOException {
        if (!categoryMap.containsKey(categoryIndex)) {
            System.out.println("Invalid category index: " + categoryIndex);
            return;
        }

        String category = categoryMap.get(categoryIndex);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("https://api.chucknorris.io/jokes/random?category=" + category);

            try (ClassicHttpResponse response = httpClient.execute(httpGet)) {
                printJoke(response);
            }
        }
    }

    public static void getQuery (String query) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("https://api.chucknorris.io/jokes/search?query=" + query);

            try (ClassicHttpResponse response = httpClient.execute(httpGet)) {
                printJoke(response);
            }
        }
    }

    public static void printJoke(ClassicHttpResponse response) throws IOException {
        try {
            if (response.getCode() != 200) {
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
            }
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject jokeJson = new JSONObject(responseBody);
                System.out.println(responseBody);

            if (jokeJson.has("value")) {
                String jokeStr = jokeJson.getString("value");
                System.out.println(jokeStr);

//                String query = jokeJson.getString("id"); // Come back to figure out how to query
//                System.out.println(query);
            } else {
                System.out.println("json isn't working as expected");
            }
        } catch (ParseException e) {
            System.out.println("Error printing response: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}