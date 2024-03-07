## Class assignment 1 - report
#### *Version Control with Git*
This assignment consists of a simple git workflow.
For the purpose of class assignments, a collection of apps from the [Spring Data REST tutorial](https://spring.io/guides/tutorials/react-and-spring-data-rest) repository were previously cloned, as follows:
1. A repository for DevOps classes were created on Github and named [devops-23-24-PSM-1231860](https://github.com/Tatiana-An/devops-23-24-PSM-1231860). Professors were added as collaborators.
2. In a local folder, Git Bash command-line was oppened, and the following commands were executed:
```bash
git init
echo "empty" >> README.md
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/Tatiana-An/devops-23-24-PSM-1231860.git
git push -u origin main
```
3. Then a new folder named tutorialApplication was created and the Spring Data REST tutorial repository was cloned:
```bash
git clone https://github.com/spring-guides/tut-react-and-spring-data-rest
```
4. a

For CA1, the [basic project](tut-react-and-spring-data-rest/basic) from the Spring Data Rest tutorial will be used as sample application.

### Part 1 - without branches (master branch only)
1. **CA1 project set up**
   1. The tut-react-and-spring-data-rest was copied into a new folder named CA1.
   2. Inside the copied tut-react-and-spring-data-rest folder, Git Bash command-line was oppened, and the following commands were executed to delete .git (otherwise this repository would be a nested inside my repository) and commit changes:
   ```bash
   rm -rf .git
   cd ..
   git add .
   git commit -m "copied tut project folder"
   git push
   ```
2. **Using tags to mark versions of the applicaction**
   
   Tags will be used throughout class assignments, to mark the versions of the application, in a pattern like: major.minor.revision.
   
   A new tag was applied using the following commands in Git Bash:
   ```bash
   git tag v1.1.0
   git push --tags
   ```
3. **Add new feature to the app**

   A new feature was added to record the years of the employee in the company. To accomplish this, the project in [basic](tut-react-and-spring-data-rest/basic) folder was oppened in IntelliJ IDE.
    
    3.1. In `Employee` class, added a variable of type integer, named jobYears, as a private field. JobYears was also added as a parameter of the constructor, along with getters and setters methods.
    
    3.2. Added jobYears in `app.js`, for it to be displayed in the webpage.

    3.3. In `DatabaseLoader`, added the missing value for the parameter jobYears, for the employee already created.

    3.4. Added `validArguments` method in `Employee` class: string parameters (firstName, lastName, description) were not allowed to be null or in blank, while jobYears parameter has to be equal or higher than zero for the object to be constructed.

    3.5. Created unit tests in `EmployeeTest` class to test the valid instatiation or when instatiation exceptions are thrown.

4. **Ending**
    
    4.1 Changes were commited using the Git Bash terminal in the IDE:
   ```bash
   git status
   git add .
   git commit -m "Add employee Job years feature with unit tests. Closed #1"
   git push
   ```
   
    4.2 A new tag was applied:
   ```bash
   git tag v1.2.0
   git push --tags
   ```
   
    4.3 A first version of this report, regarding the first part of CA1, was written in a readme.md file in the CA1 folder, using the IDE:
   ```bash
   git add readme.md
   git commit -m "Added CA1 report - first part"
   git push
   ```

### Part 2 - using branches

### Part 3 - Alternative
