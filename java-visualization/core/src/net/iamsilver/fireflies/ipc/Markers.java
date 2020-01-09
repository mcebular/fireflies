package net.iamsilver.fireflies.ipc;

import java.util.ArrayList;

public class Markers {

    private ArrayList<Marker> markers;

    public Markers() {
        markers = new ArrayList<>();

    }

    public void add(Marker m) {
        markers.add(m);
    }

    public ArrayList<Marker> getMarkers() {
        return markers;
    }

    public static Markers fromString(String str) {
        Markers m = new Markers();

        if (str.trim().isEmpty()) return m;

        for(String mkstr : str.trim().split("\t")) {
            String[] parts = mkstr.split(",");
            m.add(new Marker(Integer.parseInt(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2])));
        }
        return m;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (Marker m : markers) {
            ret.append(m.toString()).append("\t");
        }
        return ret.toString();
    }
}
