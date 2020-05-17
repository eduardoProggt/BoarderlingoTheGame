package boarderlingothegame.sprites;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

public class Bullet implements Collidable, VisibleGrafix {

	Point location;
	
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
	public BufferedImage getImage(int counterVariable) {
		return GfxLoader.bullet;
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

}
