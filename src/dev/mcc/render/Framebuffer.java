package dev.mcc.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector2f;

import dev.mcc.util.IDisposable;

public class Framebuffer implements IDisposable{
	private Vector2f textureSize;
	private int depthMap = -1, depthMapFBO = -1;
	public Framebuffer(int x, int y) {
		this.textureSize = new Vector2f(x, y);
		depthMapFBO = glGenFramebuffers();  
		depthMap = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, depthMap);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, 
				(int)textureSize.x, (int)textureSize.y, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); 
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);  
		glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap, 0);
		glDrawBuffer(GL_NONE);
		glReadBuffer(GL_NONE);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);  
		IDisposable.disposeOnExit(this);
	}
	public Vector2f getTextureSize() {
		return textureSize;
	}
	public void bindTexture() {
		glBindTexture(GL_TEXTURE_2D, depthMap);
	}
	public void unbindTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
		
	}
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	@Override
	public void dispose() {
		glDeleteFramebuffers(depthMapFBO);
		glDeleteTextures(depthMap);
	}
}
