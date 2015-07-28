package net.aegistudio.resonance.jui.resource;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import net.aegistudio.util.RecursivePanel;

@SuppressWarnings("serial")
public class ResourceManager extends JFrame
{
	protected final RecursivePanel manager;
	protected final JScrollPane scroll;
	
	protected final ScoreCatalog score;
	
	public ResourceManager(ResourceModel resModel)
	{
		super();
		super.setAlwaysOnTop(true);
		super.setTitle("Resource Manager");
		super.setSize(300, 500);
		
		manager = new RecursivePanel();
		super.add(scroll = new JScrollPane(manager));
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		score = new ScoreCatalog(resModel);
		
		manager.add(score);
		
		resModel.init(score);
	}
}
