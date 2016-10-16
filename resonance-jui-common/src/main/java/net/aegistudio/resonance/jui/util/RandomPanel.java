package net.aegistudio.resonance.jui.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import net.aegistudio.resonance.jui.scroll.Slideshow;

@SuppressWarnings("serial")
public class RandomPanel extends Container implements Slideshow
{
	public RandomPanel() {
		this.setLayout(null);
		//this.setOpaque(false);
	}
	
	Dimension preferredSize = new Dimension();
	
	public void recalculateSize() {
		int width = 0;
		int height = 0;
		for(Component c : this.getComponents()) {
			Point l = c.getLocation();
			Dimension s = c.getPreferredSize();
			
			int widthTemp = l.x + s.width;
			int heightTemp = l.y + s.height;
			
			if(widthTemp > width) width = widthTemp;
			if(heightTemp > height) height = heightTemp;
		}
		this.preferredSize.width = width;
		this.preferredSize.height = height;
		this.setPreferredSize(preferredSize);
	}
	
	public void paint(Graphics g) {
		//sildeshow = location + size
		// Draw while anyvertex of c inside slideshow.
		this.recalculateSize();
		
		if(this.isOpaque())	{
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		
		
		Component[] components = this.getComponents();
		for(int i = components.length - 1; i >= 0; i --) {
			Component c = components[i];
			int vc_x0 = c.getX();
			int vc_y0 = c.getY();
			int vc_x1 = vc_x0 + c.getPreferredSize().width;
			int vc_y1 = vc_y0 + c.getPreferredSize().height;
			
			if(this.insideSlideShow(vc_x0, vc_y0) || this.insideSlideShow(vc_x0, vc_y1)
				|| this.insideSlideShow(vc_x1, vc_y0) || this.insideSlideShow(vc_x1, vc_y1)) {
				c.setSize(c.getPreferredSize());
				c.paint(g.create(vc_x0, vc_y0, c.getPreferredSize().width, c.getPreferredSize().height));
			}
		}
	}
	
	public boolean insideSlideShow(int x, int y) {
		return x >= 0 && x <= this.getWidth() && y >= 0 && y <= this.getHeight();
	}
	
	public Component add(Component c) {
		super.add(c);
		repaint();
		return c;
	}
	
	public void remove(Component c) {
		super.remove(c);
		repaint();
	}

	@Override
	public Dimension getLogicSize() {
		return this.preferredSize;
	}
}
