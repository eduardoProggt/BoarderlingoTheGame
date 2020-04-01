package boarderlingothegame.sprites;

import java.awt.Image;
import java.awt.Point;

public interface VisibleGrafix {
	Image getImage(int counterVariable);
	/**
	 * getLocation sollte, soweit möglich dein am weitesten links oben liegenden Punkt zurückgeben
	 * 
	 * @return Point in Screen Coordinates
	 */
	Point getLocation();
}
