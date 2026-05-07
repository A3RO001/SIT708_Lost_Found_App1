# SIT708_Lost_Found_App1

## Overview
This is a Lost and Found mobile application developed using Kotlin in Android Studio. The app allows users to report lost items, upload images, store item details locally using SQLite, and view or manage stored items.

---

## Features

- Add lost/found items with:
  - Title
  - Description
  - Category
  - Image upload (from gallery)
  - Timestamp

- View all items in a list

- Filter items by category using a dropdown (Spinner)

- Delete items using long press

- Input validation using Toast messages

- Navigation between screens using buttons

---

## Technologies Used

- Kotlin
- Android Studio
- SQLite Database
- ListView & Spinner
- Android SDK

---

## Project Structure

- `MainActivity.kt` → Home screen navigation  
- `AddItemActivity.kt` → Add item form  
- `ItemListActivity.kt` → View & filter items  
- `DatabaseHelper.kt` → SQLite database operations  
- `Item.kt` → Data model  

---

## How to Run

1. Clone the repository
2. Open in Android Studio
3. Connect an emulator or Android device
4. Click **Run**

---

## Future Improvements

- Camera integration for image capture  
- Firebase/cloud database integration  
- User authentication system  
- Search functionality  
- Improved UI using RecyclerView  

---

## Author

Anay Jayakumar
