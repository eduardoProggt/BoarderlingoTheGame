package boarderlingothegame.sprites;

import java.awt.Point;

import glgfxinterface.Tile;

public interface VisibleGrafix {
	Tile getTile(int counterVariable);
	/**
	 * getLocation sollte, soweit möglich den am weitesten links oben liegenden Punkt zurückgeben
	 * 
	 * @return Point in Screen Coordinates
	 */
	Point getLocation();
	
	
}
