
package boarderlingothegame;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import boarderlingothegame.controller.ButtonsEnum;
import boarderlingothegame.controller.Controller;
import boarderlingothegame.controller.UserInputFassade;
import boarderlingothegame.sprites.Bullet;
import boarderlingothegame.sprites.Fog;
import boarderlingothegame.sprites.GfxLoader;
import boarderlingothegame.sprites.Obstacle;
import boarderlingothegame.sprites.Player;
import boarderlingothegame.sprites.PlayerStateEnum;
import boarderlingothegame.sprites.VisibleGrafix;
import glgfxinterface.GfxFassade;

class GamePanel extends JPanel implements ActionListener {

	Controller controller = new Controller();
	UserInputFassade userInput = new UserInputFassade(controller);
	Background background = new Background();
	Fog fog;
	Bullet bullet;
	int bullets;
	Player player;
	private boolean offline;
	
	private int highscore;
	private Timer time;

	/*package*/ GamePanel(boolean isOnTwitch) {
		setLayout(null);
		time = new Timer(30, this); // starting a timer and passing the
		time.start();		    	// actionlistener for the animation
		offline = !isOnTwitch;
	
		addKeyListener(userInput.getKeyListener());
		AnimationTimer.getInstance().startAnimation("SCORE");
		reset();
	}
	
	private void reset() {
		AnimationTimer animationTimer = AnimationTimer.getInstance();
		if(animationTimer.getFrame("SCORE")> highscore)
			highscore= animationTimer.getFrame("SCORE");
		animationTimer.startAnimation("SCORE");
		ObstacleController.getInstance().reset();
		player = new Player();
		controller.resetButtons();
		bullets = 3;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		autoScroll();
		
		if(controller.isPressed(ButtonsEnum.SPECIAL)) {
			highscore = 0;
		}
		
		if (controller.isPressed(ButtonsEnum.LEFT) && !player.isInAir())
			player.setState(PlayerStateEnum.BRAKING);
		else if (player.getState().equals(PlayerStateEnum.BRAKING))
			player.setState(PlayerStateEnum.IDLE);
		if(player.isInAir())
			player.calcJumpFrame(controller.isPressed(ButtonsEnum.SPACE));
		else if(controller.isPressed(ButtonsEnum.SPACE) && !player.getState().equals(PlayerStateEnum.DUCKING)) {
			player.jump();
			controller.resetButtons();
		}

		else if(controller.isPressed(ButtonsEnum.DOWN)) {
			player.setState(PlayerStateEnum.DUCKING);
		}
		else if(player.getState().equals(PlayerStateEnum.DUCKING)){
			player.setState(PlayerStateEnum.IDLE);
		}
		if(controller.isPressed(ButtonsEnum.SPACE) && player.getState().equals(PlayerStateEnum.DUCKING)) {
			System.out.println("noch "+ bullets+" Kugeln übrig");
			if(bullets>0 && (bullet == null || bullet.getLocation().x > 1000))//Noch Kugeln und noch keine da oder die letzte weit genug weg
			{
				bullet=new Bullet();
				bullets--;
			}
		}	
		ObstacleController obstCtlr = ObstacleController.getInstance();
		Obstacle collided = obstCtlr.collide(player.getHitBox());
		if(obstCtlr.collide(player.getHitBox()) != null) {
			JOptionPane.showMessageDialog(null,"Getötet von "+collided.getSpawnedBy()+"s "+collided.getNameAsString()+".");
			reset();
		}
		if(bullet!= null) {
			Obstacle shootObject = obstCtlr.collide(bullet.getHitBox());
			if(shootObject!= null) {
				obstCtlr.removeObject(shootObject);
				bullet = null;
			}
		}
		AnimationTimer.getInstance().increment();
	}
	void autoScroll() {
		//DIRTY!
		if(offline) 
			setRandomObstacles();
		int factor = 1;
		if(speedActivated())
			factor = 2;
		background.scroll(player.getSpeedRight()*factor);
		if(bullet != null)
			bullet.move();
		ObstacleController.getInstance().autoScroll(factor,player.getSpeedRight());
	}

	private boolean speedActivated() {
		return AnimationTimer.getInstance().getFrame("SPEED")<150;
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
        scaleGrafics(g2d);
		requestFocus();
		setFocusable(true);
		g2d.setFont(new Font(null, 2, 40));

		g2d.drawImage(background.getRepeatImage(0),background.getRepeatLocation() , 0, null);
		g2d.drawImage(background.getImage(0),background.getLocation().x, 0, null);
		g2d.drawImage(player.getImage(AnimationTimer.getInstance().getFrame()), (int)player.getX(), (int)player.getY(), this);
		if(bullet != null)
			g2d.drawImage(bullet.getImage(0),bullet.getLocation().x,bullet.getLocation().y,this);
		
		ObstacleController.getInstance().draw(g2d, this);//TODO, das ist seeeeehr hässlich
		
		if(fog != null && fog.getLocation() != null)
			g2d.drawImage(fog.getImage(0), fog.getLocation().x,0, this);
		
		g2d.drawString("Score: \n "+AnimationTimer.getInstance().getFrame("SCORE"), 0, 40);
		g2d.drawString("Highscore: \n "+highscore, 0, 80);

		for (int i = 0; i < bullets; i++) {
			g2d.drawImage(GfxLoader.bullet, 50, 100+ i*30, this);
		}
		
		repaint();
	}

	private void scaleGrafics(Graphics2D g2d) {
		AffineTransform at = new AffineTransform();
        at.scale(0.95, 0.95);
        g2d.setTransform(at);
	}


	private void setRandomObstacles() {
		Random wuerfel = new Random();
		int unwahrscheinlichkeitsfaktor = 40;
		if(wuerfel.nextInt() %(3*unwahrscheinlichkeitsfaktor) == 0)
			ObstacleController.getInstance().add("kaktus", "AUTO");
		if(wuerfel.nextInt() %(5*unwahrscheinlichkeitsfaktor) == 0)
			ObstacleController.getInstance().add("heli","AUTO");
		if(wuerfel.nextInt() %(20*unwahrscheinlichkeitsfaktor) == 0)
			speedUp();
		if(wuerfel.nextInt() %(30*unwahrscheinlichkeitsfaktor) == 0)
			setFog();
		if(wuerfel.nextInt() %(60*unwahrscheinlichkeitsfaktor) == 0)
			ObstacleController.getInstance().add("oma","AUTO");
	}


	public void speedUp() {
		AnimationTimer.getInstance().startAnimation("SPEED");
	}
	public void setFog() {
		if(fog != null && fog.stillRunning())
			fog.resetFogTime();
		else
			fog = new Fog();
	}

	public void addObstacle(String order, String nameOfPurchaser) {
		ObstacleController.getInstance().add(order, nameOfPurchaser);
	}

	public void transition() {
		background.transition();
	}
}

public class BoarderlingoTheGame extends JFrame {

	GamePanel gp = null;

	public BoarderlingoTheGame(boolean isOnTwitch) {
		if(true) {
		gp= new GamePanel(isOnTwitch);
		add(gp);
		setSize(1600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);}
		else {	
		List<VisibleGrafix> mockliste = new ArrayList<>();
		Player fakePlayer = new Player();
		mockliste.add(fakePlayer);
		GfxFassade gfx = GfxFassade.createInstance();
		gfx.run(1900, 650, "BoarderlingoTheGame");
		gfx.update(mockliste);}
	}

	public static void main(String[] args) {
//		AudioPlayer.playAudio("src\\boarderlingothegame\\carryon.wav");
		new BoarderlingoTheGame(false);
	}

	public void handleTwitchMessage(String message) {
		String nameOfPurchaser = message.split(" ")[0];
		String order = message.split(" ")[2];
		
		gp.addObstacle(order, nameOfPurchaser);

		if(message.toUpperCase().contains("SCHNELLER")) 
			gp.speedUp();
		if(message.toUpperCase().contains("NEBEL")) 
			gp.setFog();
		if(message.toUpperCase().contains("HINTERGRUND")) {
			gp.transition();
		}
	}
}

