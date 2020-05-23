package glgfxinterface;

import java.util.List;

import boarderlingothegame.sprites.VisibleGrafix;

public interface GfxFassade {
	
	void run(int width, int height, String title);
	static GfxFassade createInstance() {
		return new MainProgramm();
	}
}
