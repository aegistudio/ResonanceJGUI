package net.aegistudio.resonance.jui;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class MenuAdapter implements MenuListener {
	Runnable select;
	
	public MenuAdapter(Runnable select) {
		this.select = select;
	}
	
	public MenuAdapter() {
		this(() -> {});
	}
	
	@Override
	public void menuCanceled(MenuEvent arg0) {
		
	}

	@Override
	public void menuDeselected(MenuEvent arg0) {
		
	}

	@Override
	public void menuSelected(MenuEvent arg0) {
		select.run();
	}
}
