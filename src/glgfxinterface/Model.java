package glgfxinterface;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Model {
	
	private int draw_count;
	private int vertex_id;
	private int texture_id;
	
	public Model(float[] vertices,float[] texture_coords) {
		draw_count = vertices.length/2;
		
		
		vertex_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertex_id);
		glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);
		
		texture_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, texture_id);
		glBufferData(GL_ARRAY_BUFFER, createBuffer(texture_coords), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
	}
	private FloatBuffer createBuffer(float[] vertices) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
		buffer.put(vertices);
		buffer.flip();
		return buffer;
	}
	public void render() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, vertex_id);
		glVertexAttribPointer(0, 2, GL_FLOAT,false,0,0);

		glBindBuffer(GL_ARRAY_BUFFER, texture_id);
		glVertexAttribPointer(1, 2, GL_FLOAT,false,0,0);
		
		
		glDrawArrays(GL_TRIANGLES, 0, draw_count);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
}
