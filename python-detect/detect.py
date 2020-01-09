from typing import Any
import sys

import cv2
import cv2.aruco as aruco
import imutils
import numpy as np

from capture_thread import CaptureThread
from markers import Markers, Marker
from socket_client import SocketClient

markerDict: Any
capture: CaptureThread
sock: SocketClient


def init(debug = False):
    global sock
    sock = SocketClient(timeout=1)

    global capture
    capture = CaptureThread(debug)
    capture.start()
    
    global markerDict
    markerDict = aruco.getPredefinedDictionary(aruco.DICT_4X4_250)
    # cv2.imwrite("marker5.png", aruco.drawMarker(markerDict, 5, 500))
    # cv2.imshow("marker1.png", aruco.drawMarker(dict, 1, 500))
    # cv2.imwrite("marker6.png", aruco.drawMarker(markerDict, 6, 500))
    # cv2.imwrite("marker7.png", aruco.drawMarker(markerDict, 7, 500))
    # cv2.imwrite("marker8.png", aruco.drawMarker(markerDict, 8, 500))
    # cv2.waitKey()
    # import sys
    # sys.exit(0)


def loop():

    frame = capture.get_frame()
    if frame is None:
        return
    
    (cap_height, cap_width, cap_channels) = frame.shape
    # print(width, height, channels)

    global markerDict
    (corners, ids, rejects) = aruco.detectMarkers(frame, markerDict)
    # print(corners)
    frame = aruco.drawDetectedMarkers(frame, corners, ids)

    detected_markers = np.array([]).reshape(0, 3)
    for i in range(len(corners)):
        corner = corners[i][0]
        p1 = corner[0]
        p2 = corner[1]
        p3 = corner[2]
        p4 = corner[3]
        x = p1[0] - ((p1[0] - p3[0]) / 2)
        y = p1[1] - ((p1[1] - p3[1]) / 2)

        x = int(x)
        y = int(y)

        # print(x, y, cap_width, cap_height)
        detected_markers = np.vstack((detected_markers, np.array([ids[i][0], x / cap_width, y / cap_height])))
        cv2.circle(frame, (x, y), 10, (255, 255, 255), 2)

    global sock
    if not sock.is_available():
        sock.connect()
    else:
        send_data = Markers()
        for row in detected_markers:
            marker = Marker(int(row[0]), row[1], row[2])
            send_data.append(marker)
        sock.send(str(send_data))


def main():
    init(debug=True)
    done = False
    while not done:
        loop()

    global capture
    capture.stop()


if __name__ == "__main__":
    try:
        main()
    except(KeyboardInterrupt):
        print("Stopping detection thread...")
        capture.stop()
    
    print("Terminating program...")
    sys.exit(0)
