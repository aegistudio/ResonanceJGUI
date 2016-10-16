package net.aegistudio.resonance.jui.arranger;

import net.aegistudio.resonance.jui.Action;
import net.aegistudio.resonance.music.channel.Channel;

public interface ChannelRelatedAction extends Action {
	public Channel getChannel();
}
