package dev.mcc.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

public class SoundManager {
	private long device, context;
	private int handle;
	private boolean active = true;
	public void dispose() {
		if(active) {
			AL10.alDeleteSources(handle);
			ALC10.alcDestroyContext(context);
			ALC10.alcCloseDevice(device);
		}
	}
	public int getSource() {
		return handle;
	}
	public boolean isActive() {
		return active;
	}
	public void init() {
		try{
			String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
			device = ALC10.alcOpenDevice(defaultDeviceName);
			int[] attributes = {0};
			context = ALC10.alcCreateContext(device, attributes);
			ALC10.alcMakeContextCurrent(context);
			ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
			ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
			if(!alCapabilities.OpenAL10) {
				throw new IllegalStateException("Failed to create OpenAL 1.0 context.");
			}
			handle = AL10.alGenSources();
			for(Sound sound : Sound.SOUNDS) {
				sound.init();
			}
		} catch(Exception e) {
			this.active = false;
		}
	}
}
