package net.aegistudio.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class FoldableComponent extends Container
{
	protected boolean hasFold;
	protected final RecursivePanel offspringPanel;
	protected Component foldingObject;
	protected final JLabel foldingButton;
	public int ident = 20;
	
	protected FoldableComponent parent;

	public static final ImageIcon pack = new ImageIcon("res/pack.png");
	public static final ImageIcon unpack = new ImageIcon("res/unpack.png");
	
	public FoldableComponent(Component foldingObject)
	{
		super.setLayout(null);
		if(foldingObject != null)
			super.add(this.foldingObject = foldingObject);
		this.foldingButton = new JLabel();
		this.add(this.foldingButton);
		
		this.foldingButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me)
			{
				if(me.getButton() == MouseEvent.BUTTON1)
					setFold(!hasFold());
			}
		});
		offspringPanel = new RecursivePanel();
		this.add(offspringPanel);
		this.setFold(true);
	}
	
	boolean dirty = true;
	public void setFold(boolean hasFold)
	{
		this.hasFold = hasFold;
		if(foldingObject != null)
			recalculateSize();
		repaint();
	}
	
	public boolean hasFold()
	{
		return this.hasFold;
	}
	
	public Component addOffspring(Component c)
	{
		Component r = offspringPanel.add(c);
		if(c instanceof FoldableComponent)
			((FoldableComponent) c).parent = this;
		recalculateSize();
		return r;
	}
	
	public void removeOffspring(Component c)
	{
		offspringPanel.remove(c);
		if(c instanceof FoldableComponent)
			if(((FoldableComponent) c).parent == this)
				((FoldableComponent) c).parent = null;
		recalculateSize();
	}
	
	public void paint(Graphics g)
	{
		if(this.offspringPanel.getComponentCount() > 0)
		{
			if(this.hasFold) foldingButton.setIcon(unpack);
			else foldingButton.setIcon(pack);
		}
		else foldingButton.setIcon(null);
		super.paint(g);
	}
	
	Dimension preferredSize = new Dimension();
	public void recalculateSize()
	{
		this.foldingButton.setLocation(0, 0);
		this.foldingButton.setSize(20, 20);
		
		this.foldingObject.setLocation(foldingButton.getWidth(), 0);
		this.foldingObject.setSize(this.foldingObject.getPreferredSize());
		if(hasFold)
		{
			offspringPanel.setVisible(false);;
			preferredSize.width = this.foldingObject.getPreferredSize().width + foldingButton.getWidth();
			preferredSize.height = Math.max(this.foldingObject.getPreferredSize().height, this.foldingButton.getHeight());
		}
		else
		{
			offspringPanel.recalculateSize();
			offspringPanel.setLocation(ident, this.foldingObject.getHeight());
			offspringPanel.setSize(offspringPanel.getPreferredSize());
			offspringPanel.setVisible(true);
			Dimension objectSize = foldingObject.getPreferredSize();
			Dimension panelSize = offspringPanel.getPreferredSize();
			preferredSize.width = Math.max(foldingButton.getWidth() + objectSize.width, ident + panelSize.width);
			preferredSize.height = objectSize.height + panelSize.height;
		}
		setPreferredSize(preferredSize);
		setSize(preferredSize);

		if(this.parent != null)
			this.parent.recalculateSize();
		else if(getParent() instanceof RecursivePanel)
			((RecursivePanel) getParent()).recalculateSize();
	}
	
	public void setBackground(Color bg)
	{
		this.foldingObject.setBackground(bg);
	}
	
	public void setForeground(Color bg)
	{
		this.foldingObject.setForeground(bg);
	}
}
