# KungFu Taishan Backend API

Backend REST API for managing Kung Fu schools, students, and gradings. This system allows the registration and tracking of training centers, students, instructors, and their respective belts/gradings.

## Frontend Access
You can access the frontend repository or the live deployment for the KungFu Taishan application here:

- Frontend Repository: [GitHub Repository Link](https://github.com/arthurspedine/kungfu-web)

- Live Deployment: [Deployment Link](https://kungfutaishan.vercel.app/)

## Technologies Used

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Flyway (Database Migrations)
- Maven
- JWT for authentication

## Prerequisites

- JDK 17
- Maven 3.9+
- PostgreSQL (configurable)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/arthurspedine/kungfu-server.git
   cd kungfu-server
   ```
2. Configure the environment variables in the application.yml file or through environment variables:
   ```
    KUNGFU_DB_URL=
    KUNGFU_DB_USER=
    KUNGFU_DB_PASSWORD=
    JWT_SECRET=
    ADMIN_PASSWORD=
    FRONTEND_URL=
    COOKIE_NAME=
   ```
3. Initialize the Docker container. Note: Make sure the database variables match the environment variables you configured.
    ```bash
    docker compose up -d
   ```
4. Run the project:
   ```bash
    ./mvnw spring-boot:run
   ```

## CI/CD Pipeline

This project uses GitHub Actions integrated with Azure Web App for continuous deployment automation of the API.

### Workflow: Deploy KungFu Taishan Backend

The pipeline is automatically triggered on each push to the `main` branch and follows this flow:

#### 1. Build Job
- Runs in Ubuntu environment
- Performs source code checkout
- Sets up Java 17 (Temurin/Eclipse Adoptium)
- Runs Maven build (`mvn clean install -DskipTests`)
- Stores the generated JAR file as an artifact

#### 2. Deploy Job
- Waits for successful completion of the build job
- Downloads the previously generated JAR artifact
- Deploys directly to Azure Web App (app name: `kungfuapp`)
- Uses secure credentials stored in repository secrets

### Environments
- **Production link**: [https://kungfuserver.spedine.tech](https://kungfuserver.spedine.tech/)
- **Documentation**: [Swagger](https://kungfuserver.spedine.tech/swagger-ui/index.html)

### Deploy Status
The status of the latest deployment can be checked on the [Actions page](https://github.com/arthurspedine/kungfu-server/actions) of the repository.

### Manual Configuration
To configure this pipeline in a new environment:
1. Set up an Azure Web App for Java
2. Generate the publishing profile in the Azure portal
3. Add the content as a secret in GitHub with the name `azureWebAppPublicProfile`