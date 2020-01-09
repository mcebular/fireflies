package net.iamsilver.fireflies;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.iamsilver.fireflies.drawable.Colors;
import net.iamsilver.fireflies.drawable.Firefly;
import net.iamsilver.fireflies.drawable.FireflyManager;
import net.iamsilver.fireflies.drawable.LightSourceManager;
import net.iamsilver.fireflies.ipc.Marker;
import net.iamsilver.fireflies.ipc.Markers;
import net.iamsilver.fireflies.ipc.SocketListener;
import net.iamsilver.fireflies.ipc.SocketServer;

public class Fireflies extends ApplicationAdapter {

	public static final int BOID_COUNT = 200;

	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;
	BitmapFont font;
	SocketServer socket;

	LightSourceManager lightSourceManager;
	FireflyManager flock;

	FrameBuffer buffer;

	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();

		lightSourceManager = new LightSourceManager();
		flock = new FireflyManager();
		for (int i = 0; i < BOID_COUNT; i++) {
			flock.add(Firefly.atRandomPosition());
		}

		buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

		socket = new SocketServer(new SocketListener() {
			@Override
			public void received(Markers data) {
				System.out.println("Received: " + data.toString());
				for (Marker m : data.getMarkers()) {
					lightSourceManager.receivedLight(m.getId(), (1 - m.getX()) * Gdx.graphics.getWidth(), (1 - m.getY()) * Gdx.graphics.getHeight());
				}
			}
		});
		Thread t = new Thread(socket);
		t.start();
	}

	private void update() {
		processInput();

		lightSourceManager.update();
		flock.update(lightSourceManager.getSources());
	}

	private void processInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			flock.randomize();
			lightSourceManager.randomizeColors();

			// TODO: buffer should actually be properly disposed!!!
			buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		}

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			// id defines light color, thus we define negative id for a color we want for a mouse
			// (lights from external sources, i.e. markers, should provide positive ids!)
			lightSourceManager.receivedLight(-5, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		} else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			lightSourceManager.receivedLight(-9, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			Utils.saveScreenshot();
		}
	}

	int blendMode = 0;

	@Override
	public void render () {
		update();

		Gdx.gl.glClearColor(Colors.BACKGROUND.r, Colors.BACKGROUND.g, Colors.BACKGROUND.b, Colors.BACKGROUND.a);
//		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		buffer.begin();

		// -----
		// Drawing background to fade out traces
		// TODO: can't blend propery ??
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFuncSeparate(GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Colors.withAlpha(Colors.BACKGROUND, 0.1f));
//		shapeRenderer.rect(200, 200, 200, 200);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		// -----

		// -----
		// Drawing traces of boids

		int srcBlend = BlendTest.blends[blendMode / 11];
		int dstBlend = BlendTest.blends[blendMode % 11];

		Gdx.gl.glEnable(GL20.GL_BLEND);
//		Gdx.gl.glBlendFunc(srcBlend, dstBlend);
		Gdx.gl.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		flock.drawTraces(shapeRenderer);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		// -----
		buffer.end();

		// -----
		// Draw the buffer
		spriteBatch.begin();
		spriteBatch.draw(buffer.getColorBufferTexture(), 0, 0, buffer.getWidth(), buffer.getHeight(), 0, 0, buffer.getWidth(), buffer.getHeight(), false, true);
		spriteBatch.end();
		// -----


		// -----
		// Draw lights & boids
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		flock.draw(shapeRenderer);
		lightSourceManager.draw(shapeRenderer);

		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		// -----

		// -----
		// Draw debug info
		spriteBatch.begin();
		font.setColor(1, 1, 1, 1);
		font.draw(spriteBatch, String.format("%d fps", Gdx.graphics.getFramesPerSecond()), 5, 20);
		font.draw(spriteBatch, String.format("%d lights active", lightSourceManager.getActiveSourcesCount()), 5, 40);
		font.draw(spriteBatch, String.format("%d fireflies alive", flock.getCount()), 5, 60);
		spriteBatch.end();
		// -----
	}
	
	@Override
	public void dispose () {
		socket.dispose();
		spriteBatch.dispose();
		shapeRenderer.dispose();
		font.dispose();
		buffer.dispose();
	}

}
