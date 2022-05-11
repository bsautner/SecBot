sudo apt update -y
sudo apt upgrade -y
sudo apt dist-upgrade -y
sudo apt autoremove -y
sudo apt install git python3-pip python3-venv python3-dev mosquitto-clients net-tools i2c-tools raspi-config python3-setuptools -y
wget https://raw.githubusercontent.com/adafruit/Raspberry-Pi-Installer-Scripts/master/raspi-blinka.py
pip3 install --upgrade adafruit_blinka
sudo pip3 install --upgrade adafruit-python-shell
sudo python3 raspi-blinka.py

pip3 install --upgrade setuptools
pip3 install adafruit-circuitpython-rplidar
pip3 install pygame
pip3 install --upgrade setuptools
pip3 install --upgrade adafruit-python-shell
pip3 install --upgrade adafruit-circuitpython-dotstar adafruit-circuitpython-motor adafruit-circuitpython-bmp280
pip3 install --upgrade setuptools
pip3 install --upgrade adafruit-python-shell
pip3 install adafruit-circuitpython-hcsr04
pip3 install adafruit-blinka
pip3 install paho-mqtt
pip3 install adafruit-circuitpython-rplidar
pip3 install adafruit-circuitpython-mcp230xx

# https://learn.adafruit.com/adafruits-raspberry-pi-lesson-4-gpio-setup/configuring-i2c set interface to i2c