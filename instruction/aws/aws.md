#AWS Chess

Playing chess in your development environment by yourself can get old after a while. Your code is designed to run on an HTTP server where anyone in the world can connect with and play a game. However, to make that happen you need run your chess server on a device that has a public IP address and your network port externally accessible.

There are lots of ways you can accomplish this. You could lease an IP address from your internet service provider (ISP) and assign it to your laptop, or you could lease a server from a cloud provider such as Digital Ocean, Azure, or Amazon Web Services (AWS).

In this instruction we will go with AWS. Note that there is a cost involved in renting a server from AWS. Although if you are careful it shouldn't cost more than $5 a month.

## Get AWS account

Go to [AWS](https://aws.amazon.com/) and register for an account if you don't already have one. You will need a credit card, but you won't be charged for anything until you start using their services. Make sure you are aware of the pricing model for anything that you use so that you don't get any unexpected surprises with your monthly bill.

## Launch an EC2 instance

The Amazon Elastic Compute Cloud (EC2) service provides all of the functionality you need to launch a virtual server in an Amazon data center that is accessible to the world. After you have created your AWS account, use the AWS browser console to navigate to the EC2 service and lease your server with the following steps.

1. From the search bar type in EC2 and select the displayed service.
   ![EC2 service selection](awsEc2Selection.png)
1. Select the option to `Launch instance`
1. Give your instance a name like `cs240-chessserver`
1. Chose AWS linux for your Amazon Machine Image (AMI)
   ![Amazon Machine Image](ami.png)
1. Specify the security group that opens port 8080, 22
1. Set the instance type of t2.micro. If you are eligible for the free tier then you will not be billing for the first 12 months of your first t2.micro instance.
   ![Instance type](instanceType.png)
1. In the Key pair input select an existing key pair if you have created one previously, or select the `Create new key pair` option. Make sure you save the key pair to a safe place in your development environment. You will need this to connect to your server, and you do not want to let anyone else have access to it. Do not check the key pair into GitHub or any other publicly available location.
1. In the `Network settings` you specify how the world can access your server. AWS uses security groups to specify the network connection, or firewall, rules. You need to either create a new security group or use an existing one if you have a security group the exposes both port 22 and 8080 (assuming that is the port you are using for your chess server). You can create a new security group right on the launch instance page by selecting `Create security group`, `Allow SSH traffic from anywhere` and `Allow HTTP traffic from the internet`.
   ![Security group configuration](securityGroup.png)
1. Scroll past the remaining options and press `Launch instance`. This will display a message saying that the instance has been successfully launched. You can then navigate to the `Instances` view and click on your newly created server to see all of the details.
   ![EC2 Instance settings](ec2InstanceSettings.png)
   You will want to copy the `Public IPv4 address` so that you can remotely connect to the server using a secure shell (SSH) and also access your server from the browser in order to play a game of chess.

## Install MySQL

Now that you have your AWS EC2 server up and running you need to install MySQL so that you can store your user and game information.

First you need to remotely connect to your server using a secure shell (SSH). Usually you do this by opening a console, or terminal, window in your development environment and typing the command `ssh`. In order to connect, you need the key pair you used to launch your server. The command looks like this:

```sh
ssh -i youkeypairhere.pem ec2-user@youripaddresshere
```

Once you have shelled into your EC2 server you can download, install, and start MySQL with the following commands.

```sh
sudo wget https://dev.mysql.com/get/mysql80-community-release-el9-1.noarch.rpm
sudo dnf install mysql80-community-release-el9-1.noarch.rpm -y
sudo rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2023
sudo dnf install mysql-community-client -y
sudo dnf install mysql-community-server -y
sudo systemctl start mysqld
```

When MySQL was installed it created a random password for the root user. You can get the password from the MySQL log file, use that to login to the MySQL client and change the password.

```sh
sudo grep 'temporary password' /var/log/mysqld.log
mysql -u root -p
UNINSTALL COMPONENT 'file://component_validate_password';
ALTER USER 'root'@'localhost' IDENTIFIED BY 'yourpassword';
```

## Install Java

1.  SSH into server as described in the previous step
1.  Install java `sudo yum install java`
1.  Check that Java is running with `java -v`

## Modify your client code

You may need to modify your Chess Client code so that you can specify the server that it connects to.

1.  Make some code changes
    1.  Make it so your client can take the `youripaddresshere:port` as a parameter.
        ```java
            public static void main(String[] args) {
          try {
              var serverName = args.length > 0 ? args[0] : "localhost:8080";
        ```
    1.  Modify your web/index.html so your user can download your java client
        ```html
        <p>Download the <a href="client.jar">client</a>. Run with:</p>
        <pre>java -jar client.jar <span id="hostname">server</span>:8080</pre>
        <script>
          document.querySelector('#hostname').innerText = window.location.hostname;
        </script>
        ```
1.  Make your client and server jars
    1.  Build the client Jar `mvn package -pl client -DskipTests`
    1.  `cp client/target/client-jar-with-dependencies.jar server/src/main/resources/web/client.jar`
    1.  Build the server Jar `mvn package -pl server -DskipTests`
    1.  This outputs to `/server/target/server-jar-with-dependencies.jar`
1.  Copy server code
    1.  scp code `scp -i ~/keys/cs240/cs240.pem server/target/server-jar-with-dependencies.jar ec2-user@youripaddresshere:server`
1.  Start up the server
    1.  SSH into server `ssh -i ~/keys/cs240/cs240.pem ec2-user@youripaddresshere`
    1.  `java -jar server.jar`
