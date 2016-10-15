package net.aegistudio.resonance.jui.arranger;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import net.aegistudio.resonance.jui.Main;
import net.aegistudio.resonance.music.channel.MidiChannel;

@SuppressWarnings("serial")
public class ArrangeMenu extends JPopupMenu{

	protected JMenuItem arrangeTitle;
	
	protected JMenuItem newInstrumentChannel;
	protected JMenuItem newAudioChannel;
	protected JMenuItem newAutomation;
	
	protected JMenuItem eraseChannel;
	protected JMenuItem renameChannel; 
	
	public ArrangeMenu(final ArrangerModel arrangerModel) {
		arrangeTitle = new JMenuItem("Arrange");
		arrangeTitle.setForeground(Color.ORANGE.darker());
		arrangeTitle.setEnabled(false);
		this.add(arrangeTitle);
		this.add(new JSeparator());
		
		newInstrumentChannel = new JMenuItem("Create Instrument");
		Main.getMain().theme.configure(newInstrumentChannel, "channel.midi");
		this.add(newInstrumentChannel);
		newInstrumentChannel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				arrangerModel.createChannel(MidiChannel.class);
			}
		});
		
		newAudioChannel = new JMenuItem("Create Audio Record");
		Main.getMain().theme.configure(newAudioChannel, "channel.audio");
		this.add(newAudioChannel);
		
		newAutomation = new JMenuItem("Create Automation");
		Main.getMain().theme.configure(newAutomation, "channel.automation");
		this.add(newAutomation);
		
		this.add(new JSeparator());
		
		eraseChannel = new JMenuItem("Erase Current Channel");
		Main.getMain().theme.configure(eraseChannel, "common.erase");
		eraseChannel.addActionListener(new ActionListener()	{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(target != null) {
					int selection = JOptionPane.showConfirmDialog(target, 
							String.format("Are you sure to erase the channel %s (can't be undone)?", target.getChannelName()), 
							"Erasure confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if(selection == JOptionPane.YES_OPTION)
						arrangerModel.removeChannel(target);
				}
			}
		});
		this.add(eraseChannel);
		
		renameChannel = new JMenuItem("Rename Channel");
		renameChannel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(target != null)
					target.beginRename();
			}
		});
		this.add(renameChannel);
	}
	
	ChannelSection target = null;
	public void show(Component c, int x, int y)
	{
		if(c.getComponentAt(x, y) instanceof ChannelSection)
		{
			target = (ChannelSection) c.getComponentAt(x, y);
			arrangeTitle.setText(target.getChannelName());
			eraseChannel.setEnabled(true);
			renameChannel.setEnabled(true);
		}
		else
		{
			arrangeTitle.setText("Arrange");
			eraseChannel.setEnabled(false);
			renameChannel.setEnabled(false);
		}
		super.show(c, x, y);
	}
}
