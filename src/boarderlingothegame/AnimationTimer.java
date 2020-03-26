package boarderlingothegame;

import java.util.HashMap;
import java.util.Map;

public class AnimationTimer {
	int incrementEachFrameNumber = 0;
	
	Map<String,Integer> whichAnimationRunsHowLong = new HashMap();

	int getFrame() {
		return incrementEachFrameNumber;
	}
	Integer getFrame(String time) {
		Integer frame = whichAnimationRunsHowLong.get(time);
		if(frame == null)
			return Integer.MAX_VALUE;
		return incrementEachFrameNumber - frame;
	}
	public void startAnimation(String time) {
		whichAnimationRunsHowLong.put(time, incrementEachFrameNumber);
	}
	public void increment() {
		incrementEachFrameNumber++;
	}
}
