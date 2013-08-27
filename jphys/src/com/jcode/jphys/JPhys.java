package com.jcode.jphys;

import java.util.Random;

import aurelienribon.bodyeditor.BodyEditorLoader;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.jcode.jphys.physics.CircleObject;
import com.jcode.jphys.physics.CustomObject;
import com.jcode.jphys.physics.RectObject;

public class JPhys extends ApplicationAdapter {

	// -------------------------------------------------------------------------
	// Static fields
	// -------------------------------------------------------------------------

	private static final float VIEWPORT_WIDTH = 15;
	private static final float BALL_RADIUS = 0.15f;
	private static final float BOTTLE_WIDTH = 7;
	private static final float SQUARE_WIDTH = 1;
	private static final float RECT_WIDTH = 2;
	private static final int MAX_BALLS = 15;

	// Models
	private World world;
	private CircleObject[] balls;
	private RectObject squareBox;
	private RectObject rectBox;
	private RectObject groundBox;
	private CustomObject bottle;

	// Render
	private Texture bottleTexture;
	private Texture ballTexture;
	private Texture whiteTexture;
	private Texture rectTexture;

	// Render general
	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;

	// Misc
	private final TweenManager tweenManager = new TweenManager();
	private final Random rand = new Random();
	private boolean doBottle = true;
	private boolean doBox = false;
	final short GROUP_BALLS = 1;
	final short GROUP_Things = -1;

	@Override
	public void create() {
		// Models initialization

		world = new World(new Vector2(0, -10), true);
		whiteTexture = new Texture(Gdx.files.internal("data/gfx/white.png"));
		rectTexture = new Texture(Gdx.files.internal("data/gfx/rect.png"));
		rectTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ballTexture = new Texture(Gdx.files.internal("data/gfx/ball.png"));
		ballTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bottleTexture = new Texture(Gdx.files.internal("data/gfx/bottle.png"));
		bottleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		createGround();
		if (doBottle)
			createBottle(); // <-- this method uses the BodyEditorLoader class
		createBalls();
		if (doBox)
			createBoxes();

		// Render initialization

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.BLACK);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_WIDTH * h / w);
		camera.position.set(0, camera.viewportHeight / 2, 0);
		camera.update();

		// Input initialization

		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean touchDown(int x, int y, int pointer, int button) {
				restart();
				return true;
			}

		});

		// Run

		restart();
	}

	private void createBoxes() {

		this.squareBox = new RectObject(new Vector2(5, 3), world, 0,
				GROUP_Things, BodyType.StaticBody, 0.1f, whiteTexture);
		this.squareBox.setFixture(SQUARE_WIDTH, SQUARE_WIDTH, 1, 0.5f, 0.5f);
		this.squareBox.setSpriteColor(Color.RED);
		this.rectBox = new RectObject(new Vector2(0, 5), world, 1, 0,
				BodyType.StaticBody, 0.2f, rectTexture);
		this.rectBox.setFixture(RECT_WIDTH, 1, 0.5f, 0.5f);

	}

	private void createGround() {
		this.groundBox = new RectObject(new Vector2(0, 0.5f), world, 4,
				GROUP_Things, BodyType.StaticBody, 0, whiteTexture);
		this.groundBox.setFixture(VIEWPORT_WIDTH, 0.5f, 1, 0.5f, 0.5f);
		this.groundBox.update();
		this.groundBox.setSpriteColor(Color.DARK_GRAY);

	}

	private void createBalls() {

		this.balls = new CircleObject[MAX_BALLS];
		for (int i = 0; i < MAX_BALLS; i++) {
			this.balls[i] = new CircleObject(new Vector2(0, 0), world, 2,
					GROUP_BALLS, BodyType.DynamicBody, 0, ballTexture);
			this.balls[i].setFixture(BALL_RADIUS, 1, 0.5f, 0.5f);

		}
	}

	private void createBottle() {
		// 0. Create a loader for the file saved from the editor.
		BodyEditorLoader loader = new BodyEditorLoader(
				Gdx.files.internal("data/test.json"));
		this.bottle = new CustomObject(new Vector2(0, 0), world, 5,
				GROUP_Things, BodyType.DynamicBody, 0, bottleTexture);
		this.bottle.setFixture(BOTTLE_WIDTH, 1, 0.5f, 0.3f, loader, "test01");

	}

	@Override
	public void dispose() {
		bottleTexture.dispose();
		ballTexture.dispose();
		whiteTexture.dispose();
		batch.dispose();
		font.dispose();
		world.dispose();
	}

	@Override
	public void render() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) {
			pushBalls(new Vector2(-2, 0));
		}

		if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) {
			pushBalls(new Vector2(2, 0));
		}

		// Update
		tweenManager.update(1 / 60f);
		world.step(1 / 60f, 10, 10);

		if (doBottle) {
			this.bottle.update();
		}

		if (doBox) {
			squareBox.update();
			rectBox.update();
		}

		for (int i = 0; i < MAX_BALLS; i++) {
			this.balls[i].update();
		}

		// Render
		GL10 gl = Gdx.gl10;
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		this.groundBox.draw(batch);
		if (doBottle)
			this.bottle.draw(batch);
		if (doBox) {
			squareBox.draw(batch);
			rectBox.draw(batch);
		}

		for (int i = 0; i < MAX_BALLS; i++)
			balls[i].draw(batch);
		batch.end();

		batch.getProjectionMatrix().setToOrtho2D(0, 0, w, h);
		batch.begin();
		font.draw(batch, "Touch the screen to restart", 5, h - 5);
		// for (int i = 0; i < MAX_BALLS; i++)
		// font.draw(batch, "Speed: "
		// + ballModels[i].getLinearVelocity().toString() + " Angle: "
		// + ballModels[i].getAngle(), 5, h - (i + 2) * 15);
		batch.end();
	}

	private void pushBalls(Vector2 vector2) {
		for (int i = 0; i < MAX_BALLS; i++) {
			this.balls[i].getBody().applyForceToCenter(vector2);

		}

	}

	private void restart() {

		if (doBottle) {
			this.bottle.getBody().setTransform(0, 2, 0.1f);
			this.bottle.getBody().setLinearVelocity(0, 0);
			this.bottle.getBody().setAngularVelocity(0);
		}

		Vector2 vec = new Vector2();

		for (int i = 0; i < MAX_BALLS; i++) {
			float tx = rand.nextFloat() * 1.0f - 0.5f;
			float ty = camera.position.y + camera.viewportHeight / 2
					+ BALL_RADIUS;
			float angle = rand.nextFloat() * MathUtils.PI * 2;

			this.balls[i].getBody().setActive(false);
			this.balls[i].getBody().setLinearVelocity(vec.set(0, 0));
			this.balls[i].getBody().setAngularVelocity(0);
			this.balls[i].getBody().setTransform(vec.set(tx, ty), angle);
		}

		tweenManager.killAll();

		Tween.call(new TweenCallback() {
			private int idx = 0;

			@Override
			public void onEvent(int type, BaseTween<?> source) {
				if (idx < balls.length) {
					balls[idx].getBody().setAwake(true);
					balls[idx].getBody().setActive(true);
					idx += 1;
				}
			}

		}).repeat(-1, 0.1f).start(tweenManager);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
