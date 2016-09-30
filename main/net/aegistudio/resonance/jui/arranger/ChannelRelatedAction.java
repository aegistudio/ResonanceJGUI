package net.aegistudio.resonance.jui.arranger;

import net.aegistudio.resonance.channel.Channel;
import net.aegistudio.resonance.jui.history.Action;

public interface ChannelRelatedAction extends Action {
	public Channel getChannel();
}
