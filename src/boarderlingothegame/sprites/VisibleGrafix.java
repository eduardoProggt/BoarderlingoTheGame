package boarderlingothegame.sprites;

import java.awt.Point;

import glgfxinterface.Tile;

public interface VisibleGrafix {
	Tile getTile(int counterVariable);
	/**
	 * getLocation sollte, soweit m�glich den am weitesten links oben liegenden Punkt zur�ckgeben
	 * 
	 * @return Point in Screen Coordinates
	 */
	Point getLocation();
	
	
}
