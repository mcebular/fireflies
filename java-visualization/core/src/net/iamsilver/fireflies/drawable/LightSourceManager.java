package net.iamsilver.fireflies.drawable;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class LightSourceManager {

    HashMap<Integer, LightSource> sources;

    public LightSourceManager() {
        sources = new HashMap<>();
    }

    public void receivedLight(int id, float x, float y) {
        if (!sources.containsKey(id)) {
            int colorIndex = Math.abs(id) % Colors.lightColors.length;
            sources.put(id, new LightSource(x, y, Colors.lightColors[colorIndex].cpy()));
        } else {
            LightSource source = sources.get(id);
            source.increaseLight();
            source.setTarget(new Vector2(x, y));
        }
    }

    public void update() {
        for (Integer id : sources.keySet()) {
            sources.get(id).update();
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        for (LightSource source : sources.values()) {
            source.draw(shapeRenderer);
        }
    }

    public Collection<LightSource> getSources() {
        return new ArrayList<>(sources.values());
    }

    public void randomizeColors() {
        for (LightSource s: sources.values()) {
            // s.setColor(Colors.randomLightColor());
            s.setLightness(0);
        }
    }

    public int getActiveSourcesCount() {
        int count = 0;
        for (LightSource s: sources.values()) {
            if (s.isActive()) count++;
        }
        return count;
    }
}
