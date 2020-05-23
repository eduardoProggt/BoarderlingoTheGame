package boarderlingothegame.sprites;

import java.awt.image.BufferedImage;

import glgfxinterface.Tile;

import java.awt.Point;
import java.awt.Polygon;

public class Granny extends Obstacle {

	private static Tile oma1 = new Tile("src\\boarderlingothegame\\gfx\\Oma1.png",280,515); 
	private static Tile oma2 = new Tile("src\\boarderlingothegame\\gfx\\Oma2.png",280,515); 
	private boolean isMovingDown= false;
	
	public Granny(String _spawnedBy) {
		super(_spawnedBy);
		location = new Point(location = new Point(2200,50));
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
	public void moveRight(int speed) {
		location.x =location.x-speed; 
		if(getLocation().x<1000)
			isMovingDown = true;
		if(isMovingDown)
			moveDown();//TODO meeh...
	}

	public void moveDown() {
		
		location.y =location.y+3; 
	}

	@Override
	public String getNameAsString() {
		return "Oma";
	}

	@Override
	public Tile getTile(int counterVariable) {
		return (counterVariable/8)%2 == 0 && isMovingDown ? oma1:oma2;
	}

}
