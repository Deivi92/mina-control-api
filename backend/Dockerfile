# ============================================================
# Etapa 1: Construcción (Builder)
# Utiliza una imagen completa de Maven + JDK 17 para compilar
# ============================================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar el pom.xml primero para aprovechar la caché de capas de Docker
# Si el pom.xml no cambia, Maven no vuelve a descargar las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline --no-transfer-progress -q

# Copiar el código fuente y empaquetar la aplicación
COPY src ./src
RUN mvn clean package -DskipTests --no-transfer-progress -q

# ============================================================
# Etapa 2: Ejecución (Runtime)
# Utiliza una imagen ligera de JRE 17 para ejecutar el JAR
# ============================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear usuario no-root por seguridad
RUN addgroup -S minacontrol && adduser -S minacontrol -G minacontrol

# Copiar únicamente el JAR generado desde la etapa de construcción
COPY --from=builder /app/target/mina-control-api-1.0.0.jar app.jar

# Cambiar la propiedad del archivo al usuario no-root
RUN chown minacontrol:minacontrol app.jar

# Ejecutar como usuario no-root
USER minacontrol

# Exponer el puerto en el que corre la aplicación Spring Boot
EXPOSE 8080

# Variables de entorno con valores por defecto
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-Xms256m -Xmx512m"

# Comando de inicio de la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
