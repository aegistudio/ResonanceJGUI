package net.aegistudio.resonance.jui.pianoroll;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.measure.PaddingMeasuredPanel;

@SuppressWarnings("serial")
public class NoteStrip extends PaddingMeasuredPanel
{
	static Color background = new Color(0.9f, 0.9f, 0.9f, 1.0f);
	public final int note;	boolean blackKey;
	
	int beginLocation = -1;
	int endLocation = -1;
	public NoteStrip(PianoRollModel model, int note, boolean blackKey, MeasureRuler ruler, Component sectionPanel)
	{
		super(ruler, sectionPanel);
		this.note = note;
		this.blackKey = blackKey;
		super.padding.setBackground(background);
		
		MouseAdapter noteGenerate = new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				if(me.getButton() == MouseEvent.BUTTON1)
				{
					beginLocation = me.getX();
					addMouseMotionListener(this);
				}
			}
			
			public void mouseDragged(MouseEvent me)
			{
				endLocation = me.getX();
			}
			
			public void mouseReleased(MouseEvent me)
			{
				if(me.getButton() == MouseEvent.BUTTON1)
				{
					NoteComponent component = model.create(NoteStrip.this,
							ruler.getBeat(Math.min(beginLocation, endLocation)),
							ruler.getBeat(Math.abs(endLocation - beginLocation)));
					if(component != null)
					{
						add(component);
						recalculateMeasure();
					}
					
					removeMouseMotionListener(this);
					beginLocation = -1;
					endLocation = -1;
				}
			}
		};
		super.addMouseListener(noteGenerate);
	}
	
	public void paint(Graphics g)
	{
		if(blackKey)
		{
			g.setColor(background);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		
		super.paint(g);
		
		if(beginLocation >= 0 && endLocation >= 0)
		{
			g.setColor(Color.GRAY.darker());
			g.fillRect(Math.min(beginLocation, endLocation), 0,
					Math.abs(endLocation - beginLocation), getHeight());
		}
	}
}
