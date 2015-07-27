package net.aegistudio.resonance.jui.arranger;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.channel.Clip;
import net.aegistudio.resonance.measure.Measurable;
import net.aegistudio.resonance.measure.MeasureRuler;
import net.aegistudio.resonance.measure.MeasuredPanel;

@SuppressWarnings("serial")
public class ClipComponent extends Component implements Measurable
{
	protected final ArrangerModel model;
	
	protected final JLabel clipDenotation = new JLabel();

	protected int potentialCode = 0;
	
	MouseAdapter cursorAdapter;
	MouseAdapter mouseAdapter;
	
	Cursor defaultCursor;
	static Cursor trimCursor = new Cursor(Cursor.E_RESIZE_CURSOR);
	static Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);
	
	public KeywordEntry<Double, Clip> clipEntry;
	public final MeasureRuler ruler;
	
	public ClipComponent(ArrangerModel model, final ChannelSection channel, KeywordEntry<Double, Clip> clipEntry, MeasureRuler ruler)
	{
		this.model = model;
		this.clipEntry = clipEntry;
		this.ruler = ruler;
		
		this.clipDenotation.setOpaque(false);
		
		this.clipDenotation.setFont(getFont());
		
		cursorAdapter = new MouseAdapter()
		{	
			public void mouseMoved(MouseEvent me)
			{
				if(me.getX() <= cornerSize)
				{
					setCursor(trimCursor);
					potentialCode = 1;
				}
				else if(me.getX() + cornerSize > getWidth())
				{
					setCursor(trimCursor);
					potentialCode = 2;
				}
				else
				{
					setCursor(moveCursor);
					potentialCode = 0;
				}
			}
		};
		
		this.addMouseMotionListener(cursorAdapter);
		
		mouseAdapter = new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				defaultCursor = getCursor();
			}
			
			public void mouseExited(MouseEvent e)
			{
				setCursor(defaultCursor);
			}
			
			int processCode = 0;
			Dimension originalSize;
			Point beginPoint;
			Point pointOnScreen;
			
			public void mousePressed(MouseEvent event)
			{
				if(event.getButton() == MouseEvent.BUTTON1)
				{
					processCode = potentialCode;
					originalSize = getPreferredSize();
					beginPoint = getLocation();
					pointOnScreen = event.getLocationOnScreen();
					addMouseMotionListener(mouseAdapter);
				}
				else if(event.getButton() == MouseEvent.BUTTON3)
					model.removeClip(channel, ClipComponent.this);
			}
			
			public void mouseClicked(MouseEvent event)
			{
				if(event.getClickCount() >= 2)
					model.duplicate(ClipComponent.this.clipEntry.getValue());
			}
			
			public void mouseReleased(MouseEvent event)
			{
				if(event.getButton() == MouseEvent.BUTTON1)
				{
					try
					{
						if(processCode == 0)
							ClipComponent.this.clipEntry = model.move(ClipComponent.this, ruler.getBeat(getX() - beginPoint.x));
						else if(processCode == 1)
							ClipComponent.this.clipEntry = model.trim(ClipComponent.this, - ruler.getBeat((getPreferredSize().width - originalSize.width)), 0);
						else if(processCode == 2)
							ClipComponent.this.clipEntry = model.trim(ClipComponent.this, 0, ruler.getBeat(getPreferredSize().width - originalSize.width));
						
						if(getParent() instanceof MeasuredPanel)
							((MeasuredPanel) getParent()).recalculateMeasure();
					}
					catch(Exception e)
					{
						setPreferredSize(originalSize);
						setLocation(beginPoint);
					}
					removeMouseMotionListener(mouseAdapter);
				}
			}
			
			public void mouseDragged(MouseEvent me)
			{
				if(processCode == 0)
				{
					//Move
					setLocation(beginPoint.x + me.getLocationOnScreen().x - pointOnScreen.x, beginPoint.y);
				}
				else if(processCode == 1)
				{
					if(me.getLocationOnScreen().x - pointOnScreen.x + 1 < originalSize.width)
					{
						//Trim
						setLocation(beginPoint.x + me.getLocationOnScreen().x - pointOnScreen.x, beginPoint.y);
						setSize(new Dimension(originalSize.width - (me.getLocationOnScreen().x - pointOnScreen.x), originalSize.height));
					}
				}
				else if(processCode == 2)
				{
					if(originalSize.width + me.getLocationOnScreen().x - pointOnScreen.x > 1)
					{
						//Trim
						setSize(new Dimension(originalSize.width + (me.getLocationOnScreen().x - pointOnScreen.x), originalSize.height));
					}
				}
			}
		};
		
		this.addMouseListener(mouseAdapter);
	}
	
	public void setSize(Dimension s)
	{
		super.setSize(s);
		this.setPreferredSize(s);
	}
	
	int cornerSize = 7;
	
	public void paint(Graphics g)
	{
		g.setColor(this.getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(this.getForeground());
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
		
		g.fillRect(getWidth() - cornerSize, getHeight() - cornerSize, cornerSize, cornerSize);
		
		this.clipDenotation.setForeground(this.getForeground());
		
		this.clipDenotation.setSize(getWidth() - 8, 20);
		this.clipDenotation.paint(g.create(4, 2, getWidth(), 20));
	}
	
	public double start()
	{
		return this.clipEntry.getKeyword();
	}
	
	public double duration()
	{
		return this.clipEntry.getValue().getLength();
	}
}
