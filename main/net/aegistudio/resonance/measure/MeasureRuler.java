package net.aegistudio.resonance.measure;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import net.aegistudio.scroll.Ruler;

public class MeasureRuler implements Ruler 
{
	public int beatPerMeasure = 4;
	
	public int measureLength = 150;
	
	Component ruler, mainScroll;
	
	@Override
	public void drawRulerMeter(Graphics g) {
		int xOffset = (int) (ruler.getAlignmentX() % measureLength);

		g.setFont(ruler.getFont());
		g.setColor(ruler.getForeground());
		for(int x = xOffset; x < ruler.getWidth(); x += measureLength)
		{
			g.drawString(Integer.toString(1 + (int) ((x + ruler.getAlignmentX()) / measureLength)), x + 10, 15);
			g.drawLine(x, 0, x, ruler.getHeight());
			g.drawLine(x + 1, 0, x + 1, ruler.getHeight());
		}
	}

	int quantizationThreashold = 25;
	
	@Override
	public void drawMainScroll(Graphics g) {
		int xOffset = (int) (mainScroll.getAlignmentX() % measureLength);

		g.setColor(Color.GRAY);
		for(int x = xOffset; x < mainScroll.getWidth(); x += measureLength)
		{
			g.drawLine(x + 1, 0, x + 1, mainScroll.getHeight());
			g.drawLine(x, 0, x, mainScroll.getHeight());
			
			if(measureLength / beatPerMeasure > quantizationThreashold)
				for(float j = 0; j < measureLength; j += 1.0f * measureLength / beatPerMeasure)
					g.drawLine((int)(x + j), 0, (int)(x + j), mainScroll.getHeight());
		}		
	}
	
	public int getWidth(double beatLength)
	{
		return (int) (beatLength * measureLength / beatPerMeasure);
	}
	
	/** Quantization Is Used Here! **/
	public double getBeat(int location)
	{
		return 1.0 * location / measureLength * beatPerMeasure;
	}
	
	int measureLengthMinimal = 30;
	public void init(Component ruler, Component main)
	{
		this.ruler = ruler;
		this.mainScroll = main;
		
		ruler.addMouseListener(new MouseAdapter()
		{
			Point p;
			JPopupMenu menu = new JPopupMenu();
			JMenuItem rhythmPattern = new JMenuItem("Rhythm Pattern");
			JMenuItem b2pmeasure = new JMenuItem("2 / Measure");
			JMenuItem b3pmeasure = new JMenuItem("3 / Measure");
			JMenuItem b4pmeasure = new JMenuItem("4 / Measure");
			JMenuItem customize = new JMenuItem("Customize...");
			
			{
				rhythmPattern.setForeground(Color.GREEN.darker().darker());
				rhythmPattern.setEnabled(false);
				menu.add(rhythmPattern);
				menu.add(new JSeparator());
				
				menu.add(b2pmeasure);
				menu.add(b3pmeasure);
				menu.add(b4pmeasure);
				menu.add(new JSeparator());
				
				menu.add(customize);
				
				b4pmeasure.setEnabled(false);
				
				b2pmeasure.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0) {
						setMeasureLength(measureLength * 2 / beatPerMeasure);
						beatPerMeasure = 2;
						enableAll();
						b2pmeasure.setEnabled(false);
					}
				});
				
				b3pmeasure.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0) {
						setMeasureLength(measureLength * 3 / beatPerMeasure);
						beatPerMeasure = 3;
						enableAll();
						b3pmeasure.setEnabled(false);
					}
				});
				
				b4pmeasure.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0) {
						setMeasureLength(measureLength * 4 / beatPerMeasure);
						beatPerMeasure = 4;
						enableAll();
						b4pmeasure.setEnabled(false);
					}
				});
				
				customize.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e) {
						boolean unaccepted = true;
						int value = 0;
						while(unaccepted)
						{
							String errorMessage = null;
							String input = JOptionPane.showInputDialog(null, "",
									"Customize Beat(s) Per Measure", JOptionPane.QUESTION_MESSAGE);
							if(input == null) return;
							try
							{
								value = Integer.parseInt(input);
								if(value <= 0)
								{
									errorMessage = "The beat(s) per measure must be greater than zero!";
									unaccepted = true;
								}
								else unaccepted = false;
							}
							catch(RuntimeException ex)
							{
								errorMessage = "The beat(s) per measure you entered is not recognized!";
								unaccepted = true;
							}
							if(unaccepted)
								JOptionPane.showConfirmDialog(null, errorMessage,
										"Not recognized beat(s) per measure!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
						}
						enableAll();
						setMeasureLength(measureLength * value / beatPerMeasure);
						beatPerMeasure = value;
						if(beatPerMeasure == 2) b2pmeasure.setEnabled(false);
						if(beatPerMeasure == 3) b3pmeasure.setEnabled(false);
						if(beatPerMeasure == 4) b4pmeasure.setEnabled(false);
					}
					
				});
			}
			protected void enableAll()
			{
				b2pmeasure.setEnabled(true);
				b3pmeasure.setEnabled(true);
				b4pmeasure.setEnabled(true);
			}
			
			int beginMeasureLength;
			public void mousePressed(MouseEvent e)
			{
				if(e.getButton() == MouseEvent.BUTTON1)
				{
					p = e.getPoint();
					ruler.addMouseMotionListener(this);
					beginMeasureLength = measureLength;
				}
				else if(e.getButton() == MouseEvent.BUTTON3)
					menu.show(ruler, e.getX(), e.getY());
			}
			
			protected void resetMeasureLength(MouseEvent e)
			{
				int newTranslate = e.getPoint().x - p.x;
				setMeasureLength(beginMeasureLength + newTranslate);
			}
			
			public void mouseDragged(MouseEvent e)
			{
				if(p != null)
					resetMeasureLength(e);
			}
			
			public void mouseReleased(MouseEvent e)
			{
				if(e.getButton() == MouseEvent.BUTTON1)
					if(p != null)
					{
						this.resetMeasureLength(e);
						ruler.removeMouseMotionListener(this);
						p = null;
					}
			}			
		});
	}
	
	protected void setMeasureLength(int newMeasureLength)
	{
		ruler.repaint();
		mainScroll.repaint();
		
		measureLength = newMeasureLength;
		if(measureLength < measureLengthMinimal)
			measureLength = measureLengthMinimal;
	}
}
