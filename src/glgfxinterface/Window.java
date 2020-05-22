package glgfxinterface;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;

public class Window {
	private long window;
	private int height, width;
	public Window(int width, int height) {
		setSize(width,height);
	}
	
	public void createWindow(String title) {
		
		glfwDefaultWindowHints(); 
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE); 
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); 
		
		window = glfwCreateWindow(getWidth(), getHeight(), title, 0, 0);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
	
		
		glfwMakeContextCurrent(window);
		
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
		glfwShowWindow(window);
	}
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	public long getWinId() {
		return window;
	}
	private void setSize(int _width, int _height) {
		width = _width;
		height = _height;
	}

	public void destroy() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		
	}

	public void swapBuffers() {
		glfwSwapBuffers(window);
		
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
