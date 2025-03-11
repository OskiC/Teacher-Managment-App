FROM ubuntu:latest
LABEL authors="xoska"

# Use Maven image to build the application
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the entire project and build it
COPY . .
RUN mvn clean package  # This will run tests as part of the build

# Use a lightweight JDK image to run the built application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]