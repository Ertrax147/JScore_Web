# Etapa 1: Construcción con Maven
# Usamos una imagen de Maven con Java 17 para compilar nuestro proyecto
FROM maven:3.8.5-openjdk-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos solo el archivo pom.xml para descargar las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente y construimos el proyecto
COPY src ./src
RUN mvn package -DskipTests

# Etapa 2: Ejecución
# Usamos una imagen ligera de Java 17 para ejecutar la aplicación
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copiamos el archivo .jar construido desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto 8080 (el puerto por defecto de Spring Boot)
EXPOSE 8080

# El comando para iniciar la aplicación
ENTRYPOINT ["java","-jar","app.jar"]