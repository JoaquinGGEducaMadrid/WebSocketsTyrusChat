# Imagen base ligera con Java 17
FROM eclipse-temurin:17-jre-alpine

# Crear directorio de la app
WORKDIR /app

# Copiar el JAR generado por Eclipse
COPY websocketstyrus.jar app.jar

# Render asigna el puerto en la variable de entorno PORT
# No lo fijamos aquí, solo lo exponemos
EXPOSE 8080
EXPOSE 8081

# Comando de arranque
CMD ["java", "-jar", "app.jar"]
