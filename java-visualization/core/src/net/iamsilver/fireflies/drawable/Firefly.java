package net.iamsilver.fireflies.drawable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Firefly extends Drawable {

    private static final float MAX_FORCE = 0.05f;
    private static final float MAX_SPEED = 2f;
    private static final int BORDER_MARGIN = 20;

    private float lightness;
    private boolean dead = false;

    private Vector2 velocity;
    private Vector2 acceleration;
    private Color color;

    public Firefly(float x, float y) {
        super(x, y);
        this.lightness = 0;
        this.color = Colors.randomLightColor();

        Random r = new Random();
        this.velocity = new Vector2(r.nextInt(200) - 100, r.nextInt(200) - 100);
        this.velocity.nor().scl(r.nextInt(3) + 2);
        this.acceleration = Vector2.Zero.cpy();
    }

    public static Firefly atRandomPosition() {
        Random r = new Random();
        return new Firefly(r.nextFloat() * Gdx.graphics.getWidth(), r.nextFloat() * Gdx.graphics.getHeight());
    }

    private void warp() {
        if (this.position.x > Gdx.graphics.getWidth() +BORDER_MARGIN) this.position.x = -BORDER_MARGIN;
        else if (this.position.x < -BORDER_MARGIN) this.position.x = Gdx.graphics.getWidth() +BORDER_MARGIN;

        if (this.position.y > Gdx.graphics.getHeight() +BORDER_MARGIN) this.position.y = -BORDER_MARGIN;
        else if (this.position.y < -BORDER_MARGIN) this.position.y = Gdx.graphics.getHeight() +BORDER_MARGIN;
    }

    public void update(ArrayList<Firefly> boids, Collection<LightSource> goals) {
        warp();
        flock(boids, goals);

        this.dead = shouldDie(boids);

        if (this.velocity.len() < MAX_SPEED * 0.66f) {
            // prevent boids from almost stopping by limiting minimum speed
            this.velocity.nor().scl(MAX_SPEED * 0.66f);
        }

        this.position.add(this.velocity);
        this.velocity.add(this.acceleration);
        this.velocity.limit(MAX_SPEED);
        this.acceleration.scl(0);

        // if the firefly is close to any goal (that is lit), increase light, otherwise decrease it
        this.decreaseLight();
        for (LightSource light : goals) {
            if (this.position.dst(light.position) < 100) {
                this.color.lerp(light.getColor(), (0.033f) * light.getLightness());
                this.increaseLight(light.getLightness());
            }
        }
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        float alphaVal = Math.max(0.35f, this.lightness);
        shapeRenderer.setColor(Colors.withAlpha(this.color, alphaVal));
        shapeRenderer.circle(this.position.x, this.position.y, 6);
        Vector2 offset = this.velocity.cpy().nor().scl(3.5f);
        Vector2 offset1 = this.velocity.cpy().nor().scl(3.5f).rotate90(1);
        Vector2 offset2 = this.velocity.cpy().nor().scl(3.5f).rotate90(-1);
        shapeRenderer.circle(this.position.x - offset.x - offset1.x, this.position.y - offset.y - offset1.y, 4);
        shapeRenderer.circle(this.position.x - offset.x - offset2.x, this.position.y - offset.y - offset2.y, 4);
    }


    public void drawTrace(ShapeRenderer shapeRenderer) {
        float alphaVal = Math.max(0.01f, this.lightness / 10f);
        shapeRenderer.setColor(Colors.withAlpha(this.color, alphaVal));
        shapeRenderer.circle(this.position.x, this.position.y, 5);
    }

    public void increaseLight() {
        increaseLight(1f);
    }

    public void increaseLight(float multiplier) {
        this.lightness += (0.05 * multiplier);
        if (this.lightness > 1) {
            this.lightness = 1;
        }
    }

    public void decreaseLight() {
        this.lightness -= 0.005;
        if (this.lightness < 0) {
            this.lightness = 0;
        }
    }

    public void setColor(Color color) {
        this.color = color.cpy();
    }

    public void setLightness(float lightness) {
        this.lightness = lightness;
    }

    private void flock(ArrayList<Firefly> boids, Collection<LightSource> goals) {
        Vector2 align = alignment(boids);
        Vector2 cohere = cohesion(boids);
        Vector2 separate = separation(boids);
        Vector2 towardsGoals = towardsGoals(goals);

        // TODO: limit/scale vectors?

        Random r = new Random();

        this.acceleration
                .add(align)
                .add(cohere)
                .add(separate.scl(1.25f))
                .add(towardsGoals.scl(2f));
    }

    private Vector2 alignment(ArrayList<Firefly> boids) {
        int perceptionRadius = 35;
        Vector2 steering = new Vector2();
        int total = 0;
        for (Firefly other : boids) {
            float dist = this.position.dst(other.position);
            if (other != this && dist < perceptionRadius) {
                steering.add(other.velocity);
                total++;
            }
        }

        if (total > 0) {
            steering.scl(1f / total);
            steering.nor().scl(MAX_SPEED);
            steering.sub(this.velocity);
            steering.limit(MAX_FORCE);
        }

        return steering;
    }

    private Vector2 cohesion(ArrayList<Firefly> boids) {
        int perceptionRadius = 50;
        Vector2 steering = new Vector2();
        int total = 0;
        for (Firefly other : boids) {
            float dist = this.position.dst(other.position);
            if (other != this && dist < perceptionRadius) {
                steering.add(other.position);
                total++;
            }
        }

        if (total > 0) {
            steering.scl(1f / total);
            steering.sub(this.position);
            steering.nor().scl(MAX_SPEED);
            steering.sub(this.velocity);
            steering.limit(MAX_FORCE);
        }

        return steering;
    }

    private Vector2 separation(ArrayList<Firefly> boids) {
        int perceptionRadius = 20;
        Vector2 steering = new Vector2();
        int total = 0;
        for (Firefly other : boids) {
            float dist = this.position.dst(other.position);
            if (other != this && dist < perceptionRadius && dist > 0) {
                Vector2 diff = this.position.cpy().sub(other.position);
                diff.nor();
                diff.scl(1 / (dist * dist));
                steering.add(diff);
                total++;
            }
        }

        if (total > 0) {
            steering.scl(1f / total);
            steering.nor().scl(MAX_SPEED);
            steering.sub(this.velocity);
            steering.limit(MAX_FORCE);
        }

        return steering;
    }

    private Vector2 towardsGoals(Collection<LightSource> goals) {
        int perceptionRadius = LightSource.BRIGHT_SIZE;
        Vector2 steering = new Vector2();
        int total = 0;
        for (LightSource goal : goals) {
            float dist = this.position.dst(goal.position);
            if (dist < perceptionRadius && goal.isActive()) {
                steering.add(goal.position.cpy());
                total++;
            }
        }

        if (total > 0) {
            steering.scl(1f / total);
            steering.sub(this.position);
            steering.nor().scl(MAX_SPEED);
            steering.sub(this.velocity);
            steering.limit(MAX_FORCE);
        }

        return steering;
    }

    private boolean shouldDie(ArrayList<Firefly> boids) {
        int perceptionRadius = 10;
        int total = 0;
        for (Firefly other : boids) {
            float dist = this.position.dst(other.position);
            if (other != this && dist < perceptionRadius) {
                total++;
            }
        }
        if (total > 10) {
            return new Random().nextFloat() > 0.9f; // 10% chance of death
        }
        return false;
    }

    public boolean isDead() {
        return this.dead;
    }

    public Firefly copy() {
        Firefly clone = new Firefly(this.position.x, this.position.y);
        clone.velocity = this.velocity.cpy();
        return clone;
    }

}
