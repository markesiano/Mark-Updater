# Mark-Updater

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen.svg)
![Maven](https://img.shields.io/badge/Maven-3.9.9-blue.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

Backend service que proporciona APIs REST para gestionar y distribuir actualizaciones de software de manera segura.

## Tabla de Contenidos

- [Características](#-características)
- [Arquitectura](#-arquitectura)
- [Requisitos](#-requisitos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [API Endpoints](#-api-endpoints)
- [Seguridad](#-seguridad)
- [Desarrollo](#-desarrollo)
- [Deployment](#-deployment)
- [Contribución](#-contribución)

## Características

- **Gestión de Manifiestos**: Lectura y procesamiento de archivos manifest.json
- **Generación de Updates**: Creación automática de archivos ZIP con actualizaciones
- **Autenticación JWT**: Sistema de autenticación seguro basado en tokens
- **API REST**: Endpoints RESTful para integración con clientes
- **Validación de Seguridad**: Protección contra path traversal y zip bombs
- **Arquitectura Limpia**: Separación clara de responsabilidades
- **Testing Completo**: Suite de tests unitarios y de integración

## Arquitectura

El proyecto sigue una arquitectura hexagonal (Clean Architecture) con las siguientes capas:

```
src/main/java/com/backend/versions/
├── application/          # Lógica de negocio y casos de uso
│   ├── entities/        # Entidades de dominio
│   ├── usecases/        # Casos de uso de la aplicación
│   └── ManifestService.java
├── config/              # Configuración de seguridad
│   ├── JwtAuthenticationFilter.java
│   └── WebSecurityConfig.java
├── controller/          # Controladores REST
│   └── UpdateController.java
├── exceptions/          # Excepciones personalizadas
├── infrastructure/      # Implementaciones de infraestructura
│   └── FileSystemManifestService.java
└── util/               # Utilidades generales
    └── ZipUtils.java
```

## Requisitos

- **Java**: 21 o superior
- **Maven**: 3.6.3 o superior
- **Spring Boot**: 3.5.0
- **Docker**: (opcional) para deployment

## Instalación

### Clonar el repositorio
```bash
git clone <repository-url>
cd Mark-Updater
```

### Compilar el proyecto
```bash
./mvnw clean package
```

### Ejecutar tests
```bash
./mvnw test
```

## ⚙️ Configuración

### Variables de Entorno

Crear un archivo `.env` basado en `.env.example`:

```bash
# JWT Secret - debe ser una cadena segura de al menos 32 caracteres
JWT_SECRET=your-super-secret-jwt-key-here-minimum-32-characters

# Directorios de la aplicación
BASE_DIR=/path/to/your/base/directory
UPDATE_MANIFEST=/path/to/manifest.json
UPDATE_DIST=/path/to/update/files

# Puerto del servidor (opcional)
SERVER_PORT=8080
```

### Generar JWT Secret seguro
```bash
# Linux/macOS
export JWT_SECRET=$(openssl rand -base64 32)

# Windows (PowerShell)
$JWT_SECRET = [Convert]::ToBase64String((1..32 | ForEach {Get-Random -Maximum 256}))
```

## Uso

### Ejecutar localmente
```bash
./mvnw spring-boot:run
```

### Usando Docker
```bash
# Construir imagen
docker build -t backend-updater .

# Ejecutar contenedor
docker run -p 8080:8080 \
  -e JWT_SECRET="your-secret-here" \
  -e BASE_DIR="/app/data" \
  -e UPDATE_MANIFEST="/manifest.json" \
  -e UPDATE_DIST="/dist" \
  backend-updater
```

## API Endpoints

### Obtener Manifest
```http
GET /api/update/manifest
Authorization: Bearer <jwt-token>
```

**Respuesta:**
```json
{
  "version": "1.0.0",
  "files": [
    {
      "path": "App.exe",
      "checksum": "abc123",
      "size": "1024"
    }
  ]
}
```

### Descargar Archivos de Actualización
```http
POST /api/update/download
Authorization: Bearer <jwt-token>
Content-Type: application/json

["App.exe", "config.xml"]
```

**Respuesta:** Archivo ZIP con los archivos solicitados

## Seguridad

El proyecto implementa múltiples capas de seguridad:

- **Autenticación JWT**: Todos los endpoints protegidos requieren token válido
- **Validación de Entrada**: Sanitización de parámetros de entrada
- **Protección Path Traversal**: Prevención de acceso a archivos fuera del directorio base
- **Límites de Archivos**: Protección contra zip bombs (max 100MB por archivo, 1000 archivos)
- **Logging de Seguridad**: Registro de intentos de autenticación fallidos

Ver [SECURITY.md](SECURITY.md) para más detalles.

## 🛠️ Desarrollo

### Estructura de Tests
```bash
./mvnw test                           # Ejecutar todos los tests
./mvnw test -Dtest=UpdateControllerTest # Ejecutar test específico
```

### Profiles de Spring
- `default`: Configuración de desarrollo
- `test`: Configuración para tests (seguridad deshabilitada)

### Variables de Entorno en Producción
Configurar en el servidor:
```bash
export JWT_SECRET="production-secret-key"
export BASE_DIR="/opt/mark-updater"
export UPDATE_MANIFEST="/data/manifest.json"
export UPDATE_DIST="/data/dist"
```

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

**Única condición**: Incluir el aviso de copyright y licencia en todas las copias.

---

**Desarrollado por:** Jesús Eduardo Jiménez García  
**Licencia:** MIT  
**Año:** 2025 
