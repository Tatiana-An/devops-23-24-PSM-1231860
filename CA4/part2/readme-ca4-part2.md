## Class assignment 4 part 2 - report
#### *Containers with Docker*
Multi-containerization is the use of multiple containers in a single application, each responsible for a specific service or component. This approach allows for better separation of concerns, scalability, and flexibility in managing complex applications.  Multi-containerization involves running multiple, isolated containers that work together to form a cohesive application. This approach is particularly useful for separating different components such as databases, web servers, and other services. Different tools can be used to manage multi-container applications, such as Docker Compose, Kubernetes, Docker Swarm, and Podman.

Docker Compose is a tool that allows to define and run multi-container Docker applications in a simple managing process by providing a single command to start, stop, and manage the application. It uses a `.yaml` file to define the _services_ we want to run, the _network_ configurations for each service, and the _volumes_ that will be used to store data.

The **goal** of this assignment is to create a containerized environment where the Spring application and its database can run independently of the host machine's setup. To accomplish this, it is necessary to:
- Create Docker images for both the Spring application (running on Tomcat) and the H2 database.
- Use Docker Compose to define a multi-container application, which will manage the web (Spring application) and database (H2 server) services.
- Publish the images to Docker Hub for easy access and sharing.
- Use a volume with the database container to persist database data and interact with the database container.


### Step 1 - Create Dockerfile for the database
To start this assignment, it is necessary to install Docker and log in to Docker Hub, as in the previous part of the assignment. The first step is to create a Dockerfile for the database service, which will run the H2 database server.
```dockerfile
FROM openjdk:17-jdk-slim

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk-headless wget unzip

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app/

# Download H2 Database and run it
RUN wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar -O /opt/h2.jar

#Expose console and data ports
EXPOSE 8082 9092

# Start H2 Server
CMD ["java", "-cp", "/opt/h2.jar", "org.h2.tools.Server", "-web", "-webAllowOthers", "-tcp", "-tcpAllowOthers", "-ifNotExists"]
```

This Dockerfile uses the `openjdk:17-jdk-slim` image as the base image and installs the necessary packages to run the H2 database server. It downloads the H2 database JAR file, exposes the necessary ports for the H2 server, and starts the H2 server with the specified options.

We can test the Dockerfile by building the image and running a container with the following commands:
```bash
docker build -t h2-database:ca4-part2 -f CA4/part2/DockerfileDatabase .
docker run -d -p 8082:8082 -p 9092:9092 --name h2-database h2-database:ca4-part2
```
Then, I accessed the H2 database console in the browser using the URL `http://localhost:8082` and seeing the H2 database console login page.

To stop the container, I used the command:
```bash
docker stop h2-database
```

### Step 2 - Create Dockerfile for the web application
In this step, a Dockerfile for the web application is created, which will run the Spring application on Tomcat.
```dockerfile
FROM tomcat:10-jdk17-openjdk-slim

# Install necessary packages
RUN apt-get update && apt-get install -y wget git

# Clone repository
RUN git clone https://github.com/Tatiana-An/devops-23-24-PSM-1231860.git /usr/src/app

# Change to the project directory
WORKDIR /usr/src/app/CA2/part2/react-and-spring-data-rest-basic

# Build the application using Gradle
RUN chmod +x gradlew
RUN ./gradlew build

# Copy the generated .war file to Tomcat webapps directory
RUN cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps

# Expose port where the application will be running
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
```

This Dockerfile uses the `tomcat:10-jdk17-openjdk-slim` image as the base image and installs the necessary packages to clone the repository. Similarly to what we did in CA3-Part2, here the gradle wrapper permission are changed and the Spring application is built. It then copies the generated WAR file to the Tomcat webapps directory, exposes the port where the application will be running, and starts Tomcat.


### Step 3 - Create docker-compose.yml file
For managing both containers, a `docker-compose.yml` file was created in the same directory as the Dockerfiles. This file defines the services that will be run, the network configurations, and the volumes that will be used.

```yaml
version: '3.8'

services:
  db:
    build:
      context: .
      dockerfile: DockerfileDatabase
    ports:
      - "8082:8082"
      - "9092:9092"
    volumes:
      - db_data:/usr/src/data-backup
    networks:
      app_network:
        ipv4_address: 192.168.56.11

  web:
    build:
      context: .
      dockerfile: DockerfileWeb
    ports:
      - "8080:8080"
    networks:
      app_network:
        ipv4_address: 192.168.56.10
    depends_on:
      - db

volumes:
  db_data:

networks:
  app_network:
    driver: bridge
    ipam:
      config:
        - subnet: 192.168.56.0/24
```

This `docker-compose.yml` file defines two services: `db` for the H2 database and `web` for the Spring application. It specifies the build context and Dockerfile for each service, the ports that will be exposed, the volumes that will be used, and the network configurations. The `depends_on` option ensures that the database service is started before the web service.

For testing the solution, I did as follows:
1. Ran and started the containers using the following command:
```bash
docker-compose up --build
```
2. Accessed the Spring web application using the URL `http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT` in the browser. The application was running successfully.
3. Accessed the H2 console using the URL `http://localhost:8082` in the browser, where the H2 database console login page is displayed.

### Step 5 - Push the Docker images to Docker Hub
To share the Docker images, I pushed them to Docker Hub, as follows:

```bash
docker tag db:latestversion tatianaantonio/db:latestversion
docker tag web:latestversion tatianaantonio/web:latestversion

docker push tatianaantonio/db:latestversion
docker push tatianaantonio/web:latestversion
```

### Step 6 - Ending
After committing the readme file, a new tag will be created and pushed to the repository:
```bash
git tag ca4-part2
git push --tags
```

### **Alternative solution - Kubernetes**
Several tools can be used to manage multi-container applications, such as Docker Compose, Kubernetes, Docker Swarm, and Podman. An alternative solution to this assignment would be to use Kubernetes to manage the multi-container application. Kubernetes is an open-source container orchestration platform that automates the deployment, scaling, and management of application containers across clusters of hosts. While Docker Compose is suited for small applications, and therefore, suitable for development and testing environments, Kubernetes is better suited for production environments due to its scalability and robustness.

Considering Kubernetes as an alternative tool for this class assignment, the following differences can be highlighted:
- Configuration is split into multiple YAML files for different resources (Deployments, Services, PersistentVolumeClaims...) instead of a single `docker-compose.yml` file.
- Kubernetes uses Pods to run containers, which can contain one or more containers.
- Kubernetes provides more advanced features such as:
  - auto-scaling (automatically scale the number of containers/pods based on the load)
  - load balancing (automatically balance the load between containers/pods)
  - rolling updates (update containers without downtime)
  - self-healing (restart containers that fail)
- Kubernetes requires a more complex setup and configuration compared to Docker Compose.

### **Conclusion**

This class assignment provided a hand-on experience with multi-containerization using Docker Compose. It emphasized the importance of multi-container applications in managing complex applications and services. By creating Docker images for the Spring application and H2 database, defining a multi-container application with Docker Compose, and publishing the images to Docker Hub, we were able to successfully run the application in a containerized environment. The use of volumes to persist data and interact with containers was also a valuable learning experience. Overall, this assignment provided a practical understanding of multi-containerization and its applications in real-world scenarios.