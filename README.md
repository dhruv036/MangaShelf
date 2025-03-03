# MangaShelf App

MangaShelf is an Android app that helps users explore and track their favorite manga. It provides sorting, filtering, and offline support while ensuring a seamless user experience.

## Features

### Manga List (Home Screen)
- Displays a list of manga with **Title, Cover Image, Score, Popularity, and Year of Publication**.
- **Sorting & Filtering**:
  - Grouped by **publication year** (horizontally scrollable tabs).
  - Clicking a year scrolls to the first manga of that year.
  - Sorting options: **Score (Low-High, High-Low)**, **Popularity (Low-High, High-Low)**.
- **Favorites**:
  - Mark/unmark manga as favorite.
  - Persist favorite status using Room Database.
- **Offline Support**:
  - Fetch manga list from API (`https://jsonkeeper.com/b/KEJO`).
  - Cache data in Room DB; retrieve from DB if network request fails.
- **Error Handling**:
  - Shows messages for network failures, empty data, etc., with retry options.

### Manga Details Page
- Shows **Manga Title, Cover Image, Score, Popularity, Published Date, and Category**.
- **Favorite & Read Status**:
  - Mark a manga as **favorite/unfavorite**.
  - Mark a manga as **read**.
  - Changes persist and reflect on the list page.

## Tech Stack
- **Jetpack Compose**: UI framework
- **Jetpack Navigation**: Navigation between screens
- **Room Database**: Local storage
- **Retrofit & Gson**: API handling & JSON parsing
- **Kotlin Coroutines & Flow**: Asynchronous operations

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/dhruv036/MangaShelf/tree/master
