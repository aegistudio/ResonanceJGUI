package net.aegistudio.resonance.jui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import net.aegistudio.resonance.Resonance;

@SuppressWarnings("serial")
public class Playback extends JFrame{
	public Playback(Resonance resonance)
	{
		super();
		super.setLayout(new GridLayout(1, 0));
		super.setSize(600, 200);
		
		super.add(new JButton("Play")
		{
			{
				super.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) {
						resonance.play();
					}
				});
			}
		});
		
		super.add(new JButton("Stop")
		{
			{
				super.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) {
						resonance.stop();
						resonance.musicFacade.setBeatPosition(0);
					}
				});
			}
		});
	}
}
