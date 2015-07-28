package net.aegistudio.resonance.jui.resource;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import net.aegistudio.util.RenamableLabel;

@SuppressWarnings("serial")
public class RecursivePanel extends Container
{
	public RecursivePanel()
	{
		this.setLayout(null);
	}
	
	boolean dirty = true;
	public Component add(Component c)
	{
		dirty = true;
		return super.add(c);
	}
	
	public void remove(Component c)
	{
		dirty = true;
		super.remove(c);
	}
	
	public Dimension getPreferredSize()
	{
		if(dirty)
		{
			int width = 0;
			int height = 0;
			for(Component c : this.getComponents())
			{
				c.setLocation(0, height);
				c.setSize(c.getPreferredSize());
				height += c.getPreferredSize().height;
				
				if(c.getPreferredSize().width > width)
					width = c.getPreferredSize().width;
			}
			setPreferredSize(new Dimension(width, height));
		}
		return super.getPreferredSize();
	}
	
	public void paint(Graphics g)
	{
		setSize(getPreferredSize());
		super.paint(g);
	}

	public static void main(String[] arguments)
	{
		FoldableComponent fd = new FoldableComponent(new JLabel("Component"));
		
		JFrame frame = new JFrame();
		JScrollPane jsc = new JScrollPane(fd);
		jsc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.add(jsc);
		frame.setSize(600, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		int i = 0;
		while(true) try
		{
			if(fd.hasFold())
				fd.add(new RenamableLabel(Integer.toString(i)){
				{
					setPreferredSize(new Dimension(200, 20));
				}
				
				@Override
				protected void submit(String oldName, String newName) throws Exception {
					
				}});
			i ++;
			Thread.sleep(1000L);
		}
		catch(Exception e)
		{
			
		}
	}
}
