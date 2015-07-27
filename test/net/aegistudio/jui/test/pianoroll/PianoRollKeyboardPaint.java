package net.aegistudio.jui.test.pianoroll;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;

import net.aegistudio.resonance.jui.pianoroll.BlackKeyComponent;
import net.aegistudio.resonance.jui.pianoroll.WhiteKeyComponent;

public class PianoRollKeyboardPaint {
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		
		JFrame jframe = new JFrame();
		jframe.setTitle("piano roll render");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jframe.setLayout(new GridLayout(12, 1));
		
		jframe.add(new WhiteKeyComponent(true, false));		//B
		jframe.add(new BlackKeyComponent());
		jframe.add(new WhiteKeyComponent(false, false));	//A
		jframe.add(new BlackKeyComponent());
		jframe.add(new WhiteKeyComponent(false, false));	//G
		jframe.add(new BlackKeyComponent());
		jframe.add(new WhiteKeyComponent(false, true));		//F
		jframe.add(new WhiteKeyComponent(true, false));		//E		
		jframe.add(new BlackKeyComponent());
		jframe.add(new WhiteKeyComponent(false, false));	//D
		jframe.add(new BlackKeyComponent());
		jframe.add(new WhiteKeyComponent(false, true));		//C
		
		jframe.setVisible(true);
	}
}
