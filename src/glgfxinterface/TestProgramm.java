package glgfxinterface;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
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

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import boarderlingothegame.sprites.GfxLoader;
import boarderlingothegame.sprites.VisibleGrafix;

public class TestProgramm implements GfxFassade{
	
	// The window handle
	private Window win;
	TileRenderer tile;
	float offset = 0f;
	int framesTillStart = 0;
	private List<VisibleGrafix> textures = new ArrayList<>();

	public void run(int width, int height, String title) {
		System.out.println("LWJGL " + Version.getVersion() + " wurde von BoarderlingoTheGame angesprochen");

		GLFWErrorCallback.createPrint(System.err).set();

		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		win = new Window(width, height);
		win.createWindow(title);
		
		loop();

		glfwTerminate();
		glfwSetErrorCallback(null).free();
		win.destroy();

	}

	private void loop() {
	
		glfwSwapInterval(0);
		GL.createCapabilities();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_TEXTURE_2D);
		tile = new TileRenderer();


		
//		double d = 0.88;
//		projection.translate(-(float)d, 0, 0);
//		projection.scale(player.getImage(0).getWidth()/winWidth, player.getImage(0).getHeight()/winHeight, 1);
//		System.out.println(projection.toString());
		
		
		
//		BufferedImage image1 = player.getImage(0);
//		BufferedImage image2 = player.getImage(1);

//		List<Texture> textureList = new ArrayList<>();
//		textureList.add(new Texture(image1));
//		textureList.add(new Texture(image2));
			

		double fps = 60;
		double frameCap = 1d/fps;
		
		double time = System.currentTimeMillis()/1000d;
		double unprocessed = 0;	

		Tile background = new Tile("src\\boarderlingothegame\\backgrounds\\wielandstraße.png",5000,750);
		Tile sprite = new Tile("src\\boarderlingothegame\\gfx\\blingo_right.png",350,600);
		Tile hundertMalZehnpixel = new Tile("src\\boarderlingothegame\\gfx\\100mal10pixel.png",100,10);
		
		int framesPersec= 0;
		double einstiegszeit = System.currentTimeMillis();
		while ( !win.shouldClose()) {
			
			
			
			boolean canRender = false;
			
			double time2 = System.currentTimeMillis()/1000d;
			double passed = time2 - time;
			unprocessed +=  passed;
			time = time2;
			
			while(unprocessed> frameCap) {
				unprocessed-= frameCap;
				
				canRender=true;
				if(glfwGetKey(win.getWinId(),GLFW_KEY_ESCAPE) == GL_TRUE)
					glfwSetWindowShouldClose(win.getWinId(), true);
			}
			if(!canRender)
				continue;
			framesPersec++;
			if(System.currentTimeMillis() - einstiegszeit > 1000) {
				einstiegszeit = System.currentTimeMillis();
				System.out.println(framesPersec);
				framesPersec = 0;
			}
			
			
			calcNextFrame( background, sprite,hundertMalZehnpixel);

			win.swapBuffers();
			 
			glfwPollEvents();
		}
		glfwTerminate();
	}

	private void calcNextFrame( Tile background, Tile sprite,Tile hundertMalZehnpixel) {
		
		float fensterX = 1900;
		float fensterY = 650;
		float breite = 350;
		float hoehe = 600;
		framesTillStart++;

		//projection.ortho2D(0,2,0,2);
		glClear(GL_COLOR_BUFFER_BIT  | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		tile.renderTile(background, 0, 0,  new Matrix4f()/*.scale(6, 2, 1).translate((float)Math.sin((double)offset)/2,0,0)*/,win);
		
		tile.renderTile(sprite, 0, 0, new Matrix4f()/*.scale(9/10f, 9/10f, 1).rotate(offset,new Vector3f(0,0,1)).translate(i/3,0,0)*/,win);
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 50; j++) 
				tile.renderTile(hundertMalZehnpixel, i*100+(j%2)*10,j*10, new Matrix4f(),win);
		}
		
	}

	@Override
	public void update(List<VisibleGrafix> textures) {
		setTextures(textures);
	}

	public List<VisibleGrafix> getTextures() {
		return textures;
	}

	public void setTextures(List<VisibleGrafix> textures) {
		this.textures = textures;
	}

}
