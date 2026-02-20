# FrogMan Backend — Spring Boot REST API

Spring Boot REST backend for the FrogMan client-server application.

The backend is responsible for managing players, scores and storing game results using an H2 in-memory database.

---

## 🛠 Tech Stack

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

---

## 🧱 Architecture

The backend follows a layered architecture:

- **Controller layer** — REST endpoints
- **Service layer** — business logic (if applicable)
- **Repository layer** — JPA data access
- **Entity layer** — JPA entities (Player, Score, GameResult)

---

## 🔗 REST API Endpoints

### Players
- `GET /players`
- `POST /players`

### Scores
- `GET /score`
- `POST /game`

---

## 🗄 Database

- H2 in-memory database
- Automatically initialized on application startup
- Data is reset after application restart

---

## ▶️ Running the Application

1. Open the `backend/` folder in IntelliJ IDEA
2. Run `FrogManBackendApplication`
3. The application starts on default Spring Boot port (`8080`)

Optional:
- H2 Console available at: `http://localhost:8080/h2-console`
  (if enabled in configuration)

---

## 📚 Key Concepts Demonstrated

- REST API design
- CRUD operations with Spring Data JPA
- Layered application architecture
- Integration between frontend and backend
