# Mancala Game - Spring Boot 3 WebFlux Project

Welcome to the Mancala Game project! This project is built using Spring Boot 3 and WebFlux, with MongoDB as the database. It also includes Docker and Kubernetes configurations for easy deployment and scaling.

## Table of Contents
- [Introduction](#Introduction)
- [Features](#Features)
- [Prerequisites](#Prerequisites)
- [Installation](#Installation)
- [API Endpoints](#Api-endpoints)

## Introduction

Mancala is a traditional board game that involves capturing stones. This project provides a RESTful API to play the game. The backend is implemented using Spring Boot 3 with WebFlux for reactive programming. MongoDB is used to persist game state, and Docker and Kubernetes are used for containerization and orchestration.

## Features

- Reactive programming using Spring WebFlux.
- MongoDB integration for storing game states.
- Docker support for containerization.
- Kubernetes support for orchestration and scaling.
- RESTful API for game operations.

## Prerequisites

- Java 21
- Docker
- Kubernetes (Minikube or a Kubernetes cluster)
- Maven
- MongoDB

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/mancala-game.git
   cd mancala-game
    ```
2. Build the project:
   ```bash
   ./gradlew clean build 
   ```
3. Build the Docker image:
   ```bash
    docker-compose -f docker-compose.yml up  
    ```
4. Deploy the application to Kubernetes:
    ```bash
    kubectl apply -f mongo.yaml
    kubectl apply -f mancala-app.yaml
    ```
5. Access the application at `http://localhost:8080`.
6. Access the Swagger UI at `http://localhost:8080/webjars/swagger-ui/index.html#`.
7. Play the game using the API endpoints.

## API Endpoints
1. User endpoints:
   - `POST /mancala/v1/users/{displayName}`: Create a new user.
   - `GET /mancala/v1/users`: Get all users.
2. Player endpoints
    - `POST /mancala/v1/players/{playerId}/subscribe`: Subscribe to play game.
    - `GET /mancala/v1/players/{playerId}`: Get a player by id.
    - `GET /mancala/v1/players/{playerId}/games`: Get all games by player.
3. Game endpoints
    - `POST /mancala/v1/games`: Create a new game.
    - `GET /mancala/v1/games`: Get all games.
    - `GET /mancala/v1/games/{gameId}`: Get a game by id.
    - `PUT /mancala/v1/games/{gameId}/make-move`: Make a move in the game.
    - `PUT /mancala/v1/games/{gameId}/play-again`: Play a game with same opponent again.
