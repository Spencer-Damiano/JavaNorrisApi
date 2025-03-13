## App

### Purpose
Main application class that handles user interface and program flow.

### Variables
- `private final Scanner scanner` - For reading user input
- `private final ChuckNorrisApiClient apiClient` - For API operations
- `private final JokeRepository repository` - For persistence operations

### Methods
- `public static void main(String[] args)` - Application entry point
- `void run()` - Main program loop
- `void displayCategories()` - Shows available joke categories
- `void displayRandomJoke()` - Shows a random joke
- `void displayJokeByCategory(String categoryIndex)` - Shows a joke from selected category
- `void saveRandomJokeToFavorites()` - Saves a random joke to favorites
- `void displayLocalFavorites()` - Shows locally saved favorite jokes
- `void displayUserFavorites(String username)` - Shows a user's favorite jokes
- `void printMenu()` - Displays the application menu

## JokeRecord

### Purpose
An immutable data class that represents a Chuck Norris joke exactly as received from the API.

### Variables
- `List<String> categories` - Categories the joke belongs to
- `LocalDateTime createdAt` - When the joke was created
- `String iconUrl` - URL for the joke's image
- `String id` - Unique identifier for the joke
- `LocalDateTime updatedAt` - When the joke was last updated
- `URI url` - URL for accessing the joke
- `String value` - The joke text itself

### Methods
- Automatically generated accessors (via Record)
- `static JokeRecord fromJson(String jsonString)` - Creates a JokeRecord from JSON string

## PoJoke

### Purpose
A mutable wrapper around JokeRecord that provides additional functionality for manipulating jokes.

### Variables
- `private final JokeRecord originalJoke` - The original, immutable joke
- `private List<String> categories` - Mutable list of categories
- `private String value` - Mutable joke text

### Methods
- `JokeRecord getOriginalJoke()` - Returns the original, unmodified joke
- `void updateJokeText(String newText)` - Changes the joke text
- `void addCategory(String category)` - Adds a category
- `void removeCategory(String category)` - Removes a category
- `JokeRecord toRecord()` - Converts back to an immutable record
- `boolean hasChanges()` - Checks if any modifications were made
- `@Override boolean equals(Object o)` - Compares jokes by their ID
- `@Override int hashCode()` - Hash code based on joke ID
- `@Override String toString()` - Returns the joke text

## ChuckNorrisApiClient

### Purpose
Handles all external API communication with the Chuck Norris joke service.

### Variables
- `private static final String API_BASE_URL` - Base URL for the API
- `private final HttpClient httpClient` - For making HTTP requests
- `private final ObjectMapper objectMapper` - For JSON processing

### Methods
- `JokeRecord getRandomJoke()` - Fetches a random joke
- `JokeRecord getJokeByCategory(String category)` - Fetches a joke by category
- `List<String> getCategories()` - Gets all available joke categories

## JokeRepository

### Purpose
Manages persistence of jokes to local file, handling all file I/O operations.

### Variables
- `private final Path localFavoritesPath` - Path to local favorites file
- `private final ObjectMapper objectMapper` - For JSON serialization/deserialization


### Methods
- `void saveLocalFavorite(JokeRecord joke)` - Saves to local favorites
- `List<JokeRecord> getLocalFavorites()` - Gets all local favorites
- `void removeLocalFavorite(String jokeId)` - Removes from local favorites
- `void createLocalJson()` - create a file if not found

