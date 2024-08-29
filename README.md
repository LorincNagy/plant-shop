# Plant-Shop - Spring Boot Project

Project Overview
The Plant-Shop project is developed as a web application designed to efficiently manage plant orders and purchases. It utilizes a wide range of technologies, including Spring Boot and Spring Data JPA for backend development, PostgreSQL for database management, and React for the frontend. This comprehensive solution offers seamless plant ordering and purchasing capabilities and has been Dockerized for simplified deployment and management.

## Technology Stack

Frontend
JavaScript/ES6
React
HTML/CSS
Material UI
Backend
Spring Boot
Spring Data JPA
JPA (Java Persistence API)
Hibernate
PostgreSQL


## Server Side

Database Configuration
Create the PostgreSQL Database:

Install PostgreSQL if it's not already installed.

Start the PostgreSQL database server.

### Create a Database

Create a new database on the PostgreSQL server. For example, you can use the following command in the psql command-line or an admin tool:

CREATE DATABASE your_database_name;
Configuration File Setup:

The necessary environment variables for the proper functioning of the project are set up in the following steps:
Create the .env file:
You need to store the required data, such as secret keys and database access credentials, in the .env file. Do not upload this file to GitHub!

Example structure of the .env file:


### PostgreSQL database settings

DB_HOST=
DB_PORT=
DB_USERNAME=your_username
DB_PASSWORD=your_password

### Secret key for JWT signing

SECRET_KEY=your_secret_key

### Other environment variables

USER_EMAIL=your_email_address
USER_PASSWORD=your_google_app_password
After setting these environment variables, the application will use them to establish a connection to the PostgreSQL database. Make sure you have configured these variables with the correct values for your project to work as expected.

### Running the Server side

Change to the backend directory:

cd ./backend
Run the Spring Boot application:

mvn spring-boot:run


## Client Side

Install Dependencies
Navigate to the frontend directory:

cd ./frontend
Install the dependencies:

npm install
Proxy Configuration:
Pay attention to the port of your REST API. By default, it will bind to port 8080, and the frontend proxy settings depend on this configuration. If you change the port of the backend for any reason, don't forget to update the proxy settings in ./frontend/package.json accordingly.

### Running the Client side

1. Starting the Frontend with Vite
To start the frontend development server using Vite, navigate to the ./frontend directory in your terminal. Once there, execute the following command:

npm run dev
This command will start the Vite development server on port 5173. You can then open your preferred web browser and access the application at:

http://localhost:5173

2. Building and Serving the Application with Express
If you'd like to serve the application using an Express server, you'll first need to build the static files. To do this, follow these steps:

Build the Static Files:
In the ./frontend directory, run the following command to generate the production-ready static files:

npm run build
Start the Express Server:
Once the build process is complete, start the Express server by running:

node server.js
This will start the Express server, serving the static files on port 3000. You can then access the application in your browser at:

http://localhost:3000

3. Alternatively, Using Docker Compose
If you prefer to use Docker for running the application, you can utilize Docker Compose. Simply run the following command to start both the frontend and backend services:

docker-compose up -d


## Running Unit Tests
To ensure the quality and functionality of our backend services, comprehensive unit tests have been implemented. These tests can be executed to verify the behavior of individual units of code and ensure that they meet our expected outcomes.

### How to Run Tests

Navigating to the Backend Directory:
First, navigate to the backend directory of the Plant-Shop project where the unit tests are located. You can do this by opening a command prompt or terminal and running the following command:

cd D:\Own Projects\plant-shop\backend
This will change your current directory to the backend part of the Plant-Shop project.

Executing the Tests:
Once you are in the backend directory, you can run the unit tests by executing the following command:

mvn test
This command will trigger Maven to run all the unit tests defined in the project. Ensure that Maven is installed and configured properly on your system to execute this command successfully.

Locating Unit Tests
The unit tests for the backend services can be found within the Backend/src/test directory. This directory contains test classes for various components of the application, each designed to test specific functionalities and ensure they operate as expected.

By running these tests regularly, you contribute to the stability and reliability of the Plant-Shop project, helping maintain a high standard of quality for all backend functionalities.
