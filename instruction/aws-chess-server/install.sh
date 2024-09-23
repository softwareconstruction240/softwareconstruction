sudo mkdir /opt/chess

sudo useradd -r chess
sudo chown -R chess:chess /opt/chess

echo "DAWN Installing service"
sudo mv /opt/chess/service.sh /etc/init.d/chess
sudo sed -i -e 's/\r//g' /etc/init.d/chess #Make sure we don't have any /r in the script
sudo chmod +x /etc/init.d/chess
sudo chkconfig chess on #Enable the service for runlevels 2, 3, 4, and 5
sudo ln -s /opt/chess/chess /usr/bin/chess #Create the symlink to point to the actual binaries
sudo systemctl daemon-reload #Tell sysv that new scripts are loaded
sudo setcap 'cap_net_bind_service=+ep' /opt/chess/chess  #Enable use of port 80
sudo service chess start