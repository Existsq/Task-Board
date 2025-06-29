FROM openjdk:21-slim

WORKDIR /app

COPY target/eureka-server-0.0.1.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]