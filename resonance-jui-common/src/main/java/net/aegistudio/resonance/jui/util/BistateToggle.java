package net.aegistudio.resonance.jui.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public abstract class BistateToggle extends JLabel {
	ImageIcon offState;
	ImageIcon onState;
	
	boolean currentState;
	
	public BistateToggle(ImageIcon offState, ImageIcon onState) {
		this.offState = offState;
		this.onState = onState;
		
		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) try {
					if(currentState) toggleOff();
					else toggleOn();
					
					stateChange(!currentState);
				}
				catch(Exception ex) {
					JOptionPane.showConfirmDialog(null, ex.getMessage(), "State update failed!",
							JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		this.stateChange(false);
	}
	
	
	public void stateChange(boolean on) {
		this.currentState = on;
		this.setIcon(on? this.onState : this.offState);
	}
	
	protected abstract void toggleOn() throws Exception;
	
	protected abstract void toggleOff() throws Exception;
}
