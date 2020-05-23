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
	private Model scoreModel;
	private Shader shader = new Shader("shader");
	private Model highScoreModel;
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
		scoreModel = new Model(new float[]{
				6f/28f,0,
				0,0,
				6f/28f,1,
				
				6f/28f,1,
				0,0,
				0,1
				}, new float[]{
						6f/28f,0,
						0,0,
						6f/28f,1,
						
						6f/28f,1,
						0,0,
						0,1
						});
		highScoreModel = new Model(new float[]{
				0.33f,0,
				0,0,
				0.33f,1,
				
				0.33f,1,
				0,0,
				0,1
				}, new float[]{
						0.58f,0,
						0.24f,0,
						0.58f,1,
						
						0.58f,1,
						0.24f,0,
						0.24f,1
						});
		
		
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
		scoreModel.render();
		new Model(getKoords(7f/29f,8f/29f) , getKoords((float)(18+ highScore.getScore()/10000%10)/29f,(float)(19+ highScore.getScore()/10000%10)/29f)).render();
		new Model(getKoords(8f/29f,9f/29f) , getKoords((float)(18+ highScore.getScore()/1000%10)/29f,(float)(19+ highScore.getScore()/1000%10)/29f)).render();
		new Model(getKoords(9f/29f,10f/29f) , getKoords((float)(18+ highScore.getScore()/100%10)/29f,(float)(19+ highScore.getScore()/100%10)/29f)).render();
		new Model(getKoords(10f/29f,11f/29f) , getKoords((float)(18+ highScore.getScore()/10%10)/29f,(float)(19+ highScore.getScore()/10%10)/29f)).render();
		new Model(getKoords(11f/29f,12f/29f) , getKoords((float)(18+ highScore.getScore()%10)/29f,(float)(19+ highScore.getScore()%10)/29f)).render();
		shader.setUniform("projection",HIGHSCOREMAT );
		highScoreModel.render();
		new Model(getKoords(11f/29f,12f/29f) , getKoords((float)(18+ highScore.getHighscore()/10000%10)/29f,(float)(19+ highScore.getHighscore()/10000%10)/29f)).render();
		new Model(getKoords(12f/29f,13f/29f) , getKoords((float)(18+ highScore.getHighscore()/1000%10)/29f,(float)(19+ highScore.getHighscore()/1000%10)/29f)).render();
		new Model(getKoords(13f/29f,14f/29f) , getKoords((float)(18+ highScore.getHighscore()/100%10)/29f,(float)(19+ highScore.getHighscore()/100%10)/29f)).render();
		new Model(getKoords(14f/29f,15f/29f) , getKoords((float)(18+ highScore.getHighscore()/10%10)/29f,(float)(19+ highScore.getHighscore()/10%10)/29f)).render();
		new Model(getKoords(15f/29f,16f/29f) , getKoords((float)(18+ highScore.getHighscore()%10)/29f,(float)(19+ highScore.getHighscore()%10)/29f)).render();

		//Das ist alles ganz dolle dirty.
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
