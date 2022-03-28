FROM openjdk:11 as builder
ARG JAR_FILE=target/*.jar
COPY target/contadoria-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8081