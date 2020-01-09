package net.iamsilver.fireflies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.iamsilver.fireflies.drawable.Colors;

public class BlendTest {

    public static final int[] blends = {
            GL20.GL_ZERO,
            GL20.GL_ONE,

            GL20.GL_SRC_COLOR,
            GL20.GL_ONE_MINUS_SRC_COLOR,
            GL20.GL_DST_COLOR,
            GL20.GL_ONE_MINUS_DST_COLOR,

            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA,
            GL20.GL_DST_ALPHA,
            GL20.GL_ONE_MINUS_DST_ALPHA,

            GL20.GL_SRC_ALPHA_SATURATE
    };

    public static void render(ShapeRenderer shapeRenderer) {

        for (int n = 0; n < blends.length; n++) {
            int srcblend = blends[n];
            for (int m = 0; m < blends.length; m++) {
                int dstblend = blends[m];

                Gdx.gl.glEnable(GL20.GL_BLEND);
//				Gdx.gl.glBlendFunc(srcblend, dstblend);

                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_SRC_COLOR); // <-- THIS THE ONE I WANT!!!

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                shapeRenderer.setColor(Colors.withAlpha(Colors.lightColors[0], 0.1f));
                int offset = 80;
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        shapeRenderer.circle(n * offset + 30 + (25 * (i % 3)), m * offset + 30 + (25 * (int) (i / 3)), 12);
                    }
                }
                shapeRenderer.end();

                Gdx.gl.glDisable(GL20.GL_BLEND);
            }
        }

        for (int n = 0; n < blends.length; n++) {
            int srcblend = blends[n];
            for (int m = 0; m < blends.length; m++) {
                int dstblend = blends[m];

                Gdx.gl.glEnable(GL20.GL_BLEND);
//				Gdx.gl.glBlendFunc(srcblend, dstblend);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA); // <--

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                shapeRenderer.setColor(Colors.withAlpha(Colors.BACKGROUND, 0.51f));
                int offset = 80;
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < i+1; j++) {
                        shapeRenderer.circle(n * offset + 30 + (25 * (i % 3)), m * offset + 30 + (25 * (int) (i / 3)), 12);
                    }
                }
                shapeRenderer.end();

                Gdx.gl.glDisable(GL20.GL_BLEND);
            }
        }
    }
}
