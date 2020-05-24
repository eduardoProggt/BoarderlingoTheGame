package boarderlingothegame;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

import boarderlingothegame.sprites.VisibleGrafix;
import glgfxinterface.Tile;

public class Background implements VisibleGrafix {

	private Point location;
	
	private String backgroundState;
	
	public final String STADT = "Stadt";
	public final String FELD = "Feld";
	private Tile stadtBGTile = new Tile("src\\boarderlingothegame\\backgrounds\\wielandstraﬂe.png",5000,750);
	private Tile feldBGTile = new Tile("src\\boarderlingothegame\\backgrounds\\feldBG.png",3900,750); 
	private Tile stadtFeldTransitionBGTile = new Tile("src\\boarderlingothegame\\backgrounds\\StadtWaldTransition.png",1100,750); 
	
	private Queue<Tile> currentTiles = new LinkedList<>();
	
	public Background() {
		location = new Point();
		getCurrentTiles().offer(stadtBGTile);
		backgroundState = STADT;
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
			addNewBGTileToQueue();
		if(getLocation().x + currentTiles.peek().getWidth() < 0) {
			getLocation().x+=currentTiles.peek().getWidth();
			currentTiles.poll();
		}
	}

	private void addNewBGTileToQueue() {
		if(backgroundState.equals(STADT))
			currentTiles.add(stadtBGTile);
		if(backgroundState.equals(FELD))
			currentTiles.add(feldBGTile);
	}
	private int getLengthOfAllCurrentTilesInPx() {
		int retVal = 0;
		for (Tile eTile : getCurrentTiles()) {
			retVal += eTile.getWidth();
		}
		return retVal;
	}

	public Point getLocation() {
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

	public void change() {
		if(backgroundState.equals(STADT)) {
			currentTiles.add(stadtFeldTransitionBGTile);
			backgroundState = FELD;
		}
		else {
			backgroundState= STADT;
		}
	}
}

