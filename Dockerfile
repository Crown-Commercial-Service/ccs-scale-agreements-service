FROM maven:3.9.9-eclipse-temurin-23 as build

RUN mkdir -p /tmp/build

WORKDIR /tmp/build

COPY . /tmp/build

RUN mvn clean install
RUN mvn package

# Use an official OpenJDK runtime as a base image
FROM eclipse-temurin:23

RUN mkdir /app

# Copy the application JAR file and external configuration files
COPY --from=build /tmp/build/target/ccs-scale-agreements-service-*.jar /app/ccs-scale-agreements-service.jar

RUN groupadd ujava; \
    useradd -m -g ujava -c ujava ujava; \
    chown -R ujava:ujava /app

USER ujava

# Set the working directory inside the container
WORKDIR /app

# Specify the profile to be used when running the application
ENV SPRING_PROFILES_ACTIVE=prod

# Expose the port your application will run on
EXPOSE 3000

# Command to run your application
CMD ["java", "-jar", "ccs-scale-agreements-service.jar"]