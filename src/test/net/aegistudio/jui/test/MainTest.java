package net.aegistudio.jui.test;

import javax.sound.sampled.AudioSystem;

import net.aegistudio.resonance.Encoding;
import net.aegistudio.resonance.Environment;
import net.aegistudio.resonance.Resonance;
import net.aegistudio.resonance.device.MixerDevice;
import net.aegistudio.resonance.io.OutputController;
import net.aegistudio.resonance.io.OutputFacade;
import net.aegistudio.resonance.jui.Main;
import net.aegistudio.resonance.jui.arranger.ArrangerLogic;
import net.aegistudio.resonance.jui.arranger.ArrangerModel;
import net.aegistudio.resonance.jui.resource.ResourceLogic;
import net.aegistudio.resonance.jui.resource.ResourceModel;
import net.aegistudio.resonance.jui.setting.DefaultTheme;
import net.aegistudio.resonance.music.MusicController;

public class MainTest {
	public static void main(String[] arguments) throws Exception
	{
		OutputFacade outputLayer = new OutputController();
		MusicController musicController = new MusicController();
		Resonance resonance = new Resonance(outputLayer, musicController.dataflowController, musicController);
		
		ResourceModel resourceModel = new ResourceLogic(musicController.getScoreHolder());
		ArrangerModel arrangerModel = new ArrangerLogic(musicController, resourceModel);
		
		Main main = new Main(new DefaultTheme(), resonance, arrangerModel, resourceModel);
		main.setVisible(true);
		
		resonance.setEnvironment(new Environment(44100.0f, 2, new Encoding(Encoding.BITDEPTH_BIT32 | Encoding.WORDTYPE_INT | Encoding.ENDIAN_BIG),
				128, 16), new MixerDevice(AudioSystem.getMixerInfo()[0]));
	}
}
