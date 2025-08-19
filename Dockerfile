FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests   # installs ZwashCommon into local repo
WORKDIR /app/ZwashAuthentication
RUN mvn clean package -DskipTests   # builds ZwashAuthentication using installed ZwashCommon

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/ZwashAuthentication/target/*.jar ZwashAuthentication.jar
ENTRYPOINT ["java","-jar","/app/ZwashAuthentication.jar"]
