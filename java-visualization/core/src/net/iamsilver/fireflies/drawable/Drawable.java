package net.iamsilver.fireflies.drawable;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class Drawable {

    protected Vector2 position;

    public Drawable(float x, float y) {
        this.position = new Vector2(x, y);
    }

    public abstract void draw(ShapeRenderer shapeRenderer);

}
