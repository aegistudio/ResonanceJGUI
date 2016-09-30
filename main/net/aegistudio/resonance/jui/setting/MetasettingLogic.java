package net.aegistudio.resonance.jui.setting;

import javax.sound.sampled.AudioFormat;

import net.aegistudio.resonance.Environment;
import net.aegistudio.resonance.Resonance;
import net.aegistudio.resonance.device.MixerDevice;
import net.aegistudio.resonance.device.OutputDevice;
import net.aegistudio.resonance.jui.Main;

public class MetasettingLogic implements MetasettingModel
{
	private final Resonance resonance;
	
	public MetasettingLogic(Resonance resonance) {
		this.resonance = resonance;
	}
	
	@Override
	public Environment getCurrentEnvironment() {
		return this.resonance.getEnvironment();
	}
	
	@Override
	public MixerDevice getOutputDeviceInUse() {
		OutputDevice outputDevice = 
				this.resonance.getIntendOutputDevice();
		if(outputDevice instanceof MixerDevice)
			return (MixerDevice) outputDevice;
		else return null;
	}

	@Override
	public AudioFormat getOutputFormat() {
		return resonance.getEnvironment().getAudioFormat();
	}
	
	@Override
	public void applyOutputSetting(MixerDevice outputDevice, AudioFormat format,
			int bufferLevel, int bufferSizeLevel) {
		Environment environment = new Environment(format, 1 << bufferSizeLevel, 1 << bufferLevel);
		this.resonance.setEnvironment(environment, outputDevice);
	}

	@Override
	public MixerDevice[] getOutputDevices() {
		return Main.devices;
	}

	@Override
	public AudioFormat[] getSupportedFormat(MixerDevice device) {
		AudioFormat[] format = Main.formats.get(device);
		if(format != null) return format;
		else return device.getAvailableFormats();
	}
	
	@Override
	public int getBufferSize() {
		return Integer.numberOfTrailingZeros(resonance.getEnvironment().samplesPerFrame);
	}

	@Override
	public int getBufferAmount() {
		return Integer.numberOfTrailingZeros(resonance.getEnvironment().prerenderFactor);
	}

}
