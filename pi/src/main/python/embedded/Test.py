import socket
from time import sleep
import time
import board
import digitalio


HOST = '127.0.0.1'  # The server's hostname or IP address
PORT = 2323        # The port used by the server

led = digitalio.DigitalInOut(board.D7)
led.direction = digitalio.Direction.OUTPUT


def run():
    print("hello")
    led.value = True

    while True:
        try:

            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                 s.connect((HOST, PORT))
                 s.sendall(b'Hello, world\n\r')
                 data = s.recv(1024)
                 print(data.decode("utf-8") )
                 sleep(.5)
        except (ConnectionResetError, ConnectionRefusedError) as err:
            print(err)
        sleep(1)





if __name__ == '__main__':
    run()
