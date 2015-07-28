package net.aegistudio.jui.test.resource;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import net.aegistudio.util.FoldableComponent;

public class TestPseudoTree {
	@SuppressWarnings("serial")
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		
		FoldableComponent fd = new FoldableComponent(new JLabel("Component")
		{
			{
				setIcon(new ImageIcon("res/midi.png"));
			}
		});
		
		JFrame frame = new JFrame();
		JScrollPane jsc = new JScrollPane(fd);
		jsc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.add(jsc);
		frame.setSize(600, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
		FoldableComponent fd2;
		fd.addOffspring(fd2 = new FoldableComponent(new JLabel("Component")
		{
			{
				setIcon(new ImageIcon("res/midi.png"));
			}
			
		}));
		
		fd2.addOffspring(fd3 = new FoldableComponent(new JLabel("Component")
		{
			{
				setIcon(new ImageIcon("res/midi.png"));
				super.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent me)
					{
						fd2.removeOffspring(fd3);
					}
				});
			}
		}));
	}
	
	static FoldableComponent fd3 = null;
}
