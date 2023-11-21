FROM maven:3.9.5
RUN mvn clean install package
FROM openjdk:21-ea-13-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
