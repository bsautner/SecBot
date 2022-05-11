import json
import python.pi.led as led
import drive_control as dc


def process_command(self, topic, payload):
    # print(preprocessing command {topic} {payload}')
    command = json.loads(payload)
    print(payload)
    print(topic)
    if topic == "HB":
        print(command['message'])
        led.blink(led.BLUE, 10, 0.5)
        command = command['message']
        if command == "M1F":
            print("M1F")
            dc.driveMotor(0, 1)

    return
