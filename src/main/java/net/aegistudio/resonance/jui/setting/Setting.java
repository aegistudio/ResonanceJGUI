package net.aegistudio.resonance.jui.setting;

import javax.swing.JTabbedPane;

import net.aegistudio.resonance.Resonance;
import net.aegistudio.resonance.jui.Subwindow;

@SuppressWarnings("serial")
public class Setting extends Subwindow {
	JTabbedPane settingPane;
	public Setting(Resonance resonance) {
		super.setTitle("Setting");
		super.setSize(400, 400);
		
		settingPane = new JTabbedPane();
		super.setContentPane(settingPane);
		
		settingPane.addTab("Output", new MetasettingView(resonance));
	}
}
