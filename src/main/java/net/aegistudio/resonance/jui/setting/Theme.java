package net.aegistudio.resonance.jui.setting;

import java.awt.Component;
import javax.swing.ImageIcon;

public interface Theme {
	public void preset();
	
	public void configure(Component component, String componentId);

	public ImageIcon rawIcon(String name);
}
