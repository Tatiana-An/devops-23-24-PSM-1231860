## Class assignment 4 part 1 - report
#### *Containers with Docker*
Containerization is a process of encapsulating an application and its dependencies into a container. This container can run on any machine that has the Docker Engine installed, regardless the computing environment. Containers are lightweight, portable, and efficient, ensure that applications behave the same regardless of where they are deployed.
Docker is a popular containerization platform that provides tools and resources to create, deploy, and manage containers effectively. By using Docker, developers can achieve greater consistency, scalability, and efficiency in their development and deployment processes.

The goal of this assignment is to practice using Docker by creating a Docker image for a chat server application.
The steps taken to complete this assignment are described below.

### Step 1 - Set up Docker Desktop
To begin with Docker, I installed Docker Desktop, which provides a Docker Engine and a user-friendly interface for managing containers.
1. Downloaded Docker Desktop from the official website and installed it.
2. Signed up for a Docker Hub account to be able to push to my Docker Hub account.
3. Verified the installation by running the following command in the terminal:
```bash
docker --version
```

### Step 2 - Create Dockerfile to build and run
For the first version of the Dockerfile, the goal is to build and run the chat server application inside the Docker container.

**Creating the Dockerfile**
1. To start, I used the command `docker init` to create a Dockerfile. This generates a Dockerfile, .dockerignore, README.Docker.md and compose.yaml, but for this part I only used the Dockerfile.
2. I updated the Dockerfile with the following content:
- Added OpenJDK 17 as the base image for the container:
```dockerfile
# Use a base image with Gradle and JDK 17
FROM gradle:jdk17 AS builder
```
- Set the working directory for where to clone the repository:
```dockerfile
# Set the working directory
WORKDIR /CA4/part1
# Clone the chat application from Git
RUN git clone https://bitbucket.org/pssmatos/gradle_basic_demo.git
```

- Changed the working directory to the cloned repository and built the application using Gradle, minding the needed permissions:
```dockerfile
# Change the working directory to the cloned repository
WORKDIR /CA4/part1/gradle_basic_demo

# Build the application using Gradle
RUN chmod +x gradlew
RUN ./gradlew build
```
- Used `ENTRYPOINT` to specify the command to run the application and the port to be used for the chat server:
```dockerfile
# Run the chat server
ENTRYPOINT ["java", "-cp", "build/libs/basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]
```
**Building the Docker Image**
```bash
docker build -t chat-server:ca4-version1 -f CA4/Part1/DockerfileV1BuildRun .
```
**Tagging and Pushing the Image to Docker Hub**
```bash
docker tag chat-server:ca4-version1 tatianaantonio/chat-server:ca4-version1
docker push tatianaantonio/chat-server:ca4-version1
```
**Running the Docker Container**
```bash
docker run --name chat-server-v1 chat-server:ca4-version1
```
We can validate that the container is running by checking the Docker Desktop (the message "The chat server is running..." is displayed) or by running the client application in a separate terminal window:
```bash
cd ~/Desktop/devops-23-24-PSM-1231860/CA2/Part1/gradle_basic_demo
./gradlew runClient
```


### Step 3 - Create Dockerfile 
For this version of the Dockerfile, the goal is to build the chat server application on the host computer and then copy the JAR file into the Docker image. The Dockerfile will only be responsible for running the application.

1. To start, I used the command `docker init` to create a Dockerfile. This generates a Dockerfile, .dockerignore, README.Docker.md and compose.yaml, but I only used the Dockerfile.
2. I updated the Dockerfile with the following content:
- Added OpenJDK 17 as the base image for the container:
```dockerfile
FROM openjdk:21-jdk-slim
```
- Copied the JAR file into the container at the specified location:
```dockerfile
# Copy the JAR file into the container at /app
COPY /CA2/part1/gradle_basic_demo/build/libs/basic_demo-0.1.0.jar /CA4/part1/basic_demo-0.1.0.jar
```

- Changed the working directory to the directory where the JAR file was copied to, and specified the command to run the application:
```dockerfile
# Set the working directory
WORKDIR /CA4/part1
# Run the chat server
ENTRYPOINT ["java", "-cp", "basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]
```

**Building the Docker Image**
```bash
docker build -t chat-server:ca4-version2 -f CA4/Part1/DockerfileV2RunOnly .
```
**Tagging and Pushing the Image to Docker Hub**
```bash
docker tag chat-server:ca4-version2 tatianaantonio/chat-server:ca4-version2
docker push tatianaantonio/chat-server:ca4-version2
```
**Running the Docker Container**
```bash
docker run --name chat-server-v2 chat-server:ca4-version2
```
Again, we can validate that the container is running by checking the Docker Desktop (the message "The chat server is running..." is displayed) or by running the client application in a separate terminal window

### Step 4 - Conclusion
After committing the readme file, a new tag will be created and pushed to the repository:
```bash
git tag ca4-part1
git push --tags
```

### **Conclusion**

Containerizing the chat server application using Docker offers several benefits, including consistency across environments, simplified dependency management, and ease of deployment. Through this assignment, I was able to practice using Docker to create a Docker image for a chat server application. By following the steps outlined above, I successfully built the Docker image and ran the chat server application inside a Docker container. This assignment provided valuable hands-on experience with Docker and containerization, which are essential skills for modern software development and deployment practices.