package com.spencer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JokeRepository
 *
 * Manages persistence of jokes to local file, handling all file I/O operations.
 */
public class JokeRepo {
    private final Path localFavoritesPath;

    public static void main(String[] args) {
        // Initialize using the constructor that takes a string path
        JokeRepo repo = new JokeRepo("src/main/resources/jokes/favorites.json");

        NorrisApiClient client = new NorrisApiClient();

        PoJoke joke = client.getRandomPojoke();

        // Test saving a joke
        repo.saveLocalFavorite(joke);

        // Print all jokes
        List<PoJoke> jokes = repo.getLocalFavorites();
        for (PoJoke j : jokes) {
            System.out.println(j.getId() + ": " + j.getValue());
        }

        // Test removing a joke
//        repo.removeLocalFavorite(joke.getId());
//        System.out.println("After removal: " + repo.getLocalFavorites().size() + " jokes remaining");
    }

    /**
     * Constructor that takes a file path as a string
     *
     * @param filePath Path to the jokes JSON file
     */
    public JokeRepo(String filePath) {
        // Initialize the path field and ensure it's not null
        this.localFavoritesPath = Paths.get(filePath);
        // Create the JSON file if it doesn't exist
        createLocalJson();
    }

    /**
     * Default constructor - uses a default path for the jokes file
     */
    public JokeRepo() {
        // Use a default path if none is provided
        this.localFavoritesPath = Paths.get("src/main/resources/jokes/favorites.json");
        // Create the JSON file if it doesn't exist
        createLocalJson();
    }

    public void saveLocalFavorite(PoJoke joke) {
        List<PoJoke> jokes = getLocalFavorites();

        boolean found = false;
        for (PoJoke j : jokes) {
            if (j.getId().equals(joke.getId())) {
                found = true;
                break;
            }
        }

        if (!found) {
            jokes.add(joke);

            try {
                JSONArray jsonArray = new JSONArray();
                for (PoJoke j : jokes) {
                    jsonArray.put(createPoJokeJson(j));
                }

                try (FileWriter writer = new FileWriter(localFavoritesPath.toFile())) {
                    writer.write(jsonArray.toString(4));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveArrayFavorite(ArrayList<PoJoke> jokes) {
        for (PoJoke j : jokes) {
            saveLocalFavorite(j);
        }
    }

    private JSONObject createPoJokeJson(PoJoke joke) {
        JSONObject jokeObj = new JSONObject();
        jokeObj.put("id", joke.getId());
        jokeObj.put("value", joke.getValue());
        jokeObj.put("url", joke.getUrl());
        jokeObj.put("icon_url", joke.getIconUrl());

        // Handle dates - might be null
        if (joke.getCreatedAt() != null) {
            jokeObj.put("created_at", joke.getCreatedAt().toString());
        } else {
            jokeObj.put("created_at", LocalDateTime.now().toString());
        }

        if (joke.getUpdatedAt() != null) {
            jokeObj.put("updated_at", joke.getUpdatedAt().toString());
        } else {
            jokeObj.put("updated_at", LocalDateTime.now().toString());
        }

        // Add categories
        JSONArray categoriesArray = new JSONArray();
        for (String category : joke.getCategories()) {
            categoriesArray.put(category);
        }
        jokeObj.put("categories", categoriesArray);

        return jokeObj;
    }

    public List<PoJoke> getLocalFavorites() {
        ArrayList<PoJoke> results = new ArrayList<>();

        try {
            File file = localFavoritesPath.toFile();

            if (file.exists() && file.length() > 0) {
                String content = new String(Files.readAllBytes(localFavoritesPath));
                JSONArray jsonArray = new JSONArray(content);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jokeJson = jsonArray.getJSONObject(i);
                    PoJoke joke = new PoJoke(jokeJson);
                    results.add(joke);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read jokes from favorites", e);
        }

        return results;
    }

    public void removeLocalFavorite(String jokeId) {
        List<PoJoke> jokes = getLocalFavorites();
        ArrayList<PoJoke> remaining = new ArrayList<>();

        for (PoJoke joke : jokes) {
            if (!joke.getId().equals(jokeId)) {
                remaining.add(joke);
            }
        }

        try {
            JSONArray jsonArray = new JSONArray();
            for (PoJoke j : remaining) {
                jsonArray.put(createPoJokeJson(j));
            }

            try (FileWriter writer = new FileWriter(localFavoritesPath.toFile())) {
                writer.write(jsonArray.toString(4));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to remove joke from favorites", e);
        }
    }

    public void createLocalJson() {
        File file = localFavoritesPath.toFile();

        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try {
                file.createNewFile();

                JSONArray emptyArray = new JSONArray();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(emptyArray.toString(4));
                }

                System.out.println("Created new jokes favorites file at: " + file.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create local JSON file", e);
            }
        }
    }
}