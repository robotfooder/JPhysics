package com.jcode.jphys.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class BaseBoxObject {

	protected Body body;
	protected Vector2 worldPosition;
	protected BoxUserData userData;
	protected Sprite sprite;
	protected int bodyShape;
	protected float spriteRatio;
	protected int objectType;
	protected boolean isActive;

	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;

	// protected float convertToBox(float x) {
	// return x * WORLD_TO_BOX;
	// }
	//
	// protected float convertToWorld(float x) {
	// return x * BOX_TO_WORLD;
	// }

	public BaseBoxObject(Vector2 pos, World world, int boxIndex,
			int collisionGroup, BodyType bodyType, float angle,
			Texture texture, int objectType) {
		this.userData = new BoxUserData(boxIndex, collisionGroup);
		this.worldPosition = new Vector2();
		createBody(world, pos, angle, bodyType);
		this.body.setUserData(this.userData);
		this.sprite = new Sprite(texture);
		this.spriteRatio = this.sprite.getHeight() / this.sprite.getWidth();
		this.objectType = objectType;

	}

	private void createBody(World world, Vector2 pos, float angle,
			BodyType bodyType) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(pos.x, pos.y);
		bodyDef.angle = angle;
		this.body = world.createBody(bodyDef);
	}

	protected void setFixture(FixtureDef fixtureDef) {
		this.body.createFixture(fixtureDef);
		fixtureDef.shape.dispose();

	}

	protected void updateWorldPosition() {
		worldPosition.set(this.body.getPosition().x - this.sprite.getWidth()
				/ 2, this.body.getPosition().y - this.sprite.getHeight() / 2);
	}

	public void draw(SpriteBatch sp) {
		this.sprite.draw(sp);
	}

	public void update() {
		updateWorldPosition();
		this.sprite.setPosition(this.worldPosition.x, this.worldPosition.y);
		this.sprite.setRotation(this.body.getAngle()
				* MathUtils.radiansToDegrees);
	}

	public Body getBody() {
		return body;
	}

	public int getObjectType() {
		return objectType;
	}

	public void destroy(World world) {
		if (isActive) {
			world.destroyBody(body);
			isActive = false;
		}
	}

}
