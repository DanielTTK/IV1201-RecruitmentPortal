# IV1201 Recruitment Portal

A web-based recruitment portal.

The application allows applicants to create an account, log in, submit an application with competences and availability, and view or withdraw submitted applications. It also includes a recruiter/admin login.

## Features

- Applicant registration and login
- Recruiter/admin login
- Submit an application with:
  - previous experience / competences
  - years of experience
  - availability periods
  - motivation and personal description
- Review application before final submission
- View and withdraw submitted applications
- Legacy user verification with OTP email
- Custom error pages for 404, 500, and database-related errors

## Tech Stack

- Java
- Spring Boot
- Spring MVC
- Spring Security
- Thymeleaf
- Spring Data JPA / Hibernate
- Neon (DB cloud service)
- PostgreSQL
- Flyway
- Resend (OTP email sending)

## Project Structure

- `presentation/` - controllers, forms, and web layer
- `application/` - business logic and services
- `domain/` - JPA entities
- `repository/` - Spring Data repositories
- `config/` - security configuration
- `resources/templates/` - Thymeleaf views
- `resources/static/` - CSS and static assets

## Main User Flows

### Applicant
1. Create an account
2. Log in
3. Fill in competence profile
4. Review and submit application
5. View submitted applications
6. Withdraw an application if needed

### Recruiter
1. Log in through the admin portal
2. Access the admin dashboard

### Legacy User (imported from previous system)
1. Attempt login
2. Receive OTP by email
3. Verify OTP
4. Complete registration

## Configuration

The application reads configuration from `application.properties` and from a local `.env.properties` file.

Example environment variables:

```properties
SPRING_DATASOURCE_URL=your_database_url
SPRING_DATASOURCE_USERNAME=your_database_username
SPRING_DATASOURCE_PASSWORD=your_database_password

RESEND_API_KEY=your_resend_api_key
RESEND_FROM=your_sender_email
````

## Running the Project

Clone the repository and run the application using `./mvnw -q spring-boot:run`

Typical steps:

1. Set up a PostgreSQL database
2. Configure the required environment variables
3. Start the application
4. Open the site in your browser

Default local URL is typically:

```text
http://localhost:8080/home
```

## Deployment

The application has been deployed to a cloud platform, with URL:

```text
https://site--iv1201-recruitmentportal--58c2m9rcm9k7.code.run/home
```
