package net.aegistudio.jui.test.pianoroll;


import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.UIManager;

import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.pianoroll.KeyboardStrip;
import net.aegistudio.scroll.RowPanel;
import net.aegistudio.scroll.ScrollPane;

public class PianoRollPanelPaint {
	@SuppressWarnings("serial")
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		
		JFrame jframe = new JFrame();
		jframe.setTitle("piano roll render");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MeasureRuler ruler = new MeasureRuler();
		RowPanel rowPanel = new RowPanel();
		
		
		ScrollPane pianoRoll = new ScrollPane(ruler, rowPanel)
		{
			{
				super.viewPanel.setBackground(Color.WHITE);
			}
		};
		jframe.add(pianoRoll);
		
		rowPanel.setSectionWidth(KeyboardStrip.sectionWidth);
		
		for(int i = 127; i >= 0; i --)
			rowPanel.addRowContent(new KeyboardStrip(null, i, ruler));
		
		jframe.setSize(800, 500);
		
		jframe.setVisible(true);
		
		pianoRoll.getVerticalScrollBar().setValue((127 - 4 * 12) * KeyboardStrip.sectionHeight);
		while(true)
		{
			jframe.repaint();
			Thread.sleep(100L);
		}
		
	}
}
