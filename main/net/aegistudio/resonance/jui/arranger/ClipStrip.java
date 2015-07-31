package net.aegistudio.resonance.jui.arranger;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
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
					if(accept(model.current()))
						setCursor(crossCursor);
					else setCursor(normalCursor);
				}
			}
			
			public void mouseEntered(MouseEvent e)
			{
				if(accept(model.current()))
				{
					if(model.isDuplicating())
						setCursor(duplicateCursor);
					else setCursor(crossCursor);
				}
				else setCursor(normalCursor);
			}
		});
		
		this.addMouseListener(new MouseAdapter()
		{
			int button = 0;
			public void mousePressed(MouseEvent me){
				button = me.getButton();
				addMouseMotionListener(this);
			}
			
			public void mouseDragged(MouseEvent me)
			{
				if(button == MouseEvent.BUTTON1)
					if(model.isDuplicating())
						if(!(getComponentAt(me.getPoint()) instanceof ClipComponent)){
							if(accept(model.current()))
								model.insertClip(sectionPanel, ruler.getBeat(me.getX()));
						}
				if(button == MouseEvent.BUTTON3)
				{
					Component target = getComponentAt(me.getPoint());
					if(target instanceof ClipComponent)
						((ClipComponent) target).actionRemoval();
				}
			}
			
			public void mouseReleased(MouseEvent me){
				button = 0;
				removeMouseMotionListener(this);
			}
		});
	}
	
	protected abstract boolean accept(Object resource);
}
