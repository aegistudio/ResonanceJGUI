package net.aegistudio.jui.test.arranger;

import java.util.Collection;

import net.aegistudio.resonance.KeywordArray;
import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.NamedHolder;
import net.aegistudio.resonance.NamedHolder.NamedEntry;
import net.aegistudio.resonance.channel.Channel;
import net.aegistudio.resonance.channel.ChannelHolder;
import net.aegistudio.resonance.channel.Clip;
import net.aegistudio.resonance.channel.MidiChannel;
import net.aegistudio.resonance.channel.Score;
import net.aegistudio.resonance.channel.ScoreClip;
import net.aegistudio.resonance.jui.arranger.ArrangerModel;
import net.aegistudio.resonance.jui.arranger.ArrangerView;
import net.aegistudio.resonance.jui.arranger.ChannelSection;
import net.aegistudio.resonance.jui.arranger.ChannelStrip;
import net.aegistudio.resonance.jui.arranger.ClipComponent;
import net.aegistudio.resonance.jui.arranger.ClipStrip;
import net.aegistudio.resonance.jui.arranger.InstrumentSection;
import net.aegistudio.resonance.jui.arranger.ScoreClipComponent;
import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.measure.MeasuredPanel;
import net.aegistudio.resonance.mixer.Mixer;
import net.aegistudio.resonance.mixer.Track;
import net.aegistudio.resonance.plugin.Plugin;
import net.aegistudio.resonance.test.util.SineOscillator;
import net.aegistudio.scroll.RowPanel;
import net.aegistudio.scroll.ScrollPane;

public class ArrangerModelDecoy implements ArrangerModel
{
	double currentBPos = 0;
	
	@Override
	public double getCurrentBeatPosition() {
		//System.out.println("Calling getCurrentBeatPosition");
		return currentBPos;
	}

	@Override
	public void setCurrentBeatPosition(double beatPosition) {
		//System.out.println("Calling setCurrentBeatPosition");
		currentBPos = beatPosition;
	}

	NamedHolder<Score> scoreHolder = new NamedHolder<Score>("score", false)
	{
		@Override
		protected Score newObject(Class<?> clazz) {
			return new Score();
		}
	};

	Mixer mixer = new Mixer();
	{
		mixer.renameMaster("<master>");
	}
	
	ChannelHolder channels = new ChannelHolder(mixer, scoreHolder);
		
	protected String getNextDefaultName(Class<? extends Channel> channelType) {
		System.out.println("Calling getNextDefaultName");
		String prefix = channelType == MidiChannel.class? "Instrument " : "Channel ";
		for(int i = 1; true; i ++)
			if(channels.get(prefix + i) == null)
				return prefix + i;
	}


	@Override
	public void createChannel(Class<? extends Channel> channelType) {
		System.out.println("Calling createChannel");
		String channelName = this.getNextDefaultName(channelType);
		Channel channel = channels.create(channelName, channelType);
		if(channel == null) return;
		
		this.createChannelUI(channelName, channel);
	}
	
	@SuppressWarnings("serial")
	protected void createChannelUI(String channelName, Channel channel)
	{
		ChannelSection channelSection = null;
		ClipStrip clips = null;
		
		if(channel instanceof MidiChannel)
		{
			channelSection = new InstrumentSection(this, channelName, (MidiChannel)channel);
		
			clips = new ClipStrip(this, ruler, channelSection){
				
				@Override
				protected boolean accept(Object resource) {
					if(resource == null) return false;
					else return resource instanceof ScoreClip;
				}
			};
		}
		
		channelPane.addRowContent(channelSection.parent = new ChannelStrip(channelSection, clips));
	}
	
	@Override
	public Collection<? extends KeywordEntry<String, Channel>> allChannels() {
		return channels.allEntries();
	}
	
	@Override
	public void removeChannel(ChannelSection channelSection) {
		channels.remove(channelSection.getChannelName());
		channelPane.removeRowContent(channelSection.parent);
	}

	@Override
	public void renameChannel(ChannelSection channelSection, String oldName, String newName) {
		System.out.println("Calling renameChannel: " + oldName + ", " + newName);
		channels.rename(oldName, newName);
	}

	@Override
	public void removeClip(ChannelSection channel, ClipComponent clip) {
		System.out.println("remove");
		((MeasuredPanel)channel.parent.getMainScroll())
			.remove(clip);
		((MeasuredPanel)channel.parent.getMainScroll())
			.recalculateMeasure();
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
		if(mixer.hasUpdated(this))
			targetTracks = mixer.allEntries().toArray(new NamedHolder.NamedEntry[0]);
		
		return targetTracks;
	}

	@SuppressWarnings({"unchecked"})
	NamedHolder.NamedEntry<Class<? extends Plugin>>[] plugins = new NamedHolder.NamedEntry[]{
			new NamedHolder.NamedEntry<Class<SineOscillator>>("<none>", null),
			new NamedHolder.NamedEntry<Class<SineOscillator>>("Sine Oscillator", SineOscillator.class)
	};
	
	@Override
	public KeywordEntry<String, Class<? extends Plugin>>[] getPlugins() {
		return plugins;
	}

	ScrollPane arragePane;
	RowPanel channelPane;
	MeasuredPanel clipPane;
	MeasureRuler ruler;
	
	@Override
	public void initElements(ScrollPane arragePane, RowPanel channelPane, MeasuredPanel clipPane, MeasureRuler ruler) {
		System.out.println("Calling initElements");
		this.arragePane = arragePane;
		this.channelPane = channelPane;
		this.clipPane = clipPane;
		this.ruler = ruler;
	}

	@Override
	public Clip current() {
		return new ScoreClip(scoreHolder)
		{
			{
				super.scoreEntry = new NamedEntry<Score>("", null);
				this.clipLength = 4.0;
			}
		};
	}

	@Override
	public void insertClip(ChannelSection channel, double location)
	{
		System.out.println("insert!" + location);
		((MeasuredPanel)channel.parent.getMainScroll())
			.add(new ScoreClipComponent(this, channel, new KeywordArray.DefaultKeywordEntry<Double, ScoreClip>(location, (ScoreClip)current()), ruler));
		
		((MeasuredPanel)channel.parent.getMainScroll())
			.recalculateMeasure();
	}

	@Override
	public KeywordEntry<Double, Clip> trim(ChannelSection channelSection, ClipComponent clip, double offset, double length) {
		ScoreClip sclip = (ScoreClip)clip.clipEntry.getValue();
		(sclip).trim(sclip.getLength() + length - offset, sclip.getLength() + offset);
		return new KeywordArray.DefaultKeywordEntry<Double, Clip>(clip.start() + offset, sclip);
	}

	@Override
	public KeywordEntry<Double, Clip> move(ChannelSection channelSection, ClipComponent clip, double delta) {
		return new KeywordArray.DefaultKeywordEntry<Double, Clip>(clip.start() + delta, clip.clipEntry.getValue());
	}

	@Override
	public void duplicate(Clip clip) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void usePlugin(InstrumentSection section, Class<? extends Plugin> pluginClass) throws Exception{
		if(pluginClass == null) ((MidiChannel)section.channel).setPlugin(null);
		else ((MidiChannel)section.channel).setPlugin(pluginClass.newInstance());
	}

	@Override
	public NamedEntry<Track> getTargetTrack(String channelname) {
		return channels.getTargetTrack(channelname);
	}

	@Override
	public boolean isDuplicating() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void endDuplication() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(ArrangerView view) {
		// TODO Auto-generated method stub
		
	}

}
