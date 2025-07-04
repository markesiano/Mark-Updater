# Guía de Seguridad - Updater Backend

## Configuración Segura

### Variables de Entorno Requeridas

1. **JWT_SECRET**: Clave secreta para firmar tokens JWT
   - Debe ser una cadena aleatoria de al menos 32 caracteres
   - Nunca usar valores por defecto o débiles
   - Ejemplo de generación: `openssl rand -base64 32`

2. **BASE_DIR**: Directorio base de la aplicación
3. **UPDATE_MANIFEST**: Ruta al archivo manifest.json
4. **UPDATE_DIST**: Directorio con archivos de actualización

### Configuración de Producción

```bash
# Generar JWT secret seguro
export JWT_SECRET=$(openssl rand -base64 32)

# Configurar directorios
export BASE_DIR=/opt/updater
export UPDATE_MANIFEST=/manifest.json
export UPDATE_DIST=/dist
```

## Medidas de Seguridad Implementadas

### 1. Autenticación JWT
- Tokens JWT para autenticar requests
- Validación de firma con clave secreta
- Logging de intentos de autenticación fallidos

### 2. Validación de Entrada
- Validación de parámetros en endpoints
- Protección contra path traversal attacks
- Límites en número y tamaño de archivos

### 3. Protección contra Zip Bomb
- Límite de 100MB por archivo
- Máximo 1000 archivos por zip
- Validación de rutas de archivos

### 4. Configuración de Spring Security
- CSRF deshabilitado para APIs REST
- Sesiones stateless
- Filtro JWT custom

## Controles de Seguridad

- ✅ Sin credenciales hardcodeadas
- ✅ Uso de variables de entorno
- ✅ Validación de entrada
- ✅ Protección contra path traversal
- ✅ Autenticación JWT
- ✅ Logging de seguridad
- ✅ Límites de archivos
