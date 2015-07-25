package net.aegistudio.jui.test.arranger;

import java.util.Collection;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.NamedHolder;
import net.aegistudio.resonance.OrderedNamedHolder;
import net.aegistudio.resonance.channel.Channel;
import net.aegistudio.resonance.channel.Clip;
import net.aegistudio.resonance.channel.MidiChannel;
import net.aegistudio.resonance.channel.Score;
import net.aegistudio.resonance.channel.ScoreClip;
import net.aegistudio.resonance.jui.arranger.ArrangerModel;
import net.aegistudio.resonance.jui.arranger.ChannelSection;
import net.aegistudio.resonance.jui.arranger.ChannelStrip;
import net.aegistudio.resonance.jui.arranger.ClipComponent;
import net.aegistudio.resonance.jui.arranger.ClipStrip;
import net.aegistudio.resonance.jui.arranger.InstrumentSection;
import net.aegistudio.resonance.measure.MeasureRuler;
import net.aegistudio.resonance.measure.MeasuredPanel;
import net.aegistudio.scroll.RowPanel;
import net.aegistudio.scroll.ScrollPane;

public class ArrangerModelDecoy implements ArrangerModel
{
	double currentBPos = 0;
	
	@Override
	public double getCurrentBeatPosition() {
		System.out.println("Calling getCurrentBeatPosition");
		return currentBPos;
	}

	@Override
	public void setCurrentBeatPosition(double beatPosition) {
		System.out.println("Calling setCurrentBeatPosition");
		currentBPos = beatPosition;
	}

	NamedHolder<Score> scoreHolder = new NamedHolder<Score>("score", false)
	{
		@Override
		protected Score newObject(Class<?> clazz) {
			return new Score();
		}
		
	};
	
	OrderedNamedHolder<Channel> channels = new OrderedNamedHolder<Channel>("channel", true){
		@Override
		protected Channel newObject(Class<?> clazz) {
			if(clazz == MidiChannel.class) return new MidiChannel(scoreHolder){};
			return null;
		}
	};
		
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
			channelSection = new InstrumentSection(this, channelName, channel);
		
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
	public Collection<KeywordEntry<String, Channel>> allChannels() {
		return channels.allEntries();
	}
	@Override
	public void removeChannel(ChannelSection channelSection) {
		channels.remove(channelSection.getChannelName());
		channelPane.removeRowContent(channelSection.parent);
	}

	@Override
	public void renameChannel(String oldName, String newName) {
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

	@Override
	public String[] getTargerTracks() {
		return null;
	}

	@Override
	public String[] getPlugins() {
		return null;
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
				this.setScore("Verse");
				this.clipLength = 1.0;
			}
		};
	}

	@SuppressWarnings("serial")
	@Override
	public void insertClip(ChannelSection channel, double location)
	{
		System.out.println("insert!" + location);
		((MeasuredPanel)channel.parent.getMainScroll())
			.add(new ClipComponent(this, channel, location, current(), ruler)
			{
				{
					this.clipDenotation.setText(((ScoreClip)current()).getScore());
				}
			});
		((MeasuredPanel)channel.parent.getMainScroll())
			.recalculateMeasure();
	}

	@Override
	public double trim(ClipComponent clip, double offset, double length) {
		ScoreClip sclip = (ScoreClip)clip.clip;
		(sclip).trim(sclip.getLength() + length, sclip.getLength() + offset);
		return clip.start() + offset;
	}

	@Override
	public double move(ClipComponent clip, double delta) {
		return clip.start() + delta;
	}

}
