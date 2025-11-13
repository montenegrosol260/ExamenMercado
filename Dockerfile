# ===============================================
# ETAPA 1: BUILD (Compilacion)
# ===============================================

# Imagen base ligera de Alpine Linux
FROM alpine:latest AS build

# Actualizar el indice de paquetes de Alpine
RUN apk update

# Instalar OpenJDK 17 Y LA HERRAMIENTA 'dos2unix'
RUN apk add openjdk17 dos2unix

# Copiar TODO el codigo fuente del proyecto al contenedor
COPY . .

# ========= ¡LA SOLUCIÓN! =========
# Convertir el script de Windows a formato Unix
RUN dos2unix ./gradlew

# Dar permisos de ejecucion al script gradlew
RUN chmod +x ./gradlew

# Ejecutar Gradle para compilar
RUN ./gradlew bootJar --no-daemon

# ========================================
# ETAPA 2: RUNTIME (Ejecución)
# ========================================
# Imagen base con SOLO el runtime de Java (sin herramientas de compilación)
# Esto reduce el tamaño de la imagen final de ~500MB a ~200MB
FROM eclipse-temurin:17-jre-alpine

# Documentar que la aplicación escucha en el puerto 8080
# IMPORTANTE: esto NO abre el puerto, solo es documentación
# El puerto se mapea con: docker run -p 8080:8080
EXPOSE 8080

# Copiar el JAR generado en la ETAPA 1 (build) a la imagen final
# --from=build: tomar archivo de la etapa "build" anterior
# Solo se copia el JAR, NO el código fuente ni herramientas de compilación
# Esto mantiene la imagen final pequeña y segura
COPY --from=build ./build/libs/*.jar ./app.jar

# Comando que se ejecuta cuando el contenedor inicia
# ENTRYPOINT (no CMD) asegura que siempre se ejecute la aplicación
# ["java", "-jar", "app.jar"]: formato exec (preferido sobre shell)
ENTRYPOINT ["java", "-jar", "app.jar"]