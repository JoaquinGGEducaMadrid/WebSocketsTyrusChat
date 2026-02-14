FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY websocketstyrus.jar app.jar
COPY public/ ./public/

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

