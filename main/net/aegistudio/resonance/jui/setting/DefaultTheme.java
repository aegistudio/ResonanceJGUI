package net.aegistudio.resonance.jui.setting;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

public class DefaultTheme implements Theme {

	@Override
	public ImageIcon makeIcon(String icon) {
		return new ImageIcon("res/" + icon);
	}

	@Override
	public void preset() {
		try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); } catch(Exception e) {}
	}

	@Override
	public void configure(Component component) {
		
	}

}
