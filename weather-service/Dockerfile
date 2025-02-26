FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app


COPY --from=build /app/target/*.jar weather-service.jar


EXPOSE 8080

ENV EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://service-registry:8761/eureka/
ENV OPEN_API_KEY=c18d907f51bf7046b95d6cc17dac02a5


ENTRYPOINT ["java", "-jar", "weather-service.jar"]
