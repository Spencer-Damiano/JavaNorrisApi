package com.spencer;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PoJoke {
    // Default icon URL
    private static final String DEFAULT_ICON_URL = "https://api.chucknorris.io/img/avatar/chuck-norris.png";

    // All fields as public final
    public final String iconUrl = DEFAULT_ICON_URL;
    public final String updatedAt;
    public final String createdAt;
    public final String id;
    public final String value;
    public final String url;
    private final List<String> categories;


    // Store the raw JSON
    private final JSONObject rawJson;
    private final String hashId;

    public PoJoke(JSONObject rawJson) {
        this.rawJson = rawJson;

        this.updatedAt = rawJson.getString("updated_at");
        this.createdAt = rawJson.getString("created_at");
        this.id = rawJson.getString("id");
        this.value = rawJson.getString("value");
        this.url = rawJson.getString("url");

        this.hashId = this.id;

        this.categories = new ArrayList<>();
        if (rawJson.has("categories") && !rawJson.getJSONArray("categories").isEmpty()) {
            JSONArray jsonArray = rawJson.getJSONArray("categories");
            for (int i = 0; i < jsonArray.length(); i++) {
                this.categories.add(jsonArray.getString(i));
            }
        }

    }

    @Nullable
    private LocalDateTime parseDateTime(String strDate){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            return LocalDateTime.parse(strDate, formatter);
        } catch (Exception e) {
            // Handle parsing error - could return null, throw custom exception,
            // or return a default date
            System.err.println("Error parsing date: " + strDate);
            return null; // or some default value
        }
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return hashId != null ? hashId.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PoJoke other = (PoJoke) obj;
        return hashId != null ? hashId.equals(other.hashId) : other.hashId == null;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getUrl() {
        return url;
    }
}
