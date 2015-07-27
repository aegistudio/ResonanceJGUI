package net.aegistudio.resonance.jui.pianoroll;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class BlackKeyComponent extends Component
{
	public void paint(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int)(0.7 * getWidth()), getHeight());
		g.drawLine((int)(0.7 * getWidth()), getHeight() / 2,
				getWidth(), getHeight() / 2);
	}
}
