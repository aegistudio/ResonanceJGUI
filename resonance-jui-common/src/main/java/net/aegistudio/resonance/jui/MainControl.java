package net.aegistudio.resonance.jui;

import net.aegistudio.resonance.Resonance;

/**
 * Providing holistic access to the system.
 * 
 * @author aegistudio
 */

public interface MainControl {
	public Resonance getEditingCore();
	
	public Theme getTheme();
	
	public History getHistory();
}
