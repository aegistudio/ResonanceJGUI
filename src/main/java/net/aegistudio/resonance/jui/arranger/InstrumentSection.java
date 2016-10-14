package net.aegistudio.resonance.jui.arranger;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.aegistudio.resonance.music.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.music.channel.MidiChannel;
import net.aegistudio.resonance.music.mixer.Track;
import net.aegistudio.resonance.plugin.Plugin;

@SuppressWarnings("serial")
public class InstrumentSection extends ChannelSection
{
	JLabel outputIcon;
	JLabel pluginIcon;
	
	JComboBox<KeywordEntry<String, Track>> outputComboBox = new JComboBox<KeywordEntry<String, Track>>();
	JComboBox<KeywordEntry<String, Class<? extends Plugin>>> pluginComboBox = new JComboBox<KeywordEntry<String, Class<? extends Plugin>>>();

	Class<? extends Plugin> pluginClass;
	KeywordEntry<String, Track> targetTrack;
	
	public InstrumentSection(ArrangerModel model, String channelName, MidiChannel channel) {
		super(model, channelName, channel);
		super.channelDenotation.setIcon(new ImageIcon("res/midi.png"));
		
		outputIcon = new JLabel();
		outputIcon.setIcon(new ImageIcon("res/output.png"));
		super.add(outputIcon);
		
		pluginIcon = new JLabel();
		pluginIcon.setIcon(new ImageIcon("res/plugin.png"));
		super.add(pluginIcon);
		
		super.add(outputComboBox);
		super.add(pluginComboBox);
		
		Plugin plugin = channel.getPlugin();
		pluginClass = plugin == null? null : plugin.getClass();
		
		pluginComboBox.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				@SuppressWarnings("unchecked")
				KeywordEntry<String, Class<? extends Plugin>> target = 
						(KeywordEntry<String, Class<? extends Plugin>>) arg0.getItem();
				if(target.getValue() == pluginClass) return;

				try
				{
					model.usePlugin(InstrumentSection.this, target.getValue());
					pluginClass = target.getValue();
				}
				catch(Exception e)
				{
					JOptionPane.showConfirmDialog(InstrumentSection.this, "Plugin setting may fails. Due to\n" + e,
							"Plugin setting error!", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		targetTrack = model.getTargetTrack(channelName);
	}
	
	KeywordEntry<String, Track>[] currentTracks;
	KeywordEntry<String, Class<? extends Plugin>>[] currentPlugins;
	
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
		
		KeywordEntry<String, Class<? extends Plugin>>[] plugins = super.model.getPlugins();
		if(plugins != currentPlugins)
		{
			currentPlugins = plugins;
			int selected = 0;
			for(int i = 0; i < currentPlugins.length; i ++)
				if(currentPlugins[i].getValue() == this.pluginClass)
				{
					selected = i;
					break;
				}
			pluginComboBox.setModel(new DefaultComboBoxModel<KeywordEntry<String, Class<? extends Plugin>>>(currentPlugins));
			pluginComboBox.setSelectedIndex(selected);
			pluginComboBox.updateUI();
		}
		
		KeywordEntry<String, Track>[] targetTracks = super.model.getTargerTracks();
		if(targetTracks != currentTracks)
		{
			currentTracks = targetTracks;
			int selected = 0;
			for(int i = 0; i < currentTracks.length; i ++)
				if(currentTracks[i] == this.targetTrack)
				{
					selected = i;
					break;
				}
			outputComboBox.setModel(new DefaultComboBoxModel<KeywordEntry<String, Track>>(currentTracks));
			outputComboBox.setSelectedIndex(selected);
			outputComboBox.updateUI();
		}
		
		super.recalculate();
	}
}
