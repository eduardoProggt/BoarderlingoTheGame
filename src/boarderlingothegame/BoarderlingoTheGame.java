
package boarderlingothegame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.Timer;

import boarderlingothegame.controller.ButtonsEnum;
import boarderlingothegame.controller.Controller;
import boarderlingothegame.controller.UserInputFassade;
import boarderlingothegame.sprites.Fog;
import boarderlingothegame.sprites.GfxLoader;
import boarderlingothegame.sprites.Granny;
import boarderlingothegame.sprites.Heli;
import boarderlingothegame.sprites.Obstacle;
import boarderlingothegame.sprites.Cactus;
import boarderlingothegame.sprites.Player;
import boarderlingothegame.sprites.PlayerStateEnum;

import java.awt.event.*;
import java.awt.geom.Area;

class GamePanel extends JPanel implements ActionListener {

	Controller controller = new Controller();
	UserInputFassade userInput = new UserInputFassade(controller);
	Background background = new Background(695,535);
	Fog fog;
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
			calcJumpFrame();
		else if(controller.isPressed(ButtonsEnum.SPACE)) {
			jump();
			controller.resetButtons();
		}
		else if(controller.isPressed(ButtonsEnum.DOWN)) {
			player.setState(PlayerStateEnum.DUCKING);
		}
		else if(player.getState().equals(PlayerStateEnum.DUCKING)){
			player.setState(PlayerStateEnum.IDLE);
		}
		
		ObstacleController.getInstance().collide(player.getHitBox());
		if(ObstacleController.getInstance().isCollided)
			{reset();
			ObstacleController.getInstance().isCollided=false;}
		AnimationTimer.getInstance().increment();
	}
	void autoScroll() {
		//DIRTY!

		if(offline) 
			setRandomObstacles();
		
		int factor = 1;
		if(AnimationTimer.getInstance().getFrame("SPEED")<150)
			factor = 2;
		
		background.getLocation().x += player.getSpeedRight()*factor; 
		ObstacleController.getInstance().autoScroll(factor,player.getSpeedRight());

	}

	
	private void calcJumpFrame() {
		int bodenhoehe = 80;
		player.setY(player.getY()+player.getHorizontalMomentum());
		player.decreaseHorizontalMomentum(-1.8);
		
		if(controller.isPressed(ButtonsEnum.SPACE) && player.getState().equals(PlayerStateEnum.JUMPING)) {
			player.setState(PlayerStateEnum.FALLING);
			player.setHorizontalMomentum(-16);
		}
		
		if(player.getY()>bodenhoehe) {
			player.setY(bodenhoehe);
			player.setHorizontalMomentum(0);
			player.setState(PlayerStateEnum.IDLE);			
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		requestFocus();
		setFocusable(true);

		g2d.drawImage(background.getImage(0), 700 - background.getLocation().x, 0, null); 
		g2d.drawImage(player.getImage(AnimationTimer.getInstance().getFrame()), (int)player.getX(), (int)player.getY(), this);
		g2d.setFont(new Font(null, 2, 40));
		ObstacleController.getInstance().draw(g2d, this);//TODO, das ist seeeeehr hässlich
		
		if(fog != null && fog.getLocation() != null)
			g2d.drawImage(fog.getImage(0), fog.getLocation().x,0, this);
		
		g2d.drawString("Score: \n "+AnimationTimer.getInstance().getFrame("SCORE"), 0, 40);
		g2d.drawString("Highscore: \n "+highscore, 0, 80);
		
		repaint();
	}

	void jump() 
	{
		if (player.getState() == PlayerStateEnum.JUMPING)
			return;
			player.setState(PlayerStateEnum.JUMPING);
			player.setHorizontalMomentum(-32);

	}


	private void setRandomObstacles() {
		Random wuerfel = new Random();
		int unwahrscheinlichkeitsfaktor = 30;
		if(wuerfel.nextInt() %(3*unwahrscheinlichkeitsfaktor) == 0)
			ObstacleController.getInstance().add("OMA", "AUTO");
//		if(wuerfel.nextInt() %(5*unwahrscheinlichkeitsfaktor) == 0)
//			addHeli("AUTO");
//		if(wuerfel.nextInt() %(20*unwahrscheinlichkeitsfaktor) == 0)
//			speedUp();
		if(wuerfel.nextInt() %(5*unwahrscheinlichkeitsfaktor) == 0)
			setFog();
//		if(wuerfel.nextInt() %(60*unwahrscheinlichkeitsfaktor) == 0)
//			addGranny("AUTO");
	}


	public void speedUp() {
		AnimationTimer.getInstance().startAnimation("SPEED");
	}
	public void setFog() {
		fog = new Fog();
	}

	public void addObstacle(String order, String nameOfPurchaser) {
		ObstacleController.getInstance().add(order, nameOfPurchaser);
	}
}

public class BoarderlingoTheGame extends JFrame {

	GamePanel gp = null;

	public BoarderlingoTheGame(boolean isOnTwitch) {
		gp= new GamePanel(isOnTwitch);
		add(gp);
		setSize(1500, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
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
	}
}

