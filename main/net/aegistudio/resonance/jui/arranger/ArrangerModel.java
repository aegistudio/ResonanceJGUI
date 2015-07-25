package net.aegistudio.resonance.jui.arranger;

import java.util.Collection;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.channel.Channel;
import net.aegistudio.resonance.channel.Clip;
import net.aegistudio.resonance.measure.MeasureRuler;
import net.aegistudio.resonance.measure.MeasuredPanel;
import net.aegistudio.scroll.RowPanel;
import net.aegistudio.scroll.ScrollPane;

public interface ArrangerModel {
	public void initElements(ScrollPane arragePane, RowPanel channelPane, MeasuredPanel clipPane, MeasureRuler ruler);
	public void notifyChange();
	
	// Playback Related
	public double getCurrentBeatPosition();
	public void setCurrentBeatPosition(double beatPosition);
	
	// Channel Related
	public Collection<KeywordEntry<String, Channel>> allChannels();
	public void createChannel(Class<? extends Channel> channelType);
	public void removeChannel(ChannelSection channelSection);
	public void renameChannel(String oldName, String newName);
	public void mute(String channelName, Channel channel);
	public void solo(String channelName, Channel channel);
	public String[] getTargerTracks();
	public String[] getPlugins();
	
	// Clip Related
	public Clip current();
	public void insertClip(ChannelSection channel, double location);
	public void removeClip(ChannelSection channel, ClipComponent clip);
	public double trim(ClipComponent clip, double offset, double length);
	public double move(ClipComponent clip, double delta);
}
