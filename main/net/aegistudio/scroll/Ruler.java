package net.aegistudio.scroll;

import java.awt.Component;
import java.awt.Graphics;

/**
 * Ruler is responsible for the horizontal measuring and main scroll
 * area standardizing.
 * @author aegistudio
 */

public interface Ruler
{
	public void init(Component ruler, Component mainScroll);
	
	public void drawRulerMeter(Graphics g);
	
	public void drawMainScroll(Graphics g);
}
