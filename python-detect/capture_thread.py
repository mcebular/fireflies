import threading

import cv2
import cv2.aruco as aruco
import numpy as np
import imutils


class CaptureThread:

    capture: cv2.VideoCapture
    current_frame: np.array

    debug: bool = False
    working: bool = True

    def __init__(self, debug = False):
        self.capture = cv2.VideoCapture(0, cv2.CAP_DSHOW)
        self.capture.set(cv2.CAP_PROP_FRAME_WIDTH, 160)
        # self.capture.set(cv2.CAP_PROP_FRAME_HEIGHT, 720)
        
        self.debug = debug
        self.current_frame = None

    def __threadfunc__(self):
        markerDict = aruco.getPredefinedDictionary(aruco.DICT_4X4_250)
        while self.working:
            ok, frame = self.capture.read()
            self.current_frame = frame
            
            if self.debug:
                (corners, ids, rejects) = aruco.detectMarkers(frame, markerDict)
                frame = aruco.drawDetectedMarkers(frame, corners, ids)
                cv2.imshow("Camera", cv2.flip(imutils.resize(frame, width=480), 1))
               
            cv2.waitKey(1)

    def start(self):
        self.working = True
        threading.Thread(target=self.__threadfunc__).start()

    def stop(self):
        self.working = False

    def get_frame(self):
        if self.current_frame is not None:
            return self.current_frame.copy()
        else:
            return None
