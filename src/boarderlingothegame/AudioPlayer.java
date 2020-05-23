package boarderlingothegame;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioPlayer {

    private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;

    /**
     * @param filename the name of the file that is going to be played
     */
    public void playAudio(String filename){

        String strFilename = filename;

        try {
            soundFile = new File(strFilename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }
}
//
//import java.io.File;
//import java.io.IOException;
//
//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;
//import javax.sound.sampled.DataLine;
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.UnsupportedAudioFileException;
//
//public class AudioPlayer {
//	public static void playAudio(String a) {
//	AudioInputStream audioInputStream;
//	try {
//		audioInputStream = AudioSystem.getAudioInputStream(new File(a));
//
//    AudioFormat af     = audioInputStream.getFormat();
//    int size      = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
//    byte[] audio       = new byte[size];
//    DataLine.Info info      = new DataLine.Info(Clip.class, af, size);
//    audioInputStream.read(audio, 0, size);
//    
//   
//    Clip clip = (Clip) AudioSystem.getLine(info);
//    clip.open(af, audio, 0, size);
//    clip.start();
//    
//    clip.close();
//    audioInputStream.close();
//	} catch (UnsupportedAudioFileException e) {
//		System.out.println("Muss ne .wav - File sein.");
//		e.printStackTrace();
//	} catch (IOException e) {
//		e.printStackTrace();
//	} catch (LineUnavailableException e) {
//		e.printStackTrace();
//	}
//	}
//	
//}
