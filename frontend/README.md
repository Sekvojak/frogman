# FrogMan Frontend — JavaFX Client

JavaFX desktop game client for the FrogMan client-server application.

The frontend communicates with the Spring Boot backend via REST API to send game results and retrieve player data.

---

## 🛠 Tech Stack

- Java
- JavaFX
- Maven
- REST communication (HTTP)

---

## 🎮 Features

- FrogMan game implementation
- Player movement and collision detection
- Game over and results screen
- Sending game results to backend
- Retrieving score data from backend

---

## 🔗 Backend Integration

The frontend communicates with the backend REST API:

- Sends game results
- Retrieves player and score information

The backend must be running before starting the frontend.

---

## ▶️ Running the Application

1. Open the `frontend/` folder in IntelliJ IDEA
2. Run the main JavaFX application class (e.g. `App.java`)
3. Ensure backend is running on default port (`localhost:8080`)

---

## 📚 Key Concepts Demonstrated

- JavaFX UI development
- Game loop implementation
- Event handling
- REST client communication
- Modular project structure
