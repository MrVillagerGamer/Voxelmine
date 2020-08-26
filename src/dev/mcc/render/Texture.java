package dev.mcc.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import dev.mcc.util.IDisposable;

public class Texture implements IDisposable {
	private int handle = -1;
	public Texture(String path) {
		handle = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, handle);
		IntBuffer x = BufferUtils.createIntBuffer(1);
		IntBuffer y = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		ByteBuffer pixels = STBImage.stbi_load(path, x, y, channels, 4);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, x.get(0), y.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LOD, 4);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glBindTexture(GL_TEXTURE_2D, 0);
		IDisposable.disposeOnExit(this);
	}
	public int getHandle() {
		return handle;
	}
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, handle);
	}
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	@Override
	public void dispose() {
		glDeleteTextures(handle);
	}
}
