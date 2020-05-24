package glgfxinterface;

import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import boarderlingothegame.sprites.HighScore;


public class TileRenderer {
	private static final Matrix4f SCOREMAT = new Matrix4f().translate(-1,1,0).scale(0.6f,-0.12f,1);
	private static final Matrix4f HIGHSCOREMAT = new Matrix4f(SCOREMAT).translate(0,0.8f,0);
	private HashMap<String, Texture> tileTextures = new HashMap<>();
	private Model tileModel;

	private Shader shader = new Shader("shader");
	private Model highScoreModel;
	private float[] vertices = new float[]{
			1,1f, //Oben Rechts
			-1,1f,//oben Links
			1,-1f,//Unten Rechts
			
			1,-1f,//Unten Rechts
			-1,1f,//oben Links
			-1,-1f,//Unten links
	};
	
	private float[] texture = new float[]{
			1,0,
			0,0,
			1,1,
			
			1,1,
			0,0,
			0,1
			};
	
	
	public TileRenderer() {
		
		setTileModel(new Model(vertices, texture));

	}
	public void renderScore(HighScore highScore) {
		//Das ist alles ganz dolle dirty.
		highScore.increment();
		shader.bind();
		Tile tile = highScore.getImage();
		if (!getTileTextures().containsKey(tile.getTexture())) 
			getTileTextures().put(tile.getTexture(), new Texture(tile.getTexture()));
			
		getTileTextures().get(tile.getTexture()).bind(0);
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", SCOREMAT);
		
		getTileModel().changeCoords(getKoords(0f,6f/28f),getKoords(0f,6f/28f));
		getTileModel().render();

		for (float i = 0; i < 5; i++) {
			int anzStellen = (int)Math.pow(10, 4-i);
			getTileModel().changeCoords(getKoords((i+7f)/29f,(i+8f)/29f) , getKoords((float)(18+ (highScore.getScore()/anzStellen)%10)/29f,(float)(19+ (highScore.getScore()/anzStellen)%10)/29f));
			getTileModel().render();	
		}
		shader.setUniform("projection",HIGHSCOREMAT );
		getTileModel().changeCoords(getKoords(0f,0.33f),getKoords(0.24f,0.58f));
		getTileModel().render();
		
		for (float i = 0; i < 5; i++) {
			int anzStellen = (int)Math.pow(10, 4-i);
			getTileModel().changeCoords(getKoords((i+11f)/29f,(i+12f)/29f) , getKoords((float)(18+ (highScore.getHighscore()/anzStellen)%10)/29f,(float)(19+ (highScore.getHighscore()/anzStellen)%10)/29f));
			getTileModel().render();	
		}
		getTileModel().changeCoords(vertices, texture);
	}
	private float[] getKoords(float left, float right) {
		return new float[]{
				right,0,
				left,0,
				right,1,
				
				right,1,
				left,0,
				left,1
				};
	}

	public void renderTile(Tile tile, int x, int y, Matrix4f matrix) {
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
