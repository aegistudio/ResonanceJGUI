package net.aegistudio.resonance.jui.arranger;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.aegistudio.resonance.jui.MainControl;
import net.aegistudio.resonance.jui.util.BistateToggle;
import net.aegistudio.resonance.jui.util.RenamableLabel;
import net.aegistudio.resonance.music.channel.Channel;

@SuppressWarnings("serial")
public abstract class ChannelSection extends JPanel
{
	/**
	 * +---------------------------------+
	 * | I Name                      M S |
	 * | *<- Output Track                |
	 * |  +  Plugin                      |
	 * +---------------------------------+
	 * 
	 * @param channelName
	 * @param channel
	 */
	protected JLabel channelDenotation = new JLabel();
	
	protected RenamableLabel name;
	
	public ChannelStrip parent;
	
	public final JLabel mute;
	public final JLabel solo;
	
	protected final ArrangerModel model;
	
	public final Channel channel;
	
	public ChannelSection(MainControl main, ArrangerModel model, String channelName, Channel channel)
	{	
		this.setLayout(null);
		super.add(channelDenotation);
		
		this.channel = channel;
		this.model = model;
		mute = new BistateToggle(
				main.getTheme().rawIcon("channel.mute.off"), 
				main.getTheme().rawIcon("channel.mute.on")) {
	
			@Override
			protected void toggleOn() throws Exception {	
			}
	
			@Override
			protected void toggleOff() throws Exception {
			}
		};
		super.add(mute);
		
		solo = new BistateToggle(
				main.getTheme().rawIcon("channel.solo.off"), 
				main.getTheme().rawIcon("channel.solo.on")) {
			@Override
			protected void toggleOn() throws Exception {
				// TODO Auto-generated method stub
			}

			@Override
			protected void toggleOff() throws Exception {
				// TODO Auto-generated method stub
			}
		};
		super.add(solo);
		
		name = new RenamableLabel(channelName) {
			@Override
			protected void submit(String oldName, String newName) throws Exception {
				try {
					model.renameChannel(ChannelSection.this, oldName, newName);
				}
				catch(RuntimeException e) {
					throw new Exception(e.getMessage());
				}
			}
		};
		super.add(name);
	}
	
	protected void recalculate() {
		this.solo.setLocation(getSize().width - 22, 2);
		this.solo.setSize(20, 20);
		
		this.mute.setLocation(getSize().width - 44, 2);
		this.mute.setSize(20, 20);
		
		this.channelDenotation.setLocation(2, 2);
		this.channelDenotation.setSize(20, 20);
		
		this.name.setLocation(22, 0);
		this.name.setSize(this.getSize().width - 66, 24);
	}
	
	public void paint(Graphics g) {
		this.recalculate();
		super.paint(g);

		g.setColor(Color.GRAY);
		g.drawRect(-1, 0, getWidth() + 1, getHeight() - 1);
	}
	
	public void beginRename() {
		this.name.beginRename();
	}
	
	public String getChannelName() {
		return this.name.getName();
	}
}

