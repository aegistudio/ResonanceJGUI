package net.aegistudio.jui.test;

import javax.swing.UIManager;

import net.aegistudio.jui.test.arranger.ArrangerModelDecoy;
import net.aegistudio.resonance.Resonance;
import net.aegistudio.resonance.jui.Main;
import net.aegistudio.resonance.music.MusicController;
import net.aegistudio.resonance.output.OutputController;
import net.aegistudio.resonance.output.OutputFacade;

public class MainTest {
	public static void main(String[] arguments) throws Exception
	{

		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		
		OutputFacade outputLayer = new OutputController();
		MusicController musicController = new MusicController();
		Resonance resonance = new Resonance(outputLayer, musicController.dataflowController, musicController);
		
		Main main = new Main(resonance, new ArrangerModelDecoy());
		main.setVisible(true);
	}
}
