## TravelProject

This is a TraveProject  REST API that provides the ability to add countries, cities, attractions and comments to them, as well as register and leave comments on attractions.

## Technologies
- java 17 version
- Spring ( Spring boot, Spring Data, Spring Security);
- PostgreSQL
- JUnit 5
- Hibernate
- Swagger
- Maven

## Launch of the project

1. Install Java Development Kit (JDK) on your system.
2. Install and configure PostgreSQL on your system.
3. Clone this repository to your local machine.
4. Open the project in your favorite integrated development environment (IDE).
5. Configure the PostgreSQL database connection settings in the application.properties file.
6. Run the application by clicking on the "Run" button.

## Working with the API

### Registration

To register, send a POST request to /seciruty/registration with a JSON body containing the following fields:


Example request:

POST /seciruty/registration

{
     "firstName":"Alex",
     "secondName":"Brabos",
     "age":"28",
     "usersLogin":"USER",
     "usersPassword":"USER"
}


### Authentication

To authenticate, send a POST request to /security with a JSON body containing the following fields:

- username (required field): username
- password (required field): user password

Example request:

POST /security

{
   "login": "USER",
   "password": "USER"
}


In response, you will receive an authentication token, which must be used to access other endpoints.

### All Endpoint
Swagger has been added to the project for faster access to endpoints and verification of all methods.
It will be commented out in the SpringSecurityConfiguration class
![image](https://github.com/Fuzzylolpro/Travel-Project/assets/132467383/bfd5933f-f111-4616-90f8-b3fd3baa2c00)
To use it, uncomment it and restart the application
http://localhost:8080/swagger-ui/index.html#/ at this address you will see all the endpoints of the application and you can use them.
![image](https://github.com/Fuzzylolpro/Travel-Project/assets/132467383/dbad818c-7fa3-4147-aafe-6d3cdc71782a)



