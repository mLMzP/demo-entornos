# Etapa 1: Compilacion
FROM maven:3.8.5-eclipse-temurin-17 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Etapa 2: Ejecucion (JRE 17 sobre Ubuntu Jammy - Mayor compatibilidad con chips M4 - MAC)

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


