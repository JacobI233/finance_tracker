#Finance Tracker

Description: 
A full stack personal finance tracking API built with Java and Spring Boot, 
featuring JWT authentication, RESTful endpoints, and a vanilla JavaScript 
frontend dashboard for tracking income, expenses, and budgets.

<img width="1920" height="1032" alt="Dashboard" src="https://github.com/user-attachments/assets/681e0e2b-3f4b-4ba7-838c-90acd62eedd4" />
<img width="1920" height="1032" alt="Login_Page" src="https://github.com/user-attachments/assets/63d81195-c1db-43e2-b079-657e815364c6" />

Tech Stack:
- Backend: Java, Spring Boot, Spring Security
- Database: PostgreSQL, JPA/Hibernate
- Authentication: JWT
- Documentation: Swagger/OpenAPI
- Frontend: HTML, CSS, JavaScript
- Tools: Postman, Git

Features: 
- JWT authentication with BCrypt password encryption
- Monthly income vs expense summary with net savings calculation
- Budget vs actual spending comparison with visual progress bars
- Full CRUD for transactions, budgets, and categories
- Interactive dashboard built with vanilla JavaScript
- RESTful API documented with Swagger/OpenAPI

Getting Started: 
1. Install Java 17
2. Install PostgreSQL
3. Clone repository: git clone https://github.com/JacobI233/finance-tracker
4. Configure database (copy application.properties.template  to application.properties)
5. Run application (.\mvnw.cmd spring-boot:run) for windows
6. Open in browser locally (http://localhost:8080/index.html)


API Documentation:
Includes swagger UI for API calls and testing on localhost.
- http://localhost:8080/swagger-ui.html
