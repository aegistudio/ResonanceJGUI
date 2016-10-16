package net.aegistudio.resonance.jui.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class RecursivePanel extends Container {
	public RecursivePanel() {
		this.setLayout(null);
	}
	
	boolean dirty = true;
	public Component add(Component c) {
		Component result = super.add(c);
		recalculateSize();
		return result;
	}
	
	public void remove(Component c) {
		super.remove(c);
		recalculateSize();
	}
	
	Dimension preferredSize = new Dimension();
	public void recalculateSize() {
		int width = 0;
		int height = 0;
		for(Component c : super.getComponents()) {
			c.setLocation(0, height);
			c.setSize(c.getPreferredSize());
			height += c.getSize().height;
			
			if(c.getPreferredSize().width > width)
				width = c.getPreferredSize().width;
		}
		preferredSize.width = width;
		preferredSize.height = height;
		setPreferredSize(preferredSize);
		setSize(preferredSize);
	}
}
