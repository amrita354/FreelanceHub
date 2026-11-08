# FreelanceHub

FreelanceHub is a freelance management system built with Java, Spring Boot, MySQL, and Thymeleaf. It includes pages for managing projects, proposals, contracts, milestones, and invoices, along with client and freelancer dashboards.

## Tech stack

- Java 21
- Spring Boot
- Maven
- MySQL
- Thymeleaf

## Prerequisites

Install:

- Java 21
- MySQL

The project includes Maven wrapper scripts, so a separate Maven installation is not required.

## Getting started

1. Clone the repository:

   ```bash
   git clone https://github.com/arnavins/FreelanceHub.git
   cd FreelanceHub
   ```

2. Create the database and user in MySQL. Choose a password and use it in the next step:

   ```sql
   CREATE DATABASE freelancehub_db;
   CREATE USER 'freelancehub_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON freelancehub_db.* TO 'freelancehub_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. Set the `DB_PASSWORD` environment variable to the password for `freelancehub_user`.

   macOS/Linux:

   ```bash
   export DB_PASSWORD=your_password
   ```

   PowerShell:

   ```powershell
   $env:DB_PASSWORD = "your_password"
   ```

4. Start the application:

   macOS/Linux:

   ```bash
   ./mvnw spring-boot:run
   ```

   Windows:

   ```bat
   mvnw.cmd spring-boot:run
   ```

The application is configured to connect to MySQL at `127.0.0.1:3306/freelancehub_db`. Update `src/main/resources/application.properties` if your local database uses a different address or username.

## Running tests

macOS/Linux:

```bash
./mvnw test
```

Windows:

```bat
mvnw.cmd test
```

## Contributing

Contributions are welcome. For substantial changes, consider discussing the idea with the project maintainer first. When submitting a pull request, include a clear description of the change and any relevant testing details.

## License

No license is currently specified in this repository.
