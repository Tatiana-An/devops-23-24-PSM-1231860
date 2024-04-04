## Class assignment 2 part 1 - report
#### *Build tools with Gradle*
Build tools (like Maven or Gradle) are essential components in software development. They automate processes like compiling the source code,
running tests, packaging artifacts, or start applications.

The goal of this assignment is to gain hands-on experience with Gradle by, executing tasks, adding new tasks, configuring unit tests,
and managing project files using Gradle.

### Step 1 - Project setup
This class assignment uses a Gradle demo application that implements a basic multithreaded
chat room server. The project was cloned from the available bitbucked repository [gradle-basic-demo](https://bitbucket.org/psm-2021/gradle-basic-demo/), as follows:
1. In the local directory of the DevOps repository, Git Bash command-line was oppened, and the following commands were executed:
```bash
git clone https://bitbucket.org/psm-2021/gradle-basic-demo.git
cd gradle-basic-demo
rm -rf .git
git status
git add .
git commit -m "Added gradle basic demo. Closed #4"
git push
```
### Step 2 - Running the application
1. **Build the application**

   In the Git Bash terminal, this command was executed to compile the source code, run tests, and package the application into a .jar file:
```bash
./gradlew build
```
2. **Run the server**

   In the Git Bash terminal, in the project's root directory, I executed the following command to start the server:
```bash 
java -cp build/libs/basic_demo-0.1.0.jar basic_demo.ChatServerApp 59001
```
3. **Run a client**

   In another Git Bash terminal, I executed the following command to start a client:
```bash
./gradlew runClient
```
The chat application was tested by sending messages between clients and connecting/disconnecting multiple clients. Because this
server is multithreaded, it can handle several clients simultaneously. The server was stopped by pressing `Ctrl+C` in the terminal.

### Step 3 - Adding new task to run the server
1. In the IDE, the `build.gradle` was opened and a new task named `runServer` was added to run the server with a default port of 59001:
```gradle
task runServer(type: JavaExec, dependsOn: classes) {
    group = 'DevOps'
    description = 'Launches the chat server'
    classpath = sourceSets.main.runtimeClasspath
    mainClass = 'basic_demo.ChatServerApp'
    args '59001'
}
```
This task is of type `JavaExec`, which allows running a Java application from the command line, and it depends on `classes` ,
which means it will only be executed after classes are compiled. The `mainClass` property specifies the main class of the application, and the `args` property passes the server port as an argument.
After adding this task, Gradle was reloaded in the IDE.

2. Built the project by running the created task:
```bash
./gradlew runServer
```

3. Commited changes:
```bash
git status
git add .
git commit -m "Added new task in build.gradle to execute the server. Closed #5"
git push
```

### Step 3 - Adding unit test
1. Added the given unit test in class `AppTest`
```java
package basic_demo;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest { 
    @Test public void testAppHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
```

2. Added the required dependencies in `build.gradle` to use JUnit 4.12:
```gradle
dependencies {
   // Use Apache Log4J for logging
   implementation group: 'org.apache.logging.log4j', name: 'log4j-api', version: '2.11.2'
   implementation group: 'org.apache.logging.log4j', name: 'log4j-core', version: '2.11.2'
   implementation 'org.testng:testng:7.1.0'
   implementation 'junit:junit:4.12'
}
```
3. Built the project:
```bash
./gradlew build
```

4. Commited changes:
```bash
git status
git add .
git commit -m "Added unit test and updated the gradle script. Closed #6"
git push
```

### Step 4 - Adding new task to copy source files
1. To create a task that creates a backup of the source files into a `backup` folder, the following task was added to `build.gradle`:
```gradle
task backupSources(type: Copy) {
    group = 'Backup'
    description = 'Make a backup of the application sources'

    // Directory to copy
    def sourceDir = file('src')

    // Directory of copy destination
    def backupDir = file("${buildDir}/backup/src")

    // Set source and destination
    from sourceDir
    into backupDir
}
```
This task is of type `Copy`, which allows copying files or directories. The `from` property specifies the source directory,
and the `into` property specifies the destination directory.

2. The created task was executed in the Git Bash terminal:
```bash
./gradlew backupSources
```

4. Commited changes:
```bash
git status
git add .
git commit -m "Added new task Copy. Closed #7"
git push
```

### Step 5 - Adding new task to 
1. To create a task that creates a backup in an archive format (.zip) of the source files into the `backup` folder, the following
task was added to `build.gradle`:

```gradle
task zipSources(type: Zip) {
    group = 'Backup'
    description = 'Create a zip file of the application sources'

    // Directory to zip
    def sourceDir = file('src')

    // Zip file destination
    def zipFile = file("${buildDir}/archives/sources.zip")

    // Set name for zip file, source and destination
    from sourceDir
    archiveFileName = 'src.zip'
    destinationDirectory = zipFile.parentFile
}
```
This task is of type `Zip`, which allows the creation of a zip file. The `from` property specifies the source directory, and the `archiveFileName` property specifies the name of the zip file.

2. The created task was executed in the Git Bash terminal:
```bash
./gradlew zipSources
```

4. Commited changes:
```bash
git status
git add .
git commit -m "Added new task to zip src. Closed #8"
git push
```

### Conclusion
In this assignment, we successfully worked with Gradle to manage and automate the build process of a simple Java application.
We executed tasks, added new tasks, configured unit tests, and managed project files using Gradle. By completing this assignment
we gained a better understanding of Gradle capabilities and how to use them in a real-world project.