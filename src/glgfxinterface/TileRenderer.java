package glgfxinterface;

import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;


public class TileRenderer {
	private HashMap<String, Texture> tileTextures = new HashMap<>();
	private Model tileModel;
	private Shader shader = new Shader("shader");
	public TileRenderer() {
		float[] vertices = new float[]{
				1,1f, //Oben Rechts
				-1,1f,//oben Links
				1,-1f,//Unten Rechts
				
				1,-1f,//Unten Rechts
				-1,1f,//oben Links
				-1,-1f,//Unten links
		};
		
		float[] texture = new float[]{
				1,0,
				0,0,
				1,1,
				
				1,1,
				0,0,
				0,1
				};
		
		setTileModel(new Model(vertices, texture));
		
	}
	


	public void renderTile(Tile tile, int x, int y, Matrix4f matrix, Window win) {
		y = -y;//Gl ist dumm.
		shader.bind();
		if (!getTileTextures().containsKey(tile.getTexture())) 
			getTileTextures().put(tile.getTexture(), new Texture(tile.getTexture()));
			
		getTileTextures().get(tile.getTexture()).bind(0);
		matrix.mul(tile.getStdMat());
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", matrix.translate((float)x/(tile.getWidth()/2),(float)y/(tile.getHeigth()/2),0));
		
		getTileModel().render();
	}
	
	
	private Model getTileModel() {
		return tileModel;
	}
	
	
	private void setTileModel(Model model) {
		tileModel = model;
	}

	public HashMap<String, Texture> getTileTextures() {
		return tileTextures;
	}

	public void setTileTextures(HashMap<String, Texture> tileTextures) {
		this.tileTextures = tileTextures;
	}
}
