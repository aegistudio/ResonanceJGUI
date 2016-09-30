package net.aegistudio.resonance.jui.setting;

import java.awt.Component;

import javax.swing.ImageIcon;

public interface Theme {
	public void preset();
	
	public ImageIcon makeIcon(String icon);
	
	public void configure(Component component);
}
