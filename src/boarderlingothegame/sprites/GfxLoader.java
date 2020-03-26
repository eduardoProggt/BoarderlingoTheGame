package boarderlingothegame.sprites;

import java.awt.Image;

import javax.swing.ImageIcon;

public class GfxLoader {
	final static String PREFIX = "src\\boarderlingothegame\\gfx\\";
	public static Image background = new ImageIcon("src\\boarderlingothegame\\backgrounds\\wielandstraﬂe.png").getImage(); 
	public static Image idle_right1 = new ImageIcon(PREFIX+"blingo_right.png").getImage(); 
	public static Image idle_right2 = new ImageIcon(PREFIX+"blingo_right2.png").getImage(); 
	public static Image bremsen1 = new ImageIcon(PREFIX+"bremsen1.png").getImage(); 
	public static Image bremsen2 = new ImageIcon(PREFIX+"bremsen2.png").getImage(); 
	public static Image jump = new ImageIcon(PREFIX+"blingo_jump.png").getImage(); 
	public static Image pipe = new ImageIcon(PREFIX+"pipe.png").getImage(); 
	public static Image heli = new ImageIcon(PREFIX+"Heli.png").getImage(); 
	public static Image nebel = new ImageIcon(PREFIX+"Nebel.png").getImage(); 
	
	public static Image player_ducking = new ImageIcon(PREFIX+"blingo_ducking.png").getImage(); 
}


