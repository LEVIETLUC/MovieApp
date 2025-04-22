# MovieApp

MovieApp is a mobile application designed to showcase trending movies, search for movies, and view detailed information about specific movies. This project is built using modern Android development practices and tools.

## Features
- View trending movies.
- Search for movies by title.
- View detailed information about a selected movie.
- Offline caching of movie data using Room database.

## Technical Overview

### Frameworks and Libraries
- **Jetpack Compose**: For building the UI with a declarative approach.
- **Koin**: For dependency injection.
- **Retrofit**: For making network requests to the TMDB API.
- **OkHttp**: For HTTP client and logging.
- **Room**: For local database storage.
- **Paging 3**: For implementing infinite scrolling and efficient data loading.
- **Moshi**: For JSON serialization and deserialization.

### Architecture
- **MVVM (Model-View-ViewModel)**: Ensures separation of concerns and testability.
- **Repository Pattern**: For managing data sources (network and local database).

### Database
- **Room Database**: Used for offline caching of movie data.
  - `MovieDao`: Handles operations for movie entities.
  - `MovieDetailDao`: Handles operations for detailed movie entities.

### Network
- **TMDB API**: The app fetches movie data from The Movie Database (TMDB) API.
- **AuthInterceptor**: Adds the API key to every network request.

### Build Tools
- **Gradle**: For project build and dependency management.
- **Kotlin DSL**: For Gradle build scripts.

## Project Structure
- `app/src/main/java/com/example/movieapp/`: Contains the main source code.
  - `db/`: Room database entities and DAOs.
  - `di/`: Dependency injection setup using Hilt.
  - `network/`: Retrofit service and network models.
  - `repository/`: Repository classes for data management.
  - `ui/`: UI components and screens built with Jetpack Compose.
  - `viewmodels/`: ViewModel classes for managing UI-related data.
- `app/src/main/res/`: Contains resources like layouts, drawables, and values.
- `app/build.gradle.kts`: Module-level Gradle configuration.
- `build.gradle.kts`: Project-level Gradle configuration.

## Getting Started

### Prerequisites
- Android Studio Flamingo or later.
- Minimum SDK: 30
- Target SDK: 34

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/LEVIETLUC/MovieApp.git
   ```
2. Open the project in Android Studio.
3. Build the project to download dependencies.
4. Run the app on an emulator or physical device.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
