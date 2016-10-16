package net.aegistudio.resonance.jui.util;

import java.awt.Component;
import java.lang.reflect.Method;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import net.aegistudio.resonance.jui.MainControl;
import net.aegistudio.resonance.jui.Theme;

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
		URL resourceUrl = MainControl.class.getResource("/" + name);
		if(resourceUrl == null) return null;
		return new ImageIcon(resourceUrl);
	}
}
