package net.aegistudio.resonance.jui.resource;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public abstract class ComponentTreeNode extends DefaultMutableTreeNode
{
	protected final Component component;
	public ComponentTreeNode(Component component)
	{
		this.component = component;
	}
	
	public Component getUserObject()
	{
		return this.component;
	}
	
	public abstract void mouseClicked(MouseEvent me);
}
