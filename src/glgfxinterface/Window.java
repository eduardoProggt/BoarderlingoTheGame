package glgfxinterface;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;

public class Window {
	private long windowID;
	private int height, width;
	private static Window window;
	
	private Window() {

	}
	public static Window getInstance() {
		if(window ==null)
			window = new Window();
		return window;
	}
	public void createWindow(String title,int width,int height) {
		setSize(width,height);
		glfwDefaultWindowHints(); 
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE); 
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); 
		
		windowID = glfwCreateWindow(getWidth(), getHeight(), title, 0, 0);
		if ( windowID == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
	
		
		glfwMakeContextCurrent(windowID);
		
		glfwSetKeyCallback(windowID, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
		glfwShowWindow(windowID);
	}
	public boolean shouldClose() {
		return glfwWindowShouldClose(windowID);
	}
	
	public long getWinId() {
		return windowID;
	}
	private void setSize(int _width, int _height) {
		width = _width;
		height = _height;
	}

	public void destroy() {
		glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);
		
	}

	public void swapBuffers() {
		glfwSwapBuffers(windowID);
		
	}
	public static void setCallbacks() {
		glfwSetErrorCallback(new GLFWErrorCallbackI() {
			
			@Override
			public void invoke(int errorCode, long description) {
				throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
			}
		});
		
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
}
