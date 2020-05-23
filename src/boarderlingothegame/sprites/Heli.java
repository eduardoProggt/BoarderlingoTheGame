package boarderlingothegame.sprites;

import glgfxinterface.Tile;

import java.awt.Point;
import java.awt.Polygon;

public class Heli extends Obstacle {

	float speedOffset;
	private static Tile image = new Tile("src\\boarderlingothegame\\gfx\\Heli.png", 150, 100);
	
	public Heli(String spawnedBy){
		super(spawnedBy);
		location = new Point(2200,50);
		speedOffset = 3.5f;
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
	public void moveRight(int speed) {
		int absoluteSpeed = (int)((float)speed + speedOffset);
		getLocation().x = getLocation().x -absoluteSpeed;
		
	}

	@Override
	public String getNameAsString() {

		return "Heli";
	}

	@Override
	public Tile getTile(int counterVariable) {
		return image;
	}



}
