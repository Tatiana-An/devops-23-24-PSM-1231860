## Class assignment 2 part 2 - report
#### *Build tools with Gradle*
Gradle, like Maven, is a build tool, an essential components in software development that automate processes like compiling the source code,
running tests, packaging artifacts, or start applications.

The goal of this assignment is to convert the "basic" version of the Tutorial application from Maven to Gradle.
This involves adding dependencies and creating tasks in the `build.gradle` file to compile, test, and run the application.

### Step 1 - Creating a new branch
In order to isolate changes and keep a stable version of the project in the main branch, a new branch named `tut-basic-gradle` was
created. In `CA2` directory of the DevOps repository, Git Bash command-line was oppened, and the following commands were executed:
```bash
git branch tut-basic-gradle
git checkout tut-basic-gradle
```

### Step 2 - Setting up the project
1. Changed to the working directory of the project:
```bash
cd part2/react-and-spring-data-rest
```
2. Followed the instruction in the readme file
3. Using Spring Initializr (https://start.spring.io), a new Gradle project was created with the following dependencies:
   - Project: Gradle - Groovy
   - Language: Java
   - Spring Boot: 3.2.4
   - Project Metadata:
     - Group: com.greglturnquist
     - Artifact: react-and-spring-data-rest-basic
     - Name: react-and-spring-data-rest-basic
     - Package name: com.greglturnquist.payroll
     - Packaging: Jar
     - Java: 17
   - Dependencies: Rest Repositories, Thymeleaf, Spring Data JPA, H2 Database
4. The project was downloaded and extracted to the `part2/react-and-spring-data-rest` directory. Checked the available gradle tasks by running:
```bash
./gradlew tasks
```
5. Adaptations were made to integrate the Maven project into Gradle:
    - Deleted `src` folder, using the bash command `rm -rf src`
    - Copied the `src` folder from the Maven project with `webpack.config.js` and `package.json`, into the new Gradle project directory
    - Deleted the `src/main/resources/static/built/` directory to avoid conflicts with the Gradle build
6. Commited changes
```bash
git status
git add .
git commit -m "Setup of gradle spring boot project for CA2 part2. Closed #10"
git push
```


### Step 3 - Building the application
1. **Solving minor compilation errors**

Upon trying to build the project, the following compilation error was encountered:
```bash
Employee.java:22: error: package javax.persistence does not exist
import javax.persistence.Entity;
```
This was solved by changing the import statement in the `Employee.java` file from `javax.persistence.*` to `jakarta.persistence.*`.

2. **Building and running the application**

```bash
./gradlew build
./gradlew bootRun
```

Localhost was opened in the browser at `http://localhost:8080/` to check if the application was running. However, the webpage was empty, because gradle was still missing the frontend part of the application.
The server was stopped by pressing `Ctrl+C` in the terminal.

### Step 4 - Adding and configuring a gradle plugin to manage frontend


1. **Plugin configuration**

Added the `org.siouan.frontend` plugin (of java version 17) to the `plugins` block, in the `build.gradle` file:
```gradle
id "org.siouan.frontend-jdk17" version "8.0.0"
```
Also, in the `build.gradle` , added to the block `frontend` the following configuration to manage frontend assets through Gradle commands:
```gradle
frontend {
nodeVersion = "16.20.2"
assembleScript = "run build"
cleanScript = "run clean"
checkScript = "run check"
}
```
2. **Updating package.json**
In order to bridge the Node.js and Gradle build processes, the `package.json` file was updated with the following scripts:
```json
"scripts": {
"watch": "webpack --watch -d --output ./target/classes/static/built/bundle.js",
"packageManager": "npm@9.6.7",
"webpack": "webpack",
"build": "npm run webpack",
"check": "echo Checking frontend",
"clean": "echo Cleaning frontend",
"lint": "echo Linting frontend",
"test": "echo Testing frontend"
}
```

3. **Building and running the application**

3.1. Upon trying to build the project, the following compilation error was encountered:
```bash
Execution failed for task ':resolvePackageManager'.
> org.siouan.frontendgradleplugin.domain.InvalidJsonFileException: Invalid JSON file
```

3.2. This was solved by adding the configuration of package manager in the `package.json` file:
```json
"packageManager": "npm@9.6.7",
```

3.3. Then the application was built, compiling both the Java and Javascript code, which generates the frontend assets.
```bash
./gradlew build
```
3.4. Ran the application:
```bash
./gradlew bootRun
```

Localhost was opened in the browser at `http://localhost:8080/` to check if the application was running. The webpage was
displayed correctly, showing the React frontend part of the application. The server was stopped by pressing `Ctrl+C` in the terminal.

3.5. Commited changes:
```bash
git status
git add .
git commit -m "Added and configured a gradle plugin to manage the frontend. Closed #11"
git push
```

### Step 5 - Adding new task to copy .jar generated by build
1. To package the built .jar files, the following task was added to `build.gradle`:
```gradle
task copyJar(type: Copy) {
	group = 'Copy'
	description = 'Copy JAR file generated by build to the dist folder'

	from 'build/libs/'
	into 'dist'
	include '*.jar'
}
```
This task is of type `Copy`, which allows copying files or directories. The `from` property specifies the source directory,
and the `into` property specifies the destination directory. After adding this task, Gradle was reloaded in the IDE.

2. The created task was executed in the Git Bash terminal:
```bash
./gradlew copyJar
```
The task was executed successfully, and the .jar file was copied to the `dist` folder.

4. Commited changes:
```bash
git status
git add .
git commit -m "Added task to gradle (and executed) to copy .jar generated by build to folder dist. Closed #12"
git push
```

### Step 5 - Adding new task to delete files generated by webpack
1. To create a task that deletes the files generated by webpack, the following task was added to `build.gradle`:
```gradle
task cleanWebpack(type: Delete) {
	group = 'Cleanup'
	description = 'Delete files generated by webpack'
	delete 'src/main/resources/static/built'

	clean.dependsOn this
}
```
This task is of type `Delete`, which allows deleting files or directories. The `delete` property specifies the directory
to be deleted. The `clean.dependsOn this` line ensures that the `cleanWebpack` task is automatically executed before the `clean` task.

2. The created task was executed in the Git Bash terminal:
```bash
./gradlew cleanWebpack
```

4. Commited changes:
```bash
git status
git add .
git commit -m "Added task to gradle (and executed) to delete files generated by webpack. Closed #13"
git push
```
### Step 6 - Ending

2.5. Merged the branch "tut-basic-gradle" into the main branch, using the command --no-ff (no fast forward) to keep the branch history:
   ```bash
   git checkout main
   git merge --no-ff tut-basic-gradle
   git push
   ```

2.6. After committing the readme file, a new tag will be created and pushed to the repository:
   ```bash
   git tag ca2-part2
   git push --tags
   ```

### Alternative build automation tool to Gradle - Apache Ant
Building automation tools allow developers to automate repetitive tasks, manage dependencies, while building and packaging
projects with ease. Apache Ant is an alternative to Gradle, which is a Java-based build tool that uses XML to describe the
build process. While Apache Ant predates Gradle or Maven, it continues to be a popular choice for many developers and organizations
due to its simplicity and flexibility, especially in legacy projects.

- **Building and running the application with Apache Ant**

To build and run the application using Apache Ant, a build script (`build.xml` file) needs to be created to define the build
process. In Ant, different phases of a build process are called "targets", which should be defined in the build script, as follows:
```xml
<!-- Target to compile Java source files -->
<target name="compile" description="Compile Java source files">
    <javac srcdir="src" destdir="build/classes"/>
</target>

<!-- Target to run the application -->
<target name="run" depends="compile" description="Run the application">
    <java classname="com.greglturnquist.ReactAndSpringDataRestApplication" classpath="build/classes"/>
</target>
````

To compile and run the application using Apache Ant, the following commands can be executed in the terminal:
```bash
ant compile
ant run
```

- **Adding a new task to copy jar with Apache Ant**

To add a new task to copy the generated .jar file to a `dist` folder, a new target should be added to `build.xml` file:
```xml
<!-- Target to copy the generated .jar file to dist folder -->
<target name="copyJar" description="Copy jar file to dist folder">
    <copy file="build/libs/react-and-spring-data-rest-basic.jar" todir="dist"/>
</target>
```

- **Adding a new task to delete webpack-generated files with Apache Ant**

To add a new task to delete the files generated by webpack before executing the clean task, a new target should be added to `build.xml` file:
```xml
<project name="ReactAndSpringDataRest" default="clean" basedir=".">
<!-- Target to delete webpack-generated files -->
<target name="cleanWebpack" description="Delete webpack-generated files">
    <delete dir="src/main/resources/static/built"/>
</target>
    <!-- Modify clean target to execute cleanWebpack in first place -->
    <target name="clean" description="Clean up project">
        <antcall target="cleanWebpack"/>
        <delete dir="build"/>
    </target>
</project>
```
The cleanWebpack target deletes the directory containing webpack-generated files, while the clean target, which is typically
provided by Ant for cleaning up the project, is modified to execute cleanWebpack before deleting the build directory.

**Limitations of Apache Ant**

One disadvantage of Apache Ant is the limited *convention over configuration*. This means it does not enforce a strict
project structure or configurations, which can lead to inconsistencies across projects and require more manual configuration.
Another important limitation of Apache Ant, that would be an obstacle to the implementation of this class assignment using Ant,
is the lack of a built-in *dependency management system*. Apache Ant does not natively support frontend development tasks like
managing JavaScript, CSS, or other assets typically associated with modern web development.

**Dependency management in Apache Ant**

A possible solution to the lack of dependency management in Apache Ant is to integrate Ant with dependency management tools
like Ivy or use custom tasks to manage frontend assets. While Apache Ivy is primarily focused on dependency resolution and
retrieval, it can be integrated with Apache Ant to provide some of the functionality typically associated with build automation
tools like Maven or Gradle.

**Conclusion**
In summary, Apache Ant is a viable alternative to Gradle for build automation, especially when used together with Apache Ivy.
However, this addition would not fully replace the need for a comprehensive build automation tool like Gradle or Maven,
particularly for complex projects. Besides, this solution also requires more manual configuration to achieve a similar level of automation.