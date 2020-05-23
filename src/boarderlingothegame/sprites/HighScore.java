package boarderlingothegame.sprites;

import glgfxinterface.Tile;

public class HighScore {
	private int score;
	private int highscore;
	
	private static Tile image = new Tile("src\\boarderlingothegame\\gfx\\TexturFuerHighscore.png",1620,751);

	public void resetToZero() {
		score = 0;
		highscore = 0;
	}
	public void reset() {
		if(highscore < score)
		highscore = score;
		score = 0;
	}
	public void increment() {
		score++;
	}
	public int getHighscore() {
		return highscore;
	}
	public int getScore() {
		return score;
	}
	public static Tile getImage() {
		return image;
	}
	
}
