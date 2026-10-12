FROM amazoncorretto:17
WORKDIR /app
COPY target/ProjectArt-1.1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]