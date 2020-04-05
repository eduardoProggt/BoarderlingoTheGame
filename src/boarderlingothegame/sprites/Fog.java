package boarderlingothegame.sprites;

import java.awt.Image;
import java.awt.Point;

import boarderlingothegame.AnimationTimer;

public class Fog implements VisibleGrafix {

	
	public Fog() {
		AnimationTimer.getInstance().startAnimation("NEBEL");
	}
	
	@Override
	public Image getImage(int counterVariable) {
		return GfxLoader.nebel;
	}

	@Override
	public Point getLocation() {
		AnimationTimer animationTimer = AnimationTimer.getInstance();
		if(animationTimer.getFrame("NEBEL") != null) {
			int nebelTime = animationTimer.getFrame("NEBEL").intValue();
			if(nebelTime > 550)
				return new Point( 500+(nebelTime-550)*25,0);
			else if(nebelTime<=550 && nebelTime >= 50) {
				return new Point( 500,0);
			}
			else if(nebelTime >= 0){
				return new Point( 500+(50-nebelTime)*25,0);
			}
		}
		return null;
	}
	public boolean stillRunning() {
		Integer frame = AnimationTimer.getInstance().getFrame("NEBEL");
		if(frame == null)
			return false;
		return frame.intValue() > 0;
	}
	public void resetFogTime() {
		AnimationTimer.getInstance().resetToFrame("NEBEL",50);
	}

}
