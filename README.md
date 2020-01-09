Za zagon vizualizacije:

```bash
cd java-visualization/
gradlew.bat build
gradlew.bat run
```

Za zagon detekcije:

```
cd python-detect/
python detect.py
```

Procesa tečeta neodivisno en od drugega. 
Med seboj komunicirata prek socket-a, ki se odpre na portu 12001.

-----

TODO:

- Python detect script does not terminate propery (stops than hangs).
- Visualization breaks on window resize
- Light source changes in lightness rework (increase is done on socket receive, decrease is done every update cycle)
- Let detect and visualization pass parameters, such as "debug" and "fullscreen"