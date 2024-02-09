# Plant-Shop - Spring Boot Project

## Project Overview
The Plant-Shop project is developed as a web application designed to efficiently manage plant orders and purchases. It utilizes a wide range of technologies, including Spring Boot and Spring Data JPA for backend development, PostgreSQL for database management, and React for the frontend. This comprehensive solution offers seamless plant ordering and purchasing capabilities and has been Dockerized for simplified deployment and management.

## Technology Stack

### Frontend:
- JavaScript/ES6
- React
- HTML/CSS
- Material UI

### Backend:
- Spring Boot
- Spring Data JPA
- JPA (Java Persistence API)
- Hibernate
- PostgreSQL

## Server side

### Database Configuration
Create the PostgreSQL Database:

1. Install PostgreSQL if it's not already installed.
Start the PostgreSQL database server.
Create a Database:

Create a new database on the PostgreSQL server. For example, you can use the following command in the psql command-line or an admin tool:

CREATE DATABASE your_database_name;

"2. Define Environment Variables in the main application or within the docker-compose.yml file (depending on your configuration preference):

Set the following environment variables to configure the connection details required for your project. These details will be used by the application to establish a connection with the PostgreSQL database."

   - `SPRING_DATASOURCE_URL`: The URL of your PostgreSQL database, e.g., `jdbc:postgresql://${DB_HOST}:${DB_PORT}/your_database_name`
   - `DB_USERNAME`: Your PostgreSQL database username, e.g., ``
   - `DB_PASSWORD`: Your PostgreSQL database password, e.g., ``

2. Additionally, consider setting any other environment variables that your application may require for proper operation. For example:

   - `USER_EMAIL`: Your user's gmail email address, e.g., ``
   - `USER_PASSWORD`: Your Google application password for authentication, e.g., ``

   Make sure to replace the placeholder values with your actual database and user information.

After setting these environment variables, the application will use them to establish a connection to the PostgreSQL database. Make sure you have configured these variables with the correct values for your project to work as expected.

### Building and Running the Application
Change to the '/backend' directory by using the following command:
cd ./backend

To build and run the Spring Boot application using a JAR file, you can follow these steps:

1. Build the JAR file with Maven:
mvn clean package

2. Once the JAR file is built, run the Spring Boot application using the JAR file:
java -jar target/security-0.0.1-SNAPSHOT.jar

### Alternatively, you can use Docker Compose:

3. Use Docker Compose with your environment variables:
docker-compose up -d

The application will start in a Docker container, and it will also run on port 8080 by default.


## React Frontend Configuration for Docker
For deploying your React application, you have two primary options depending on your development and production needs. You can configure the React app to run with its own development server for live reloading features, or you can serve it through an Express server for a more production-like environment.

### Option 1: Running with React Development Server
To run your React application in a Docker container using the React development server, which is useful for development due to its live reloading capability, follow this configuration in your DockerfileFrontend:

# BUILD REACT FRONTEND:
FROM node:18.14.0-alpine as frontend
WORKDIR /app/frontend
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . .
# Start the React development server
CMD ["npm", "run", "dev"]

By default, this server runs on port 5173 so make sure your docker-compose.yml maps this port correctly to access the application on localhost:5173.

## Option 2: Serving through Express Server

# BUILD REACT FRONTEND:
FROM node:18.14.0-alpine as frontend
WORKDIR /app/frontend
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

## BUILD EXPRESS FRONTEND SERVER:
FROM node:18.14.0-alpine as express-server
WORKDIR /app/express-server
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY server.js .
COPY --from=frontend /app/frontend/build ./static
EXPOSE 3000
CMD ["node", "server.js"]

The Express server listens on port 3000, so make sure your docker-compose.yml maps this port correctly to access the application on localhost:3000.


# Client side
## Install Dependencies
cd ./frontend
npm install
Proxy
Pay attention to the port of your REST API. By default, it will bind to port 8080, and the frontend proxy settings depend on this configuration. If you change the port of the backend for any reason, don't forget to update the proxy settings in ./frontend/package.json accordingly.

## Running the Code
To run the code, navigate to the "./frontend" directory and execute the following command:
npm run dev
This will start your frontend using the Vite package on port 5173. You can access the application in your preferred web browser by opening the following URL: http://localhost:5173

# Running Unit Tests
To ensure the quality and functionality of our backend services, comprehensive unit tests have been implemented. These tests can be executed to verify the behavior of individual units of code and ensure that they meet our expected outcomes.

## How to Run Tests
Navigating to the Backend Directory: First, navigate to the backend directory of the Plant-Shop project where the unit tests are located. You can do this by opening a command prompt or terminal and running the following command:


cd D:\Own Projects\plant-shop\backend
This will change your current directory to the backend part of the Plant-Shop project.

## Executing the Tests
Once you are in the backend directory, you can run the unit tests by executing the mvn test command. This command will trigger Maven to run all the unit tests defined in the project:

mvn test
Ensure that Maven is installed and configured properly on your system to execute this command successfully.

Locating Unit Tests
The unit tests for the backend services can be found within the Backend/src/test directory. This directory contains test classes for various components of the application, each designed to test specific functionalities and ensure they operate as expected.
By running these tests regularly, you contribute to the stability and reliability of the Plant-Shop project, helping us maintain a high standard of quality for all backend functionalities.


