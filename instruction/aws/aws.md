#AWS Chess

Use the following steps if you would like to deploy your chess server so that you can play it from anywhere in the world.

1. Get AWS account
1. Launch an EC2 instance
   1. Chose AWS linux for your AMI image
   1. Specify the security group that opens port 8080, 22
   1. Set the instance size t2.micro
1. Install MySQL
   1. SSH into server `ssh -i ~/keys/cs240/cs240.pem ec2-user@youripaddresshere`
   1. sudo wget https://dev.mysql.com/get/mysql80-community-release-el9-1.noarch.rpm
   1. sudo dnf install mysql80-community-release-el9-1.noarch.rpm -y
   1. sudo rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2023
   1. sudo dnf install mysql-community-client -y
   1. sudo dnf install mysql-community-server -y
   1. sudo systemctl start mysqld
   1. sudo grep 'temporary password' /var/log/mysqld.log
   1. mysql -u root -p
   1. UNINSTALL COMPONENT 'file://component_validate_password';
   1. ALTER USER 'root'@'localhost' IDENTIFIED BY 'yourpassword';
1. Install Java
   1. SSH into server `ssh -i ~/keys/cs240/cs240.pem ec2-user@youripaddresshere`
   1. Install java `sudo yum install java`
   1. Check that Java is running
1. Make some code changes
   1. Make it so your client can take the `youripaddresshere:port` as a parameter.
      ```java
          public static void main(String[] args) {
        try {
            var serverName = args.length > 0 ? args[0] : "localhost:8080";
      ```
   1. Modify your web/index.html so your user can download your java client
      ```html
      <p>Download the <a href="client.jar">client</a>. Run with:</p>
      <pre>java -jar client.jar <span id="hostname">server</span>:8080</pre>
      <script>
        document.querySelector('#hostname').innerText = window.location.hostname;
      </script>
      ```
1. Make your client and server jars
   1. Build the client Jar `mvn package -pl client -DskipTests`
   1. `cp client/target/client-jar-with-dependencies.jar server/src/main/resources/web/client.jar`
   1. Build the server Jar `mvn package -pl server -DskipTests`
   1. This outputs to `/server/target/server-jar-with-dependencies.jar`
1. Copy server code
   1. scp code `scp -i ~/keys/cs240/cs240.pem server/target/server-jar-with-dependencies.jar ec2-user@youripaddresshere:server`
1. Start up the server
   1. SSH into server `ssh -i ~/keys/cs240/cs240.pem ec2-user@youripaddresshere`
   1. `java -jar server.jar`
