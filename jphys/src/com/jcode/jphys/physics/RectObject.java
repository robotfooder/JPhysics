package com.jcode.jphys.physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * @author jakobssonje
 * 
 */
public class RectObject extends BaseBoxObject {

	private float height;
	private float width;

	public RectObject(Vector2 pos, World world, int boxIndex,
			int collisionGroup, BodyType bodyType, float angle, Texture texture) {
		super(pos, world, boxIndex, collisionGroup, bodyType, angle, texture);

	}

	/**
	 * @param width
	 * @param height
	 * @param density
	 * @param friction
	 * @param restitution
	 *            Use this when creating a square independent from the sprite
	 *            ratio
	 */
	public void setFixture(float width, float height, float density,
			float friction, float restitution) {
		PolygonShape bodyShape = new PolygonShape();

		this.width = width;
		this.height = height;
		bodyShape.setAsBox(this.width, this.height);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		fixtureDef.shape = bodyShape;

		super.setFixture(fixtureDef);

		this.sprite.setSize(this.width * 2, this.height * 2);
		this.sprite.setOrigin(this.width, this.height);

	}

	public void setFixture(float width, float density, float friction,
			float restitution) {
		setFixture(width, width * this.spriteRatio, density, friction,
				restitution);

	}

	public void setSpriteColor(Color color) {
		this.sprite.setColor(color);
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

}
