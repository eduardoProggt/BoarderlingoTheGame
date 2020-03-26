
package boarderlingothegame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.geom.Area;

class GamePanel extends JPanel implements ActionListener {

	Controller controller = new Controller();
	UserInputFassade userInput = new UserInputFassade(controller);
	Background background = new Background(695,535);
	List<Opstacle> opstacles = new ArrayList<Opstacle>(); 
	Player player = new Player();
	private boolean offline;
	
	private int score,highscore;
	private int timeSeitJump = 0;
	private Timer time;
	int sprunghoehe=260;

	int incrementEachFrameNumber = 0;
	private int speedupTime = 0;
	private int nebelTime = 0;

	GamePanel(boolean isOnTwitch) {
		setLayout(null);
		time = new Timer(30, this); // starting a timer and passing the
		time.start();		    	// actionlistener for the animation
		offline = !isOnTwitch;
		
		
		addKeyListener(userInput.getKeyListener());
	}
	
	private void reset() {
		if(score> highscore)
			highscore= score;
		score = 0;
		opstacles = new ArrayList<Opstacle>();
		player = new Player();
		controller.resetButtons();
		timeSeitJump = 0;
	}
	
	public void actionPerformed(ActionEvent e) {
		autoScroll();
		
		//_________DEBUG_________
		if(controller.isPressed(ButtonsEnum.SPECIAL)) {
			highscore = 0;
		}
		//_________DEBUG_________
		
		if (controller.isPressed(ButtonsEnum.LEFT) && !player.isInAir())
			player.setState(PlayerStateEnum.BRAKING);
		else if (player.getState().equals(PlayerStateEnum.BRAKING))
			player.setState(PlayerStateEnum.IDLE);
		if(player.isInAir())
			calcJumpFrame();
		else if(controller.isPressed(ButtonsEnum.SPACE)) {
			jump();
			controller.resetButtons();//TODO TEST
		}
		else if(controller.isPressed(ButtonsEnum.DOWN)) {
			player.setState(PlayerStateEnum.DUCKING);
		}
		else if(player.getState().equals(PlayerStateEnum.DUCKING)){
			player.setState(PlayerStateEnum.IDLE);
		}
		
		opstacles.stream().forEach(this::collisionDetection);

		if(speedupTime > 0) {
			speedupTime --;
		}
		if(nebelTime > 0) {
			nebelTime --;
		}
		score++;
		incrementEachFrameNumber++;
	}

	private void collisionDetection(Opstacle eObst) {
		if( eObst.getHitBox().intersects(player.getHitBox().getBounds()) ){
		    Area a = new Area(eObst.getHitBox());
		    a.intersect(new Area(player.getHitBox()));
		    if(!a.isEmpty()){
				JOptionPane.showMessageDialog(null,"AUA (Gelähmt gar quer)");
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
		g2d.drawImage(player.getImage(incrementEachFrameNumber), (int)player.getX(), (int)player.getY(), this);
		g2d.setFont(new Font(null, 2, 40));
		
		for(Opstacle eObst : opstacles) {
			g2d.drawImage(eObst.getImage(incrementEachFrameNumber),eObst.getLocation().x,eObst.getLocation().y,this);
			//g2d.drawPolygon(eObst.getHitBox());
			g2d.drawString(eObst.getSpawnedBy(), eObst.getLocation().x,eObst.getLocation().y);
		}
		if(nebelTime > 550)
			g2d.drawImage(GfxLoader.nebel, 500+(nebelTime-550)*25,0, this);
		else if(nebelTime<=550 && nebelTime >= 50) {
			g2d.drawImage(GfxLoader.nebel, 500,0, this);
		}
		else if(nebelTime > 0){
			g2d.drawImage(GfxLoader.nebel, 500+(50-nebelTime)*25,0, this);
		}
		
		// g2d.drawPolygon(player.getHitBox());
		
		g2d.drawString("Score: \n "+score, 0, 40);
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

	void autoScroll() {
		//DIRTY!

		if(offline) 
			setRandomObstacles();
		
		int factor = 1;
		if(speedupTime>0)
			factor = 2;
		
		background.getLocation().x += player.getSpeedRight()*factor; 

		for(Opstacle eObst : opstacles) {
			
			if(eObst.getLocation().x > -150) {
				if (eObst instanceof Heli)
					eObst.moveRight(10);
				eObst.moveRight(player.getSpeedRight()*factor);
			}
		}
		for (Iterator<Opstacle> iterator = opstacles.iterator(); iterator.hasNext();) {
		    Opstacle eObst = iterator.next();
		    if(eObst.getLocation().x <= -150) {
		        iterator.remove();
		    }
		}
		System.out.println(player.getState());
	}

	private void setRandomObstacles() {
		Random wuerfel = new Random();
		if(wuerfel.nextInt() %100 == 0)
			addPipe("AUTO");
//		if(wuerfel.nextInt() %180 == 0)
//			addHeli("AUTO");
//		if(wuerfel.nextInt() %900 == 0)
//			speedUp();
//		if(wuerfel.nextInt() %1400 == 0)
//			setFog();
	}

	public void addPipe(String string) {

		opstacles.add(new Pipe(string));
	}
	public void addHeli(String string) {

		opstacles.add(new Heli(string));
	}

	public void speedUp() {
		speedupTime  = 150;
		
	}

	public void setFog() {
		nebelTime = 600;
		
	}
}

public class BoarderlingoTheGame extends JFrame {

	GamePanel gp = null;

	BoarderlingoTheGame(boolean isOnTwitch) {
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
		if(message.toUpperCase().contains("SCHNELLER")) {
			gp.speedUp();
		}
		if(message.toUpperCase().contains("NEBEL")) {
			gp.setFog();
		}
		
	}
}

