# FrogMan — JavaFX Client + Spring Boot Backend

FrogMan is a client-server school project consisting of:

- 🎮 **JavaFX desktop game client** (`frontend/`)
- 🌐 **Spring Boot REST backend** (`backend/`)
- 🗄️ **H2 in-memory database** for storing players and scores

The project demonstrates backend development, REST communication, layered architecture, and integration between frontend and backend.

---

## 📂 Repository Structure
frogman/
├── backend/ → Spring Boot REST API
├── frontend/ → JavaFX game client
└── .gitignore


---

## 🚀 Features

- Player management
- Score tracking
- Game result persistence
- REST API communication between frontend and backend
- Layered backend architecture (Controller → Service → Repository)

---

## 🛠️ Tech Stack

- Java
- Spring Boot
- JavaFX
- Maven
- H2 Database
- Git

---

## 🧱 Backend Architecture

The backend follows a typical layered structure:

- **Controller layer** — REST endpoints  
- **Service layer** — business logic  
- **Repository layer** — database access (JPA)  
- **H2 database** — in-memory persistence  

---

## 🔗 Example REST API Endpoints

- `GET /players`
- `POST /players`
- `GET /score`
- `POST /game`

---

## ▶️ How to Run

### Backend
1. Open the `backend/` folder in IntelliJ IDEA
2. Run the Spring Boot application
3. H2 runs automatically in-memory

### Frontend
1. Open the `frontend/` folder in IntelliJ IDEA
2. Run the JavaFX application

---

## 📚 What I Learned

- Designing layered backend architecture
- Implementing REST APIs in Spring Boot
- Connecting JavaFX frontend with backend services
- Working with H2 in-memory database
- Structuring a client-server application

---

## 👤 Author

Dominik Kontrik  
GitHub: https://github.com/Sekvojak
