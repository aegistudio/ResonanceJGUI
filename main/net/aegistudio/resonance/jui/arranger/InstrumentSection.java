package net.aegistudio.resonance.jui.arranger;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.aegistudio.resonance.channel.Channel;

@SuppressWarnings("serial")
public class InstrumentSection extends ChannelSection
{
	JLabel outputIcon;
	JLabel pluginIcon;
	
	JComboBox<String> outputComboBox = new JComboBox<String>();
	JComboBox<String> pluginComboBox = new JComboBox<String>();
	
	public InstrumentSection(ArrangerModel model, String channelName, Channel channel) {
		super(model, channelName, channel);
		super.channelDenotation.setIcon(new ImageIcon("res/midi.png"));
		
		outputIcon = new JLabel();
		outputIcon.setIcon(new ImageIcon("res/output.png"));
		super.add(outputIcon);
		
		outputComboBox.addItem("<master>");
		outputComboBox.setSelectedItem("<master>");
		
		pluginIcon = new JLabel();
		pluginIcon.setIcon(new ImageIcon("res/plugin.png"));
		super.add(pluginIcon);
		
		pluginComboBox.addItem("<none>");
		pluginComboBox.setSelectedItem("<none>");
		
		super.add(outputComboBox);
		super.add(pluginComboBox);
	}

	protected void recalculate()
	{
		outputIcon.setLocation(5, 24);
		outputIcon.setSize(20, 20);
		
		pluginIcon.setLocation(5, 48);
		pluginIcon.setSize(20, 20);
		
		outputComboBox.setLocation(outputIcon.getX() + 22, outputIcon.getY() - 2);
		outputComboBox.setSize(this.getWidth() - outputComboBox.getX() - 2, 24);
		
		pluginComboBox.setLocation(pluginIcon.getX() + 22, pluginIcon.getY() - 2);
		pluginComboBox.setSize(this.getWidth() - pluginComboBox.getX() - 2, 24);
		
		super.recalculate();
	}
}
