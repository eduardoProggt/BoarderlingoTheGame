
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
import boarderlingothegame.sprites.GfxLoader;
import boarderlingothegame.sprites.Granny;
import boarderlingothegame.sprites.Heli;
import boarderlingothegame.sprites.Obstacle;
import boarderlingothegame.sprites.Pipe;
import boarderlingothegame.sprites.Player;
import boarderlingothegame.sprites.PlayerStateEnum;

import java.awt.event.*;
import java.awt.geom.Area;

class GamePanel extends JPanel implements ActionListener {

	Controller controller = new Controller();
	UserInputFassade userInput = new UserInputFassade(controller);
	Background background = new Background(695,535);
	AnimationTimer animationTimer = new AnimationTimer();
	List<Obstacle> opstacles; 
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
		animationTimer.startAnimation("SCORE");
		reset();
	}
	
	private void reset() {
		if(animationTimer.getFrame("SCORE")> highscore)
			highscore= animationTimer.getFrame("SCORE");
		animationTimer.startAnimation("SCORE");
		opstacles = new ArrayList<Obstacle>();
		player = new Player();
		controller.resetButtons();
	}
	
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
		
		doForAllObstacles(this::collisionDetection);
		animationTimer.increment();
	}

	private synchronized void doForAllObstacles(Consumer<Obstacle> function) {
		for (Iterator<Obstacle> iterator = opstacles.iterator(); iterator.hasNext();) {
			try {
			    Obstacle eObst = iterator.next();
			    function.accept(eObst);
			} catch (Exception exc) {
				Obstacle eObst = iterator.next();
				System.out.println(eObst.getSpawnedBy());
			}
		}
	}

	private void collisionDetection(Obstacle eObst) {
		if( eObst.getHitBox().intersects(player.getHitBox().getBounds()) ){
		    Area collision = new Area(eObst.getHitBox());
		    collision.intersect(new Area(player.getHitBox()));
		    if(!collision.isEmpty()){
				JOptionPane.showMessageDialog(null,"AUA (Gelähmt gar quer)");
				System.out.println(eObst.getSpawnedBy());
				reset();
				return;
		    }
		}
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

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		requestFocus();
		setFocusable(true);

		g2d.drawImage(background.getImage(0), 700 - background.getLocation().x, 0, null); 
		g2d.drawImage(player.getImage(animationTimer.getFrame()), (int)player.getX(), (int)player.getY(), this);
		g2d.setFont(new Font(null, 2, 40));
		
		doForAllObstacles(e-> this.drawObstacles(g2d,e));
		
		//TODO Refactor this stuff:
		if(animationTimer.getFrame("NEBEL") != null) {
			int nebelTime = animationTimer.getFrame("NEBEL").intValue();
			if(nebelTime > 550)
				g2d.drawImage(GfxLoader.nebel, 500+(nebelTime-550)*25,0, this);
			else if(nebelTime<=550 && nebelTime >= 50) {
				g2d.drawImage(GfxLoader.nebel, 500,0, this);
			}
			else if(nebelTime > 0){
				g2d.drawImage(GfxLoader.nebel, 500+(50-nebelTime)*25,0, this);
			}
		}
		// </TODO>
		
		g2d.drawString("Score: \n "+animationTimer.getFrame("SCORE"), 0, 40);
		g2d.drawString("Highscore: \n "+highscore, 0, 80);
		
		repaint();
	}

	private void drawObstacles(Graphics2D g2d, Obstacle eObst) {
		
			try {
				g2d.drawImage(eObst.getImage(animationTimer.getFrame()),eObst.getLocation().x,eObst.getLocation().y,this);
				g2d.drawPolygon(eObst.getHitBox());
				g2d.drawString(eObst.getSpawnedBy(), eObst.getLocation().x,eObst.getLocation().y);
			} catch (Exception e) {
				System.out.println(eObst.getSpawnedBy());
			}

		
	} 
	void jump() 
	{
		if (player.getState() == PlayerStateEnum.JUMPING)
			return;
			player.setState(PlayerStateEnum.JUMPING);
			player.setHorizontalMomentum(-32);

	}

	void autoScroll() {
		//DIRTY!

		if(offline) 
			setRandomObstacles();
		
		int factor = 1;
		if(animationTimer.getFrame("SPEED")<150)
			factor = 2;
		
		background.getLocation().x += player.getSpeedRight()*factor; 

		final int f = factor;
		doForAllObstacles(eObst -> moveObstacles(f, eObst));	
		removeInvalidObjects();
	}

	private void moveObstacles(int factor, Obstacle eObst) {
		if(eObst.getLocation().x > -150) {
			if (eObst instanceof Heli)
				eObst.moveRight(10);
			if (eObst instanceof Granny) {//Dringend refactoren!!
				((Granny)eObst).moveDown();
			}
			eObst.moveRight(player.getSpeedRight()*factor);
		}
	}

	private synchronized void removeInvalidObjects() {

		for (Iterator<Obstacle> iterator = opstacles.iterator(); iterator.hasNext();) {
		    Obstacle eObst = iterator.next();
		    if(eObst.getLocation().x <= -150) {
		        iterator.remove();
		    }
		}
	}

	private void setRandomObstacles() {
		Random wuerfel = new Random();
		int unwahrscheinlichkeitsfaktor = 30;
		if(wuerfel.nextInt() %(3*unwahrscheinlichkeitsfaktor) == 0)
			addPipe("AUTO");
		if(wuerfel.nextInt() %(5*unwahrscheinlichkeitsfaktor) == 0)
			addHeli("AUTO");
		if(wuerfel.nextInt() %(20*unwahrscheinlichkeitsfaktor) == 0)
			speedUp();
		if(wuerfel.nextInt() %(40*unwahrscheinlichkeitsfaktor) == 0)
			setFog();
		if(wuerfel.nextInt() %(60*unwahrscheinlichkeitsfaktor) == 0)
			addGranny("AUTO");
	}

	public synchronized void addPipe(String string) {
		opstacles.add(new Pipe(string));
	}
	public synchronized void addHeli(String string) {
		opstacles.add(new Heli(string));
	}
	public synchronized void addGranny(String string) {
		opstacles.add(new Granny(string));
	}
	public void speedUp() {
		animationTimer.startAnimation("SPEED");
	}
	public void setFog() {
		animationTimer.startAnimation("NEBEL");
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
		if(message.toUpperCase().contains("KAKTUS"))
			gp.addPipe(message.split(" ")[0]);
		if(message.toUpperCase().contains("HELI"))
			gp.addHeli(message.split(" ")[0]);
		if(message.toUpperCase().contains("SCHNELLER")) 
			gp.speedUp();
		if(message.toUpperCase().contains("NEBEL")) 
			gp.setFog();
		if(message.toUpperCase().contains("OMA")) 
			gp.addGranny(message.split(" ")[0]);
	}
}

