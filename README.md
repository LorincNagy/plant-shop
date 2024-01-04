# plant-shop - Spring Boot Project

## Overview
This project is developed as a web application for efficiently managing orders and purchases of plants. It leverages various technologies, including Spring Boot, Spring Data JPA for backend development, PostgreSQL for database management, and React for the frontend, providing a comprehensive solution for plant ordering and purchasing.

## Server side

### Database Configuration
Create a PostgreSQL database and set the following environment variables with your database connection details:

SPRING_DATASOURCE_URL: The URL of your PostgreSQL database.
SPRING_DATASOURCE_USERNAME: Your database username.
SPRING_DATASOURCE_PASSWORD: Your database password.

### Building and Running the Application
cd./backend
Use the following Maven command to build and run the Spring Boot application:

mvn spring-boot:run
The application will start on port 8080 by default.

# Client side
## Install Dependencies
cd ./frontend/ThreeTree
npm install
Proxy
Pay attention to the port of your REST API. By default, it will bind to port 8080, and the frontend proxy settings depend on this configuration. If you change the port of the backend for any reason, don't forget to update the proxy settings in ./frontend/package.json accordingly.

## Running the Code
To run the code, navigate to the "./frontend/ThreeTree" directory and execute the following command:
npm run dev
This will start your frontend using the Vite package on port 5173. You can access the application in your preferred web browser by opening the following URL: http://localhost:5173

# Technology Stack
## Frontend:
JavaScript/ES6
React
HTML/CSS
Material UI
## Backend:
Spring Boot
Spring Data JPA
JPA (Java Persistence API)
Hibernate
PostgreSQL

