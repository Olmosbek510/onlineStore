# Online Store web application

This project is a web application developed using the Spring Framework.
It leverages Spring MVC for handling web requests, Thymeleaf for rendering HTML views, Spring Security for securing the application, and Spring JDBC for database interactions.
All routes within this application are authorized based on user roles.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)

## Features

- Role-based access control
- Secure authentication and authorization using Spring Security
- Dynamic HTML content rendering with Thymeleaf
- Efficient database operations with Spring JDBC
- RESTful web services
- MVC architecture

## Technologies Used

- **Spring Framework**: Core framework used for building the application.
- **Spring MVC**: Used for handling web requests and implementing the MVC pattern.
- **Thymeleaf**: Template engine for rendering HTML views.
- **Spring Security**: Provides authentication and authorization mechanisms.
- **Spring JDBC**: Simplifies database interactions.
- **Java**: Programming language used for development.
- **Maven**: Build automation tool.

## Installation

1. **Clone the repository:**
    ```bash
    git clone https://github.com/Olmosbek510/onlineStore.git
    ```
2. **Navigate to the project directory:**
    ```bash
    cd your-repo
    ```
3. **Build the project using Maven:**
    ```bash
    mvn clean install
    ```

## Usage

1. **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
2. **Access the application:**
    Open your web browser and navigate to `http://localhost:8080`.

## Configuration

### Database

- The database configuration is located in `src/main/java/uz/inha/config/DbConfig.java`.
- Update the database URL, username, and password as per your setup.

### Security

- User roles and security configurations are defined in `src/main/java/uz/inha/config/security/SecurityConfig.java`.
- Modify the security configuration to match your requirements.

## Contributing

1. **Fork the repository.**
2. **Create a new branch:**
    ```bash
    git checkout -b feature/your-feature
    ```
3. **Make your changes and commit them:**
    ```bash
    git commit -m 'Add some feature'
    ```
4. **Push to the branch:**
    ```bash
    git push origin feature/your-feature
    ```
5. **Create a new Pull Request.**

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
