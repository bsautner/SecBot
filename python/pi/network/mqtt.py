import sys

sys.path.append('../')
import paho.mqtt.client as mqtt_client

from time import sleep
import time
import json
import random

import system.command_processor as command_processor

broker = "10.0.0.205"
port = 1883
client_id = f'python-mqtt-{random.randint(0, 1000)}'
username = 'ben'
password = 'imarobot'


def start():
    print("starting mqtt...")


class MQTT:

    def __init__(self, command_processor: object, mqtt_client: object) -> object:
        print("Initializing MQTT")
        self.client = mqtt_client
        self.command_processor = command_processor

    def on_disconnect(self):
        print("mqtt disconnected")

    def publish(self, topic, payload):
        try:
            v = json.dumps(payload)

            result = self.client.publish(topic, v)
            # result: [0, 1]
            status = result[0]
            if status == 1:
                print(f"Failed to send message to topic {topic}")
        except (ConnectionResetError, ConnectionRefusedError) as err:
            print(err)


def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
            client.subscribe("#")

        else:
            print("Failed to connect to mqtt, return code %d\n", rc)

    def on_message(self, userdata, msg):
        print(msg.topic + " " + str(msg.payload))
        command_processor.process_command(self, msg.topic, msg.payload)

    def on_disconnect(self, client, userdata, rc):
        if rc != 0:
            print("Unexpected disconnection.")

    client = mqtt_client.Client(client_id)
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port, 30)
    client.on_message = on_message
    client.on_disconnect = on_disconnect
    client.loop_start()
