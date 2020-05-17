package glgfxinterface;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Shader {

	int program;
	int fragmentShader;
	int vertexShader;
	
	public Shader(String filename) {
		super();
		program = glCreateProgram();
		runVertexShader(filename);
		runFragmentShader(filename);
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		
		glBindAttribLocation(program, 0, "vertices");
		glBindAttribLocation(program, 1, "textures");
		
		glLinkProgram(program);
		if(glGetProgrami(program, GL_LINK_STATUS)!=1) {
			System.err.println(glGetProgramInfoLog(program));
			System.exit(1);}
		glValidateProgram(program);
		if(glGetProgrami(program, GL_VALIDATE_STATUS)!=1) {
			System.err.println(glGetProgramInfoLog(program));
			System.exit(1);}
	}
	public void setUniform(String name, int value) {
		int location = glGetUniformLocation(program, name);
		if(location!=-1) 
			glUniform1i(location, value);
		
	}
	public void setUniform(String name, Matrix4f value) {
		int location = glGetUniformLocation(program, name);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4*4);
		value.get(buffer);
		
		if(location!=-1) 
			glUniformMatrix4fv(location,false, buffer);
		
	}
	public void bind() {
		glUseProgram(program);
	}
	
	private void runVertexShader(String filename) {
		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, readFile(filename+".vs"));
		glCompileShader(vertexShader);
		if(glGetShaderi(vertexShader, GL_COMPILE_STATUS)!=1) {
			System.err.println("VERTEXSCHADER "+glGetShaderInfoLog(vertexShader));
			System.exit(1);
		}
	}


	private void runFragmentShader(String filename) {
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, readFile(filename+".fs"));
		glCompileShader(fragmentShader);
		if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS)!=1) {
			System.err.println("FRAGMENTSHADER "+glGetShaderInfoLog(fragmentShader));
			System.exit(1);
		}
	}
	private String readFile(String fileName) {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(new File("./shaders/"+fileName)));
			String line;
			while((line = reader.readLine())!=null) {
				stringBuilder.append(line);
				stringBuilder.append("\n");
			}
			reader.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
	

	
	
}
