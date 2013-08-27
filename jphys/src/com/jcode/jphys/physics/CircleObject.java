package com.jcode.jphys.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class CircleObject extends BaseBoxObject {
	
	

	public CircleObject(Vector2 pos, World world, int boxIndex,
			int collisionGroup, BodyType bodyType, float angle, Texture texture) {
		super(pos, world, boxIndex, collisionGroup, bodyType, angle, texture);
		// TODO Auto-generated constructor stub
	}
	
	public void setFixture(float radius, float density, float friction,
			float restitution) {
	
		CircleShape shape = new CircleShape();
		shape.setRadius(radius);

		FixtureDef fd = new FixtureDef();
		fd.density = density;
		fd.friction = friction;
		fd.restitution = restitution;
		fd.shape = shape;
		super.setFixture(fd);
		
		this.sprite.setSize(radius * 2, radius * 2);
		this.sprite.setOrigin(radius, radius);

	}

}
