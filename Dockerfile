# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy both common and authentication code
COPY ZwashCommon /app/ZwashCommon
RUN mvn clean package -DskipTests
COPY ZwashAuthentication /app/ZwashAuthentication

# Install common into local Maven repo
RUN cd ZwashCommon && mvn clean install -DskipTests

# Build Authentication service
RUN cd ZwashAuthentication && mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/ZwashAuthentication/target/*.jar ZwashAuthentication.jar
ENTRYPOINT ["java", "-jar", "/app/ZwashAuthentication.jar"]
