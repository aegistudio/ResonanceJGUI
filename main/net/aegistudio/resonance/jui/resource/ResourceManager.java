package net.aegistudio.resonance.jui.resource;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class ResourceManager {
	@SuppressWarnings("serial")
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		
		ComponentTree jtree = new ComponentTree();
		ComponentTreeNode<JLabel> root2 = new ComponentTreeNode<JLabel>(new JLabel("Score"))
		{
			@Override
			public void mouseClicked(MouseEvent me) {
				System.out.println("score");
			}
		};
		

		ImageIcon img1 = new ImageIcon("res/midi.png");
		((JLabel)root2.getComponent()).setIcon(img1);
		
		RenamableTreeNode rn = new RenamableTreeNode(img1, "Test")
		{
			int functionFlag = -1;
			{
				super.menu = new JPopupMenu();
				super.menu.add(new JMenuItem("Test")
				{
					{
						super.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent arg0) {
								functionFlag = 0;
							}
						});
					}
				});
			}
			protected void menuUnshown()
			{
				if(functionFlag == 0)
				{
					doRename();
					super.component.updateUI();
				}
				functionFlag = -1;
			}
			
			public void doubleClick()
			{
				doRename();
			}
		};
		root2.add(rn);
		
		jtree.superRoot.add(root2);
		jtree.treeModel.reload();
		
		
		JFrame frame = new JFrame();
		frame.setSize(400, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JScrollPane(jtree));
		frame.setVisible(true);
		
		while(true)
		{
			frame.repaint();
			Thread.sleep(200L);
		}
	}
}
