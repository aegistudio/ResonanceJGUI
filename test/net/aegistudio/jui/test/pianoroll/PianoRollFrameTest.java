package net.aegistudio.jui.test.pianoroll;

import javax.swing.UIManager;

import net.aegistudio.resonance.channel.Score;
import net.aegistudio.resonance.jui.pianoroll.PianoRoll;
import net.aegistudio.resonance.jui.pianoroll.PianoRollLogic;

public class PianoRollFrameTest {
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		
		//PianoRoll pianoRoll = new PianoRoll(new PianoRollModelDecoy());
		PianoRoll pianoRoll = new PianoRoll(new PianoRollLogic(new Score()));
		pianoRoll.setVisible(true);
	}
}
