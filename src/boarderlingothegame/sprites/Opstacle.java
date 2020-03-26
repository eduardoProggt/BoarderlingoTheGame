package boarderlingothegame.sprites;

import java.awt.Image;
import java.awt.Rectangle;

public abstract class Opstacle implements Collidable, VisibleGrafix {
	
	public Opstacle(String _spawnedBy) {
		spawnedBy = _spawnedBy;
	}
	
	private String spawnedBy;
	
	public abstract Image getImage(int frame);
	public abstract void moveRight(int speed);
	public String getSpawnedBy() {
		return spawnedBy;
	}
	
}
