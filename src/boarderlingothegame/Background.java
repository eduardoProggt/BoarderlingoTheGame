package boarderlingothegame;

import java.awt.Image;
import java.awt.Point;

import boarderlingothegame.sprites.GfxLoader;
import boarderlingothegame.sprites.VisibleGrafix;

public class Background implements VisibleGrafix {

	private Point location;
	private BGState state = BGState.REPEATING;
	
	private String backgroundState;
	
	public final String STADT = "Stadt";
	public final String FELD = "Feld";
	
	public Background() {
		location = new Point();
		backgroundState = STADT;
	}

	@Override
	public Image getImage(int counterVariable) {
		if(backgroundState.equals(STADT))
		    return GfxLoader.background;
		else {
			return GfxLoader.feldBG;
		}
	}

	public Point getLocation() {
		if(firstLayerOutOfVisibleFrame()) {
			location.x =  -getBGImageBegin();
			if(state.equals(BGState.INTRANSITION))
				if(backgroundState == STADT)
					backgroundState = FELD;
				else if(backgroundState == FELD)
					backgroundState = STADT;
			state = BGState.REPEATING;
			} else if(state.equals(BGState.GOINGTOCHANGE))
				state = BGState.INTRANSITION;
		return location;
	}

	private boolean firstLayerOutOfVisibleFrame() {
		return -location.x > getBGImageLaenge();
	}

	public void setLocation(Point location) {
		this.location = location;
	}
	public void transition() {
		if(state == BGState.REPEATING)
			state = BGState.GOINGTOCHANGE;
		return;
	}
	public void scroll(int pixelsLeft) {
		getLocation().x -= pixelsLeft; 
	}
	private int getBGImageBegin(){
		if(backgroundState.equals(STADT))
			return 0;
		else {
			return 1100;
		}
	}
	private int getBGImageLaenge(){
		if(backgroundState.equals(STADT))
			return 5000;
		else {
			return 5000;
		}
	}

	public int getRepeatLocation() {
		if(!state.equals(BGState.INTRANSITION)) {

			return(getBGImageLaenge() + getLocation().x) -getBGImageBegin();
		}
		else
			return getBGImageLaenge() + getLocation().x;
	}

	public Image getRepeatImage(int i) {
		if(!state.equals(BGState.INTRANSITION))
			return getImage(i);
		else
			return hässlicheMockFunktionDieNochAusgewechseltWird();
	}

	private Image hässlicheMockFunktionDieNochAusgewechseltWird() {
		if(backgroundState.equals(STADT))
		    return GfxLoader.feldBG;
		else {
			return GfxLoader.background;
		}
	}

}
enum BGState{
	REPEATING,
	GOINGTOCHANGE,
	INTRANSITION;
}
