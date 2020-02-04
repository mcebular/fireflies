# Fireflies

_Fireflies_ is an interactive visualization project in which the user can manipulate the movement of boids (fireflies) by shining with colorful lights.

![screenshot](https://github.com/mc0239/fireflies/raw/master/screenshot.jpg)

## Running the project

Requirements: To run the visualization (the JAR file), you'll need a Java Runtime installed. Any Java Runtime newer than 7 should suffice.

If you want to run the project, you can do so by downloading the build from releases page: https://github.com/mc0239/fireflies/releases. Please note that build for the detection is made only for 64-bit Windows.

Download both files, `fireflies-detect-0.0.1-win64.exe` and `fireflies-visualization-0.0.1-win64.jar`.

First, run the *detect* executable, which should start the marker detection process and open another window with the live camera feed.

Then, run the visualization process by running the *visualization* JAR file. 


## Building the project

### Visualization

Requirements: JDK 7+

To run the visualization (assuming under Windows):

```bash
cd java-visualization/
gradlew.bat build
gradlew.bat run
```

Note: interaction with the visualization is possible by holding the left or right mouse button. It's also possible to make a screenshot with `P` key and exit the visualization by pressing `ESC`.

To build the visualization:

```bash
cd java-visualization/
gradlew.bat desktop:dist
```

A jar file will be generated in `java-visualization\desktop\build\libs\`.

### Marker detection

Requirements: Python 3.7

To run ArUco marker detection:

```
cd python-detect/
python detect.py
```

To detect markers, it's best to print them. You can generate markers with an online generator (i.e. http://chev.me/arucogen/). Make sure your selected dictionary is **4x4 (250)**).

You can test the detection by opening the website on your phone and pointing the phone's screen (with the marker) at the camera.

Visualization and detection processes are running independently of each other (i.e. you can run visualization without the detection).
Processes communicate between each other using a web socket, which opens on port 12001.

To build the detection (for the current platform!) (you need [pyinstaller](http://www.pyinstaller.org/)):

```
cd python-detect/
pyinstaller --onefile detect.py
```

An exe file will be generated in `python-detect\dist\`.

## Future work

If one was to build upon this project, there's a couple of things that should be resolved first (a list of TODOs):

- Visualization breaks on window resize (wrapping around borders and light source positions do not work properly)
   - This can be bypassed by modifying width/height/fullscreen parameters before running the project, in file `java-visualization\desktop\src\net\iamsilver\fireflies\desktop\DesktopLauncher.java`
- Python detect script does not terminate propery (stops than hangs).
- Light source changes and lightness rework (increase is done on socket receive, decrease is done every update cycle)
- Make visualization parameters external (i.e. settable via input arguments) such as number of boids, drawing threshold, maximum boid speed and force, parameters of the flocking, etc.
- Better define and document the communication between the detection and visualization processes.

# License

OpenCV is provided under [The 3-Clause BSD License](https://opencv.org/license/).

LibGDx is provided under [The Apache License, Version 2.0](https://github.com/libgdx/libgdx/blob/master/LICENSE).

---

Copyright 2019- Martin Čebular

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
