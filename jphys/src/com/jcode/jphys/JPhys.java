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
import com.jcode.jphys.physics.BaseBoxObject;
import com.jcode.jphys.physics.BoxObjectManager;
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

	// Object handler
	private BoxObjectManager boxObjectManager;

	// Textures
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
	private boolean doBottle = false;
	private boolean doBox = true;
	final short GROUP_BALLS = 1;
	final short GROUP_Things = -1;
	public static final int RECT_OBJECT = 0;
	public static final int CIRCLE_OBJECT = 1;
	public static final int CUSTOM_OBJECT = 2;

	@Override
	public void create() {

		// create box object manager
		this.boxObjectManager = new BoxObjectManager(new Vector2(0, -10));

		// load textures
		whiteTexture = new Texture(Gdx.files.internal("data/gfx/white.png"));
		rectTexture = new Texture(Gdx.files.internal("data/gfx/rect.png"));
		rectTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ballTexture = new Texture(Gdx.files.internal("data/gfx/ball.png"));
		ballTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bottleTexture = new Texture(Gdx.files.internal("data/gfx/bottle.png"));
		bottleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// Models initialization
		createBoxObjects();

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

	private void createBoxObjects() {

		// ground
		RectObject ground = (RectObject) this.boxObjectManager.AddObject(
				new Vector2(0, 0.5f), RECT_OBJECT, 0, BodyType.StaticBody, 0,
				whiteTexture, GROUP_Things);
		ground.setFixture(VIEWPORT_WIDTH, 0.5f, 1, 0.5f, 0.5f);
		ground.update();
		ground.setSpriteColor(Color.DARK_GRAY);

		if (this.doBox) {
			// square
			RectObject square = (RectObject) this.boxObjectManager.AddObject(
					new Vector2(5, 3), RECT_OBJECT, 1, BodyType.StaticBody,
					0.1f, whiteTexture, GROUP_Things);
			square.setFixture(SQUARE_WIDTH, SQUARE_WIDTH, 1, 0.5f, 0.5f);
			square.setSpriteColor(Color.RED);

			// rect
			RectObject rect = (RectObject) this.boxObjectManager.AddObject(
					new Vector2(0, 5), RECT_OBJECT, 2, BodyType.KinematicBody,
					0.2f, rectTexture, GROUP_Things);
			rect.setFixture(RECT_WIDTH, 1, 0.5f, 0.5f);
			rect.spinning(0.8f);
		}

		// balls
		for (int i = 0; i < MAX_BALLS; i++) {
			CircleObject ball = (CircleObject) this.boxObjectManager.AddObject(
					new Vector2(0, 0), CIRCLE_OBJECT, i + 3,
					BodyType.DynamicBody, 0, ballTexture, GROUP_BALLS);
			ball.setFixture(BALL_RADIUS, 1, 0.5f, 0.5f);
		}

		if (doBottle) {
			// bottle
			BodyEditorLoader loader = new BodyEditorLoader(
					Gdx.files.internal("data/test.json"));
			CustomObject bottle = (CustomObject) this.boxObjectManager
					.AddObject(new Vector2(0, 0), CUSTOM_OBJECT, MAX_BALLS + 4,
							BodyType.DynamicBody, 0, bottleTexture,
							GROUP_Things);
			bottle.setFixture(BOTTLE_WIDTH, 1, 0.5f, 0.3f, loader, "test01");
		}

	}

	@Override
	public void dispose() {
		bottleTexture.dispose();
		ballTexture.dispose();
		whiteTexture.dispose();
		rectTexture.dispose();
		batch.dispose();
		font.dispose();
		this.boxObjectManager.dispose();
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
		this.boxObjectManager.stepWorld(1 / 60f, 10, 10);

		this.boxObjectManager.Update();

		// Render
		GL10 gl = Gdx.gl10;
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		this.boxObjectManager.draw(batch);
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
		for (BaseBoxObject obj : this.boxObjectManager.getCircleObjects()) {
			obj.getBody().applyForceToCenter(vector2);
		}

	}

	private void restart() {

		if (doBottle) {
			for (BaseBoxObject obj : this.boxObjectManager.getCustomObjects()) {
				obj.getBody().setTransform(0, 2, 0.1f);
				obj.getBody().setLinearVelocity(0, 0);
				obj.getBody().setAngularVelocity(0);
			}
		}

		Vector2 vec = new Vector2();

		for (BaseBoxObject obj : this.boxObjectManager.getCircleObjects()) {
			float tx = rand.nextFloat() * 1.0f - 0.5f;
			float ty = camera.position.y + camera.viewportHeight / 2
					+ BALL_RADIUS;
			float angle = rand.nextFloat() * MathUtils.PI * 2;

			obj.getBody().setActive(false);
			obj.getBody().setLinearVelocity(vec.set(0, 0));
			obj.getBody().setAngularVelocity(0);
			obj.getBody().setTransform(vec.set(tx, ty), angle);
		}

		tweenManager.killAll();

		Tween.call(new TweenCallback() {
			private int idx = 0;

			@Override
			public void onEvent(int type, BaseTween<?> source) {
				if (idx < boxObjectManager.getCircleObjects().size()) {
					BaseBoxObject obj = boxObjectManager.getCircleObjects()
							.get(idx);
					obj.getBody().setAwake(true);
					obj.getBody().setActive(true);
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
