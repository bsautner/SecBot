import time

import python.pi.network.mqtt as mqtt


def main():
    print("control_io.py")
    # mqtt.connect_mqtt()

    while True:
        time.sleep(1)


if __name__ == '__main__':
    main()
