FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar config-server.jar

EXPOSE 8181

# Run the application with environment variables
ENTRYPOINT ["java", "-jar", "config-server.jar"]
