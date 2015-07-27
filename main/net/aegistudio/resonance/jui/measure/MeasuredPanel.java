package net.aegistudio.resonance.jui.measure;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import net.aegistudio.scroll.RandomPanel;

@SuppressWarnings("serial")
public class MeasuredPanel extends RandomPanel
{
	MeasureRuler ruler;
	Component sectionPanel;
	
	public MeasuredPanel(MeasureRuler ruler, Component sectionPanel)
	{
		this.ruler = ruler;
		this.sectionPanel = sectionPanel;
	}
	
	public void recalculateMeasure()
	{
		for(Component c : this.getComponents())
			if(c instanceof Measurable)
		{
			c.setLocation(ruler.getWidth(((Measurable) c).start()), 0);
			c.setPreferredSize(new Dimension(ruler.getWidth(((Measurable) c).duration()),
					this.sectionPanel.getPreferredSize().height));
			c.setSize(c.getPreferredSize());
			c.validate();
		}
		this.recalculateSize();
	}
	
	int beatPerMeasure = 0;
	float measureLength = 0.0f;
	
	public void paint(Graphics g)
	{
		if(measureLength != ruler.measureLength || beatPerMeasure != ruler.beatPerMeasure)
		{
			measureLength = ruler.measureLength;
			beatPerMeasure = ruler.beatPerMeasure;
			this.recalculateMeasure();
		}
		super.paint(g);
	}
}
