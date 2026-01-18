FROM eclipse-temurin:17-jdk-alpine

RUN apk add --no-cache nodejs npm

WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]