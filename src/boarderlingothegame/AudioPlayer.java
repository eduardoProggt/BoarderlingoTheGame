package boarderlingothegame;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
	static void playAudio(String a) {
	AudioInputStream audioInputStream;
	try {
		audioInputStream = AudioSystem.getAudioInputStream(new File(a));

    AudioFormat af     = audioInputStream.getFormat();
    int size      = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
    byte[] audio       = new byte[size];
    DataLine.Info info      = new DataLine.Info(Clip.class, af, size);
    audioInputStream.read(audio, 0, size);
    
   // for(int i=0; i < 32; i++) {
        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.open(af, audio, 0, size);
        clip.start();
   // }
	} catch (UnsupportedAudioFileException e) {
		System.out.println("Muss ne .wav - File sein.");
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (LineUnavailableException e) {
		e.printStackTrace();
	}
	}
}
