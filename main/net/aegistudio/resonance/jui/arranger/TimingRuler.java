package net.aegistudio.resonance.jui.arranger;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.aegistudio.resonance.jui.measure.MeasureRuler;

public class TimingRuler extends MeasureRuler
{
	public final ArrangerModel model;
	public TimingRuler(ArrangerModel model)
	{
		this.model = model;
	}
	
	public void init(Component ruler, Component main)
	{
		super.init(ruler, main);
		ruler.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				model.setCurrentBeatPosition(TimingRuler.this.getBeat(e.getPoint().x));
			}
		});
	}
	
	public void drawRulerMeter(Graphics ruler)
	{
		super.drawRulerMeter(ruler);
		int realPosition = super.getWidth(model.getCurrentBeatPosition());
		int position = (int) (this.ruler.getAlignmentX() + realPosition);
		if(position > 0 && position < this.ruler.getWidth())
		{
			ruler.setColor(Color.red);
			ruler.drawLine(position, 0, position, this.ruler.getHeight());
		}
	}
	
	public void postdrawMainScroll(Graphics main)
	{
		int realPosition = super.getWidth(model.getCurrentBeatPosition());
		int position = (int) (this.mainScroll.getAlignmentX() + realPosition);
		if(position > 0 && position < this.mainScroll.getWidth())
		{
			main.setColor(Color.red);
			main.drawLine(position, 0, position, this.mainScroll.getHeight());
		}
	}
}
