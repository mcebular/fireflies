package net.iamsilver.fireflies.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class LightSource extends Drawable {

    public static final int BULB_SIZE = 4;
    public static final int BRIGHT_SIZE = 120;

    private Vector2 target;
    private float lightness;
    private Color color;

    public LightSource(float x, float y, Color color) {
        super(x, y);
        this.lightness = 0;
        this.color = color.cpy();
    }

    public void setTarget(Vector2 target) {
        this.target = target;
    }

    public float getLightness() {
        return lightness;
    }

    public void setLightness(float lightness) {
        this.lightness = lightness;
    }

    public boolean isActive() {
        return this.lightness > 0f;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void update() {
        this.decreaseLight();

        if (target != null) {
            float dist = target.dst(this.position);
            Vector2 towards = target.cpy().sub(this.position).nor().scl(dist / 10);
            this.position.add(towards);
        }
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        float dimAlpha = Math.min(this.lightness, 0.33f);
        shapeRenderer.setColor(Colors.withAlpha(this.color, dimAlpha));
        shapeRenderer.circle(this.position.x, this.position.y, BRIGHT_SIZE);

        shapeRenderer.setColor(Colors.withAlpha(this.color, dimAlpha * 3));
        // shapeRenderer.rect(this.position.x - BULB_SIZE / 2f, this.position.y - BULB_SIZE / 2f, BULB_SIZE, BULB_SIZE);
        // shapeRenderer.circle(this.position.x, this.position.y, BULB_SIZE);

        // There's probably a better way to do this maybe use a loop LMAO
        shapeRenderer.triangle(
                this.position.x, this.position.y,
                this.position.x - 2f * BULB_SIZE, this.position.y - 1f * BULB_SIZE,
                this.position.x - 2f * BULB_SIZE, this.position.y + 1f * BULB_SIZE);
        shapeRenderer.triangle(
                this.position.x, this.position.y,
                this.position.x - 1f * BULB_SIZE, this.position.y - 2f * BULB_SIZE,
                this.position.x + 1f * BULB_SIZE, this.position.y - 2f * BULB_SIZE);
        shapeRenderer.triangle(
                this.position.x, this.position.y,
                this.position.x + 2f * BULB_SIZE, this.position.y - 1f * BULB_SIZE,
                this.position.x + 2f * BULB_SIZE, this.position.y + 1f * BULB_SIZE);
        shapeRenderer.triangle(
                this.position.x, this.position.y,
                this.position.x - 1f * BULB_SIZE, this.position.y + 2f * BULB_SIZE,
                this.position.x + 1f * BULB_SIZE, this.position.y + 2f * BULB_SIZE);

        shapeRenderer.triangle(
                this.position.x, this.position.y,
                this.position.x - 1f * BULB_SIZE, this.position.y - 2f * BULB_SIZE,
                this.position.x - 2f * BULB_SIZE, this.position.y - 1f * BULB_SIZE);
        shapeRenderer.triangle(
                this.position.x, this.position.y,
                this.position.x + 1f * BULB_SIZE, this.position.y + 2f * BULB_SIZE,
                this.position.x + 2f * BULB_SIZE, this.position.y + 1f * BULB_SIZE);
        shapeRenderer.triangle(
                this.position.x, this.position.y,
                this.position.x - 1f * BULB_SIZE, this.position.y + 2f * BULB_SIZE,
                this.position.x - 2f * BULB_SIZE, this.position.y + 1f * BULB_SIZE);
        shapeRenderer.triangle(
                this.position.x, this.position.y,
                this.position.x + 1f * BULB_SIZE, this.position.y - 2f * BULB_SIZE,
                this.position.x + 2f * BULB_SIZE, this.position.y - 1f * BULB_SIZE);
    }

    public void increaseLight() {
        this.lightness += 0.06;
        if (this.lightness > 1) {
            this.lightness = 1;
        }
    }

    public void decreaseLight() {
        this.lightness -= 0.012;
        if (this.lightness < 0) {
            this.lightness = 0;
        }
    }
}
