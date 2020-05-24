package boarderlingothegame.sprites;

import java.awt.image.BufferedImage;
import java.awt.Point;

import boarderlingothegame.AnimationTimer;
import glgfxinterface.Tile;

public class Fog implements VisibleGrafix {

	Tile image = new Tile("src\\boarderlingothegame\\gfx\\Nebel.png",1620,751);
	
	public Fog() {
		AnimationTimer.getInstance().startAnimation("NEBEL");
	}
	

	@Override
	public Point getLocation() {
		AnimationTimer animationTimer = AnimationTimer.getInstance();
		if(animationTimer.getFrame("NEBEL") != null) {
			int nebelTime = animationTimer.getFrame("NEBEL").intValue();
			
			if(nebelTime > 550 && nebelTime<=610)
				return new Point( 500+(nebelTime-550)*25,0);
			
			if(nebelTime<=550 && nebelTime >= 50) 
				return new Point( 500,0);
			
			if(nebelTime >= 0)
				return new Point( 500+(50-nebelTime)*25,0);
			if(nebelTime >610)
				return null;
		}
		return null;
	}
//	public boolean stillRunning() {
//		Integer frame = AnimationTimer.getInstance().getFrame("NEBEL");
//		if(frame == null)
//			return false;
//		return frame.intValue() > 0;
//	}
	public void resetFogTime() {
		AnimationTimer.getInstance().resetToFrame("NEBEL",50);
	}


	@Override
	public Tile getTile(int counterVariable) {
		return image;
	}

}
