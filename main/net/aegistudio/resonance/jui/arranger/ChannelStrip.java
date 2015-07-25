package net.aegistudio.resonance.jui.arranger;

import java.awt.Component;
import java.awt.Dimension;

import net.aegistudio.resonance.measure.MeasuredPanel;
import net.aegistudio.scroll.Content;

public class ChannelStrip implements Content
{
	protected final ChannelSection channelSection;
	protected final MeasuredPanel clips;
	
	public ChannelStrip(ChannelSection channelSection, MeasuredPanel clips)
	{
		this.channelSection = channelSection;
		this.clips = clips;
		
		this.channelSection.setPreferredSize(new Dimension(200, 100));
	}

	@Override
	public Component getSectionScroll() {
		return channelSection;
	}

	@Override
	public Component getMainScroll() {
		return clips;
	}
}
