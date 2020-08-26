package dev.mcc.render;

import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import dev.mcc.util.IDisposable;
import dev.mcc.util.IRenderable;

public abstract class Mesh implements IDisposable, IRenderable {
	private int vbo = -1, len = 0;
	public Mesh() {
		
	}
	public Mesh(Vector3f[] v, Vector3f[] n, Vector3f[] c, Vector2f[] t, Vector3f[] u, Vector3f[] b) {
		load(v, n, c, t, u, b);
	}
	@Override
	public void dispose() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(vbo);
	}
	public void load(Vector3f[] v, Vector3f[] n, Vector3f[] c, Vector2f[] t, Vector3f[] u, Vector3f[] b) {
		if(vbo != -1) {
			IDisposable.disposeNow(this);
		}
		len = v.length;
		float[] data = new float[v.length*17];
		for(int i = 0; i < data.length; i+=17) {
			data[i+0] = v[i/17].x;
			data[i+1] = v[i/17].y;
			data[i+2] = v[i/17].z;
			data[i+3] = n[i/17].x;
			data[i+4] = n[i/17].y;
			data[i+5] = n[i/17].z;
			data[i+6] = c[i/17].x;
			data[i+7] = c[i/17].y;
			data[i+8] = c[i/17].z;
			data[i+9] = t[i/17].x;
			data[i+10] = t[i/17].y;
			data[i+11] = u[i/17].x;
			data[i+12] = u[i/17].y;
			data[i+13] = u[i/17].z;
			data[i+14] = b[i/17].x;
			data[i+15] = b[i/17].y;
			data[i+16] = b[i/17].z;
		}
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		IDisposable.disposeOnExit(this);
	}
	@Override
	public void render() {
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 17*4, 0*4);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 17*4, 3*4);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 17*4, 6*4);
		glVertexAttribPointer(3, 2, GL_FLOAT, false, 17*4, 9*4);
		glVertexAttribPointer(4, 3, GL_FLOAT, false, 17*4, 11*4);
		glVertexAttribPointer(5, 3, GL_FLOAT, false, 17*4, 14*4);
		
		glDrawArrays(GL_TRIANGLES, 0, len);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
}
