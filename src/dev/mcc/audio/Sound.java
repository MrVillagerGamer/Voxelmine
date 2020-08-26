package dev.mcc.audio;

import java.io.File;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import dev.mcc.Voxelmine;
import dev.mcc.util.IDisposable;

public class Sound implements IDisposable{
	public static final ArrayList<Sound> SOUNDS = new ArrayList<Sound>();
	public static final Sound DIRT = new Sound("res/ogg/dirt.ogg");
	public static final Sound SAND = new Sound("res/ogg/sand.ogg");
	private int handle = -1;
	private String path;
	public Sound(String path) {
		this.path = path;
		File file = new File(path);
		if(!file.exists()) this.path = null;
		IDisposable.disposeOnExit(this);
		SOUNDS.add(this);
	}
	public void init() {
		if(path != null) {
			String fileName = path;
			MemoryStack.stackPush();
			IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);
			MemoryStack.stackPush();
			IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);
			ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(fileName, channelsBuffer, sampleRateBuffer);
			int channels = channelsBuffer.get();
			int sampleRate = sampleRateBuffer.get();
			MemoryStack.stackPop();
			MemoryStack.stackPop();
			int format = -1;
			if(channels == 1) {
			    format = AL10.AL_FORMAT_MONO16;
			} else if(channels == 2) {
			    format = AL10.AL_FORMAT_STEREO16;
			}
			handle = AL10.alGenBuffers();
			AL10.alBufferData(handle, format, rawAudioBuffer, sampleRate);
		}
	}
	public void play() {
		if(path != null && Voxelmine.getSoundManager().isActive()) {
			int sourceHandle = Voxelmine.getSoundManager().getSource();
			AL10.alSourcei(sourceHandle, AL10.AL_BUFFER, handle);
			AL10.alSourcePlay(sourceHandle);
		}
	}
	@Override
	public void dispose() {
		if(path != null && Voxelmine.getSoundManager().isActive()) {
			AL10.alDeleteBuffers(handle);
		}
	}
}
