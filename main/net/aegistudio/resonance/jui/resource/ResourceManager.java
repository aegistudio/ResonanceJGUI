package net.aegistudio.resonance.jui.resource;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

public class ResourceManager {
	@SuppressWarnings("serial")
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		
		JTree jtree = new JTree();
		ComponentTreeNode root = new ComponentTreeNode(new JLabel("Root"))
		{

			@Override
			public void mouseClicked(MouseEvent me) {
				System.out.println("root");
			}
			
		};
		ComponentTreeNode root2 = new ComponentTreeNode(new JLabel("Root2"))
		{

			@Override
			public void mouseClicked(MouseEvent me) {
				System.out.println("root2");
			}
			
		};
		
		root.add(root2);
		jtree.setModel(new DefaultTreeModel(root, false));
		
		ImageIcon img1 = new ImageIcon("res/midi.png");
		
		((JLabel)root.getUserObject()).setIcon(img1);
		jtree.addTreeSelectionListener(new TreeSelectionListener()
		{

			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				((ComponentTreeNode)arg0.getNewLeadSelectionPath()
					.getLastPathComponent()).mouseClicked(null);;
			}
			
		});
		
		jtree.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				System.out.println(jtree.getComponentAt(e.getPoint()));
			}
		});
	
		TreeCellRenderer render = new TreeCellRenderer()
		{
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, 
					boolean expanded, boolean leaf, int row, boolean hasFocus)  
			{
				if(value == root) return new JLabel();
				Component component = (Component) (((ComponentTreeNode)value).getUserObject());
				component.setBackground(sel? Color.BLUE : Color.GRAY);
				return component;
			}
		};
		jtree.setCellRenderer(render);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(jtree);
		frame.setVisible(true);
	}
}
