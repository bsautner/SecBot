from time import sleep
import time
import json
import random
import paho.mqtt.client as mqtt_client
import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BOARD)
GPIO.setup(33, GPIO.OUT)
GPIO.setup(31, GPIO.OUT)

p1 = GPIO.PWM(33, 50)  # channel=12 frequency=50Hz
p1.start(0)

p2 = GPIO.PWM(31, 50)  # channel=12 frequency=50Hz
p2.start(0)

broker = "localhost"
port = 1883
topic = "LIDAR"
client_id = f'python-mqtt-{random.randint(0, 1000)}'
username = 'ben'
password = 'imarobot'


def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Motors Connected to MQTT Broker!")

        else:
            print("Failed to connect motors to mqtt, return code %d\n", rc)

    client = mqtt_client.Client(client_id)
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port, 30)
    return client


# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code " + str(rc))
    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    client.subscribe("#")


# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    # print(msg.topic+" "+str(msg.payload))
    if msg.topic == "MOTOR":
        command = json.loads(msg.payload)
        print(msg.topic + " " + command['motion'])
        if command['motion'] == "FORWARD":
            f()
        if command['motion'] == "REVERSE":
            r()
        if command['motion'] == "STOP":
            stop()

    return


def publish(client):
    msg_count = 0
    # while True:
    #     time.sleep(1)
    #     msg = f"{topic},bar2"
    #     result = client.publish(topic, msg)
    #     # result: [0, 1]
    #     status = result[0]
    #     if status == 0:
    #         print(f"Send `{msg}` to topic `{topic}`")
    #     else:
    #         print(f"Failed to send message to topic {topic}")
    #     msg_count += 1


def run():
    print("trying to connect...")
    client = connect_mqtt()
    client.on_connect = on_connect
    client.on_message = on_message
    # client.loop_start()
    client.loop_forever()


def f():
    p1.ChangeDutyCycle(50)
    p2.ChangeDutyCycle(0)


def r():
    p1.ChangeDutyCycle(0)
    p2.ChangeDutyCycle(50)


def stop():
    p1.ChangeDutyCycle(0)
    p2.ChangeDutyCycle(0)


if __name__ == '__main__':
    run()
