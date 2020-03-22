package boarderlingothegame;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;

public class Heli extends Opstacle {

	int speed;
	
	Heli(){
		location = new Point(2000,100);
		speed = 10;
	}
	
	private Point location;
	
	
	@Override
	public Point getLocation() {
		return location;
	}

	@Override
	public Polygon getHitBox() {
		Polygon retPol = new Polygon();
		
		retPol.addPoint(getLocation().x,getLocation().y);
		retPol.addPoint(getLocation().x+150,getLocation().y);
		retPol.addPoint(getLocation().x+150,getLocation().y+100);
		retPol.addPoint(getLocation().x,getLocation().y+100);
		
		return retPol;
	}

	@Override
	public Image getImage(int unused) {
		return GfxLoader.heli;
	}

	@Override
	public void moveRight() {
		getLocation().x = getLocation().x -speed;
		
	}



}