FROM gradle:jdk17 as builder

# Set the working directory
WORKDIR /CA4/part1

##Clone the Git repository
RUN git clone https://bitbucket.org/pssmatos/gradle_basic_demo.git

##Change the working directory to the root directory of the cloned repository
WORKDIR /CA4/part1/gradle_basic_demo
RUN chmod +x gradlew

##Build the project
RUN ./gradlew build

FROM openjdk:17-jdk-slim

WORKDIR /ca4-part1

##Copy the built JAR file from the first stage
COPY --from=builder /CA4/part1gradle_basic_demo/build/libs/*.jar ca4-part1.jar

##Specify the command to run the application
##Replace <server port> with the actual server port you want to use
ENTRYPOINT ["java", "-cp", "ca4-part1.jar", "basic_demo.ChatServerApp", "59001"]