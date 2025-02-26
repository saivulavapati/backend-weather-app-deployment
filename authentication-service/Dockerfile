FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar authentication-service.jar
EXPOSE 8082

ENV CONFIG_SERVER_URL=http://localhost:8181
ENV JWT_SECRETKEY=d05fa4badc91955cd239184b42c1b6f72d2a2f6798798354c0609f876923f41d
ENV JWT_EXPIRATION=1800000
ENV JWT_COOKIE_NAME=weather-jwt-cookie
ENV KAFKA_BOOTSTRAP_SERVERS=localhost:9092


ENTRYPOINT ["java", "-jar", "authentication-service.jar"]
