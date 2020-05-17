package boarderlingothegame.sprites;

import java.awt.Point;
import java.awt.image.BufferedImage;

public interface VisibleGrafix {
	BufferedImage getImage(int counterVariable);
	/**
	 * getLocation sollte, soweit möglich den am weitesten links oben liegenden Punkt zurückgeben
	 * 
	 * @return Point in Screen Coordinates
	 */
	Point getLocation();
	
	
}
