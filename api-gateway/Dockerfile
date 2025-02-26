FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar api-gateway.jar
EXPOSE 9000

ENV JWT_SECRETKEY=d05fa4badc91955cd239184b42c1b6f72d2a2f6798798354c0609f876923f41d
ENV CONFIG_SERVER_URL=http://localhost:8181
ENV FRONTEND_URL=http://localhost:3000

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
