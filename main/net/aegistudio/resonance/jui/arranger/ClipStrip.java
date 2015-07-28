package net.aegistudio.resonance.jui.arranger;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.measure.PaddingMeasuredPanel;

@SuppressWarnings("serial")
public abstract class ClipStrip extends PaddingMeasuredPanel
{
	static Cursor duplicateCursor = new Cursor(Cursor.TEXT_CURSOR);
	static Cursor crossCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
	static Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	
	public ClipStrip(final ArrangerModel model, MeasureRuler ruler, ChannelSection sectionPanel)
	{
		super(ruler, sectionPanel);
		
		this.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent me)
			{
				if(me.getButton() == MouseEvent.BUTTON1)
					if(accept(model.current()))
						model.insertClip(sectionPanel, ruler.getBeat(me.getX()));
				
				if(me.getButton() == MouseEvent.BUTTON3)
				{
					model.endDuplication();
					setCursor(crossCursor);
				}
			}
			
			public void mouseEntered(MouseEvent e)
			{
				if(accept(model.current()))
				{
					if(model.isDuplicating()) setCursor(duplicateCursor);
					else setCursor(crossCursor);
				}
				else setCursor(normalCursor);
			}
		});
	}
	
	protected abstract boolean accept(Object resource);
}
