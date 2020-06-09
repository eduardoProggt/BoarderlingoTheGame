package boarderlingothegame.sprites;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import glgfxinterface.Tile;

public class Bullet implements Collidable, VisibleGrafix {

	Point location;
	public static Tile image = new Tile("src\\boarderlingothegame\\gfx\\Bullet.png", 20, 10);
	
	public Bullet() {
		location = new Point(400,500);
	}
	
	@Override
	public Polygon getHitBox() {
		Polygon ret = new Polygon();
		ret.addPoint(getLocation().x, getLocation().y);
		ret.addPoint(getLocation().x+20, getLocation().y);
		ret.addPoint(getLocation().x+20, getLocation().y+10);
		ret.addPoint(getLocation().x, getLocation().y+10);
		return ret;
	}

	@Override
	public Point getLocation() {
		return location;
	}
	public void move() {
		getLocation().x = getLocation().x+15;
	}

	@Override
	public String getNameAsString() {
		return "Geschoss";
	}

	@Override
	public Tile getTile(int counterVariable) {
		return image;
	}

}
