package net.aegistudio.resonance.jui.arranger;

import java.util.Collection;

import net.aegistudio.resonance.music.*;
import net.aegistudio.resonance.music.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.music.NamedHolder.NamedEntry;
import net.aegistudio.resonance.music.channel.*;
import net.aegistudio.resonance.music.mixer.*;

import net.aegistudio.resonance.jui.MainControl;
import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.measure.MeasuredPanel;
import net.aegistudio.resonance.jui.resource.ResourceProvider;
import net.aegistudio.resonance.jui.resource.Resource;
import net.aegistudio.resonance.jui.resource.ScoreResource;

import net.aegistudio.resonance.jui.scroll.RowPanel;
import net.aegistudio.resonance.jui.scroll.ScrollPane;
import net.aegistudio.resonance.plugin.Plugin;

public class ArrangerLogic implements ArrangerModel {
	protected final MainControl main;
	protected final MusicController musicLayer;
	protected final ResourceProvider resource;
	
	public ArrangerLogic(MainControl main, ResourceProvider resource) {
		this.main = main;
		this.musicLayer = ((MusicController)main.getEditingCore().musicFacade);
		this.musicLayer.getMixer().renameMaster("<master>");
		this.resource = resource;
	}
	
	protected ArrangerView view;
	public void setView(ArrangerView view) {
		this.view = view;
	}
	
	protected String getNextDefaultName(Class<? extends Channel> channelType) {
		String prefix = channelType == MidiChannel.class? "Instrument " : "Channel ";
		for(int i = 1; true; i ++)
			if(musicLayer.getChannelHolder().get(prefix + i) == null)
				return prefix + i;
	}


	@Override
	public void createChannel(Class<? extends Channel> channelType) {
		String channelName = this.getNextDefaultName(channelType);
		Channel channel = musicLayer.getChannelHolder()
				.create(channelName, channelType);
		if(channel == null) return;
		
		this.view.insertChannel(channelName, channel);
	}
	
	@Override
	public Collection<? extends KeywordEntry<String, Channel>> allChannels() {
		return musicLayer.getChannelHolder().allEntries();
	}
	
	@Override
	public void removeChannel(ChannelSection channelSection) {
		musicLayer.getChannelHolder().remove(channelSection.getChannelName());
		channelPane.removeRowContent(channelSection.parent);
		
		main.getHistory().abandon(a -> {
			if(a instanceof ChannelRelatedAction)
				return ((ChannelRelatedAction)a).getChannel() == channelSection.channel;
			return false;
		});
	}

	@Override
	public void renameChannel(ChannelSection channelSection, String oldName, String newName) {
		main.getHistory().push(new ChannelRelatedAction() {
			
			@Override
			public void redo() {
				musicLayer.getChannelHolder().rename(oldName, newName);
				channelSection.name.updateName(newName);
			}

			@Override
			public void undo() {
				musicLayer.getChannelHolder().rename(newName, oldName);
				channelSection.name.updateName(oldName);
			}

			@Override
			public Channel getChannel() {
				return channelSection.channel;
			}
			
			public String toString() {
				return "Rename channel";
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void removeScoreClip(ChannelSection channel, KeywordEntry clip)
	{
		channel.channel.getClips(ScoreClip.class).remove(clip);	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void insertScoreClip(ChannelSection channel, KeywordEntry clip)
	{
		channel.channel.getClips(ScoreClip.class).add(clip);
	}
	
	@Override
	public void insertClip(ChannelSection channel, double location) {
		if(current() instanceof ScoreClip) {
			KeywordEntry<Double, ScoreClip> clipEntry = new KeywordArray.DefaultKeywordEntry<>(
					location, ((ScoreClip)current()).clone());
			
			ScoreClipComponent scoreClipComponent = new ScoreClipComponent(this, channel, clipEntry, ruler);
			insertScoreClip(channel, clipEntry);
			((MeasuredPanel)channel.parent.getMainScroll())
				.add(scoreClipComponent);
		}
		
		((MeasuredPanel)channel.parent.getMainScroll())
			.recalculateMeasure();
	}
	
	@Override
	public void removeClip(ChannelSection channel, ClipComponent clip) {
		
		Clip clipInstance = clip.clipEntry.getValue();
		if(clipInstance instanceof ScoreClip)
			removeScoreClip(channel, clip.clipEntry);
		
		((MeasuredPanel)channel.parent.getMainScroll())
			.remove(clip);
		((MeasuredPanel)channel.parent.getMainScroll())
			.recalculateMeasure();
	}
	
	@Override
	public KeywordEntry<Double, Clip> trim(ChannelSection channelSection, ClipComponent clip, double offset, double length) {
		Clip clipInstance = clip.clipEntry.getValue();
		if(clipInstance instanceof ScoreClip)
		{
			ScoreClip sclip = (ScoreClip)clipInstance;
			removeScoreClip(channelSection, clip.clipEntry);
			sclip.trim(sclip.getLength() + length - offset, sclip.getOffset() - offset);
			KeywordEntry<Double, Clip> entry = new KeywordArray.DefaultKeywordEntry<Double, Clip>(Math.max(clip.start() + offset, 0.0), sclip);
			insertScoreClip(channelSection, entry);
			return entry;
		}
		else return null;
	}

	@Override
	public KeywordEntry<Double, Clip> move(ChannelSection channelSection, ClipComponent clip, double delta) {
		if(clip.clipEntry.getValue() instanceof ScoreClip)
		{
			removeScoreClip(channelSection, clip.clipEntry);
			KeywordEntry<Double, Clip> entry = new KeywordArray.DefaultKeywordEntry<Double, Clip>(Math.max(clip.start() + delta, 0.0), clip.clipEntry.getValue());
			insertScoreClip(channelSection, entry);
			return entry;
		}
		else return null;
	}

	@Override
	public void mute(String channelName, Channel channel) {
		
	}

	@Override
	public void solo(String channelName, Channel channel) {
		
	}

	@Override
	public void notifyChange() {
		
	}
	
	KeywordEntry<String, Track>[] targetTracks;
	
	@SuppressWarnings("unchecked")
	@Override
	public KeywordEntry<String, Track>[] getTargerTracks() {
		if(musicLayer.getMixer().hasUpdated(this))
			targetTracks = musicLayer.getMixer().allEntries().toArray(new NamedHolder.NamedEntry[0]);
		
		return targetTracks;
	}

	//XXX---------------------------------------------------------------------------------------
	@SuppressWarnings({"unchecked"})
	NamedHolder.NamedEntry<Class<? extends Plugin>>[] plugins = new NamedHolder.NamedEntry[]{
			new NamedHolder.NamedEntry<Class<Plugin>>("<none>", null),
			//new NamedHolder.NamedEntry<Class<SineOscillator>>("Sine Oscillator", SineOscillator.class)
	};
	
	@Override
	public KeywordEntry<String, Class<? extends Plugin>>[] getPlugins() {
		return plugins;
	}

	//XXX---------------------------------------------------------------------------------------
	
	ScrollPane arragePane;
	RowPanel channelPane;
	MeasuredPanel clipPane;
	MeasureRuler ruler;
	
	@Override
	public void initElements(ScrollPane arragePane, RowPanel channelPane, MeasuredPanel clipPane, MeasureRuler ruler) {
		this.arragePane = arragePane;
		this.channelPane = channelPane;
		this.clipPane = clipPane;
		this.ruler = ruler;
	}

	Resource<?> currentResource;
	Clip currentClip;
	Clip duplicateClip;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Clip current() {
		if(duplicateClip != null) return duplicateClip;
		Resource newCurrentResource = resource.getCurrentResource(Resource.class);
		if(newCurrentResource != currentResource) {
			currentClip = null;
			currentResource = newCurrentResource;
			if(currentResource != null) {
				if(currentResource instanceof ScoreResource) {
					ScoreClip currentScoreClip = new ScoreClip(musicLayer.getScoreHolder());
					currentScoreClip.setScore(currentResource.name());
					currentClip = currentScoreClip;
				}
			}
		}
		return currentClip;
	}

	@Override
	public void duplicate(Clip clip) {
		duplicateClip = clip;
	}

	@Override
	public void usePlugin(InstrumentSection section, Class<? extends Plugin> pluginClass) throws Exception{
		if(pluginClass == null) ((MidiChannel)section.channel).setPlugin(null);
		else ((MidiChannel)section.channel).setPlugin(pluginClass.newInstance());
	}

	@Override
	public NamedEntry<Track> getTargetTrack(String channelname) {
		return musicLayer.getChannelHolder().getTargetTrack(channelname);
	}


	@Override
	public double getCurrentBeatPosition() {
		return musicLayer.getBeatPosition();
	}


	@Override
	public void setCurrentBeatPosition(double beatPosition) {
		musicLayer.setBeatPosition(beatPosition);
	}

	@Override
	public boolean isDuplicating() {
		return duplicateClip != null;
	}

	@Override
	public void endDuplication() {
		duplicateClip = null;
	}
}
