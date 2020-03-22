package boarderlingothegame;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;

public class Pipe extends Opstacle{

	int speed;
	
	Pipe(){
		location = new Point(2000,500);
		speed  = 20;
	}
	
	private Point location;
	
	@Override
	public Point getLocation() {
		return location;
	}

	@Override
	public Polygon getHitBox() {
		Polygon retPol = new Polygon();
		retPol.addPoint(getLocation().x, getLocation().y);
		retPol.addPoint(getLocation().x+130, getLocation().y);
		retPol.addPoint(getLocation().x+130, getLocation().y+200);
		retPol.addPoint(getLocation().x, getLocation().y+200);
		return retPol;
	}

	@Override
	public Image getImage(int unused) {
		return GfxLoader.pipe;
	}

	@Override
	public void moveRight(int speed) {
		getLocation().x = getLocation().x -speed;
	}

}
