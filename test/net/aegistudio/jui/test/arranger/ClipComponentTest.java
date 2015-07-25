package net.aegistudio.jui.test.arranger;

import java.awt.Color;

import javax.swing.JFrame;

import net.aegistudio.resonance.jui.arranger.ClipComponent;

public class ClipComponentTest {
	@SuppressWarnings("serial")
	public static void main(String[] arguments) throws InterruptedException
	{
		ClipComponent clipComponent = new ClipComponent(new ArrangerModelDecoy())
		{
			{
				this.clipDenotation.setText("Clip");
			}

			@Override
			protected void move(int deltaX) throws Exception {
				System.out.println(deltaX);
			}

			@Override
			protected void trim(int deltaX, int deltaSize) throws Exception {
				System.out.println(deltaX);
				System.out.println(deltaSize);
			}
		};
		
		clipComponent.setBackground(Color.GREEN.darker());
		clipComponent.setForeground(Color.YELLOW);
		
		JFrame jframe = new JFrame();
		jframe.setSize(600, 480);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jframe.setLayout(null);
		clipComponent.setSize(200, 100);
		clipComponent.setPreferredSize(clipComponent.getSize());
		clipComponent.setLocation(200, 100);
		jframe.add(clipComponent);
		
		jframe.setVisible(true);
	}
}
