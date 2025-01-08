# Movie API

## Overview

This project is a **RESTful API** designed to provide users with data on **popular**, **incoming**, and **top-rated movies** by fetching information from **TMDb (The Movie Database)** API using **Feign Client**. To improve performance, the API implements **caching**, ensuring that frequent requests for the same movie data are served faster. Additionally, users can **add movies to their personal watchlist**. The API uses **JWT (JSON Web Token)** for user authentication and authorization.

---

## Features

- **Get Popular Movies**: Fetches a list of popular movies from TMDb.
- **Get Incoming Movies**: Retrieves movies that are scheduled for release soon.
- **Get Top-Rated Movies**: Displays the top-rated movies according to TMDb.
- **Caching**: Caches the movie data to minimize API calls to TMDb and improve response times for frequently requested data.
- **User Watchlist**: Allows users to add movies to their watchlist and view their saved movies.
- **JWT Authentication**: Secures the API with JSON Web Tokens for authentication. Only authenticated users can add movies to their watchlist.

---

## Tech Stack

- **Java** (17+)
- **Spring Boot** (3.x)
- **Feign Client** (For making HTTP requests to external APIs)
- **TMDb API** (To fetch movie data)
- **Spring Cache** (For caching data)
- **Spring Data JPA** (For managing watchlist data)
- **JWT** (For user authentication)
- **Gradle** (Dependency Management & Build)
- **PostgreSQL** (Database)
- **Swagger** (For API documentation)

---

## Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/movie-api.git
cd movie-api
