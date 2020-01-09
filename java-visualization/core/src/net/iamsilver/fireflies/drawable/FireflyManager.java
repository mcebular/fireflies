package net.iamsilver.fireflies.drawable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import net.iamsilver.fireflies.Fireflies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import java.util.Random;

public class FireflyManager {

    private ArrayList<Firefly> fireflies;

    public FireflyManager() {
        fireflies = new ArrayList<>();
    }

    public void add(Firefly firefly) {
        fireflies.add(firefly);
    }

    public void update(Collection<LightSource> goals) {
        for (Firefly f : fireflies) {
            f.update(this.fireflies, goals);
        }

        // kill fireflies of overcrowded
        ListIterator<Firefly> iter = fireflies.listIterator();
        while(iter.hasNext()) {
            if(iter.next().isDead()) {
                iter.remove();
            }
        }

        // spawn fireflies if less than 200 of em
        if (fireflies.size() < Fireflies.BOID_COUNT) {
            // Less there are fireflies, more likey are new to spawn
            Random r = new Random();
            if (r.nextFloat() < 1f - (float) fireflies.size() / (float) Fireflies.BOID_COUNT) {
                Firefly f = new Firefly(-10, r.nextInt(Gdx.graphics.getHeight()));
                for(int i = 0; i < r.nextInt(5) + 5; i++) {
                    fireflies.add(f.copy());
                }
            }
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        for (Firefly f : fireflies) {
            f.draw(shapeRenderer);
        }
    }

    public void drawTraces(ShapeRenderer shapeRenderer) {
        for (Firefly f : fireflies) {
            f.drawTrace(shapeRenderer);
        }
    }

    public void randomize() {
        for (Firefly f : fireflies) {
            f.setColor(Colors.randomLightColor());
            f.position = Firefly.atRandomPosition().position.cpy();
            f.setLightness(0);
        }
    }

    public int getCount() {
        return this.fireflies.size();
    }
}
