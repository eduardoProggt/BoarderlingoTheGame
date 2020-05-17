package boarderlingothegame.sprites;

import java.awt.Point;
import java.awt.image.BufferedImage;

public interface VisibleGrafix {
	BufferedImage getImage(int counterVariable);
	/**
	 * getLocation sollte, soweit m�glich den am weitesten links oben liegenden Punkt zur�ckgeben
	 * 
	 * @return Point in Screen Coordinates
	 */
	Point getLocation();
	
	
}
