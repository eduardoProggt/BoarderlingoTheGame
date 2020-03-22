package boarderlingothegame;

import java.awt.Image;
import java.awt.Point;

public class Background implements VisibleGrafix {

	private Point location;
	
	public Background(int x, int y) {
		location = new Point(x,y);
	}

	@Override
	public Image getImage(int counterVariable) {
		return GfxLoader.background;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

}