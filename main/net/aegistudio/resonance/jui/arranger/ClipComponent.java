package net.aegistudio.resonance.jui.arranger;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public abstract class ClipComponent extends Component
{
	protected final ArrangerModel model;
	
	protected final JLabel clipDenotation = new JLabel();
	
	MouseAdapter mouseAdapter;
	
	Cursor defaultCursor;
	Cursor trimCursor;
	Cursor moveCursor;
	
	public ClipComponent(ArrangerModel model)
	{
		this.model = model;
		this.clipDenotation.setOpaque(false);
		
		this.clipDenotation.setFont(getFont());
		
		this.trimCursor = new Cursor(Cursor.E_RESIZE_CURSOR);
		this.moveCursor = new Cursor(Cursor.MOVE_CURSOR);
		
		mouseAdapter = new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				defaultCursor = getCursor();
				addMouseMotionListener(mouseAdapter);
			}
			
			public void mouseExited(MouseEvent e)
			{
				setCursor(defaultCursor);
			}
			
			Dimension originalSize;
			Point beginPoint;
			Point pointOnScreen;
			
			public void mousePressed(MouseEvent event)
			{
				originalSize = getPreferredSize();
				beginPoint = getLocation();
				pointOnScreen = event.getLocationOnScreen();
				addMouseMotionListener(mouseAdapter);
			}
			
			public void mouseReleased(MouseEvent event)
			{
				try
				{
					if(potentialCode == 0)
						move(getX() - beginPoint.x);
					else if(potentialCode == 1)
						trim(-(getPreferredSize().width - originalSize.width), 0);
					else if(potentialCode == 2)
						trim(0, (getPreferredSize().width - originalSize.width));						
				}
				catch(Exception e)
				{
					setPreferredSize(originalSize);
					setLocation(beginPoint);
				}
				removeMouseMotionListener(mouseAdapter);
			}
			
			protected int potentialCode = 0;
			
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
			
			public void mouseDragged(MouseEvent me)
			{
				if(potentialCode == 0)
				{
					//Move
					setLocation(beginPoint.x + me.getLocationOnScreen().x - pointOnScreen.x, beginPoint.y);
				}
				else if(potentialCode == 1)
				{
					if(me.getLocationOnScreen().x - pointOnScreen.x + 1 < originalSize.width)
					{
						//Trim
						setLocation(beginPoint.x + me.getLocationOnScreen().x - pointOnScreen.x, beginPoint.y);
						setSize(new Dimension(originalSize.width - (me.getLocationOnScreen().x - pointOnScreen.x), originalSize.height));
					}
				}
				else if(potentialCode == 2)
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
	
	protected abstract void move(int deltaX) throws Exception;
	
	protected abstract void trim(int deltaX, int deltaSize) throws Exception;
}
