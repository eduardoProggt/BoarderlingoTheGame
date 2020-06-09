package glgfxinterface;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import boarderlingothegame.controller.GameController;
import boarderlingothegame.sprites.Cactus;
import boarderlingothegame.sprites.Granny;
import boarderlingothegame.sprites.Heli;
import boarderlingothegame.sprites.Obstacle;
import boarderlingothegame.web.WebSocketServer;


public class MainProgramm implements GfxFassade{
	
	private Queue<Object> twitchOrders = new LinkedList<>();
	private Window win;
	float offset = 0f;
	int framesTillStart = 0;

	public void run(int width, int height, String title) {
		System.out.println("LWJGL " + Version.getVersion());
		GLFWErrorCallback.createPrint(System.err).set();

		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		win = Window.getInstance();
		win.createWindow(title,width, height);
		
		loop();

		glfwTerminate();
		glfwSetErrorCallback(null).free();
		win.destroy();

	}

	private void loop() {
	
		setupGL();
		GameController gameController = new GameController();
		
		double fps = 60;
		double time = System.currentTimeMillis()/1000d;
		double unprocessed = 0;	
		int framesPersec= 0;
		double einstiegszeit = System.currentTimeMillis();
		while ( !win.shouldClose()) {
			
			boolean canRender = false;
			
			double time2 = System.currentTimeMillis()/1000d;
			double passed = time2 - time;
			unprocessed +=  passed;
			time = time2;
			
			while(unprocessed> 1d/fps) {
				unprocessed-= 1d/fps;
				
				canRender=true;
				if(glfwGetKey(win.getWinId(),GLFW_KEY_ESCAPE) == GL_TRUE)
					glfwSetWindowShouldClose(win.getWinId(), true);
				gameController.processInput();
				
			}
			if(!canRender) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			framesPersec++;
			if(System.currentTimeMillis() - einstiegszeit > 1000) {
				einstiegszeit = System.currentTimeMillis();
				System.out.println(framesPersec);
				framesPersec = 0;
			}
			
			glClear(GL_COLOR_BUFFER_BIT  | GL_DEPTH_BUFFER_BIT);
			if(!gameController.isPaused()) {
				if(!getTwitchOrders().isEmpty() && framesPersec == 30)
					computeNewTwitchOrder(gameController);
				gameController.calcNextFrame();
				win.swapBuffers();
			}
			else if(glfwGetKey(win.getWinId(),GLFW_KEY_ENTER) == GL_TRUE)
				gameController.setPaused(false);
			
			glfwPollEvents();

		}
		glfwTerminate();
	}

	private void computeNewTwitchOrder(GameController gameController) {
		//Das ist hässlich, aber selbst Urwill sagt, man kann das so machen
		if(getTwitchOrders().peek() instanceof Obstacle)
			gameController.addObstacle((Obstacle)getTwitchOrders().poll());
		if(getTwitchOrders().peek() instanceof String)
			gameController.executeOrder((String)getTwitchOrders().poll());
	}

	private void setupGL() {
		glfwSwapInterval(0);
		GL.createCapabilities();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_TEXTURE_2D);
	}

	@Override
	public void handleNewTwitchOrder(String order, String nameOfPurchaser) {
		if(order.toUpperCase().contains("KAKTUS"))
			getTwitchOrders().offer(new Cactus(nameOfPurchaser));
		
		if(order.toUpperCase().contains("HELI"))
			getTwitchOrders().offer(new Heli(nameOfPurchaser));
		
		if(order.toUpperCase().contains("OMA"))
			getTwitchOrders().offer(new Granny(nameOfPurchaser));
	
		if(order.toUpperCase().contains("SCHNELLER")) 
			getTwitchOrders().offer("SCHNELLER");
			
		if(order.toUpperCase().contains("NEBEL")) 
			getTwitchOrders().offer("NEBEL");
			
		if(order.toUpperCase().contains("HINTERGRUND")) {
			getTwitchOrders().offer("HINTERGRUND");
		}
		if(order.toUpperCase().contains("KUGEL")) {
			getTwitchOrders().offer("KUGEL");
		}
	}

	public Queue<Object> getTwitchOrders() {
		return twitchOrders;
	}

}
