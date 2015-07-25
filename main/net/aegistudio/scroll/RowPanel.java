package net.aegistudio.scroll;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

public class RowPanel implements Content
{
	protected int sectionWidth = 200;
	
	@SuppressWarnings("serial")
	protected RandomPanel sectionPanel = new RandomPanel()
	{
		public Dimension getPreferredSize()
		{
			Dimension size = super.getPreferredSize();
			size.width = sectionWidth;
			return size;
		};
	};
	
	@SuppressWarnings("serial")
	protected RandomPanel mainScrollPanel = new RandomPanel()
	{
		public Dimension getPreferredSize()
		{
			Dimension size = super.getPreferredSize();
			if(size.height < sectionPanel.getPreferredSize().height)
				size.height = sectionPanel.getPreferredSize().height;			
			return size;
		};
	};
	
	@Override
	public Component getSectionScroll() {
		return this.sectionPanel;
	}

	@Override
	public Component getMainScroll() {
		return this.mainScrollPanel;
	}

	public RowPanel()
	{
		this.sectionPanel.setLayout(null);
		this.mainScrollPanel.setLayout(null);
	}
	
	public final ArrayList<Content> rows = new ArrayList<Content>();
	
	public void addRowContent(Content content)
	{
		rows.add(content);
	
		this.update();
	}
	
	public void removeRowContent(Content content)
	{
		rows.remove(content);
		
		this.update();
	}
	
	public void update()
	{
		sectionPanel.removeAll();
		mainScrollPanel.removeAll();
		
		int accumulatedHeight = 0;
		for(Content row : rows)
		{
			row.getSectionScroll().setLocation(0, accumulatedHeight);
			row.getMainScroll().setLocation(0, accumulatedHeight);
			
			this.sectionPanel.add(row.getSectionScroll());
			this.mainScrollPanel.add(row.getMainScroll());
			
			row.getSectionScroll().validate();
			row.getSectionScroll().repaint();
			
			row.getMainScroll().validate();
			row.getMainScroll().repaint();
			
			accumulatedHeight += row.getSectionScroll().getPreferredSize().height;
		}
		this.sectionPanel.recalculateSize();
		this.mainScrollPanel.recalculateSize();
	}
	
	public void setSectionWidth(int width)
	{
		this.sectionWidth = width;
	}
}
