## Class assignment 1 - report
#### *Version Control with Git*
Version control is the practice of tracking and managing changes to software code. Git is an open-source distributed version control system that helps software teams create projects of all sizes with efficiency, speed, and asynchronicity.


This assignment consists of a simple git workflow, aiming to explore the fundamental actions of Git, such as creating a repository, branching, merging, and tagging.


For the purpose of class assignments, a collection of apps from the [Spring Data REST tutorial](https://spring.io/guides/tutorials/react-and-spring-data-rest) repository will be used as a sample application. That repository was previously cloned, as follows:
1. A repository for DevOps classes was created on Github and named [devops-23-24-PSM-1231860](https://github.com/Tatiana-An/devops-23-24-PSM-1231860). Professors were added as collaborators.
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


4. **Debug the server and client parts**

   4.1. The server was started using the Git Bash terminal in the IDE.
   ```bash
   cd basic
   ./mvnw spring-boot:run
   ```

   4.2. The application was tested in the browser, and the webpage was refreshed to check the changes.
   ```bash
    http://localhost:8080
   ```

   4.3. Debugged the server-side using the IDE.

   4.4. Debugged the client-side using the browser developer tools
   - Opened the developer tools in  browser, by right-clicking on the page and selecting "Inspect".
   - Navigated to the "Console" tab to view errors or warnings.
   - Navigated to the "Network" tab to inspect network requests and responses.
   - Navigated to the "Sources" tab to go through code, inspect variables, and analyze the stack.

5. **Ending**
    
    5.1. Changes were commited using the Git Bash terminal in the IDE:
   ```bash
   git status
   git add .
   git commit -m "Add employee Job years feature with unit tests. Closed #1"
   git push
   ```
   
    5.2. A new tag was applied:
   ```bash
   git tag v1.2.0
   git push --tags
   ```
   
   5.3. The first version of this report, regarding the first part of CA1, was written in a readme.md file in the CA1 folder, using the IDE:
   ```bash
   git add readme.md
   git commit -m "Added CA1 report - first part"
   git push
   ```

### Part 2 - using branches

Almost all version control systems today support branches – independent lines of work that stem from a common codebase. Branches are used to develop new features, fix bugs, and refactor code in isolation, without affecting the "stable" main code.

1. **Using a branch to add a new feature**

   1.1. Created a new branch named "email-field" and switched to it:
   ```bash
   git branch "email-field"
   git checkout email-field
   git branch
   ```

   1.2. In `Employee` class, added a new parameter "email". Also added "email" to `app.js` and `DatabaseLoader`. 

   1.3. Created unit tests in `EmployeeTest` class to test for valid and invalid instatiations. 

   1.4. Tested and debugged the server and the webpage, as previously done.

   1.5. Commited changes:
   ```bash
   git status
   git add basic/*
   git commit -m "Feat: Add new feature email field. Closed #2"
   git push origin email-field
   ```   

   1.5. Merged the branch "email-field" into the main branch, using the command --no-ff (no fast forward) to keep the branch history:
   ```bash
   git checkout main
   git merge --no-ff email-field
   git push
   ```

   1.6. A new tag was applied:
   ```bash
   git tag v1.3.0
   git push --tags
   ```

2. **Using a branch to fix a bug**

   2.1. Created a new branch named "email-field" and switched to it:
   ```bash
   git branch -b "fix-invalid-email"
   git branch
   ```

   2.2. In `Employee` class, added a validation to only accept the instatiation of Employees with valid email (i.e. string containg "@" and dot).

   2.3. Added the appropriate unit tests in `EmployeeTest` class.

   2.4. Tested and debugged the server and the webpage, as previously.

   2.5. Merged the branch "fix-invalid-email" into the main branch, using the command --no-ff (no fast forward) to keep the branch history:
   ```bash
   git checkout main
   git merge --no-ff fix-invalid-email
   git push
   ```

   2.6. A new tag was applied:
   ```bash
   git tag v1.3.1
   git push --tags
   ```
   
   2.7. In the end of the assignment, after the readme file is finished, the repository will be marked with the final ca1-part2 tag.


### Part 3 - Alternative solution

   While Git is one of the most popular version control systems, there are several alternatives available: Subversion, Mercurial, Perforce, and TFVC, among others.
   This section provides an analysis of Mercurial as an alternative version control solution to Git. 

   3.1. **Comparison of Mercurial and Git**

   Mercurial is an open-source distributed version control system, like Git, written in Python. Both use a distributed repository model, where each developer
   has a complete copy of the repository on their local machine. Both support branching, merging, and distributed workflows, allowing developers to work independently
   and collaborate seamlessly. The main differences are:

   - Performance:

      - Git is generally considered to have better performance for large repositories and complex branching and merging operations.
      - Mercurial may have slightly slower performance, especially for certain types of operations like clone and push.

   
   - Branching and Merging:

      - In Git, branches are pointers to commits, and branching and merging are fundamental parts of the workflow. 
      - In Mercurial, branches are more explicitly defined and have a separate namespace from regular commits, which can lead to a cleaner and more structured history.


   - Ease of Use:

      - Git can be more complex to use, especially for beginners who are not familiar with command-line interfaces.
      - Mercurial is often considered to have a more user-friendly and intuitive interface, with simpler commands and workflows.


   - Community and Ecosystem:

      - Git has a larger user base and ecosystem of tools, services, and community support compared to Mercurial. 
      - There are more third-party integrations, hosting platforms, and development tools available for Git, making it easier to find resources and support.


   - Windows Support:

     - Mercurial has better support for Windows compared to Git, with native support for file permissions, line endings, and other features that may be important for Windows users.


   3.2. **Using Mercurial for the assignment**

   To perform the assigment tasks, a similar workflow and commands can be applied, but using `hg` instead of `git`.

   - **Repository Initialization**:
   ```bash
   hg init
   echo "# repository-name" >> README.md
   hg add README.md
   hg commit -m "first commit"
   ```

   - **Pushing Changes**:
   ```bash
   hg push
   ```

   - **Tagging Versions**:
   ```bash
   hg tag <tagname>
   hg push --tags
   ```

   - **Branching for Features and Fixes**
   ```bash
   hg branch <branchname>
   hg merge
   ```
