## Class assignment 3 part 1 - report
#### *Virtualization with Vagrant*
Vagrant is a tool for building and managing virtual machine environments in a single workflow. It provides easy-to-use workflow and focuses on automation, making it easier to manage and share virtual environments.

The goal of this assignment is to is to use Vagrant to setup a virtual environment to execute the tutorial spring boot application, gradle basic version.
The steps taken to complete this assignment are described below.

### Step 1 - Vagrant Multi VM for Spring Basic Tutorial Application
Using the Spring Basic Tutorial application, the workflow described in https://bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo/ was followed as an initial solution to setup two VMs, one for the application and another for the database.

1. Vagrant software was downloaded from the official website and installed.
2. Made sure my personal repository was set to 'public' in GitHub.
3. The repository above was cloned and the Vagrantfile was copied to a local folder.
```bash
git clone https://bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo/
git show master: ~/Desktop/Vagrant/vagrant-multi-spring-tut-demo/ > Vagrantfile
```

### Step 2 - Studying the provided Vagrantfile
- `Vagrant.configure("2") do |config|`: This line starts the configuration block, refering to the version 2 of Vagrant.


- `config.vm.box = "ubuntu/bionic64"`: This line sets the box to be used as the base image for the VMs. In this case it is the Ubuntu 18.04 64-bit box.


- `config.vm.provision "shell", inline: <<-SHELL`: This line starts the shell provisioner block, which is used to execute shell commands, and provides instructions for both VMs.
  - installs necessary packages and dependencies using `apt-get`
  - also installs `iputils-ping`, `avahi-daemon`, `libnss-mdns`, `unzip`, and `openjdk-11-jdk-headless`


- `config.vm.define "db" do |db|`: This line defines the first VM, named "db", and then:
  - configures network IP address as '192.168.56.11'
  - configures port forwarding for H2 console and server as '8082' and '9092', respectively
  - downloads H2 database Jar file through `wget`
  - provisions the H2 server process to always run in the background


- `config.vm.define "web" do |web|`: This line defines the second VM, named "web", and then:
  - configures network IP address as '192.168.56.10'
  - assigns more RAM for the "web" VM
  - configures port forwarding for the server as '8080'
  - installs necessary packages and dependencies
  - installs `tomcat9` and `tomcat9-admin` packages
  - clones tut-basic-gradle project from GitHub
  - changes permissions for gradlew and builds the project
  - finally, deploys the resulting WAR file to Tomcat webapps directory



### Step 3 - Updating the Vagrantfile
Firstly, I updated the Vagrantfile to clone my personal repository, instead of the tut-basic-gradle.

Upon running the Vagrantfile, some errors were found. After some troubleshooting, the following changes were made to the Vagrantfile:
1. **Java version**

   Changed the version of Java to 17 for both VMs, because of Spring Boot compatibility.
```ruby
config.vm.provision "shell", inline: <<-SHELL
sudo apt-get update -y
sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \ openjdk-17-jdk-headless
SHELL
```

2. **Exit script upon error**

   I realized that the script would still continue executing even if a command failed. To fix this and ensure that the script exits immediately if any command results in an error, the following line was added to the beginning of the script:
```ruby
set -e
```

3. **Spring Boot incompatibilities**

   One of the major issues was successfully builing the project due to Spring Boot incompatibilities. Since Spring Boot 3.0 or Spring Framework 6.0, web applications built with these versions need to conform to the Jakarta Servlet API. The Jakarta Servlet API (Servlet 5.0) is only supported from Tomcat 10 and later versions.

- One of the options to solve this, would be to update the Vagrantfile to install _**Tomcat 10**_. In this case, I would need to:
  - adjust the `application.properties` file to use the external database server's connection details instead of the embedded database.
  - adjust the `app.js` endopoint path to match the modified context path (/basic-0.0.1-SNAPSHOT/api/employees).
  - adjust the `build.gradle` by adding 'id.war' to the plugins block.
  
  This option would have the advantage of better scalability and separation of concerns, as the server infrastructure could be managed independently from the application code. However, the deployment process would be more complex as it involves additional steps such as packaging the application into a WAR file, deploying it to the server, and managing server configurations. Besides, the application would be dependent on server configuration, which decreases portability and could lead to compatibility issues in the future.


- Another option would be to use the Spring Boot _**embedded Tomcat**_ server. This option has the advantage of simplicity, as the application would be self-contained and could be easily deployed to any environment without additional server configuration. And although this option offers less control on server configuration and less scalability, it is more suitable for small-scale applications.

  To achieve this, I added the following line to Vagrantfile:
  ```ruby
  nohup ./gradlew bootRun > /home/vagrant/spring-boot-app.log 2>&1 &
  ```
  This line runs the Spring Boot application in the background, with the "no hang up" option to keep the process running even after the SSH session is closed. The standard output is redirected to a log file, so that it can be checked later.

4. **Testing**
While on the directory that contains your Vagrantfile, I ran the command:
```bash
vagrant up
```

Finally, the Vagrant is able to successfully run the Spring Boot application. To check if the application was running, I openned the browser to verify the database on address http://192.168.56.11:8082/ and the web application on address http://192.168.56.10:8080/


For shutting down the VMs and then removing both, I ran the command:
```bash
vagrant halt
vagrant destroy db
vagrant destroy web
```

### **Step 4 - connecting to H2 server in the database VM
The final step required connecting a Spring Boot application, serving as the frontend or logic layer, to a H2 database server running on a separate VM.
To achieve this, I updated the `application.properties` file in the Spring Boot application to use the external database server's connection details instead of the embedded database.

```properties
spring.datasource.url=jdbc:h2:tcp://192.168.56.11:9092/./jpadb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

### Ending
After commiting this report, the end of this assignment will be marked with the following tag:
```bash
git tag ca3-part2
git push --tags
```

### ALTERNATIVE HYPERVISOR SOLUTION TO VIRTUALBOX
There are several tools to serve as alternatives to VirtualBox, such as VMware Workstation, VMware Fusion, and Hyper-V. The choice of hypervisor depends on the user's needs and preferences.
VMware is a leading provider of virtualization software and services, offering a range of products that enable users to run multiple virtual machines (VMs) on a single physical host, providing flexibility, scalability and resource optimization.
Comparing to VirtualBox, in terms of:
- Performance and Scalability: VMware typically performs better than VirtualBox, especially for enterprise environments with high workloads and resource requirements.
- Features and Management: VMware offers more advanced features and management tools, including advanced networking, storage management, and automation capabilities.
- Compatibility and Integration: VMware is more compatible with a wide range of enterprise software and hardware, while VirtualBox is more lightweight and may be preferred for individual users or small-scale deployments where simplicity and ease of use are prioritized.
- Licensing and Support: VMware products are typically more expensive than VirtualBox, but they also offer more comprehensive support and services for customers.

To implement this alternative solution, these are most relevant differences for configuring in the Vagrantfile:
1. Setting up VMware:
   Download and install VMware Workstation from the official website to host machine.

2. Adjusting the Vagrantfile:
   - Change the provider to VMware:
     ```ruby
     web.vm.provider "vmware_desktop" do |v|
     ```
   - Set the box to be used as the base image for the VMs:
     ```ruby
     config.vm.box = "bento/ubuntu-18.04"
     db.vm.box = "bento/ubuntu-18.04"
     web.vm.box = "bento/ubuntu-18.04"
     ```

3. Running the Vagrantfile:
   - Run the Vagrantfile with the following command:
     ```bash
     vagrant up --provider=vmware_desktop
     ```
   - To halt and destroy the VMs, run the following commands:
     ```bash
        vagrant halt
        vagrant destroy db
        vagrant destroy web
        ```

### **Conclusion**

Through this assignment, I was able to successfully setup a virtual environment using Vagrant to run a Spring Boot application and connect it to a H2 database server running on a separate VM. The main challenge was to troubleshoot and resolve compatibility issues between the Spring Boot application and the Tomcat server. Moreover, the connection with H2 database also required some troubleshooting to ensure that the application could access the database server running on a separate VM. Overall, this assignment provided valuable hands-on experience with Vagrant and virtualization, and helped me gain a better understanding of how to manage and configure virtual environments for software development and testing purposes.