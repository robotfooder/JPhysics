package com.jcode.jphys;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Path{
	   ArrayList<Vector2>  positions;
	   ArrayList<Float> times;
	   Vector2 velocity;
	   int currentPointIndex;
	   int nextPointIndex;
	   int direction=1;
	   static final float CHECK_RADIUS=5f;
	   public Path(int count){
	      positions=new ArrayList<Vector2>();
	      times=new ArrayList<Float>();
	      velocity=new Vector2();
	   }
	   public void addPoint(Vector2 pos,float time){
	      positions.add(pos);
	      
	      times.add(time);
	    }
	   public void reset(){
	      currentPointIndex=0;
	      nextPointIndex=getNextPoint();
	      setNextPointVelocity();
	   }

	   public Vector2 getCurrentPoint(){
		   return positions.get(currentPointIndex);
	   }
	   
	   public boolean updatePath(Vector2 bodyPosition){
	      return reachedNextPoint(bodyPosition);
	   } 

	   boolean reachedNextPoint(Vector2 bodyPosition){
	         Vector2 nextPointPosition=positions.get(nextPointIndex);
	         float d=nextPointPosition.dst2(bodyPosition);
	         if(d<CHECK_RADIUS){
	            currentPointIndex=nextPointIndex;
	            nextPointIndex=getNextPoint();
	        	setNextPointVelocity();
	            return true;
	         }
	         return false;
	   } 
	   int getNextPoint(){
	         int nextPoint=currentPointIndex+direction;
	         if(nextPoint==positions.size()){
	             nextPoint=0;
	         }else if(nextPoint==-1){
	             nextPoint=positions.size()-1;
	         }
	         return nextPoint;
	   } 

	   void setNextPointVelocity(){
	        Vector2 nextPosition=positions.get(nextPointIndex);
	        Vector2 currentPosition=positions.get(currentPointIndex);
	        float dx=nextPosition.x-currentPosition.x;
	        float dy=nextPosition.y-currentPosition.y;
	        float time=times.get(nextPointIndex);
	        velocity.set(dx/time,dy/time);
	   }
	   public Vector2 getVelocity(){
	        return velocity;
	   }
	}