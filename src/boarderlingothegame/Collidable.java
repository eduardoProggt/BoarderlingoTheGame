package boarderlingothegame;

import java.awt.Point;
import java.awt.Polygon;

public interface Collidable {
	/**
	 * getLocation sollte, soweit möglich dein am weitesten links oben liegenden Punkt zurückgeben
	 * 
	 * @return Point in Screen Coordinates
	 */
	Point getLocation();
	/**
	 * getHitBox ist einfach die Fläche, bei deren Eintritt eine Kollision des eintretenden Objektes getriggert wird
	 * 
	 * @return Polygon
	 */
	Polygon getHitBox();
}
