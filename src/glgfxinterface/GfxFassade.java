package glgfxinterface;

import java.util.List;

import boarderlingothegame.sprites.VisibleGrafix;

public interface GfxFassade {
	
	void run(int width, int height, String title);
	void update(List<VisibleGrafix> textures);
	static GfxFassade createInstance() {
		return new TestProgramm();
	}
}
