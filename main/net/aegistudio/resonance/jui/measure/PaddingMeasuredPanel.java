package net.aegistudio.resonance.jui.measure;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class PaddingMeasuredPanel extends MeasuredPanel
{
	public final Component padding;
	public PaddingMeasuredPanel(MeasureRuler ruler, Component sectionPanel) {
		super(ruler, sectionPanel);
		
		super.add(padding = new Component()
		{
			Dimension preferredSize = new Dimension();
			public Dimension getPreferredSize()
			{
				return preferredSize;
			}
			
			public void paint(Graphics g)
			{
				preferredSize.width = PaddingMeasuredPanel.this.getParent().getWidth();
				preferredSize.height = sectionPanel.getPreferredSize().height;
				
				g.setColor(getBackground());
				g.drawRect(0, 0, getWidth() + 4, getHeight() - 1);
			}
		});
		padding.setBackground(Color.GRAY.brighter());
	}
	
	public void recalculateSize()
	{
		super.recalculateSize();
		super.setComponentZOrder(padding, super.getComponentCount() - 1);
	}

}
