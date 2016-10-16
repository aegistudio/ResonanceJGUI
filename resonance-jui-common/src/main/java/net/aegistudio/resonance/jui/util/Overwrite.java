package net.aegistudio.resonance.jui.util;

import java.awt.Component;
import java.io.File;

import javax.swing.JOptionPane;

public class Overwrite {
	String fileMessage;
	
	public Overwrite(File file) {
		if(file.exists())
			fileMessage = "File %s already exists! Do you wish to overwrite?";
	}
	
	public boolean ask(Component component) {
		if(fileMessage == null) return true;
		else {
			int message = JOptionPane.showConfirmDialog(component, fileMessage);
			return message == JOptionPane.OK_OPTION;
		}
	}
}
