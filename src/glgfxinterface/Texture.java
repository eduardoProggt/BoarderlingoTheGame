package glgfxinterface;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture {
	private int id;
	private int width;
	private int height;
	
	public Texture(String fileString) {
		BufferedImage bufferedImage = null;

			try {
			File input = new File(fileString);
			bufferedImage = ImageIO.read(input);
	
			width = bufferedImage.getWidth();
			height = bufferedImage.getHeight();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int[] pixels_raw = new int[width*height];
			pixels_raw = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
			ByteBuffer pixels = BufferUtils.createByteBuffer(width*height*4);
		
			for (int y=0; y<height; y++){
			    for (int x=0; x< width; x++){
			     int pixel = pixels_raw[y*width+x];
			     
			     pixels.put((byte) ((pixel >> 16) & 0xFF));//RED
			     pixels.put((byte) ((pixel >>  8) & 0xFF));//GREEN
			     pixels.put((byte) ((pixel      ) & 0xFF));//BLUE
			     pixels.put((byte) ((pixel >> 24) & 0xFF));//ALPHA
			     
			    }
			   }
			
			pixels.flip();
			id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);
			
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
	}
	public void bind(int sampler) {
		if(sampler >= 0 && sampler <32){
			glActiveTexture(GL_TEXTURE0+sampler);
			glBindTexture(GL_TEXTURE_2D, id);
		}
	}
}
