package net.aegistudio.resonance.jui.resource;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class ComponentTree extends Container
{
	public final JTree tree;
	public final DefaultTreeModel treeModel;
	public final ComponentTreeNode<JLabel> superRoot;
	public final TreeCellRenderer renderer;
	
	public ComponentTree()
	{
		tree = new JTree();
		superRoot = new ComponentTreeNode<JLabel>(new JLabel())
		{
			@Override
			public void mouseClicked(MouseEvent me) {
			}
			
		};
		
		tree.setModel(treeModel = new DefaultTreeModel(superRoot, false));
		
		tree.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				TreePath path = tree.getPathForLocation(e.getPoint().x, e.getPoint().y);
				if(path == null) return;
				tree.setSelectionPath(path);
				ComponentTreeNode<?> node = ((ComponentTreeNode<?>)path.getLastPathComponent());
				node.getComponent().setBounds(tree.getRowBounds(tree.getRowForLocation(e.getPoint().x, e.getPoint().y)));
				add(node.getComponent());
				tree.removeNotify();
				Thread callback = new Thread()
				{
					public void run()
					{
						node.mouseClicked(e);
						remove(node.getComponent());
						tree.addNotify();
						repaint();
						System.out.println("Here!");
					}
				};
				callback.start();
			}
		});
		
		renderer = new TreeCellRenderer()
		{
			Color selectedColor = new Color(0, .62f, .8f).darker();
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, 
					boolean expanded, boolean leaf, int row, boolean hasFocus)  
			{
				Component component = (Component) (((ComponentTreeNode<?>)value).getComponent());
				component.setBackground(sel? selectedColor : Color.WHITE);
				component.setForeground(sel? Color.WHITE : Color.BLACK);

				return component;
			}
		};
		tree.setCellRenderer(renderer);	
		
		this.add(tree);
	}
	
	public void paint(Graphics g)
	{
		tree.setSize(getSize());
		tree.paint(g);
	}
	
	public void setPreferredSize(Dimension d)
	{
		super.setPreferredSize(d);
		tree.setPreferredSize(d);
	}
}
