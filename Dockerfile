FROM eclipse-temurin:11-jdk
COPY target/ProjectART.jar /tmp/
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "ProjectART.jar"]