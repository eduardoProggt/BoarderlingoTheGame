package boarderlingothegame.sprites;

import java.awt.Point;
import java.awt.Polygon;

public interface Collidable {
	/**
	 * getHitBox ist einfach die Fl�che, bei deren Eintritt eine Kollision des eintretenden Objektes getriggert wird
	 * 
	 * @return Polygon
	 */
	Polygon getHitBox();
	String getNameAsString();
}
