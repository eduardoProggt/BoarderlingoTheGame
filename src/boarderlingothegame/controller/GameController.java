package boarderlingothegame.controller;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.awt.Polygon;
import java.awt.geom.Area;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JOptionPane;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import boarderlingothegame.AnimationTimer;
import boarderlingothegame.AudioPlayer;
import boarderlingothegame.Background;
import boarderlingothegame.halloffame.HallOfFameWindow;
import boarderlingothegame.sprites.Bullet;
import boarderlingothegame.sprites.Cactus;
import boarderlingothegame.sprites.Fog;
import boarderlingothegame.sprites.Granny;
import boarderlingothegame.sprites.Heli;
import boarderlingothegame.sprites.HighScore;
import boarderlingothegame.sprites.Obstacle;
import boarderlingothegame.sprites.Player;
import boarderlingothegame.sprites.PlayerStateEnum;
import boarderlingothegame.sprites.VisibleGrafix;
import glgfxinterface.Tile;
import glgfxinterface.TileRenderer;
import glgfxinterface.Window;

public class GameController {
	private InputController controller = new InputController();
	private Background background = new Background();
	private Player player = new Player();
	private TileRenderer renderer = new TileRenderer();		
	private Queue<Obstacle> obstacles = new LinkedList<>();
	private AudioPlayer audioPlayer = new AudioPlayer();
	private HighScore highScore = new HighScore();
	private VisibleGrafix overlay = null;
	private int framesTillStart = 0;
	private Bullet bullet;
	private HallOfFameWindow hallOfFameWindow = new HallOfFameWindow();
	private boolean paused;
	
	
	public void calcNextFrame() {
		int scrollGeschwindigkeit = player.getSpeedRight();
		drawBackground(background, scrollGeschwindigkeit);
		drawHighScore();
		drawPlayer();
		
		updateObstacles(scrollGeschwindigkeit);
		deleteObsoleteObstacles();
		drawObstacles();
		drawBullet();
		drawOverlay();

		framesTillStart++;
		AnimationTimer.getInstance().increment();
	}


	private void drawBullet() {
		
		for (int i = 0; i < player.getBullets(); i++) {
			renderer.renderTile(bullet.image,30,110+(i*30) , new Matrix4f());

		}
		
		if(bullet != null) {
			renderer.renderTile(bullet.getTile(framesTillStart), (int)bullet.getLocation().getX(),(int)bullet.getLocation().getY(), new Matrix4f());
			bullet.move();
		}
		
	}


	private void drawOverlay() {
		if(overlay != null && overlay.getLocation() != null)
			renderer.renderTile(overlay.getTile(framesTillStart), (int)overlay.getLocation().getX()-200,(int)overlay.getLocation().getY(), new Matrix4f());
	}


	private void drawHighScore() {
		renderer.renderScore(highScore);
	}


	private void updateObstacles(int scrollGeschwindigkeit) {
		for (Iterator<Obstacle> iterator = getObstacles().iterator(); iterator.hasNext();) {
			Obstacle obstacle = iterator.next();

			collideWithBullet(iterator, obstacle);
			
			obstacle.moveRight(scrollGeschwindigkeit);
			if(collisionDetection(obstacle.getHitBox(), player.getHitBox())) {
				iterator.remove();
				killPlayer(iterator, obstacle);
				break;
			}
				
		}
	}


	public void collideWithBullet(Iterator<Obstacle> iterator, Obstacle obstacle) {
		if(bullet != null && collisionDetection(obstacle.getHitBox(), bullet.getHitBox())) {
			bullet = null;
			iterator.remove();
		}
	}

	private void killPlayer(Iterator<Obstacle> iterator, Obstacle obstacle) {
		Tile tot = new Tile("src\\boarderlingothegame\\gfx\\tot.png", 858, 540);
		renderer.renderTile(tot, 500, 50, new Matrix4f());
		player.resetBullets();
		setPaused(true);
		hallOfFameWindow.update(obstacle.getSpawnedBy());
		//audioPlayer.playAudio("src\\boarderlingothegame\\Sounds\\boarderlingoDamage.wav");
		highScore.reset();
		while(iterator.hasNext()) {
			iterator.next().moveRight(-700);
			
		}
	}


	private void deleteObsoleteObstacles() {
		if(!getObstacles().isEmpty() && getObstacles().peek().getLocation().getX()<-500)
			getObstacles().poll();
	}

	private void drawObstacles() {
		for (Obstacle obstacle : getObstacles()) {
			renderer.renderTile(obstacle.getTile(framesTillStart), (int)obstacle.getLocation().getX(),(int)obstacle.getLocation().getY(), new Matrix4f());
		}
	}
	
	private boolean collisionDetection(Polygon hitbox1, Polygon hitbox2) {
		if( hitbox1.getBounds().intersects(hitbox2.getBounds()) ){
		    Area area1 = new Area(hitbox1);
		    Area area2 = new Area(hitbox2);
			area1.intersect(area2);
		    boolean collisionHappened = !area1.isEmpty();
			return collisionHappened;
		   
		}
		return false;
	}
	
	public void addObstacle(Obstacle obstacle) {
		getObstacles().offer(obstacle);
	}
	
	private void drawPlayer() {
		if(player.isInAir())
			player.calcJumpFrame();
		renderer.renderTile(player.getTile(framesTillStart), (int)player.getX(), (int)player.getY(), new Matrix4f());
	}

	private void drawBackground(Background background, int scrollGeschwindigkeit) {
		int x = background.getLocation().x;
		for (Tile tile : background.getCurrentTiles()) {
			renderer.renderTile(tile,x, -100,  new Matrix4f());
			x+= tile.getWidth();
			if(x>Window.getInstance().getWidth())
				break;
		}
		background.move(scrollGeschwindigkeit);
	}

	public void processInput() {
		player.resetState();//Damit er aus einem Ducking - State von alleine rauskommt

		if(controller.downPressed())
			player.duck();
		if(controller.spaceTipped())
			if(player.getState().equals(PlayerStateEnum.DUCKING))
				bullet = player.shoot();
			else
				player.jump();
		if(controller.leftPressed())
			player.brake();
		if(controller.keyVTipped()) {
			hallOfFameWindow.update("TEst"+(int)(Math.random()*10));
			
		}
	}

	private Queue<Obstacle> getObstacles() {
		return obstacles;
	}


	public void executeOrder(String order) {
		switch (order) {
		case "SCHNELLER":
			player.boost();
			break;
			
		case "NEBEL":
			overlay = new Fog();
			break;
		
		case "HINTERGRUND":
			background.change();
			break;
		}
	}


	public boolean isPaused() {
		return paused;
	}
	public void setPaused(boolean b) {
		paused = b;
		
	}
}
