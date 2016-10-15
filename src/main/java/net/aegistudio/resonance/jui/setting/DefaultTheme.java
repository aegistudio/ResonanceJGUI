package net.aegistudio.resonance.jui.setting;

import java.awt.Component;
import java.lang.reflect.Method;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import net.aegistudio.resonance.jui.Main;

public class DefaultTheme implements Theme {
	@Override
	public void preset() {
		try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); } catch(Exception e) {}
	}

	@Override
	public void configure(Component component, String componentId) {
		try {
			Method setIcon = component.getClass().getMethod("setIcon", Icon.class);
			setIcon.invoke(component, rawIcon(componentId));
		}
		catch(Exception e) {
			
		}
	}

	@Override
	public ImageIcon rawIcon(String name) {
		URL resourceUrl = Main.class.getResource("/" + name);
		if(resourceUrl == null) return null;
		return new ImageIcon(resourceUrl);
	}
}
