FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app


COPY --from=build /app/target/*.jar user-profile-service.jar


EXPOSE 8081

ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/userdb
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=root
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV CONFIG_SERVER_URL=http://localhost:8181
ENV KAFKA_BOOTSTRAP_SERVERS=localhost:9092


ENTRYPOINT ["java", "-jar", "user-profile-service.jar"]
