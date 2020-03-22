
package boarderlingothegame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
	
	private int score,highscore;
	private int timeSeitJump = 0;
	private Timer time;

	int incrementEachFrameNumber = 0;
	private int speedupTime = 0;
	private int nebelTime = 0;

	GamePanel() {
		setLayout(null);
		time = new Timer(30, this); // starting a timer and passing the
		time.start();		    	// actionlistener for the animation
		
		addKeyListener(userInput.getKeyListener());
	}
	
	private void reset() {
		if(score> highscore)
			highscore= score;
		score = 0;
		opstacles = new ArrayList<Opstacle>();
		opstacles.add(new Pipe());
		opstacles.add(new Heli());
		player = new Player();
		controller.resetButtons();
		timeSeitJump = 0;
	}
	
	public void actionPerformed(ActionEvent e) {
		autoScroll();
		
		//_________DEBUG_________
		if(controller.isPressed(ButtonsEnum.SPECIAL)) {
			addPipe();
			setFog();
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
			g2d.drawPolygon(eObst.getHitBox());
			g2d.drawPolygon(player.getHitBox());
		}
		if(nebelTime > 550)
			g2d.drawImage(GfxLoader.nebel, 500+(nebelTime-550)*25,0, this);
		else if(nebelTime<=550 && nebelTime >= 50) {
			g2d.drawImage(GfxLoader.nebel, 500,0, this);
		}
		else if(nebelTime > 0){
			g2d.drawImage(GfxLoader.nebel, 500+(50-nebelTime)*25,0, this);
		}
		
		
		g2d.setFont(new Font(null, 2, 40));
		g2d.drawString("Score: \n "+score, 0, 40);
		g2d.drawString("Highscore: \n "+highscore, 0, 80);
		
		repaint();
	} 
	void jump() 
	{
		if (player.getState() != PlayerStateEnum.JUMPING) // For upward motion during jump
			player.setState(PlayerStateEnum.JUMPING);

	}

	void autoScroll() {
		//DIRTY!
		int factor = 1;
		if(speedupTime>0)
			factor = 2;
		
		background.getLocation().x += player.getSpeedRight()*factor; 

		List<Opstacle> despawn = new ArrayList<Opstacle>();
		for(Opstacle eObst : opstacles) {
			
			if(eObst.getLocation().x > -150) {
				if (eObst instanceof Heli)
					eObst.moveRight(10);
				eObst.moveRight(player.getSpeedRight()*factor);
			}
			else despawn.add(eObst);
		}
		
		opstacles.removeAll(despawn);
		System.out.println(opstacles.size());
	}

	public void addPipe() {

		opstacles.add(new Pipe());
	}
	public void addHeli() {

		opstacles.add(new Heli());
	}

	public void speedUp() {
		speedupTime  = 150;
		
	}

	public void setFog() {
		nebelTime = 600;
		
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
		if(message.toUpperCase().contains("SCHNELLER")) {
			gp.speedUp();
		}
		if(message.toUpperCase().contains("NEBEL")) {
			gp.setFog();
		}
		
	}
	
	private static void testWithoutServer(BoarderlingoTheGame boarderlingoTheGame) {
		boarderlingoTheGame.handleTwitchMessage("KAKTUS");
		boarderlingoTheGame.handleTwitchMessage("HELI");
		
	}
}

