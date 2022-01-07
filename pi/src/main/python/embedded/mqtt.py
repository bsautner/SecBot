from time import sleep
import time
import json
import random
import paho.mqtt.client as mqtt_client

broker = "secbot"
port = 1883
topic = "LIDAR"
client_id= f'python-mqtt-{random.randint(0, 1000)}'
username = 'ben'
password = 'imarobot'

def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")

        else:
            print("Failed to connect, return code %d\n", rc)

    client = mqtt_client.Client(client_id)
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port, 30)
    return client

def publish(client):
    msg_count = 0
    while True:
        time.sleep(1)
        msg = f"{topic},bar2"
        result = client.publish(topic, msg)
        # result: [0, 1]
        status = result[0]
        if status == 0:
            print(f"Send `{msg}` to topic `{topic}`")
        else:
            print(f"Failed to send message to topic {topic}")
        msg_count += 1

def run():
    print("trying to connect...")
    client = connect_mqtt()
    client.loop_start()
    sleep(4)
    publish(client)




if __name__ == '__main__':
    run()
