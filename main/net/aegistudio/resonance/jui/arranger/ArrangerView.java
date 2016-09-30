package net.aegistudio.resonance.jui.arranger;

import net.aegistudio.resonance.channel.Channel;

/**
 * Receives update notifies from model.
 * 
 * @author aegistudio
 */

public interface ArrangerView {
	public void insertChannel(String channelName, Channel channel);
	
	
}
