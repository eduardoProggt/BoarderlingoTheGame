package boarderlingothegame.controller;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import glgfxinterface.Window;

public class InputController {

	int space,keyV;
	
	public boolean spaceTipped() {
		boolean retval=false;
		if(space == 0 && glfwGetKey(Window.getInstance().getWinId(),GLFW_KEY_SPACE) == 1)
			retval = true;
		space = glfwGetKey(Window.getInstance().getWinId(),GLFW_KEY_SPACE);
		return retval;
	}
	public boolean keyVTipped() {
		boolean retval=false;
		if(keyV == 0 && glfwGetKey(Window.getInstance().getWinId(),GLFW_KEY_V) == 1)
			retval = true;
		keyV = glfwGetKey(Window.getInstance().getWinId(),GLFW_KEY_V);
		return retval;
	}
	public boolean downPressed() {
		return glfwGetKey(Window.getInstance().getWinId(),GLFW_KEY_DOWN) == 1;
	}
	public boolean leftPressed() {
		return glfwGetKey(Window.getInstance().getWinId(),GLFW_KEY_LEFT) == 1;
	}	
  
}
