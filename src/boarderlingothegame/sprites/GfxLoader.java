package boarderlingothegame.sprites;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import glgfxinterface.Tile;

public class GfxLoader {


	
	
	
	
	final static String PREFIX = "src\\boarderlingothegame\\gfx\\";
	public static BufferedImage background = getImage("src\\boarderlingothegame\\backgrounds\\wielandstraﬂe.png"); 
	public static BufferedImage feldBG = getImage("src\\boarderlingothegame\\backgrounds\\feldBG.png");
	
	public static BufferedImage idle_right1 = getImage(PREFIX+"blingo_right.png"); 
	public static BufferedImage idle_right2 = getImage(PREFIX+"blingo_right2.png"); 
	public static BufferedImage bremsen1 = getImage(PREFIX+"bremsen1.png"); 
	public static BufferedImage bremsen2 = getImage(PREFIX+"bremsen2.png"); 
	public static BufferedImage jump = getImage(PREFIX+"blingo_jump.png"); 
	public static BufferedImage pipe = getImage(PREFIX+"pipe.png"); 
	public static BufferedImage heli = getImage(PREFIX+"Heli.png"); 
	public static BufferedImage nebel = getImage(PREFIX+"Nebel.png"); 
	public static BufferedImage oma1 = getImage(PREFIX+"Oma1.png"); 
	public static BufferedImage oma2 = getImage(PREFIX+"Oma2.png"); 
	public static BufferedImage bullet = getImage(PREFIX+"Bullet.png");
	
	public static BufferedImage player_ducking = getImage(PREFIX+"blingo_ducking.png");
	public static BufferedImage testBild = getImage(PREFIX+"TestbildnichtQ.png");

	private static BufferedImage getImage(String string) {

		try {
//			feldBG.get
			return ImageIO.read(new File(string));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	} 
}


