package net.aegistudio.jui.test.resource;

import javax.swing.UIManager;

import net.aegistudio.resonance.jui.resource.ResourceManager;

public class TestResourceManager {
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		ResourceManager mgr = new ResourceManager(new ResourceModelDecoy());
		mgr.setDefaultCloseOperation(ResourceManager.EXIT_ON_CLOSE);
		mgr.setVisible(true);
	}
}
