# pull mavan and pin version to 3.9.8
FROM maven:3.9.8 as build
WORKDIR /tmp/build
COPY . /tmp/build
RUN mvn clean install
RUN mvn package
# pull open jdk 21
FROM openjdk:21
WORKDIR /app
# Copy app jar files and external configuration files
COPY --from=build /tmp/build/target/ccs-scale-agreements-service-*.jar /app/ccs-scale-agreements-service.jar
# Create the user and usergroup for the app to run as
RUN groupadd -g 10001 ujava && \
    useradd -u 10000 -g ujava ujava \
    && chown -R ujava: /app
USER ujava
# Working directory for the app
WORKDIR  /app
# profile to be used when running the application
ENV SPRING_PROFILES_ACTIVE=prod
# Expose the port for the application to run on
EXPOSE 3000
# Command to run once the container starts
#CMD ["java", "-jar", "ccs-agreements-service.jar"]
ENTRYPOINT ["java", "-jar", "./ccs-agreements-service.jar"]
