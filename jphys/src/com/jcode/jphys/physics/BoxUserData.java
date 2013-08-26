package com.jcode.jphys.physics;

public class BoxUserData {
	int collisionGroup;
	int boxId;

	public BoxUserData(int boxid, int collisiongroup) {
		set(boxid, collisiongroup);
	}

	public void set(int boxid, int collisiongroup) {
		this.boxId = boxid;
		this.collisionGroup = collisiongroup;
	}

	public int getBoxId() {
		return this.boxId;
	}

	public int getCollisionGroup() {
		return this.collisionGroup;
	}

}
