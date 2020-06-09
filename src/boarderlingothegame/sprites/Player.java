package boarderlingothegame.sprites;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

import boarderlingothegame.AnimationTimer;
import glgfxinterface.Tile;

public class Player implements Collidable,VisibleGrafix {

	private double x,y, horizontalMomentum;
	
	
	private PlayerStateEnum state;
	
	private int speedRight, bullets;
	private final int SPRUNGKRAFT = 20;

	private Tile idleRight1 = new Tile("src\\boarderlingothegame\\gfx\\blingo_right.png",350,600); 
	private Tile idleRight2= new Tile("src\\boarderlingothegame\\gfx\\blingo_right2.png",350,600); 
	private Tile brake1= new Tile("src\\boarderlingothegame\\gfx\\bremsen1.png",350,600); 
	private Tile brake2= new Tile("src\\boarderlingothegame\\gfx\\bremsen2.png",350,600); 
	private Tile ducking = new Tile("src\\boarderlingothegame\\gfx\\blingo_ducking.png",350,600);
	private Tile jumping = new Tile("src\\boarderlingothegame\\gfx\\blingo_jump.png",350,600);
	
	public Player(){
		bullets= 3;
		setX(10);
		setY(20);
		setState(PlayerStateEnum.IDLE);
		setSpeedRight(8);
	}

	public boolean isInAir() {
		return getState().equals(PlayerStateEnum.JUMPING) 
		    || getState().equals(PlayerStateEnum.FALLING);
	}
	
	@Override
	public Polygon getHitBox() {
		
		Polygon retPoly = new Polygon();
		int x = (int)getX();
		int y = (int)getY();
		if(!isInAir()) {
			if(getState().equals(PlayerStateEnum.DUCKING))
			{//TODO nochmal ordentlich
				retPoly.addPoint(x, y+135);
				retPoly.addPoint(x+350, y+135);
				retPoly.addPoint(x+350, y+500);
				retPoly.addPoint(x, y+500);
			}
			else {
				retPoly.addPoint(x+220, y+30);
				retPoly.addPoint(x+280, y+30);
				retPoly.addPoint(x+280, y+135);
				retPoly.addPoint(x+350, y+150);
				retPoly.addPoint(x+350, y+280);
				retPoly.addPoint(x+275, y+500);
				retPoly.addPoint(x+350, y+530);
				retPoly.addPoint(x+350, y+600);
				retPoly.addPoint(x, y+600);
				retPoly.addPoint(x, y+135);
				retPoly.addPoint(x+165, y+135);
				retPoly.addPoint(x+165, y+80);
			}
		}
		else {
			retPoly.addPoint(x+220, y+30);
			
			retPoly.addPoint(x+280, y+30);
			retPoly.addPoint(x+280, y+135);
			retPoly.addPoint(x+350, y+150);//Rechter ellenbogen
			retPoly.addPoint(x+350, y+260);
			retPoly.addPoint(x+337, y+480);//Vorderrad
			retPoly.addPoint(x+305, y+495);
			retPoly.addPoint(x+250, y+455);
			retPoly.addPoint(x+105, y+505);
			retPoly.addPoint(x+110, y+550);
			retPoly.addPoint(x+78, y+570);
			retPoly.addPoint(x+60, y+560);
			retPoly.addPoint(x+50, y+520);
			retPoly.addPoint(x+10, y+530);
			retPoly.addPoint(x+0, y+520);//Linke Fuﬂspitze
			retPoly.addPoint(x+0, y+360);
			retPoly.addPoint(x+55, y+135);
			retPoly.addPoint(x+170, y+135);
			retPoly.addPoint(x+170, y+70);
		}
		return retPoly;
	}
	@Override
	public Point getLocation() {
		return new Point((int)getX(),(int)getY());
	} 

	public Tile getTile(int counterVariable) {
		if(getState().equals(PlayerStateEnum.IDLE)) {
		if (counterVariable % 3 == 0 | counterVariable % 5 == 0)
			return idleRight1;
		else
			return idleRight2;
		}
		if(isInAir())
			return jumping;
		if(getState().equals(PlayerStateEnum.DUCKING))
			return ducking;	
		if(getState().equals(PlayerStateEnum.BRAKING)) {
			if (counterVariable % 3 == 0 | counterVariable % 5 == 0)
				return brake1; // set image
			else
				return brake2;
		}
		
		return idleRight1; //Default
	}
	
	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y =y;
	}
	public PlayerStateEnum getState() {
		return state;
	}

	private void setState(PlayerStateEnum state) {
		this.state = state;
	}

	public int getSpeedRight() {
			
		if(!isBoosting()) {
			if(getState().equals(PlayerStateEnum.BRAKING))
				return speedRight/2;
			return speedRight;
		}
		else{
			if(getState().equals(PlayerStateEnum.BRAKING))
				return (int)(2f*(float)speedRight/3f);
			return speedRight*2;
		}
	}

	public boolean isBoosting() {
		return AnimationTimer.getInstance().getFrame("SCHNELLER") < 300;
	}

	public void setSpeedRight(int speedRight) {
		this.speedRight = speedRight;
	}

	public double getHorizontalMomentum() {
		return horizontalMomentum;
	}

	public void setHorizontalMomentum(int horizontalMomentum) {
		this.horizontalMomentum = horizontalMomentum;
	}
	public void decreaseHorizontalMomentum(double diff) {
		horizontalMomentum -= diff;
	}

	public void applyHorizontalMomentum(double momentum) {
		horizontalMomentum += momentum;
		
	}

	public void jump() {
		if (getState() == PlayerStateEnum.JUMPING) {
		
			setState(PlayerStateEnum.FALLING);
			setHorizontalMomentum(-SPRUNGKRAFT/2);
		}
		if(getState() == PlayerStateEnum.IDLE) {
			setState(PlayerStateEnum.JUMPING);
			setHorizontalMomentum(-SPRUNGKRAFT);
		}
	}
	public void calcJumpFrame() {
		int bodenhoehe = 20;
		setY(getY()+getHorizontalMomentum());
		decreaseHorizontalMomentum(-0.55);
		
		if(getY()>bodenhoehe) {
			setY(bodenhoehe);
			setHorizontalMomentum(0);
			setState(PlayerStateEnum.IDLE);			
		}
	}

	@Override
	public String getNameAsString() {
		// TODO Noch braucht der Player keinen namen. Hier sehen Sie eine Verletzung des YAGNI
		return "Robin Nosterafuuh";
	}

	public void duck() {
		if(!isInAir())
			setState(PlayerStateEnum.DUCKING);
	}

	public void brake() {
		if(!isInAir())
			setState(PlayerStateEnum.BRAKING);
	}

	public void resetState() {
		if(!isInAir())
			setState(PlayerStateEnum.IDLE);
		
	}

	public Bullet shoot() {
		if(getBullets() < 1)
			return null;
		bullets = getBullets() - 1;
		return new Bullet();
	}

	public void boost() {
		AnimationTimer.getInstance().startAnimation("SCHNELLER");
		
	}

	public int getBullets() {
		return bullets;
	}
	public void resetBullets() {
		bullets = 3;
	}
}
