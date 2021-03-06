package net.aegistudio.resonance.jui.arranger;

import java.util.Collection;

import net.aegistudio.resonance.music.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.music.channel.Channel;
import net.aegistudio.resonance.music.channel.Clip;
import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.measure.MeasuredPanel;
import net.aegistudio.resonance.jui.scroll.RowPanel;
import net.aegistudio.resonance.jui.scroll.ScrollPane;
import net.aegistudio.resonance.music.mixer.Track;
import net.aegistudio.resonance.plugin.Plugin;

public interface ArrangerModel {
	public void initElements(ScrollPane arragePane, RowPanel channelPane, MeasuredPanel clipPane, MeasureRuler ruler);
	public void notifyChange();
	public void setView(ArrangerView view);
	
	// Playback Related
	public double getCurrentBeatPosition();
	public void setCurrentBeatPosition(double beatPosition);
	
	// Channel Related
	public Collection<? extends KeywordEntry<String, Channel>> allChannels();
	public void createChannel(Class<? extends Channel> channelType);
	public void removeChannel(ChannelSection channelSection);
	public void renameChannel(ChannelSection channelSection, String oldName, String newName);
	public void mute(String channelName, Channel channel);
	public void solo(String channelName, Channel channel);
	public KeywordEntry<String, Track>[] getTargerTracks();
	public KeywordEntry<String, Track> getTargetTrack(String channelname);
	
	public KeywordEntry<String, Class<? extends Plugin>>[] getPlugins();
	public void usePlugin(InstrumentSection section, Class<? extends Plugin> pluginClass) throws Exception;
	
	// Clip Related
	public Clip current();
	public void duplicate(Clip clip);
	public boolean isDuplicating();
	public void endDuplication();
	public void insertClip(ChannelSection channel, double location);
	public void removeClip(ChannelSection channel, ClipComponent clip);
	public KeywordEntry<Double, Clip> trim(ChannelSection channel, ClipComponent clip, double offset, double length);
	public KeywordEntry<Double, Clip> move(ChannelSection channel, ClipComponent clip, double delta);
}
