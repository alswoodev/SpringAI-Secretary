FROM eclipse-temurin:17-jdk-alpine

RUN apk add --no-cache nodejs npm
RUN apk add --no-cache gettext

WORKDIR /app

COPY build/libs/*.jar app.jar
COPY .env .env

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]