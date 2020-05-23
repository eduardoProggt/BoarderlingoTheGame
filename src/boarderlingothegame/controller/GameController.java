package boarderlingothegame.controller;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.util.LinkedList;
import java.util.Queue;

import org.joml.Matrix4f;

import boarderlingothegame.Background;
import boarderlingothegame.sprites.Cactus;
import boarderlingothegame.sprites.Granny;
import boarderlingothegame.sprites.Heli;
import boarderlingothegame.sprites.Obstacle;
import boarderlingothegame.sprites.Player;
import boarderlingothegame.sprites.PlayerStateEnum;
import glgfxinterface.Tile;
import glgfxinterface.TileRenderer;
import glgfxinterface.Window;

public class GameController {
	private InputController controller = new InputController();
	private Background background = new Background();
	private Player player = new Player();
	private TileRenderer renderer = new TileRenderer();		
	private Queue<Obstacle> obstacles = new LinkedList<>();
	private int framesTillStart = 0;
	
	public void calcNextFrame() {
		int scrollGeschwindigkeit = player.getSpeedRight();
		
		drawBackground(background, scrollGeschwindigkeit);
		drawPlayer();
		
		updateObstacles(scrollGeschwindigkeit);
		deleteObsoleteObstacles();
		drawObstacles();


		framesTillStart++;
	}

	private void drawObstacles() {
		for (Obstacle obstacle : getObstacles()) {
			renderer.renderTile(obstacle.getTile(framesTillStart), (int)obstacle.getLocation().getX(),(int)obstacle.getLocation().getY(), new Matrix4f());
		}
	}

	private void deleteObsoleteObstacles() {
		if(!getObstacles().isEmpty() && getObstacles().peek().getLocation().getX()<-500)
			getObstacles().poll();
	}

	private void updateObstacles(int scrollGeschwindigkeit) {
		for (Obstacle obstacle : getObstacles()) {
			obstacle.moveRight(scrollGeschwindigkeit);
		}
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
		}
		background.move(scrollGeschwindigkeit);
	}

	public void processInput() {
		player.resetState();//Damit er aus einem Ducking - State von alleine rauskommt

		if(controller.downPressed())
			player.duck();
		if(controller.spaceTipped())
			if(player.getState().equals(PlayerStateEnum.DUCKING))
				player.shoot();
			else
				player.jump();
		if(controller.leftPressed())
			player.brake();
		if(controller.keyVTipped())
			addObstacle(new Granny("TEST"));
	}

	private Queue<Obstacle> getObstacles() {
		return obstacles;
	}

}
