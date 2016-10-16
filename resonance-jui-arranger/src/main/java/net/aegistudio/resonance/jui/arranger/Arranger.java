package net.aegistudio.resonance.jui.arranger;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import net.aegistudio.resonance.music.channel.Channel;
import net.aegistudio.resonance.music.channel.MidiChannel;
import net.aegistudio.resonance.music.channel.ScoreClip;
import net.aegistudio.resonance.jui.MainControl;
import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.measure.MeasuredPanel;
import net.aegistudio.resonance.jui.scroll.RowPanel;
import net.aegistudio.resonance.jui.scroll.ScrollPane;
import net.aegistudio.resonance.jui.util.Subwindow;
import net.aegistudio.resonance.jui.resource.ResourceProvider;

@SuppressWarnings("serial")
public class Arranger extends Subwindow implements ArrangerView
{
	public final ScrollPane arrangePane; 
	public final RowPanel channelPane;
	public final MeasuredPanel clipPane;

	public final MeasureRuler ruler;
	
	JPopupMenu arrangeMenu;
	
	protected final ArrangerModel model;
	protected final MainControl mainControl;
	
	public Arranger(MainControl mainControl, ResourceProvider resourceModel) {
		this.mainControl = mainControl;
		this.model = new ArrangerLogic(mainControl, resourceModel);
		this.model.setView(this);
		
		this.channelPane = new RowPanel() { {
			//	sectionPanel.setOpaque(true);
				sectionPanel.setBackground(Color.GRAY.brighter());
				sectionPanel.addMouseListener(new MouseAdapter(){
					public void mousePressed(MouseEvent me) {
						if(me.getButton() == MouseEvent.BUTTON3)
							arrangeMenu.show(sectionPanel, me.getX(), me.getY());
					}
				});
			}
		};
		
		this.arrangeMenu = new ArrangeMenu(mainControl, model);
		this.ruler = new TimingRuler(model);
		this.arrangePane = new ScrollPane(ruler, channelPane);
		this.arrangePane.viewPanel.setBackground(Color.WHITE);
		this.arrangePane.rulerMeter.setBackground(Color.GRAY.brighter());
		this.arrangePane.viewPanel.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent me){
				repaint();
			}
		});
		
		this.add(arrangePane);
		
		super.setTitle("Arranger");
		super.setSize(800, 500);
	//	super.setAlwaysOnTop(true);
		
		this.clipPane = new MeasuredPanel(ruler, this.channelPane.getMainScroll());
		
		this.model.initElements(arrangePane, channelPane, clipPane, ruler);
	}
	
	public void repaint()
	{
		if(this.isValid()) 
		{
			arrangePane.invalidate();
			arrangePane.validate();
		}
		super.repaint();
	}
	
	public void resonanceTick() {
		if(isVisible()) arrangePane.repaint();
	}

	@Override
	public void insertChannel(String channelName, Channel channel) {
		ChannelSection channelSection = null;
		ClipStrip clips = null;
		
		if(channel instanceof MidiChannel) {
			channelSection = new InstrumentSection(mainControl, model, channelName, (MidiChannel)channel);
		
			clips = new ClipStrip(model, ruler, channelSection) {
				@Override
				protected boolean accept(Object resource) {
					if(resource == null) return false;
					else return resource instanceof ScoreClip;
				}
			};
		}
		
		channelPane.addRowContent(channelSection.parent = new ChannelStrip(channelSection, clips));
		repaint();
	}
}
