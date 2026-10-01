#Imagen de Java 21
FROM eclipse-temurin:21-jdk-alpine

#directorio del contenedor
WORKDIR /app

#copiamos .jar compilado desde la carpeta target al contenedor
COPY target/*.jar app.jar

#exponemos el puerto 8080 para que podamos acceder a la web
EXPOSE 8080

#comando para ejecutar docker al encender el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]
