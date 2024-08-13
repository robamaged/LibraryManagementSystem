Library Management System Documentation
Project Description
The Library Management System API is designed to facilitate the management of books, patrons, and borrowing records for a library. This application is built using Spring Boot and provides a RESTful API to manage the various aspects of a library's operations.


Getting Started
Prerequisites

Java Development Kit (JDK): Ensure that JDK 11 or higher is installed.
Maven: Make sure Maven is installed for dependency management and build tasks.
PostgreSQL: The project uses PostgreSQL for database management.
Intellij ultimate IDE
Postman for testing


Running the Application
Clone the Repository: Clone the project repository to your local machine.
git clone https://github.com/robamaged/LibraryManagementSystem.git


Open in IntelliJ IDEA Ultimate: Import the project into IntelliJ IDEA Ultimate.
Configure PostgreSQL: Set up PostgreSQL on your local machine and update the database configuration in the application.yml file.
-> Datasource -> postreSQL
Create ->new ->database -> write database name “lms”

2 of 10 -> check lms 

0 of 3 -> check all schemas under lms database





In url type the name of the database created in previous step “lms” ,and change Username and password with your username and password of postgres
Build and Run: Use Maven to build the project, and then run the application within IntelliJ IDEA.




Interacting with API Endpoints using postman:
1. Register     http://localhost:8090/api/v1/auth/register
Request Body:{
    "email":"test@gmail.com",
    "Password":"password"
}



Copy the token(needed for later use).


2. Add a New Book

Endpoint:
Method: POST /api/books

Authorization tab-> Type:Bearer Token -> paste token to allow for endpoint access

Request Body :{
  "title": "Effective Java",
  "author": "Joshua Bloch",
  "publicationYear": 2008,
  "isbn": "9780134685991"
}







3. Add a New Patron         http://localhost:8090/api/patrons
Endpoint:
Method: POST /api/patrons
Request Body:{
  "name": "John Doe",
  "contactInformation": "john.doe@example.com"
}







3. Retrieve All Books
Endpoint:
Method: GET /api/books






4. Retrieve All Patrons
Endpoint:
Method: GET /api/patrons
Expected Response:








5. Borrow a Book
Endpoint:
Method: POST /api/borrow/{bookId}/patron/{patronId}
URL: /api/borrow/1/patron/1
Expected Response:



Endpoint:
Method: PUT /api/return/{bookId}/patron/{patronId}
URL: /api/return/1/patron/1
Expected Response:





7. Update Book Information
Endpoint:
Method: PUT /api/books/{id}
URL: /api/books/1
Request Body:{
  "title": "Effective Java, 3rd Edition",
  "author": "Joshua Bloch",
  "publicationYear": 2018,
  "isbn": "9780134685991"
}






8. Delete a Patron with id that doesnot exist in databse
Endpoint:
Method: DELETE /api/patrons/{id}
URL: /api/patrons/2
Expected Response:







8. Delete a Patron that did not yet return a book he/she borrowed
Endpoint:
Method: DELETE /api/patrons/{id}
URL: /api/patrons/1
Expected Response:








9. Delete a Book that is currently borrowed
Endpoint:
Method: DELETE /api/books/{id}
URL: /api/books/1
Expected Response:






Unit Testing:
BookControllerTests.java
PatronControllerTests.java
BorrowingRecordControllerTests.java


 Developed comprehensive unit tests for all API endpoints, including tests for authentication, authorization, and error handling scenarios (e.g., unauthorized access, invalid tokens). These tests ensure that security measures are correctly enforced across the application.

( config Package)
JWT Security Configuration:

JWT Creation: Detailed the process of token generation, including signing algorithms, token expiration, and payload structure.
Token Validation: Described how tokens are validated, including checking signatures, expiration, and claims.
Error Handling: Explained how security-related errors (e.g., invalid token, expired token) are handled and communicated to the client.
API Endpoint Protection:

Access Control: Specified which endpoints are protected by JWT and the roles or permissions required to access them.

Security Filters: Documented the security filters used (e.g., JwtAuthenticationFilter, JwtAuthorizationFilter) and their roles in the request processing pipeline.
