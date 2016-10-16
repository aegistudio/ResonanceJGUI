package net.aegistudio.resonance.jui.util;

import javax.swing.JInternalFrame;

@SuppressWarnings("serial")
public class Subwindow extends JInternalFrame {
	public Subwindow() {
		super.setResizable(true);
		super.setClosable(true);
		super.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/** Invoke on resonance tick, updating rulers. **/
	public void resonanceTick() {	}
}
