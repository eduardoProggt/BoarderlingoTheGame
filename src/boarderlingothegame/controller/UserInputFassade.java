package boarderlingothegame.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UserInputFassade {
	
	Controller controller;
	
	public UserInputFassade(Controller _controller){
		controller = _controller;
	}
	
	public KeyAdapter getKeyListener() {
		return new KeyAdapter() 
		{
			public void keyPressed(KeyEvent kp) {
				controller.buttonDown(kp);}
	
			public void keyReleased(KeyEvent kr) {
				controller.buttonUp(kr);}
		};
	}
}

