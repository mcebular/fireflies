import socket
import time


def current_time_millis():
    return int(round(time.time() * 1000))


class SocketClient:

    timeout: int
    sock: socket = None
    last_send: int = 0

    def __init__(self, timeout):
        """

        :param timeout: in seconds
        """
        self.timeout = timeout

    def connect(self):
        """
        Establish a connection with a new socket.
        """
        try:
            self.sock = socket.socket()
            self.sock.connect((socket.gethostname(), 12_001))
            self.last_send = current_time_millis()

            print("SocketClient: connection successful.")
        except ConnectionError as err:
            self.sock = None

            # print(err)
            print("SocketClient: connection failed.")

    def is_available(self) -> bool:
        """

        :return: True if connection exists, false otherwise
        """
        return self.sock is not None

    def send(self, data: str) -> bool:
        """
        Sends data to the server. Will send an empty data string if timeout is exceeded (to prevent server side from
        timing out).
        Sending data to the server expects a response "ok" from the server. Function returns True if such response was
        received.

        :param data: string to be send via socket
        :return: True if server responeded as expected, false otherwise
        """
        data_len = len(data.strip())
        if data_len > 0 or (current_time_millis() - self.last_send > 1000 * self.timeout):
            # send even if it's empty to prevent socket from closing (server has a time-out of 5 seconds)
            try:
                print("SocketClient: sending data.")
                self.sock.sendall(data.encode())
                self.last_send = current_time_millis()
                response = self.sock.recv(64)
                resp_time = current_time_millis()
                while True:
                    if current_time_millis() - resp_time > 1000 * self.timeout:
                        raise ConnectionError("Server didn't respond.")

                    if response == b'ok':
                        print("SocketClient: response ok.")
                        return True

            except ConnectionError as err:
                self.sock = None

                # print(err)
                print("SocketClient: error sending data, removing socket connection.")
                return False
