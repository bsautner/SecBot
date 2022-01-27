import sys

sys.path.append('../')
import paho.mqtt.client

from time import sleep
import time
import json

import system.command_processor as command_processor



class MQTT:

    def __init__(self, command_processor, mqtt_client):
        print("Initializing MQTT")
        self.client = mqtt_client
        self.command_processor = command_processor

    def on_disconnect(self):
        print("mqtt disconnected")

    # The callback for when the client receives a CONNACK response from the server.
    def on_connect(self, client, userdata, flags, rc):
        print("MQTT connected with result code " + str(rc))
        # Subscribing in on_connect() means that if we lose the connection and
        # reconnect then subscriptions will be renewed.
        client.subscribe("#")

    # The callback for when a PUBLISH message is received from the server.
    def on_message(self, client, userdata, msg):
        self.command_processor.process_command(msg.topic, msg.payload)
        print(msg.topic + " " + str(msg.payload))

        return

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

    def run(self):
        print("trying to connect mqtt broker...")

        self.client.on_connect = self.on_connect
        self.client.on_message = self.on_message
        self.client.loop_start()
        # client.loop_forever()
