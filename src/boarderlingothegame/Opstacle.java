package boarderlingothegame;

import java.awt.Image;
import java.awt.Rectangle;

public abstract class Opstacle implements Collidable, VisibleGrafix {
	int speed;
	
	public abstract Image getImage(int frame);
	public abstract void moveRight();
	
}
