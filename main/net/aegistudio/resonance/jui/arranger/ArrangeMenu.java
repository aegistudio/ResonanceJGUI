package net.aegistudio.resonance.jui.arranger;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import net.aegistudio.resonance.channel.MidiChannel;

@SuppressWarnings("serial")
public class ArrangeMenu extends JPopupMenu{
	
	protected JMenuItem newInstrumentChannel;
	protected JMenuItem newAudioChannel;
	protected JMenuItem newAutomation;
	
	protected JMenuItem eraseChannel;
	protected JMenuItem renameChannel;
	
	public ArrangeMenu(final ArrangerModel arrangerModel)
	{
		JMenuItem arrangeTitle = new JMenuItem("Arrange");
		arrangeTitle.setForeground(Color.ORANGE.darker());
		arrangeTitle.setEnabled(false);
		this.add(arrangeTitle);
		this.add(new JSeparator());
		
		newInstrumentChannel = new JMenuItem("Create Instrument");
		newInstrumentChannel.setIcon(new ImageIcon("res/midi.png"));
		this.add(newInstrumentChannel);
		newInstrumentChannel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				arrangerModel.createChannel(MidiChannel.class);
			}
		});
		
		newAudioChannel = new JMenuItem("Create Audio Record");
		newAudioChannel.setIcon(new ImageIcon("res/audio.png"));
		this.add(newAudioChannel);
		
		newAutomation = new JMenuItem("Create Automation");
		newAutomation.setIcon(new ImageIcon("res/automation.png"));
		this.add(newAutomation);
		
		this.add(new JSeparator());
		
		eraseChannel = new JMenuItem("Erase Current Channel");
		eraseChannel.setIcon(new ImageIcon("res/erase.png"));
		eraseChannel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(target != null)
				{
					int selection = JOptionPane.showConfirmDialog(target, String.format("Are you sure to erase the channel %s (can't be undo)?", target.getChannelName()), "Erasure confirm",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
			eraseChannel.setEnabled(true);
			renameChannel.setEnabled(true);
		}
		else
		{
			eraseChannel.setEnabled(false);
			renameChannel.setEnabled(false);
		}
		super.show(c, x, y);
	}
}
