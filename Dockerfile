FROM openjdk:11 as builder
ARG JAR_FILE=target/*.jar
RUN mvn -f pom.xml clean package
COPY target/contadoria-0.0.1-SNAPSHOT.jar contadoria.jar
ENTRYPOINT ["java","-jar","contadoria.jar"]
EXPOSE 8081
