# Use the official Maven image to build the application
FROM maven:3.8.4-openjdk-17 AS build 

# Set the working directory
WORKDIR /app 

# Copy only POM file to fetch dependencies

COPY pom.xml .

# Fetch the dependencies
RUN mvn dependencies:go-offline

# Copy the source code

COPY src src 

# Build the application with test skipping

RUN mvn clean package -DskipTests 

# Create a lightweight image with only 
FROM openjdk:17-jdk-alpine

# Set the working dierectory in the container
WORKDIR /app 

# Copy the JAR file from the build stage

COPY --from=build /app/target/activityleaderboard-0.0.1-SNAPSHOT.jar /app/


# Expose the port that the application will run on
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "activityleaderboard-0.0.1-SNAPSHOT.jar"]


