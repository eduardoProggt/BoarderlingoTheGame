package boarderlingothegame.sprites;

import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Obstacle implements Collidable, VisibleGrafix {
	
	public Obstacle(String _spawnedBy) {
		spawnedBy = _spawnedBy;
	}
	
	private String spawnedBy;
	protected Point location;
	public abstract void moveRight(int speed);
	public String getSpawnedBy() {
		return spawnedBy;
	}
	
}
