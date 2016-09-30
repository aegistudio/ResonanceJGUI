package net.aegistudio.resonance.jui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar
{
	public final JMenu file, edit, view, setting, help;
	public final Main main;
	
	public MenuBar(Main main)
	{
		this.main = main;
		
		this.file = new JMenu("File");
		this.file.setMnemonic('f');
		this.add(file);
		
		this.edit = new JMenu("Edit");
		this.edit.setMnemonic('e');
		this.add(edit);
		
		this.view = new JMenu("View");
		this.view.setMnemonic('v');
		this.add(view);
		
		this.view.add(new SubwindowShowItem("Arranger", main.arranger));
		//this.view.add(new SubwindowShowItem("Mixing Board", null));
		
		this.view.add(new SubwindowShowItem("Playback", main.playback));
		this.view.add(new SubwindowShowItem("Resource Manager", main.resourceManager));
		
		this.view.addMenuListener(new MenuListener()
		{
			@Override
			public void menuCanceled(MenuEvent arg0) {		}

			@Override
			public void menuDeselected(MenuEvent arg0) {	}

			@Override
			public void menuSelected(MenuEvent arg0)
			{
				for(int i = 0; i < view.getItemCount(); i ++)
					((SubwindowShowItem)view.getItem(i)).checkWindowStatus();
			}
		});
		
		this.setting = new JMenu("Setting");
		this.setting.setMnemonic('s');
		this.add(setting);
		
		this.setting.add(new SubwindowShowItem("Setting...", main.setting));
		
		this.help = new JMenu("Help");
		this.help.setMnemonic('h');
		this.add(help);
	}
}
