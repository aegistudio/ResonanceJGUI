package net.aegistudio.resonance.jui.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;

@SuppressWarnings("serial")
public class SubwindowShowItem extends JCheckBoxMenuItem {	
	
	Component window;
	public SubwindowShowItem(String name, Subwindow window) {
		super(name);
		this.window = window;
		
		super.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(!window.isVisible()) {
						//window.show();
						window.setVisible(true);
						setSelected(true);
					}
					else {
						//window.hide();
						window.setVisible(false);
						setSelected(false);
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void checkWindowStatus() {
		this.setSelected(window.isVisible());
	}
}
