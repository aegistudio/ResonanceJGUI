package net.aegistudio.resonance.jui.resource;

import javax.swing.JScrollPane;

import net.aegistudio.resonance.jui.MainControl;
import net.aegistudio.resonance.jui.util.RecursivePanel;
import net.aegistudio.resonance.jui.util.Subwindow;

@SuppressWarnings("serial")
public class ResourceManager extends Subwindow
{
	protected final RecursivePanel manager;
	protected final JScrollPane scroll;
	
	protected final ScoreCatalog score;
	
	public ResourceManager(MainControl main, ResourceModel resModel) {
		super();
		//super.setAlwaysOnTop(true);
		super.setTitle("Resource Manager");
		super.setSize(300, 500);
		
		manager = new RecursivePanel();
		super.add(scroll = new JScrollPane(manager));
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		score = new ScoreCatalog(main.getTheme(), resModel);
		
		manager.add(score);
		
		resModel.init(score);
	}
}
