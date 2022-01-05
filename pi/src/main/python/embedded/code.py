import time
import random
import board
import adafruit_hcsr04
import paho.mqtt.client as mqtt_client


sonar = adafruit_hcsr04.HCSR04(trigger_pin=board.D5, echo_pin=board.D6)

broker = "localhost"
port = 1883
topic = "SONAR"
client_id= f'python-mqtt-{random.randint(0, 1000)}'
username = 'ben'
password = 'imarobot'
print(client_id)

def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)

    client = mqtt_client.Client(client_id)
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port)
    return client


def publish(client):
    msg_count = 0
    while True:
        time.sleep(1)
        msg = f"{topic},{sonar.distance}"
        result = client.publish(topic, msg)
        # result: [0, 1]
        status = result[0]
        if status == 0:
            print(f"Send `{msg}` to topic `{topic}`")
        else:
            print(f"Failed to send message to topic {topic}")
        msg_count += 1


def run():
    client = connect_mqtt()
    client.loop_start()
    publish(client)


if __name__ == '__main__':
    run()

#while True:
#    try:
#        print(sonar.distance)
#    except RuntimeError:
#        print("Retrying!")
#    time.sleep(2)
