package com.jcode.jphys.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.jcode.jphys.Path;

public class PathObject extends RectObject {

	Path path;

	public PathObject(Vector2 pos, World world, int boxIndex,
			int collisionGroup, BodyType bodyType, float angle,
			Texture texture, int objectType) {
		super(pos, world, boxIndex, collisionGroup, bodyType, angle, texture,
				objectType);
	}

	public void setPath(Path p) {
		path = p;
		path.reset();
		setPosition(path.getCurrentPoint());
		setWorldVelocity(path.getVelocity());
		update();
	}

	@Override
	public void update() {

		super.update();
		if (path.updatePath(worldPosition))
			setWorldVelocity(path.getVelocity());

	}

}
