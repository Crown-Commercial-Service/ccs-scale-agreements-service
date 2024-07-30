# pull mavan and pin version to 3.9.8
FROM maven:3.9.8 as build

ENV SPRING_PROFILES_ACTIVE prod
ENV TMP_DIR /tmp/build

WORKDIR $TMP_DIR

COPY . $TMP_DIR

RUN mvn clean install
RUN mvn package

# pull open jdk 21
FROM openjdk:21

WORKDIR /app

# Copy app jar files and external configuration files
COPY --from=build $TMP_DIR/target/ccs-agreements-service-*.jar /app/ccs-agreements-service.jar

# Create the user and usergroup for the app to run as
RUN groupadd -g 10001 ujava && \
    useradd -u 10000 -g ujava ujava \
    && chown -R ujava: /app

USER ujava

# Working directory for the app
WORKDIR  /app

# Expose the port for the application to run on
EXPOSE 3000

# Command to run once the container starts
CMD ["java", "-jar", "ccs-agreements-service.jar"]