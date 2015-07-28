package net.aegistudio.resonance.jui.resource;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public abstract class ComponentTreeNode<C extends Component> extends DefaultMutableTreeNode
{
	protected C component;
	public ComponentTreeNode(C component)
	{
		this.component = component;
	}
	
	public ComponentTreeNode<C> getUserObject()
	{
		return this;
	}
	
	public Component getComponent()
	{
		return this.component;
	}
	
	/**
	 * Could block until edit finished.
	 * @param me
	 */
	public abstract void mouseClicked(MouseEvent me);
}
