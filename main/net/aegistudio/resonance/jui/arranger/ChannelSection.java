package net.aegistudio.resonance.jui.arranger;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.aegistudio.resonance.channel.Channel;
import net.aegistudio.util.BistateToggle;
import net.aegistudio.util.RenamableLabel;

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
	
	protected JLabel mute;
	protected JLabel solo;
	
	public ChannelSection(final ArrangerModel model, String channelName, Channel channel)
	{	
		this.setLayout(null);
		super.add(channelDenotation);
		
		mute = new BistateToggle("res/mute_nonactive.png", "res/mute_active.png")
		{
	
			@Override
			protected void toggleOn() throws Exception {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			protected void toggleOff() throws Exception {
				// TODO Auto-generated method stub
				
			}
		};
		super.add(mute);
		
		solo = new BistateToggle("res/solo_nonactive.png", "res/solo_active.png")
		{
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
		
		name = new RenamableLabel(channelName)
		{
			@Override
			protected void submit(String oldName, String newName) throws Exception {
				try
				{
					model.renameChannel(oldName, newName);
				}
				catch(RuntimeException e)
				{
					throw new Exception(e.getMessage());
				}
			}
		};
		super.add(name);
	}
	
	protected void recalculate()
	{
		this.solo.setLocation(getSize().width - 22, 2);
		this.solo.setSize(20, 20);
		
		this.mute.setLocation(getSize().width - 44, 2);
		this.mute.setSize(20, 20);
		
		this.channelDenotation.setLocation(2, 2);
		this.channelDenotation.setSize(20, 20);
		
		this.name.setLocation(22, 0);
		this.name.setSize(this.getSize().width - 66, 24);
	}
	
	public void paint(Graphics g)
	{
		this.recalculate();
		super.paint(g);

		g.setColor(Color.GRAY);
		g.drawRect(-1, 0, getWidth() + 1, getHeight() - 1);
	}
	
	public String getChannelName()
	{
		return this.name.getName();
	}
}

