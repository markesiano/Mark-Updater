## High-Level Architecture

The `Updater-Backend-Mark-EPOS` is a Spring Boot application designed to manage and serve software updates. It follows a layered architecture, separating concerns into distinct packages for application logic, configuration, controllers, and infrastructure.

*   **Purpose**: Provides a backend service for generating and serving update manifests and zip files for an EPOS system.
*   **Internal Parts**:
    *   **`VersionsApplication`**: The main entry point [VersionsApplication](src/main/java/com/backend/versions/VersionsApplication.java).
    *   **`controller`**: Handles incoming HTTP requests [controller](src/main/java/com/backend/versions/controller).
    *   **`application`**: Contains core business logic and use cases [application](src/main/java/com/backend/versions/application).
    *   **`infrastructure`**: Provides concrete implementations for interfaces defined in the application layer [infrastructure](src/main/java/com/backend/versions/infrastructure).
    *   **`config`**: Manages security and other application configurations [config](src/main/java/com/backend/versions/config).
    *   **`exceptions`**: Defines custom exceptions [exceptions](src/main/java/com/backend/versions/exceptions).
    *   **`util`**: Provides general utility functions [util](src/main/java/com/backend/versions/util).
*   **External Relationships**: Exposes RESTful APIs for update requests and interacts with the file system for manifest and update file management.

## Mid-Level Architecture

### **`controller` Layer**

*   **Purpose**: Exposes RESTful endpoints for clients to request update manifests and download update files. It acts as the entry point for external interactions.
*   **Internal Parts**:
    *   **`UpdateController`**: Manages API endpoints related to updates [UpdateController](src/main/java/com/backend/versions/controller/UpdateController.java).
*   **External Relationships**:
    *   Receives HTTP requests from clients.
    *   Delegates business logic to the `ManifestService` and `GenerateUpdateZipUseCase` from the `application` layer.
    *   Returns responses, potentially including update manifests or file streams.

### **`application` Layer**

*   **Purpose**: Contains the core business logic of the application, defining interfaces for services and implementing use cases. This layer is independent of infrastructure details.
*   **Internal Parts**:
    *   **`ManifestService`**: An interface defining operations related to update manifests [ManifestService](src/main/java/com/backend/versions/application/ManifestService.java).
    *   **`GenerateUpdateZipUseCase`**: A use case responsible for generating update zip files [GenerateUpdateZipUseCase](src/main/java/com/backend/versions/application/usecases/GenerateUpdateZipUseCase.java).
    *   **`entities`**: Contains domain entities like `Manifest` [Manifest](src/main/java/com/backend/versions/application/entities/Manifest.java).
*   **External Relationships**:
    *   `ManifestService` is implemented by `FileSystemManifestService` in the `infrastructure` layer.
    *   `GenerateUpdateZipUseCase` utilizes `ZipUtils` from the `util` layer.
    *   Used by the `UpdateController` to perform business operations.

### **`infrastructure` Layer**

*   **Purpose**: Provides concrete implementations for interfaces defined in the `application` layer, handling external concerns like file system access.
*   **Internal Parts**:
    *   **`FileSystemManifestService`**: Implements the `ManifestService` interface, providing file system-based operations for manifests [FileSystemManifestService](src/main/java/com/backend/versions/infrastructure/FileSystemManifestService.java).
*   **External Relationships**:
    *   Interacts directly with the file system to read and write manifest and update files.
    *   Is injected into the `application` layer (e.g., into `UpdateController` or other services that depend on `ManifestService`).

### **`config` Layer**

*   **Purpose**: Configures application-wide settings, particularly security.
*   **Internal Parts**:
    *   **`JwtAuthenticationFilter`**: Handles JWT token validation for authentication [JwtAuthenticationFilter](src/main/java/com/backend/versions/config/JwtAuthenticationFilter.java).
    *   **`WebSecurityConfig`**: Configures Spring Security, defining access rules and integrating the JWT filter [WebSecurityConfig](src/main/java/com/backend/versions/config/WebSecurityConfig.java).
*   **External Relationships**:
    *   Integrates with Spring Security to secure API endpoints.
    *   Processes incoming requests before they reach the controllers.

### **`exceptions` Layer**

*   **Purpose**: Defines custom exception classes to provide more specific error handling within the application.
*   **Internal Parts**:
    *   **`FileNotAvailableException`**: Custom exception for file availability issues [FileNotAvailableException](src/main/java/com/backend/versions/exceptions/FileNotAvailableException.java).
    *   **`GlobalExceptionHandler`**: Centralized exception handling for the application [GlobalExceptionHandler](src/main/java/com/backend/versions/exceptions/GlobalExceptionHandler.java).
    *   **`ManifestNotFoundException`**: Custom exception when a manifest is not found [ManifestNotFoundException](src/main/java/com/backend/versions/exceptions/ManifestNotFoundException.java).
    *   **`ManifestParseException`**: Custom exception for manifest parsing errors [ManifestParseException](src/main/java/com/backend/versions/exceptions/ManifestParseException.java).
*   **External Relationships**:
    *   Exceptions are thrown by various layers (e.g., `application`, `infrastructure`) and caught by `GlobalExceptionHandler` to return consistent error responses to clients.

### **`util` Layer**

*   **Purpose**: Provides general utility functions that can be used across different layers.
*   **Internal Parts**:
    *   **`ZipUtils`**: Utility class for handling zip file operations [ZipUtils](src/main/java/com/backend/versions/util/ZipUtils.java).
*   **External Relationships**:
    *   Used by the `application` layer, specifically by `GenerateUpdateZipUseCase`, for file compression.

## Data Flow

1.  An external client sends an HTTP request to an endpoint exposed by the **`UpdateController`** [UpdateController](src/main/java/com/backend/versions/controller/UpdateController.java).
2.  The request first passes through the **`JwtAuthenticationFilter`** [JwtAuthenticationFilter](src/main/java/com/backend/versions/config/JwtAuthenticationFilter.java) for authentication and authorization, configured by **`WebSecurityConfig`** [WebSecurityConfig](src/main/java/com/backend/versions/config/WebSecurityConfig.java).
3.  The **`UpdateController`** [UpdateController](src/main/java/com/backend/versions/controller/UpdateController.java) then invokes methods on the **`ManifestService`** [ManifestService](src/main/java/com/backend/versions/application/ManifestService.java) (implemented by **`FileSystemManifestService`** [FileSystemManifestService](src/main/java/com/backend/versions/infrastructure/FileSystemManifestService.java)) or the **`GenerateUpdateZipUseCase`** [GenerateUpdateZipUseCase](src/main/java/com/backend/versions/application/usecases/GenerateUpdateZipUseCase.java) to perform the requested business logic.
4.  The **`FileSystemManifestService`** [FileSystemManifestService](src/main/java/com/backend/versions/infrastructure/FileSystemManifestService.java) interacts with the underlying file system to read or write manifest files.
5.  The **`GenerateUpdateZipUseCase`** [GenerateUpdateZipUseCase](src/main/java/com/backend/versions/application/usecases/GenerateUpdateZipUseCase.java) uses **`ZipUtils`** [ZipUtils](src/main/java/com/backend/versions/util/ZipUtils.java) to create update zip archives.
6.  Any errors encountered during this process are handled by custom exceptions defined in the **`exceptions`** package [exceptions](src/main/java/com/backend/versions/exceptions), which are centrally managed by the **`GlobalExceptionHandler`** [GlobalExceptionHandler](src/main/java/com/backend/versions/exceptions/GlobalExceptionHandler.java) to return appropriate HTTP error responses.
7.  Finally, the **`UpdateController`** [UpdateController](src/main/java/com/backend/versions/controller/UpdateController.java) sends the processed response back to the client.

