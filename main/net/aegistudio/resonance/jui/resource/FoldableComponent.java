package net.aegistudio.resonance.jui.resource;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class FoldableComponent extends Container
{
	protected boolean hasFold = true;
	protected final RecursivePanel offspringPanel;
	protected final Component foldingObject;
	public int ident = 20;
	
	public FoldableComponent(Component foldingObject)
	{
		super.setLayout(null);
		super.add(this.foldingObject = foldingObject);
		this.foldingObject.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me)
			{
				setFold(!hasFold());
			}
		});
		offspringPanel = new RecursivePanel();
	}
	
	boolean dirty = true;
	public void setFold(boolean hasFold)
	{
		this.hasFold = hasFold;
		dirty = true;
	}
	
	public boolean hasFold()
	{
		return this.hasFold;
	}
	
	public Component add(Component c)
	{
		dirty = true;
		return offspringPanel.add(c);
	}
	
	public void remove(Component c)
	{
		dirty = true;
		offspringPanel.remove(c);
	}
	
	public Dimension getPreferredSize()
	{
		if(dirty)
		{
			this.foldingObject.setLocation(0, 0);
			this.foldingObject.setSize(this.foldingObject.getPreferredSize());
			if(hasFold)
			{
				super.remove(offspringPanel);
				setPreferredSize(this.foldingObject.getPreferredSize());
			}
			else
			{
				offspringPanel.setLocation(ident, this.foldingObject.getHeight());
				offspringPanel.setSize(offspringPanel.getPreferredSize());
				super.add(offspringPanel);
				Dimension objectSize = foldingObject.getPreferredSize();
				Dimension panelSize = offspringPanel.getPreferredSize();
				setPreferredSize(new Dimension(Math.max(objectSize.width, ident + panelSize.width),
						objectSize.height + panelSize.height));
			}
			dirty = false;
		}
		return super.getPreferredSize();
	}
	
	public void paint(Graphics g)
	{
		this.setSize(getPreferredSize());
		super.paint(g);
	}
}
