package com.jcode.jphys.physics;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class CustomObject extends BaseBoxObject {

	private Vector2 customModelOrigin;

	public CustomObject(Vector2 pos, World world, int boxIndex,
			int collisionGroup, BodyType bodyType, float angle,
			Texture texture, int objectType) {
		super(pos, world, boxIndex, collisionGroup, bodyType, angle, texture,
				objectType);

	}

	public void setFixture(float width, float density, float friction,
			float restitution, BodyEditorLoader loader, String modelName) {
		FixtureDef fd = new FixtureDef();
		fd.density = density;
		fd.friction = friction;
		fd.restitution = restitution;
		loader.attachFixture(this.body, modelName, fd, width);
		this.customModelOrigin = loader.getOrigin(modelName, width).cpy();
		this.sprite.setSize(width, width * this.spriteRatio);
	}

	@Override
	public void update() {
		Vector2 bottlePos = this.body.getPosition().sub(this.customModelOrigin);
		this.sprite.setPosition(bottlePos.x, bottlePos.y);
		this.sprite.setOrigin(this.customModelOrigin.x,
				this.customModelOrigin.y);
		this.sprite.setRotation(this.body.getAngle()
				* MathUtils.radiansToDegrees);

	}

}
