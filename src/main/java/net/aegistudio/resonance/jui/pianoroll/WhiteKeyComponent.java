package net.aegistudio.resonance.jui.pianoroll;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class WhiteKeyComponent extends Component
{
	protected final boolean paintUpperBound, paintLowerBound;
	public final JLabel label = new JLabel();
	
	public WhiteKeyComponent(boolean paintUpperBound, boolean paintLowerBound)
	{
		this.paintUpperBound = paintUpperBound;
		this.paintLowerBound = paintLowerBound;
		label.setBackground(Color.WHITE);
		label.setHorizontalAlignment(JLabel.CENTER);
	}
	
	public void paint(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.BLACK);
		if(paintUpperBound) g.drawLine(0, 0, getWidth(), 0);
		if(paintLowerBound) g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
		
		label.setSize((int)(0.3 * getWidth()), 20);
		label.paint(g.create((int)(0.7 * getWidth()), 0, (int)(0.3 * getWidth()), 20));
	}
}
