import threading
import time

def first():
    while True:
        print("First")
        time.sleep(1)

def second():
    while True:
        print("Second")
        time.sleep(1)


if __name__ == '__main__':
    threading.Thread(target=first).start()
    threading.Thread(target=second).start()
