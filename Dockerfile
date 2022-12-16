# Generate a docker image for this Spring Boot application
# This image will be used to run the application in a container
# The image will be based on the official openjdk image

# To build the image, run the following command from the root of the project
# docker build -t <image_name> .

# To run the container use the following command
# docker run -p 8080:8080 <image_name>

# Specify the base image

FROM openjdk:17-jdk
# Receive the arg JAR_FILE from the command line
# Default value is build/libs/\*.jar [for Gradle]
ARG JAR_FILE=build/libs/\*.jar

# Specify the working directory
WORKDIR /usr/src/app

# Copy the jar file to the working directory
COPY ${JAR_FILE} app.jar

# Expose the port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","app.jar"]

