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
	public double getCurrentBeatPosition();
	
	public void setCurrentBeatPosition(double beatPosition);
	
	public Collection<KeywordEntry<String, Channel>> allChannels();
	
	public void createChannel(Class<? extends Channel> channelType);
	
	public void removeChannel(ChannelSection channelSection);
	
	public void renameChannel(String oldName, String newName);
	
	public void removeClip(Channel channel, Clip clip);
	
	public void mute(String channelName, Channel channel);
	
	public void solo(String channelName, Channel channel);
	
	public void initElements(ScrollPane arragePane, RowPanel channelPane, MeasuredPanel clipPane, MeasureRuler ruler);
	
	public void notifyChange();
	
	public String[] getInstrumentTracks();
	
	public String[] getPlugins();
}
