package net.iamsilver.fireflies.ipc;

public class Marker {

    private int id;
    private float x;
    private float y;

    public Marker(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("%d,%f,%f", id, x, y);
    }
}
