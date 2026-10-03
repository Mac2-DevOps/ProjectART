FROM openjdk:11
COPY target/ProjectART.jar /tmp/
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "ProjectART.jar"]