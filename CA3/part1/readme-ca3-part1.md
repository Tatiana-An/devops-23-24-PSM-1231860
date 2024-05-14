## Class assignment 3 part 1 - report
#### *Virtualization*
Virtualization is a technology that enables the creation of virtual instances of computer systems, allowing multiple operating systems and applications to run on a single physical machine. It makes possible to abstract the hardware layer and to create virtual resources, saving costs in IT infrastructure.

The goal of this assignment is to correctly set up a virtual machine and test running the previous projects from CA1 and CA2, considering the dependencies and configurations needed. In this assignment will be using VirtualBox as the hypervisor for its ability to support a wide range of guest operating systems, its ease of use, and extensive features.

The steps taken to complete this assignment are described below.

### Step 1 - Setting up the Virtual Machine and Ubuntu
To set up VirtualBox, steps from the lecture slides were followed:
1. Installed VirtualBox
   - Downloaded the software from the official website.
   - Installed the VirtualBox software.
2. Configured the VM
   - Network adapter 1 was set as NAT
   - Network adapter 2 was set as Host-only Adapter (vboxnet0)
2. Installed Ubuntu
   - Downloaded the Ubuntu Server ISO file from the official website.
   - Mounted the ISO file to the VM.
   - Started the VM and followed the on-screen instructions.
   - Followed the remaining instruction from the lecture slides to configure the network and SSH server.

   3.1. Updated the package repositories, installed net-tools, and configured the network with the IP of the second adapter.
   ```bash
   sudo apt update
   sudo apt install net-tools
   sudo nano /etc/netplan/01-netcfg.yaml
   sudo netplan apply
   ```

   3.2. Installed and configured SSH and FTP servers.
   ```bash
   sudo apt install openssh-server
   sudo nano /etc/ssh/sshd_config
   ```
   Uncommented the line `PasswordAuthentication yes`
   ```bash
   sudo service ssh restart
   sudo apt install vsftpd
   sudo nano /etc/vsftpd.conf
   ```
   Uncommented the line `write_enable=YES`
   ```bash
   sudo service vsftpd restart
   ```
   3.3. Now that the SSH server was enabled, I connected to the VM from the host machine using SSH. In the power shell:
   ```bash
   ssh tatiana@192.168.56.5
   ```
   
   3.4. Installed Git and Java (version without graphical interface)
   ```bash
   sudo apt install git
   sudo apt install openjdk-17-jdk-headless
   ```
   
   3.5. Tested the Spring Tutorial application
   ```bash
   git clone https://github.com/spring-guides/tut-react-and-spring-data-rest
   cd tut-react-and-spring-data-rest/basic
   ./mvnw spring-boot:run
   ```
   After the successfully running the application, I accessed it on the browser on address http://192.168.56.5:8080/

### Step 2 - Cloning personal repository
1. Set my personal repository to 'public' in GitHub
2. Still using the SSH connection to the VM, I cloned my personal repository
```bash
git clone https://github.com/Tatiana-An/devops-23-24-PSM-1231860.git
```

### Step 3 - Running Maven Spring Boot project from CA1
1. Installed Maven in the VM and changed the permissions to execute
```bash
sudo apt install maven
chmod +x mvnw
```

2. Run the Spring Boot project from CA1
```bash
cd CA1/tut-react-and-spring-data-rest/basic
./mvnw spring-boot:run
```
To check if the application was running, I accessed it on the browser on address http://192.168.56.5:8080/

### Step 4 - Running Spring Boot and Gradle basic demo projects from CA2
1. Installed Gradle in the VM and changed the permissions to execute
```bash
wget https://services.gradle.org/distributions/gradle-8.6-bin.zip
sudo mkdir /opt/gradle
sudo unzip -d /opt/gradle gradle-8.6-bin.zip
echo "export GRADLE_HOME=/opt/gradle/gradle-8.6" >> ~/.bashrc
echo "export PATH=$PATH:$GRADLE_HOME/bin" >> ~/.bashrc
source ~/.bashrc
gradle --version
```

2. After assuring that Ubuntu version 20 was being used, permissions were changed to execute the Gradle wrapper
```bash
chmod +x gradlew
```

3. Run the project from CA2 part2

```bash
cd CA2/Part2/react-and-spring-data-rest-basic
./gradlew bootRun
```
To check if the application was running, I accessed it on the browser on address http://192.168.56.5:8080/

4. Run the project from CA2 part1
To be able to run the chat application, the server was run on the VM, but the client had to be run on the local machine itself, because the VM does not support a graphical interface.
- Run the server on the VM, using the following commands:
```bash
cd CA2/Part1/gradle-basic-demo
./gradlew build
./gradlew runServer
```

- In another terminal, run the client on the host machine, according to the port indicated in the arguments of the task runClient in the `build.gradle` file:
```bash
cd CA2/Part1/gradle-basic-demo
./gradlew runClient --args="192.168.56.5 59001"
```

5. After commiting this report, the end of this assignment will be marked with the following tag:
```bash
git tag ca3-part1
git push --tags
```

### **Conclusion**

Through this assignment, the practical implementation of virtualization, by setting up a virtual machine and configuring Ubuntu.
Virtualization enables the abstraction of hardware resources, facilitating the creation of virtual instances of computer systems. This not only optimizes resource utilization but also provides flexibility in deploying and managing diverse operating systems and applications on a single physical machine.

The step-by-step process included setting up VirtualBox, configuring the virtual machine with Ubuntu, and installing essential tools and services like SSH, FTP, Git, Maven, and Gradle. Consequently, this allowed us to run and test previous Spring Boot projects from CA1 and CA2, with Maven and Gradle, validating the functionality of the virtualized environment. Additionally, running a chat application demonstrated the versatility of virtualization, enabling server-side execution on the virtual machine while running the client on the host machine.

One of the challenges in this CA was to configure the correct versions of dependencies like Java, Maven, and Gradle, ensuring compatibility with the projects. Another relevant challenge was to overcome the lack of a graphical interface in the VM, which required adapting the workflow to run the client on the host machine. This limitation underscores the importance of understanding the constraints and capabilities of virtualization technologies to optimize development workflows effectively.

In conclusion, this assignment provided invaluable hands-on experience in virtualization, essential in DevOps practices for creating isolated environments, testing applications, and optimizing resource utilization. By successfully setting up and running projects in a virtualized environment, we gained practical insights into the benefits and challenges of virtualization, preparing us for real-world scenarios in software development and deployment.