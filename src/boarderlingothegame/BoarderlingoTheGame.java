
package boarderlingothegame;

import glgfxinterface.GfxFassade;

public class BoarderlingoTheGame implements Runnable{

	private GfxFassade gfx;

	public BoarderlingoTheGame(boolean isOnTwitch) {
		Thread t = new Thread (this, "MainLoop");
        t.start();
	}

	public static void main(String[] args) {
		new BoarderlingoTheGame(false);
	}

	public void handleTwitchMessage(String message) {
		String nameOfPurchaser = message.split(" ")[0];
		String order = message.split(" ")[2];
		
		gfx.handleNewTwitchOrder(order,nameOfPurchaser);
	}

	@Override
	public void run() {
		gfx = GfxFassade.createInstance();
		gfx.run(1900, 650, "BoarderlingoTheGame");
		
	}
}