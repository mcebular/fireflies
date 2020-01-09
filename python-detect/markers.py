from typing import List


class Marker:
    id: int
    x: float
    y: float

    def __init__(self, mid: int, x: float, y: float):
        self.id = mid
        self.x = x
        self.y = y

    def __str__(self):
        return "{:d},{:f},{:f}\t".format(self.id, self.x, self.y)


class Markers:
    markers: List[Marker]

    def __init__(self):
        self.markers = []

    def append(self, marker):
        self.markers.append(marker)

    def __str__(self):
        res = ""
        for mk in self.markers:
            res += str(mk)
        return res + "\n"
