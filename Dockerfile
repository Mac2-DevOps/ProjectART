FROM amazoncorretto:17

WORKDIR /app

COPY target/ProjectArt-1.1.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
