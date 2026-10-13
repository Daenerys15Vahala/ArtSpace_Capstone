# ArtSpace - Capstone Project

**A full-stack art gallery application for discovering, sharing, and managing artwork.**

ArtSpace gives artists a place to showcase their work and gives visitors an easy way to explore a growing gallery. Registered users can upload artwork, save favorites, and manage their own contributions, while administrators have access to additional management tools.

## Project Overview

ArtSpace was developed as a software engineering capstone project to bring together frontend development, backend REST APIs, authentication, relational databases, and third-party API integration in one application.

**Goal:** Create an approachable, visually engaging space for artists to share their work and for art enthusiasts to discover and save artwork they enjoy.

## Features

- **Public gallery:** Browse uploaded artwork and view artwork details.
- **User accounts:** Register, log in, and log out.
- **Artwork uploads:** Authenticated artists can upload an image and provide artwork information.
- **Artwork management:** Artists can manage their own uploaded artwork.
- **Favorites:** Signed-in users can save and remove favorite artworks.
- **Explore page:** Discover additional artwork through the Pixabay API.
- **Admin dashboard:** Administrators can view users and the artwork they have uploaded.
- **Role-based security:** Different permissions for visitors, registered users, and administrators.

## User Roles

| Role | Access |
| **Guest** | Browse the public gallery and explore artwork. |
| **User / Artist** | Browse, upload and manage their own artwork, and save favorites. |
| **Admin** | Access the admin dashboard to view users and their artwork, in addition to authorized application features. |


## Database Design

The core relational data model includes:

- **Users:** Account details and application roles.
- **Artworks:** Title, description, image path, creation date, owner, and category.
- **Favorites:** Associations between users and artwork they have saved.

**Key relationships:**

- One user can upload many artworks.
- One category can contain many artworks.
- Each artwork belongs to one user and one category.
- Users can favorite multiple artworks; an artwork can be favorited by multiple users.

Foreign key constraints help maintain data integrity. For example, related favorite records must be handled when deleting an artwork.


## Getting Started

### Prerequisites

- Java 17
- Maven (or the Maven wrapper included in the project)
- Node.js and npm
- MySQL
- Git
- A Pixabay API key for the Explore integration

### 1. Clone the repository

```bash
git clone https://github.com/Daenerys15Vahala/ArtSpace_Capstone.git
cd ArtSpace_Capstone
```

### 2. Set up the database

Create the MySQL database:

```sql
CREATE DATABASE IF NOT EXISTS art_space;
```

If the repository includes a database schema script, run it against `art_space`. Configure the Spring Boot datasource to match your local MySQL installation.

### 3. Configure the backend

Set your local database credentials and Pixabay API key using the configuration approach in your Spring Boot application (for example, environment variables referenced from `application.properties`). **Do not commit passwords or API keys to GitHub.**

Start the Spring Boot application using IntelliJ IDEA or the project's Maven wrapper:

```bash
# macOS / Linux
./mvnw spring-boot:run

# Windows PowerShell
.\mvnw.cmd spring-boot:run
```

The backend runs at **http://localhost:8080** with the project's current local configuration.

### 4. Start the frontend

Open the frontend directory in a separate terminal, then install dependencies and run Vite:

```bash
cd frontend
npm install
npm run dev
```

Open **http://localhost:5173** in your browser.

> **Configuration note:** Check the actual frontend folder name and configuration property names in your local checkout before following these commands. If your frontend is in a different directory, run the npm commands there instead.


## Testing and Verification

The application was manually tested through its UI and API requests, including:

- Registration and login flows.
- Public gallery browsing and artwork details.
- Artwork uploads and gallery updates.
- Adding and removing favorites.
- Role-based access to the admin dashboard.
- Backend authorization behavior, including forbidden access for non-admin users.
- Database relationships and foreign key constraints.

**Tools used:** Browser developer tools, Bruno / Postman, and MySQL queries.

## Challenges and Solutions

| Challenge | Solution |
| --- | --- |
| Authentication and authorization | Configured Spring Security roles, authenticated sessions, and CSRF handling for protected requests. |
| Database relationships | Investigated foreign key constraints between artworks and favorites and removed dependent favorites before deleting an artwork during testing. |
| Frontend/backend communication | Integrated React with Spring Boot endpoints using HTTP requests, JSON responses, and session credentials. |
| Different user permissions | Added backend access rules and frontend navigation/route checks for the admin experience. |
| Uploading and displaying images | Stored artwork image paths and served uploaded files for display in the gallery. |

## Results

ArtSpace brings together the major components of a working full-stack application:

- A React interface connected to a Spring Boot REST API.
- User registration, login, and role-aware navigation.
- Artwork uploads, gallery browsing, and favorites.
- A MySQL database with relational integrity.
- An admin dashboard for viewing users and their uploaded work.
- Integration with an external artwork API.

The project provided practical experience in designing relational models, building APIs, securing routes, integrating frontend and backend code, and debugging real application issues.

## Future Improvements

Potential next steps include:

- Search and filtering by artist, title, or category.
- Artist profile pages and more portfolio customization.
- Improved validation and user-facing error messages.
- Automated unit and integration tests.
- Cloud deployment and managed image storage.
- Optional artwork purchase functionality.

## Screenshots

_Add screenshots of the home gallery, artwork upload page, favorites, Explore page, and admin dashboard here._

## Author

Valentina Bernal Moreno
Developed as a software engineering capstone project.

**ArtSpace — A place to create, share, and discover art.** 🎨
