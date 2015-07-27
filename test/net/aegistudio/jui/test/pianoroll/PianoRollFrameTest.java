package net.aegistudio.jui.test.pianoroll;

import javax.swing.UIManager;

import net.aegistudio.resonance.jui.pianoroll.PianoRoll;

public class PianoRollFrameTest {
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		
		PianoRoll pianoRoll = new PianoRoll(new PianoRollModelDecoy());
		pianoRoll.setVisible(true);
	}
}
