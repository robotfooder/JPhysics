package com.jcode.jphys.physics;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
			int collisionGroup, BodyType bodyType, float angle, Texture texture) {
		this.userData = new BoxUserData(boxIndex, collisionGroup);
		this.worldPosition = new Vector2();
		createBody(world, pos, angle, bodyType);
		this.body.setUserData(this.userData);
		this.sprite = new Sprite(texture);
		this.spriteRatio = this.sprite.getHeight()/this.sprite.getWidth();

	}

	private void createBody(World world, Vector2 pos, float angle,
			BodyType bodyType) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(pos.x, pos.y);
		bodyDef.angle = angle;
		this.body = world.createBody(bodyDef);
	}

	public void setFixture(FixtureDef fixtureDef) {
		this.body.createFixture(fixtureDef);
		fixtureDef.shape.dispose();

	}

	protected void updateWorldPosition() {
		worldPosition.set(this.body.getPosition().x - this.sprite.getWidth()
				/ 2, this.body.getPosition().y - this.sprite.getHeight() / 2);
	}

}
