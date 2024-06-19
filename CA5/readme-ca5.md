## Class assignment 5 - report
#### *CI/CD Pipelines with Jenkins*
Continuous Integration (CI) and Continuous Deployment (CD) are essential practices that allow the automation of building, testing, and deployment processes. For this class assignment, we will create a CI/CD pipeline using Jenkins, a popular automation server. Jenkins allows the creation of a series of automated steps (pipeline), using a domain-specific language known as Jenkinsfile.

The goal of this assignment is to use Jenkins to create pipeline scripts for existing projects in our class repositories with different stages:

**[Task 1](#task-1---setting-up-jenkins):** Create a Jenkins server and a pipeline with the given script


**[Task 2](#task-2---creating-a-pipeline-for-gradle-basic-demo-project-ca2part1):** Create a pipeline for gradle basic demo project (CA2Part1), including the following stages:
- Checkout the repository
- Assemble the project
- Test the project
- Archive the generated binary file from the assemble


**[Task 3](#task-3---creating-a-pipeline-for-tutorial-spring-boot-app-ca2part2):** Create a pipeline for the tutorial spring boot app (CA2Part2), including the following stages:
- Checkout the repository
- Assemble the project
- Test the project
- Generate Javadoc and publish it in Jenkins
- Archive the generated binary file from the assemble
- Generate and Publish a docker image in Docker Hub.

### Task 1 - Setting up Jenkins

#### 1. Jenkins Installation

- Firstly, Jenkins was installed by using a pre-built docker image with Jenkins:
```bash
    docker run -d -p 8080:8080 -p 50000:50000 -v jenkins-data:/var/jenkins_home --name=jenkins jenkins/jenkins:lts-jdk17
```
- Accessed the Jenkins dashboard at `http://localhost:8080` and entered the initial admin password, that can be found in the container log.
- Under `Manage Jenkins`, navigated to `Plugins` to install the suggested plugins. I also installed other plugins that should be needed later in this assignment:
  - Docker
  - Docker API 
  - Docker Commons
  - Docker pipeline
  - HTML Publisher plugin
  - NodeJS

#### 2. Experimenting with an example pipeline script
- After restarting Jenkins, in order to practice with Jenkins, I created a new Job by clicking on `New Item` and selecting `Pipeline`. Named it "CA5-task1" and clicked `OK`.
- As a first experiment, we used a script directly in the pipeline configuration. After selecting `Pipeline script`, I added the following given script:
```groovy
pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        echo 'Checking out...'
        git 'https://bitbucket.org/pssmatos/gradle_basic_demo'
      }
    }
    stage('Build') {
      steps {
        echo 'Building...'
        sh './gradlew clean build'
      }
    }
    stage('Archiving') {
      steps {
        echo 'Archiving...'
        archiveArtifacts 'build/distributions/*'
      }
    }
  }
}
```
This simple script has 3 stages: checkout, build and archiving.
- After saving, I clicked on `Build Now` to run the pipeline.
- Under the `Console Output` section, it is possible to see the output of each stage of the pipeline.

### Task 2 - Creating a pipeline for gradle basic demo project (CA2Part1)
- In my repository, under the directory `CA5`, I created a new file Jenkinsfile1.
- In a similar way as the previous script, I created the following pipeline script for the gradle basic demo project (CA2Part1)
```groovy
pipeline {
  agent any

  stages {
    //Checkout the project from the repository
    stage('Checkout') {
      steps {
        echo 'Checking out repository...'
        git branch: 'main', url: 'https://github.com/Tatiana-An/devops-23-24-PSM-1231860.git'
        dir('CA2/part1/gradle_basic_demo') {
        }
      }
    }

    //Assemble to generate the binary file
    stage('Assemble') {
      steps {
        echo 'Assembling...'
        dir('CA2/part1/gradle_basic_demo') {
          //Change gradlew permissions
          sh 'chmod +x gradlew'
          sh './gradlew assemble'
        }
      }
    }

    //Run the tests
    stage('Test') {
      steps {
        echo 'Running tests...'
        dir('CA2/part1/gradle_basic_demo') {
          sh './gradlew test'
        }
      }
    }

    //Archive the generated binary file
    stage('Archive') {
      steps {
        echo 'Archiving...'
        dir('CA2/part1/gradle_basic_demo') {
          archiveArtifacts artifacts: 'build/libs/*.jar'
        }
      }
    }
  }
}
```

This Jenkinsfile defines a pipeline with 4 stages: checkout, assemble, test, and archive. The script checks out my repository, assembles the project, runs the tests, and archives the generated binary file. The script is saved in the repository under the directory `CA5` but all stages are executed in the directory `CA2/part1/gradle_basic_demo`. Since our class repositories are public, I didn't need to configure any credentials to access the repository. Otherswise, it is possible to use Jenkins' credentials manager.
- After pushing the Jenkinsfile1 to the repository, I created a new pipeline job in Jenkins named "CA5-task2" and configured it to use the Jenkinsfile1 script:
  - Clicked on "New Item"
  - Enter the name, selected "Pipeline" and clicked "OK"
  - Under the "Pipeline" section, selected "Pipeline script from SCM"
  - Choose Git as the SCM (Source Code Management)
  - In the "Repository URL" field, entered the URL of my repository 
  - Changed the branch to */main
  - For the "Script Path" field, entered the path to Jenkinsfile1 from the repository root (CA5/Jenkinsfile1) and clicked "Save"
- Ran the pipeline by clicking on "Build Now".
- The pipeline executed successfully.

### Task 3 - Creating a pipeline for tutorial spring boot app (CA2Part2)
To achive the requirements for this second exercise, we need a Jenkins server with capabilities to run dockers.
- Deleted the previous Jenkins container and followed the instructions provided (available at https://www.jenkins.io/doc/book/installing/docker/) to configure a network and two machines: one for Jenkins and another for Docker. 
- Configured my dockerhub credentials in Jenkins, under `Manage Jenkins`, in the `Credentials` section. Added my Docker username and password, and used the generated ID to reference the credentials in the pipeline script.
- To fulfill all requirements, I created a new Jenkinsfile2 in the repository under the directory `CA5`:

```groovy
pipeline {
  agent any

  environment {
    DOCKER_CREDENTIALS_ID = '8cb6f141-8476-4915-b4d9-1b9fca9ba26f'
    DOCKER_IMAGE = 'tatianaantonio/ca5-task3'
    DOCKER_TAG = "${env.BUILD_ID}"
  }

  stages {
    //Checkout the project from the repository
    stage('Checkout') {
      steps {
        echo 'Checking out code from the repository'
        git branch: 'main', url: 'https://github.com/Tatiana-An/devops-23-24-PSM-1231860.git'
      }
    }

    //Assemble to generate the binary file
    stage('Assemble') {
      steps {
        dir('CA2/part2/react-and-spring-data-rest-basic') {
          echo 'Assembling the application...'
          sh 'chmod +x ./gradlew'
          sh './gradlew assemble'
        }
      }
    }

    //Run the tests
    stage('Test') {
      steps {
        dir('CA2/part2/react-and-spring-data-rest-basic') {
          echo 'Running tests...'
          sh './gradlew test'
        }
      }
    }

    //Generate Javadoc and publish it
    stage('Javadoc') {
      steps {
        dir('CA2/part2/react-and-spring-data-rest-basic') {
          echo 'Generating Javadoc...'
          sh './gradlew javadoc'
          publishHTML(target: [
                  allowMissing: false,
                  alwaysLinkToLastBuild: false,
                  keepAll: true,
                  reportDir: 'build/docs/javadoc',
                  reportFiles: 'index.html',
                  reportName: 'Javadoc'
          ])
        }
      }
    }

    //Archive the generated binary file
    stage('Archive') {
      steps {
        dir('CA2/part2/react-and-spring-data-rest-basic') {
          echo 'Archiving artifacts...'
          archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
        }
      }
    }

    //Create Dockerfile
    stage('Create Dockerfile') {
      steps {
        dir('CA2/part2/react-and-spring-data-rest-basic') {
          script {
            def dockerfileContent = """
                        FROM gradle:jdk21
                        WORKDIR /app
                        COPY build/libs/*.jar app.jar
                        EXPOSE 8080
                        ENTRYPOINT ["java", "-jar", "app.jar"]
                        """
            writeFile file: 'Dockerfile', text: dockerfileContent
          }
        }
      }
    }

    //Publish the Docker image
    stage('Push Docker Image') {
      steps {
        script {
          echo 'Publishing Docker image...'
          docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIALS_ID}") {
            dir('CA2/part2/react-and-spring-data-rest-basic') {
              def customImage = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
              customImage.push()
              customImage.push('latest')
            }
          }
        }
      }
    }
  }
}
```
This Jenkinsfile defines a pipeline with 6 stages: checkout, assemble, test, javadoc, archive, create Dockerfile, and push Docker image. The script checks out my repository, assembles the project, runs the tests, generates Javadoc, archives the generated binary file, creates a Dockerfile, and pushes the Docker image to Docker Hub. The script is saved in the repository under the directory `CA5` but all stages are executed in the directory `CA2/part2/react-and-spring-data-rest-basic`.

- Following the same steps as before, I created a new pipeline job in Jenkins named "CA5-task3" and configured it to use the Jenkinsfile2 script.
- After this last version of the pipeline script was pushed, it executed successfully in Jenkins.

### Step 6 - Ending
After committing the readme file, a new tag will be created and pushed to the repository:
```bash
git tag ca5
git push --tags
```

### **Conclusion**

This class assignment provided a practical understanding of CI/CD pipelines using Jenkins. After experimenting and gaining some hand-on experience with Jenkins in the first task, we created two pipeline scripts for existing projects in our class repositories. Each task required the creation of a Jenkinsfile with different stages, the configuration of Jobs in the Jenkins dashboard, and the execution of the pipelines.
Overall, this assignment provided a practical understanding of Jenkins as a powerful CI/CD tool and the creation of pipelines for different projects.