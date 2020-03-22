
package boarderlingothegame;

import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.geom.Area;
import java.io.File;
import java.util.*;

class GamePanel extends JPanel implements ActionListener {

	Controller controller = new Controller();
	UserInputFassade userInput = new UserInputFassade(controller);
	Background background = new Background(695,535);
	Queue<Opstacle> opstacles = new LinkedList<>(); 
	Player player = new Player();
	
	private int timeSeitJump = 0;
	private Timer time;

	int incrementEachFrameNumber = 0;

	GamePanel() {
		setLayout(null);
		time = new Timer(30, this); // starting a timer and passing the
		time.start();		    	// actionlistener for the animation
		
		addKeyListener(userInput.getKeyListener());
	}

	public void actionPerformed(ActionEvent e) {
		autoScroll();
		
		if (controller.isPressed(ButtonsEnum.LEFT))
			player.setState(PlayerStateEnum.BRAKING);
		else if (player.getState().equals(PlayerStateEnum.BRAKING))
			player.setState(PlayerStateEnum.IDLE);
		if(player.isInAir())
			calcJumpFrame();
		else if(controller.isPressed(ButtonsEnum.SPACE)) {
			jump();
		}
		else if(controller.isPressed(ButtonsEnum.DOWN)) {
			player.setState(PlayerStateEnum.DUCKING);
		}
		else if(player.getState().equals(PlayerStateEnum.DUCKING)){
			player.setState(PlayerStateEnum.IDLE);
		}
//		Area areaPlayer = new Area(player.getHitBox());
//		Area areaPipe = new Area(testPipe.getHitBox());
//		areaPlayer.intersect(areaPipe);
//		if(!areaPlayer.isEmpty()) {
		for(Opstacle eObst : opstacles) {
		if(player.getHitBox().contains(eObst.getLocation()) 
				|| player.getHitBox().contains(eObst.getLocation().getX()+130 ,eObst.getLocation().getY())
				|| player.getHitBox().contains(eObst.getLocation().getX(),eObst.getLocation().getY()+100) 
				|| player.getHitBox().contains(eObst.getLocation().getX()+150,eObst.getLocation().getY()+100) 
				) {
			JOptionPane.showMessageDialog(null,"AUA (Gelähmt gar quer)");
			opstacles = new LinkedList<>();
			opstacles.add(new Pipe());
			opstacles.add(new Heli());
			player = new Player();
			controller.resetButtons();
			timeSeitJump = 0;
		
		}}
		incrementEachFrameNumber++;
	}


	private void calcJumpFrame() {
		int bodenhoehe = 80;
		int sprunghoehe=-50;

		double x = timeSeitJump;
		if(player.isInAir()) {
			int newCalcedJumpYValue= bodenhoehe-1-(int)Math.round(-0.6*(x*x)+25*x);
			player.setY(newCalcedJumpYValue);
		}

		if(player.getY() < sprunghoehe && player.getState().equals(PlayerStateEnum.JUMPING))
			player.setState(PlayerStateEnum.FALLING);
		if(player.getState().equals(PlayerStateEnum.FALLING))
		{
			//Joah, was eigentlich?
		}

		if(player.getY() >= bodenhoehe)
		{
			player.setState(PlayerStateEnum.IDLE);
			timeSeitJump = 0;
		}
		timeSeitJump++;
	}


	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		requestFocus();
		setFocusable(true);

		g2d.drawImage(background.getImage(0), 700 - background.getLocation().x, 0, null); 
		g2d.drawImage(player.getImage(incrementEachFrameNumber), player.getX(), player.getY(), this);
		
		for(Opstacle eObst : opstacles) {
			g2d.drawImage(eObst.getImage(incrementEachFrameNumber),eObst.getLocation().x,eObst.getLocation().y,this);
		}
		repaint();
	} 
	void jump() 
	{
		if (player.getState() != PlayerStateEnum.JUMPING) // For upward motion during jump
			player.setState(PlayerStateEnum.JUMPING);

	}



	
	void autoScroll() {
		
		background.getLocation().x += player.getSpeedRight(); 

		
		for(Opstacle eObst : opstacles) {
			if(eObst.getLocation().x > -150)
				eObst.moveRight();
		}
		if(opstacles.peek()!= null &&opstacles.peek().getLocation().x<-150) {
			opstacles.poll();
		}

		background.getLocation().x = background.getLocation().x%1000 +1000;
	}

	public void addPipe() {

		opstacles.add(new Pipe());
	}
	public void addHeli() {

		opstacles.add(new Heli());
	}
}

public class BoarderlingoTheGame extends JFrame {

	GamePanel gp = new GamePanel();

	BoarderlingoTheGame() {
		add(gp);
		setSize(1500, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	public static void main(String[] args) {
		testWithoutServer(new BoarderlingoTheGame());
	}



	public void handleTwitchMessage(String message) {
		if(message.toUpperCase().contains("KAKTUS"))
			gp.addPipe();
		if(message.toUpperCase().contains("HELI"))
			gp.addHeli();
		
	}
	
	private static void testWithoutServer(BoarderlingoTheGame boarderlingoTheGame) {
		boarderlingoTheGame.handleTwitchMessage("KAKTUS");
		boarderlingoTheGame.handleTwitchMessage("HELI");
		
	}
}

