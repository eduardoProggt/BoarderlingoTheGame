package boarderlingothegame;

import java.util.HashMap;
import java.util.Map;

public class AnimationTimer {
	
	private int incrementEachFrameNumber = 0;
	private Map<String,Integer> whichAnimationRunsHowLong = new HashMap();
	private static AnimationTimer singleton;
	
	private AnimationTimer() {};
	
	public static synchronized AnimationTimer getInstance() {
		if (singleton == null)
			singleton=new AnimationTimer();
		return singleton;
	}

	int getFrame() {
		return incrementEachFrameNumber;
	}
	public Integer getFrame(String key) {
		Integer frame = whichAnimationRunsHowLong.get(key);
		if(frame == null)
			return Integer.MAX_VALUE;
		return incrementEachFrameNumber - frame;
	}
	public void startAnimation(String key) {
		whichAnimationRunsHowLong.put(key, incrementEachFrameNumber);
	}
	public void increment() {
		incrementEachFrameNumber++;
	}
	public void resetToFrame(String key, int frame){
		whichAnimationRunsHowLong.put(key, incrementEachFrameNumber+frame);
	}
}
