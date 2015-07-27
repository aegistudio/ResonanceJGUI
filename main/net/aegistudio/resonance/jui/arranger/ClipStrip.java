package net.aegistudio.resonance.jui.arranger;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.measure.MeasuredPanel;

@SuppressWarnings("serial")
public abstract class ClipStrip extends MeasuredPanel
{
	static Cursor crossCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
	static Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	
	public final Component padding;
	
	public ClipStrip(final ArrangerModel model, MeasureRuler ruler, ChannelSection sectionPanel)
	{
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
				preferredSize.width = ClipStrip.this.getParent().getWidth();
				preferredSize.height = sectionPanel.getPreferredSize().height;
				
				g.setColor(Color.GRAY.brighter());
				g.drawRect(-2, 0, getWidth() + 4, getHeight() - 1);
			}
		});
		
		this.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent me)
			{
				if(me.getButton() == MouseEvent.BUTTON1)
					if(accept(model.current()))
						model.insertClip(sectionPanel, ruler.getBeat(me.getX()));
			}
			
			public void mouseEntered(MouseEvent e)
			{
				if(accept(model.current()))
					setCursor(crossCursor);
				else setCursor(normalCursor);
			}
		});
	}
	
	public void recalculateSize()
	{
		super.recalculateSize();
		super.setComponentZOrder(padding, super.getComponentCount() - 1);
	}
	
	protected abstract boolean accept(Object resource);
}
