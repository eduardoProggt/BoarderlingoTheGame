package glgfxinterface;

import org.joml.Matrix4f;

public class Tile {
	public static Tile tiles[] = new Tile[255];
	public static byte not = 0;
	
	private byte id;
	private String textureString;
	private int width, heigth;
	private Matrix4f stdMat;
	
	public Tile(String texture, int width, int height) {
		setWidth(width);
		setHeigth(height);
		stdMat = createStandardMat(1900, 650, getWidth(), getHeigth());
		this.id = not;
		not++;
		this.textureString = texture;
		if (tiles[id] != null) 
			throw new IllegalStateException("Tiles at [" + id + "] is already being used!");
		tiles[id] = this;
	}
	private Matrix4f createStandardMat(float fensterX, float fensterY, float breite, float hoehe) {
		float halbesFensterX = fensterX/2;
		float halbesFensterY = fensterY/2;
		Matrix4f projection = new Matrix4f();
		projection.translate(-(halbesFensterX-(breite/2))/halbesFensterX,(halbesFensterY-(hoehe/2) )/halbesFensterY,0);
		projection.scale(breite/fensterX,hoehe/fensterY,1);
		return projection;
	}
	public byte getId() {
		return id;
	}
	
	public String getTexture() {
		return textureString;
	}

	public int getHeigth() {
		return heigth;
	}

	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	public Matrix4f getStdMat() {
		return stdMat;
	}
}
