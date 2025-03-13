package com.spencer;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class NorrisApiClient {

    public Map<String, String> catagoryMap = new TreeMap<String, String>();
    public ArrayList<String> categoryList = new ArrayList<String>();

    public static void main(String[] args) {
        NorrisApiClient client = new NorrisApiClient();
        System.out.println("from NorrisApiClient");


        JSONObject jsonTest = client.getRandomResponseRaw();
        System.out.println(jsonTest.toString());
        PoJoke jokeOne = client.getRandomPojoke();

        System.out.println(jokeOne.getId());

        PoJoke jokeCat = client.getCatagoryPojoke("5");
        System.out.println(jokeCat.getId());

        client.printCategories();

        ArrayList<PoJoke> queryTest = client.getQueryResponseRaw("roman");

        for (PoJoke pojoke : queryTest) {
            System.out.println(pojoke);
        }

        ArrayList<PoJoke> queryJunk = client.getQueryResponseRaw("thshjethjkdsgjkf");

        if (queryJunk.size() == 0) {
            System.out.println();
            System.out.println("No Junk found");
        } else {
            for (PoJoke pojoke : queryJunk) {
                System.out.println(pojoke);
            }
        }



    }

    public PoJoke getRandomPojoke () {
        return new PoJoke(getRandomResponseRaw());
    }

    public PoJoke getCatagoryPojoke (String category) {
        return new PoJoke(getCatagoryResponseRaw(category));
    }

    public JSONObject getCatagoryResponseRaw(String cat) {
        String catagory = null;

        if (catagoryMap.isEmpty()) {
            System.out.println("setting Catagories");
            setCategories();
        }

        if (categoryList.contains(cat)) {
            catagory = cat;
        } else if (catagoryMap.containsKey(cat)) {
            catagory = catagoryMap.get(cat);
        }


        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.chucknorris.io/jokes/random?category=" + catagory)
                .get()
                .build();

        return getJsonResponse(request);
    }

    public JSONObject getRandomResponseRaw() {
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.chucknorris.io/jokes/random")
                .get()
                .build();

        return getJsonResponse(request);
    }


    private JSONObject getJsonResponse(Request req) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        JSONObject jsonResponse = null;
        Response response = null;

        try {
            response = client.newCall(req).execute();
            jsonResponse = new JSONObject(response.body().string());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response.close();
        return jsonResponse;
    }

    public void setCategories() {
        if (catagoryMap.isEmpty()) {
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.chucknorris.io/jokes/categories")
                    .get()
                    .build();


            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            JSONObject jsonResponse = null;
            JSONArray jsonArray = null;
            Response response = null;

            try {
                response = client.newCall(request).execute();
                jsonArray = new JSONArray(response.body().string());
//                System.out.println(jsonArray.toString());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            response.close();

            for (int i = 0; i < jsonArray.length(); i++) {
                categoryList.add(jsonArray.getString(i));
                String key = Integer.toString(i + 1);
                catagoryMap.put(key, categoryList.get(i));

            }
        }
    }

    public void printCategories() {
        if (catagoryMap.isEmpty()) {
            setCategories();
        }

        for (Map.Entry<String, String> entry : catagoryMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public ArrayList<PoJoke> queryResponsePojokes (String query) {
        ArrayList<PoJoke> pojokes = new ArrayList<>();
        return getQueryResponseRaw(query);

    }

    private ArrayList<PoJoke> getQueryResponseRaw(String query) {
        ArrayList<PoJoke> results = new ArrayList<>();

        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.chucknorris.io/jokes/search?query=" + query)
                .get()
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        JSONObject jsonResponse = null;
        Response response = null;

        try {
            response = client.newCall(request).execute();
            jsonResponse = new JSONObject(response.body().string());

            // Check if we have results
            int total = jsonResponse.getInt("total");

            if (total > 0) {
                JSONArray resultArray = jsonResponse.getJSONArray("result");

                // Process each joke in the results
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject jokeJson = resultArray.getJSONObject(i);
                    PoJoke joke = new PoJoke(jokeJson);
                    results.add(joke);
                }
            }
            // If total is 0, we'll just return the empty ArrayList

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return results;
    }
}
