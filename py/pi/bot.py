import threading

import py.pi.device.led as led
import sonar as sonar
import time

import py.pi.mqtt as mqtt


def run():
    print("Bot is running")
    try:

        mqtt.connect(on_connect)
        #

    except Exception as e:
        print("mqtt error:", e)
        led.blink(led.RED, 10, 0.5)


def on_connect(self, mosq, obj, rc):
    print("MQTT Connected!")
    self.subscribe("#")
    mqtt.publish(self, "robot", "status=online")

    time.sleep(1)
    x = threading.Thread(target=sonar.run, args=(self,))
    x.start()





if __name__ == '__main__':
    run()
