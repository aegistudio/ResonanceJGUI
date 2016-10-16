package net.aegistudio.resonance.jui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import net.aegistudio.resonance.jui.util.SubwindowShowItem;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
	public final JMenu file, edit, view, setting, help;
	public final Main main;
	
	public MenuBar(Main main) {
		this.main = main;
		
		this.file = new JMenu("File");
		this.file.setMnemonic('f');
		this.add(file);
		
		this.edit = new JMenu("Edit");
		this.edit.setMnemonic('e');
		
		JMenuItem undo = new JMenuItem("Undo");
		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 
				InputEvent.CTRL_MASK));
		undo.addActionListener(a -> Main.main.history.undo());
		this.edit.add(undo);
		
		JMenuItem redo = new JMenuItem("Redo");
		redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 
				InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		redo.addActionListener(a -> Main.main.history.redo());
		this.edit.add(redo);
		
		this.edit.addMenuListener(new MenuAdapter(() -> {
			Action toUndo = main.getHistory().toUndo();
			undo.setEnabled(toUndo != null);
			if(toUndo != null) undo.setText("Undo " + toUndo.toString());
			else undo.setText("Undo");
			
			Action toRedo = main.getHistory().toRedo();
			redo.setEnabled(toRedo != null);
			if(toRedo != null) redo.setText("Redo " + toRedo.toString());
			else redo.setText("Redo");
		}));
		
		this.add(edit);
		
		this.view = new JMenu("View");
		this.view.setMnemonic('v');
		this.add(view);
		
		this.view.add(new SubwindowShowItem("Arranger", main.arranger));
		//this.view.add(new SubwindowShowItem("Mixing Board", null));
		
		this.view.add(new SubwindowShowItem("Playback", main.playback));
		this.view.add(new SubwindowShowItem("Resource Manager", main.resourceManager));
		
		this.view.addMenuListener(new MenuAdapter(() -> {
			for(int i = 0; i < view.getItemCount(); i ++)
				((SubwindowShowItem)view.getItem(i)).checkWindowStatus();
		}));
		
		this.setting = new JMenu("Setting");
		this.setting.setMnemonic('s');
		this.add(setting);
		
		this.setting.add(new SubwindowShowItem("Setting...", main.setting));
		
		this.help = new JMenu("Help");
		this.help.setMnemonic('h');
		this.add(help);
	}
}
