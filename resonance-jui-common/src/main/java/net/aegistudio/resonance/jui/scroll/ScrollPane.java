package net.aegistudio.resonance.jui.scroll;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ScrollPane extends JScrollPane
{
	protected final Ruler ruler;
	protected final Content content;
	
	public final Component rulerMeter;
	public final JPanel viewPanel;
	
	protected int rulerMeterHeight = 20;
	
	public ScrollPane(Ruler ruler, Content content)
	{
		super();
		
		this.ruler = ruler;
		this.content = content;
		
		this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		
		this.rulerMeter = new Component()
		{
			Dimension preferredSize = new Dimension();
			
			public Dimension getPreferredSize()
			{
				preferredSize.height = rulerMeterHeight;
				preferredSize.width = ScrollPane.this.getViewport().getView()
						.getPreferredSize().width;
				return preferredSize;
			}
			
			public void paint(Graphics g)
			{
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
				ruler.drawRulerMeter(g);
			}
		};
		this.setColumnHeaderView(rulerMeter);
		
		this.viewPanel = new JPanel()
		{	
			public Dimension getPreferredSize()
			{
				if(content.getMainScroll() instanceof Slideshow)
				{
					Dimension logicSize = ((Slideshow)content.getMainScroll()).getLogicSize();
					Dimension physicSize = getViewport().getSize();
					
					Dimension targetDimension = new Dimension(logicSize.width, logicSize.height);
					
					if(logicSize.width < physicSize.width) targetDimension.width = physicSize.width;
					if(logicSize.height < physicSize.height) targetDimension.height = physicSize.height;
					
					content.getMainScroll().setPreferredSize(targetDimension);
				}
				return content.getMainScroll().getPreferredSize();
			}
			
			public void paint(Graphics g)
			{
				g.setColor(viewPanel.getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
				ruler.predrawMainScroll(g);
				content.getMainScroll().paint(g);
				ruler.postdrawMainScroll(g);
			}
		};
		this.ruler.init(rulerMeter, viewPanel);
		
		this.setViewportView(viewPanel);
		this.setRowHeaderView(content.getSectionScroll());
		this.viewPanel.add(content.getMainScroll());
	}

	public void setRulerMeterHeight(int height)
	{
		this.rulerMeterHeight = height;
	}
}
