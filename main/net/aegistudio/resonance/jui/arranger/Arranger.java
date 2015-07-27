package net.aegistudio.resonance.jui.arranger;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;

import net.aegistudio.resonance.jui.Main;
import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.measure.MeasuredPanel;
import net.aegistudio.scroll.RowPanel;
import net.aegistudio.scroll.ScrollPane;

@SuppressWarnings("serial")
public class Arranger extends JFrame
{
	protected final Main main;
	public final ScrollPane arrangePane; 
	public final RowPanel channelPane;
	public final MeasuredPanel clipPane;

	public final MeasureRuler ruler;
	
	JPopupMenu arrangeMenu;
	
	protected final ArrangerModel model;
	
	public Arranger(Main main, ArrangerModel model)
	{
		this.main = main;
		this.model = model;
		
		this.arrangeMenu = new ArrangeMenu(model);
		this.ruler = new TimingRuler(model);
		
		super.setTitle("Arranger");
		super.setSize(800, 500);
		super.setAlwaysOnTop(true);
		
		this.channelPane = new RowPanel()
		{
			{
			//	sectionPanel.setOpaque(true);
				sectionPanel.setBackground(Color.GRAY.brighter());
				sectionPanel.addMouseListener(new MouseAdapter(){
					public void mousePressed(MouseEvent me)
					{
						if(me.getButton() == MouseEvent.BUTTON3)
							arrangeMenu.show(sectionPanel, me.getX(), me.getY());
					}
				});
			}
		};	
		
		this.clipPane = new MeasuredPanel(ruler, this.channelPane.getMainScroll());
		this.arrangePane = new ScrollPane(ruler, channelPane);
		this.arrangePane.viewPanel.setBackground(Color.WHITE);
		this.arrangePane.rulerMeter.setBackground(Color.GRAY.brighter());
		
		this.add(arrangePane);
		
		this.model.initElements(arrangePane, channelPane, clipPane, ruler);
	}
	
	public void repaint()
	{
		arrangePane.invalidate();
		arrangePane.validate();
		super.repaint();
	}
}
