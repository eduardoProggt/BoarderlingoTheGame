package boarderlingothegame.controller;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Controller {

	
    public void buttonDown(KeyEvent keyEvent){
    	if(keyEvent.getKeyCode()==KeyEvent.VK_LEFT)
    		updatePressed(ButtonsEnum.LEFT);
        if(keyEvent.getKeyCode()==KeyEvent.VK_RIGHT)
    		updatePressed(ButtonsEnum.RIGHT);
        
        if(keyEvent.getKeyCode()==KeyEvent.VK_DOWN)
    		updatePressed(ButtonsEnum.DOWN);
        
        if(keyEvent.getKeyCode()==KeyEvent.VK_SPACE)
    		updatePressed(ButtonsEnum.SPACE);
        
        if(keyEvent.getKeyCode()==KeyEvent.VK_R)
    		updatePressed(ButtonsEnum.SPECIAL);
    }

	public void buttonUp(KeyEvent keyEvent) {
    if(keyEvent.getKeyCode()==KeyEvent.VK_LEFT)
		updateReleased(ButtonsEnum.LEFT);
    if(keyEvent.getKeyCode()==KeyEvent.VK_RIGHT)
		updateReleased(ButtonsEnum.RIGHT);
    
    if(keyEvent.getKeyCode()==KeyEvent.VK_DOWN)
		updateReleased(ButtonsEnum.DOWN);
    
    if(keyEvent.getKeyCode()==KeyEvent.VK_SPACE)
		updateReleased(ButtonsEnum.SPACE);
    
    if(keyEvent.getKeyCode()==KeyEvent.VK_R)
		updateReleased(ButtonsEnum.SPECIAL);
    }
	
	public boolean isPressed(ButtonsEnum button) {
		if(!pressedKeysMap.containsKey(button))
			return false;
		return pressedKeysMap.get(button).booleanValue();
	}
	
    private void updatePressed(ButtonsEnum button) {
			pressedKeysMap.put(button, Boolean.TRUE);
	}
    private void updateReleased(ButtonsEnum button) {
			pressedKeysMap.put(button, Boolean.FALSE);
	}
	private Map<ButtonsEnum,Boolean> pressedKeysMap = new HashMap();
	
	
	public void resetButtons() {
		for(ButtonsEnum e : ButtonsEnum.values()) 
			updateReleased(e);
	}
}
