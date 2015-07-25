package net.aegistudio.jui.test.arranger;

import javax.swing.JFrame;
import javax.swing.UIManager;

import net.aegistudio.resonance.channel.MidiChannel;
import net.aegistudio.resonance.jui.arranger.ChannelSection;
import net.aegistudio.resonance.jui.arranger.InstrumentSection;

public class ChannelSectionTest {
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		
		ChannelSection ch = new InstrumentSection(null, "Channel", new MidiChannel(null));
		JFrame jf =  new JFrame();
		jf.add(ch);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(200, 100);
		jf.setVisible(true);
		while(true)
		{
			jf.repaint();
			Thread.sleep(200L);
		}
	}
}
