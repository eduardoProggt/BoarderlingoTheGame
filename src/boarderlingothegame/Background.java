package boarderlingothegame;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.awt.Point;

import boarderlingothegame.sprites.VisibleGrafix;
import java.util.Queue;
import glgfxinterface.Tile;

public class Background implements VisibleGrafix {

	private Point location;
	
	private String backgroundState;
	
	public final String STADT = "Stadt";
	public final String FELD = "Feld";
	private Tile background = new Tile("src\\boarderlingothegame\\backgrounds\\wielandstraﬂe.png",5000,750);
	private Tile feld = new Tile("src\\boarderlingothegame\\backgrounds\\feldBG.png",0,0); 
	
	private Queue<Tile> currentTiles = new LinkedList<>();
	
	public Background() {
		location = new Point();
//		backgroundState = STADT;
	}

	public Queue<Tile> getCurrentTiles(){
		
		return currentTiles;
	}
	public void move(int speed) {
		getLocation().x-=speed;
		updateBackground();
	}
	private void updateBackground() {
		if(getLocation().x + getLengthOfAllCurrentTilesInPx() < 1900/*window-width*/)
			currentTiles.add(background);//oder was auch immer
		if(getLocation().x + currentTiles.peek().getWidth() < 0) {
			currentTiles.poll();
			getLocation().x+=currentTiles.peek().getWidth();
		}
	}
	private int getLengthOfAllCurrentTilesInPx() {
		int retVal = 0;
		for (Tile eTile : getCurrentTiles()) {
			retVal += eTile.getWidth();
		}
		return retVal;
	}

	public Point getLocation() {
//		if(firstLayerOutOfVisibleFrame()) {
//			location.x =  -getBGImageBegin();
//			if(state.equals(BGState.INTRANSITION))
//				if(backgroundState == STADT)
//					backgroundState = FELD;
//				else if(backgroundState == FELD)
//					backgroundState = STADT;
//			state = BGState.REPEATING;
//			} else if(state.equals(BGState.GOINGTOCHANGE))
//				state = BGState.INTRANSITION;
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public void scroll(int pixelsLeft) {
		getLocation().x -= pixelsLeft; 
	}

	@Override
	public Tile getTile(int counterVariable) {
		
		return getCurrentTiles().peek();
	}

}

