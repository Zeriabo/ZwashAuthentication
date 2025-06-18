# Dockerfile
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
# Install the parent POM firs

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/*.jar ZwashAuthentication.jar
ENTRYPOINT ["java","-jar","/ZwashAuthentication.jar"]

