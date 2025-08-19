# Stage 1: Build with Maven
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run with JDK
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/*.jar ZwashAuthentication.jar
ENTRYPOINT ["java","-jar","/app/ZwashAuthentication.jar"]
