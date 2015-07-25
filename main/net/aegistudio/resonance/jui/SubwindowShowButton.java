package net.aegistudio.resonance.jui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class SubwindowShowButton extends JButton
{
	Component window;
	public SubwindowShowButton(JFrame window)
	{
		super();
		
		this.window = window;
		
		super.addActionListener(new ActionListener()
		{
			
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					if(!window.isShowing())
						window.show();
					else window.hide();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
}
