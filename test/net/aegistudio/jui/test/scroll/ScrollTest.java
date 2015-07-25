package net.aegistudio.jui.test.scroll;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import net.aegistudio.resonance.measure.Measurable;
import net.aegistudio.resonance.measure.MeasureRuler;
import net.aegistudio.resonance.measure.MeasuredPanel;
import net.aegistudio.scroll.Content;
import net.aegistudio.scroll.RandomPanel;
import net.aegistudio.scroll.RowPanel;
import net.aegistudio.scroll.ScrollPane;

public class ScrollTest {
	static RowPanel content;
	
	@SuppressWarnings("serial")
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		
		final MeasureRuler ruler = new MeasureRuler();
		
		content = new RowPanel();
		
		final Measurable black1 = new Measurable()
		{

			@Override
			public double start() {
				return 0.0;
			}

			@Override
			public double duration() {
				return 4.0;
			}
			
			public void paint(Graphics g)
			{
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		
		final Measurable black2 = new Measurable()
		{

			@Override
			public double start() {
				return 5.0;
			}

			@Override
			public double duration() {
				return 5.0;
			}
			
			public void paint(Graphics g)
			{
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		
		Content aRow = new Content()
		{	
			Component sectionScroll = new Component()
			{
				Dimension preferredSize = new Dimension();
				public Dimension getPreferredSize()
				{
					preferredSize.width = 200;
					preferredSize.height = 100;
					return preferredSize;
				}
				
				public void paint(Graphics g)
				{
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);
				}
			};
			
			RandomPanel panel = new MeasuredPanel(ruler, sectionScroll);
			{
				panel.add(black1);
			}
			
			public Component getSectionScroll()
			{
				return sectionScroll;
			}

			@Override
			public Component getMainScroll() {
				black1.setPreferredSize(new Dimension(black1.getPreferredSize().width, sectionScroll.getPreferredSize().height));
				return panel;
			}
		};
		content.addRowContent(aRow);
		
		Content bRow = new Content()
		{
	
			Component sectionScroll = new JButton();
			{
				sectionScroll.setPreferredSize(new Dimension(200, 100));
				sectionScroll.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent me)
					{
						System.out.println("Clicked!");
					}
				});
			}
			
			RandomPanel panel = new MeasuredPanel(ruler, sectionScroll);
			{
				panel.add(black2);
			}
			
			public Component getSectionScroll()
			{
				return this.sectionScroll;
			}
			

			@Override
			public Component getMainScroll() {
				return panel;
			}
		};
		content.addRowContent(bRow);
		
		ScrollPane scrollPane = new ScrollPane(ruler, content);
		
		scrollPane.viewPanel.setBackground(Color.WHITE);
		
		JFrame jframe = new JFrame("test");
		jframe.setSize(600, 480);
		jframe.add(scrollPane);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
		
		while(true)
		{
			jframe.repaint();
			Thread.sleep(100L);
		}
	}
}
