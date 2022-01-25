import sys
sys.path.append('../')

import paho.mqtt.client as mqtt_client
from time import sleep
import time
import json
import random
import system.command_processor as command_processor

broker = "localhost"
port = 1883
client_id = f'python-mqtt-{random.randint(0, 1000)}'
username = 'ben'
password = 'imarobot'



def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")

        else:
            print("Failed to connect to mqtt, return code %d\n", rc)

    client = mqtt_client.Client(client_id)
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port, 30)
    return client

client = connect_mqtt()

def on_disconnect():
    print("mqtt disconnected")
# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code " + str(rc))
    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    client.subscribe("#")


# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    command_processor.processCommand(msg.topic, msg.payload)


    return


def publish(topic, payload):
    try:
        v = json.dumps(payload)

        result = client.publish(topic, v)
        # result: [0, 1]
        status = result[0]
        if status == 1:
            print(f"Failed to send message to topic {topic}")
    except (ConnectionResetError, ConnectionRefusedError) as err:
        print(err)




def run():
    print("trying to connect...")

    client.on_connect = on_connect
    client.on_message = on_message
    client.loop_start()
    # client.loop_forever()
