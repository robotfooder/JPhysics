package com.jcode.jphys.physics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.jcode.jphys.JPhys;

public class BoxObjectManager {

	ArrayList<BaseBoxObject> rectObjects;
	ArrayList<BaseBoxObject> circleObjects;
	ArrayList<BaseBoxObject> customObjects;
	World world;

	public BoxObjectManager(Vector2 gravity) {
		world = new World(gravity, true);
		rectObjects = new ArrayList<BaseBoxObject>();
		circleObjects = new ArrayList<BaseBoxObject>();
		customObjects = new ArrayList<BaseBoxObject>();

	}

	public BaseBoxObject AddObject(Vector2 pos, int objType, int boxIndex,
			BodyType bodyType, float angle, Texture texture, int collisionGroup) {
		BaseBoxObject temp;
		// CREATE different objects depending on the type considering we have to
		// child classes
		// Object1 and Object2 inheriting the base class BaseBoxObject
		switch (objType) {
		case JPhys.RECT_OBJECT:
			temp = new RectObject(pos, world, boxIndex, collisionGroup,
					bodyType, angle, texture, objType);
			rectObjects.add(temp);
			break;
		case JPhys.CIRCLE_OBJECT:
			temp = new CircleObject(pos, world, boxIndex, collisionGroup,
					bodyType, angle, texture, objType);
			circleObjects.add(temp);
			break;
		default:
			temp = new CustomObject(pos, world, boxIndex, collisionGroup,
					bodyType, angle, texture, objType);
			customObjects.add(temp);

		}

		return temp;
	}

	public void Draw(SpriteBatch sp) {
		for (BaseBoxObject obj : rectObjects) {
			obj.draw(sp);
		}
		for (BaseBoxObject obj : circleObjects) {
			obj.draw(sp);
		}
		for (BaseBoxObject obj : customObjects) {
			obj.draw(sp);
		}
	}

	public void Update() {

		for (BaseBoxObject obj : rectObjects) {
			obj.update();
		}
		for (BaseBoxObject obj : circleObjects) {
			obj.update();
		}
		for (BaseBoxObject obj : customObjects) {
			obj.update();
		}

	}

	public void stepWorld(float timeStep, int velocityIterations,
			int positionIterations) {
		this.world.step(timeStep, velocityIterations, positionIterations);

	}

	public void dispose() {
		world.dispose();
	}
}
