FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar wishlist-service.jar
EXPOSE 8083


ENTRYPOINT ["java", "-jar", "wishlist-service.jar"]
