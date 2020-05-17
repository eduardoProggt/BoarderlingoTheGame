package glgfxinterface;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import boarderlingothegame.sprites.Player;
import boarderlingothegame.sprites.VisibleGrafix;

public class TestProgramm implements GfxFassade{

	// The window handle
	private Window win;
	private List<VisibleGrafix> textures = new ArrayList<>();

	public void run(int width, int height, String title) {
		System.out.println("LWJGL " + Version.getVersion() + " wurde von BoarderlingoTheGame angesprochen");

		GLFWErrorCallback.createPrint(System.err).set();

		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		win = new Window(width, height);
		win.createWindow(title);
		
		loop();

		// Free the window callbacks and destroy the window
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		win.destroy();

		// Terminate GLFW and free the error callback
	}

	private void loop() {
	
		glfwSwapInterval(0);
		GL.createCapabilities();
		glEnable(GL_TEXTURE_2D);
		
		float[] textureMesh = new float[] {
				1,0,
				0,0,
				1,1,
				
				1,1,
				0,0,
				0,1
		};
		
		Player player = new Player();
		getTextures().add(player);
		float winWidth = 1900;
		float winHeight = 700f;
		Matrix4f projection = new Matrix4f().ortho2D(-1,1,-1,1);
		
		double d = 0.88;
		projection.translate(-(float)d, 0, 0);
		projection.scale(player.getImage(0).getWidth()/winWidth, player.getImage(0).getHeight()/winHeight, 1);
		System.out.println(projection.toString());
		
		
		
		BufferedImage image1 = player.getImage(0);
		BufferedImage image2 = player.getImage(1);
		Shader shader = new Shader("shader");
		List<Texture> textureList = new ArrayList<>();
		textureList.add(new Texture(image1));
		textureList.add(new Texture(image2));
			

		double fps = 60;
		double frameCap = 1d/fps;
		
		double time = System.currentTimeMillis()/1000d;
		double unprocessed = 0;	
		Model model = new Model(getVerticesOfGrafix(),textureMesh);
		while ( !win.shouldClose()) {
			boolean canRender = false;
			
			double time2 = System.currentTimeMillis()/1000d;
			double passed = time2 - time;
			unprocessed +=  passed;
			time = time2;
			
			while(unprocessed>= frameCap) {
				unprocessed-= frameCap;
				
				canRender=true;
				if(glfwGetKey(win.getWinId(),GLFW_KEY_ESCAPE) == GL_TRUE)
					glfwSetWindowShouldClose(win.getWinId(), true);
			}
			if(!canRender)
				continue;
			
			glClear(GL_COLOR_BUFFER_BIT ); // clear the framebuffer
			
			projection.translate(0.01f, 0, 0);
		
			textureList.get(1).bind(0);
				shader.bind();
				shader.setUniform("sampler",0);
				shader.setUniform("projection",projection);
				
				model.render();
			win.swapBuffers();
			 
			
		}
		glfwTerminate();
	}

	private float[] getVerticesOfGrafix() {

		return new float[] {
		1,1, //Unten Rechts
		-1,1,//Unten Links
		1,-1,//Oben Rechts
		
		1,-1,//Oben Rechts
		-1,1,//Unten Links
		-1,-1,//Oben links
};
	}

//	private void draw(VisibleGrafix gfx, float winWidth,float winHeight) {
//		
//		Point2D ol = new Point2D(gfx.getLocation().x/winWidth, gfx.getLocation().y/winHeight);
//		
//		Point2D or = new Point2D(gfx.getLocation().x/winWidth + gfx.getImage(0).getWidth()/winWidth, gfx.getLocation().y/winHeight);
//		Point2D ur = new Point2D(gfx.getLocation().x/winWidth + gfx.getImage(0).getWidth()/winWidth, gfx.getLocation().y/winHeight+ gfx.getImage(0).getHeight()/winHeight);
//		Point2D ul = new Point2D(gfx.getLocation().x/winWidth, gfx.getLocation().y/winHeight+ gfx.getImage(0).getHeight()/winHeight);
//	
//		
//		drawConvertToSignedGLCoordinates(ol,or,ur,ul);
//	}
//
//	private void drawConvertToSignedGLCoordinates(Point2D ol,Point2D or,Point2D ur,Point2D ul) {
//		glBegin(GL_QUADS);	
//			glTexCoord2f(1f, 0);
//			glVertex2f(ur.x*2-1,(ur.y*2-1));
//			
//			glTexCoord2f(0f, 0f);
//			glVertex2f(ul.x*2-1,(ul.y*2-1));
//			
//			glTexCoord2f(0, 1f);
//			glVertex2f(ol.x*2-1,(ol.y*2-1));
//			
//			glTexCoord2f(1, 1);
//			glVertex2f(or.x*2-1,(or.y*2-1));
//		glEnd();
//	}

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
