# Fireflies

_Fireflies_ is an interactive visualization project in which the user can manipulate the movement of boids (fireflies) by shining with colorful lights.

![screenshot](https://github.com/mc0239/fireflies/raw/master/screenshot.jpg)

## Running the project

### Visualization

To run the visualization (assuming under Windows):

```bash
cd java-visualization/
gradlew.bat build
gradlew.bat run
```

Note: interaction with the visualization is possible by holding the left or right mouse button. It's also possible to make a screenshot with `P` key and exit the visualization by pressing `ESC`.

### Marker detection

To run ArUco marker detection (assuming python 3 installed):

```
cd python-detect/
python detect.py
```

Visualization and detection processes are running independently of each other (i.e. you can run visualization without the detection).
Processes communicate between each other using a web socket, which opens on port 12001.

## Future work

If one was to build upon this project, there's a couple of things that should be resolved first (a list of TODOs):

- Python detect script does not terminate propery (stops than hangs).
- Visualization breaks on window resize (wrapping around borders and light source positions do not work properly)
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
