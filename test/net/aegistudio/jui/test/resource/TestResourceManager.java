package net.aegistudio.jui.test.resource;

import javax.swing.UIManager;

import net.aegistudio.resonance.NamedHolder;
import net.aegistudio.resonance.channel.Score;
import net.aegistudio.resonance.jui.resource.ResourceLogic;
import net.aegistudio.resonance.jui.resource.ResourceManager;

public class TestResourceManager {
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		//ResourceManager mgr = new ResourceManager(new ResourceModelDecoy());	

		ResourceManager mgr = new ResourceManager(new ResourceLogic(new NamedHolder<Score>("score", false){

			@Override
			protected Score newObject(Class<?> clazz) {
				return new Score();
			}
		}));
		mgr.setDefaultCloseOperation(ResourceManager.EXIT_ON_CLOSE);
		mgr.setVisible(true);
	}
}
