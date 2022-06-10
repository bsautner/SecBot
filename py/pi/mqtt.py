import json
import random
import paho.mqtt.client as mqtt_client

broker = "10.0.0.205"
port = 1883
client_id = f'python-mqtt-{random.randint(0, 1000)}'
username = 'ben'
password = 'imarobot'
client = mqtt_client.Client(client_id)


def connect(on_connect):
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port, 30)
    client.on_message = on_message
    client.on_disconnect = on_disconnect
    client.loop_forever()





def on_disconnect(self):
    print("mqtt disconnected")


def publish(self, topic, payload):
    try:
        v = json.dumps(payload)

        result = self.publish(topic, v)
        # result: [0, 1]
        status = result[0]
        if status == 1:
            print(f"Failed to send message to topic {topic}")
    except (ConnectionResetError, ConnectionRefusedError) as err:
        print(err)


def on_message(self, userdata, msg):
    print(msg.topic + " " + str(msg.payload))
    # command_processor.process_command(self, msg.topic, msg.payload)


def on_disconnect(client, userdata, rc):
    if rc != 0:
        print("Unexpected disconnection.")
