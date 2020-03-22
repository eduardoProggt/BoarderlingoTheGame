package boarderlingothegame;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;

public class Player implements Collidable,VisibleGrafix {
	private final int DEFAULT_SPEED = 16; 
	
	private int x,y;
	
	private PlayerStateEnum state;
	private int speedRight;

	Player(){
		setX(150);
		setY(80);
		setState(PlayerStateEnum.IDLE);
		setSpeedRight(DEFAULT_SPEED);
	}

	boolean isInAir() {
		return getState().equals(PlayerStateEnum.JUMPING) 
		    || getState().equals(PlayerStateEnum.FALLING);
	}
	
	@Override
	public Polygon getHitBox() {
		
		Polygon retPoly = new Polygon();
		if(!isInAir()) {
			if(getState().equals(PlayerStateEnum.DUCKING))
			{//TODO nochmal ordentlich
				retPoly.addPoint(x, y+135);
				retPoly.addPoint(x+350, y+135);
				retPoly.addPoint(x+350, y+500);
				retPoly.addPoint(x, y+500);
			}
			else {
				retPoly.addPoint(x, y+30);
				retPoly.addPoint(x+350, y+30);
				retPoly.addPoint(x+350, y+280);
				retPoly.addPoint(x+275, y+500);
				retPoly.addPoint(x+350, y+530);
				retPoly.addPoint(x+350, y+600);
				retPoly.addPoint(x, y+600);
			}
		}
		else {
			retPoly.addPoint(x, y+30);
			retPoly.addPoint(x+350, y+30);
			retPoly.addPoint(x+350, y+260);
			retPoly.addPoint(x+337, y+480);
			retPoly.addPoint(x+78, y+570);
			retPoly.addPoint(x+0, y+520);
		}
		return retPoly;
	}
	@Override
	public Point getLocation() {
		return new Point(getX(),getY());
	} 
	@Override
	public Image getImage(int counterVariable) {
		if(getState().equals(PlayerStateEnum.IDLE)) {
		if (counterVariable % 3 == 0 | counterVariable % 5 == 0)
			return GfxLoader.idle_right1;
		else
			return GfxLoader.idle_right2;
		}
		if(isInAir())
			return GfxLoader.jump;
		if(getState().equals(PlayerStateEnum.DUCKING))
			return GfxLoader.player_ducking;	
		if(getState().equals(PlayerStateEnum.BRAKING)) {
			if (counterVariable % 3 == 0 | counterVariable % 5 == 0)
				return GfxLoader.bremsen1; // set image
			else
				return GfxLoader.bremsen2;
		}
		
		return  GfxLoader.idle_right1; //Default
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	public PlayerStateEnum getState() {
		return state;
	}

	public void setState(PlayerStateEnum state) {
		this.state = state;
	}

	public int getSpeedRight() {
		return speedRight;
	}

	public void setSpeedRight(int speedRight) {
		this.speedRight = speedRight;
	}

}
