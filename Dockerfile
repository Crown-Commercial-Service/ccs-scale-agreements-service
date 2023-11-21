FROM openjdk:21-ea-13-jdk-slim
ARG JAR_FILE=/var/jenkins_home/jobs/javabuildtest/workspace/javabuildtest/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
