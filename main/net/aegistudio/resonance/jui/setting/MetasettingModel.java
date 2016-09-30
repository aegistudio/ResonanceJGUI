package net.aegistudio.resonance.jui.setting;

import javax.sound.sampled.AudioFormat;

import net.aegistudio.resonance.Environment;
import net.aegistudio.resonance.device.MixerDevice;

public interface MetasettingModel
{
	public MixerDevice getOutputDeviceInUse();
	
	public Environment getCurrentEnvironment();
	
	public MixerDevice[] getOutputDevices();
	
	public AudioFormat[] getSupportedFormat(MixerDevice device);
	
	public int getBufferSize();
	
	public int getBufferAmount();
	
	public void applyOutputSetting(MixerDevice outputDevice, 
			AudioFormat outputformat, int bufferLevel, int bufferSizeLevel);

	public AudioFormat getOutputFormat();
}
