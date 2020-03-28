package boarderlingothegame.sprites;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;

public class Granny extends Obstacle {

	public Granny(String _spawnedBy) {
		super(_spawnedBy);
		location = new Point(location = new Point(1600,200));
	}

	@Override
	public Point getLocation() {
		
		return location;
	}

	@Override
	public Polygon getHitBox() {
		Polygon retPol = new Polygon();
		int x = (int)getLocation().getX();
		int y = (int)getLocation().getY();
		if(getLocation().y<300) {
		retPol.addPoint(x, y);
		retPol.addPoint(x+50, y);
		retPol.addPoint(x+50, y+300);
		retPol.addPoint(x, y+300);}
		return retPol;
	}

	@Override
	public Image getImage(int frame) {
		if(frame%8<4 && getLocation().x<1000)
			return GfxLoader.oma1;
		else
			return GfxLoader.oma2;
	}

	@Override
	public void moveRight(int speed) {
		location.x =location.x-speed; 

	}

	public void moveDown() {
		if(getLocation().x<1000)
		location.y =location.y+3; 
		
	}

}
