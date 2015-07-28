package net.aegistudio.resonance.jui.arranger;

import java.awt.Graphics;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.channel.Score;
import net.aegistudio.resonance.channel.ScoreClip;
import net.aegistudio.resonance.jui.measure.MeasureRuler;

@SuppressWarnings("serial")
public class ScoreClipComponent extends ClipComponent
{

	public ScoreClipComponent(ArrangerModel model, ChannelSection channel, KeywordEntry<Double, ScoreClip> clipEntry,
			MeasureRuler ruler) {
		super(model, channel, clipEntry, ruler);
		scoreClip = clipEntry;
	}
	
	KeywordEntry<Double, ScoreClip> scoreClip;
	
	public void paint(Graphics g)
	{
		String name = "";
		KeywordEntry<String, Score> score = scoreClip.getValue().getScore();
		if(score != null) name = score.getKeyword();
		super.clipDenotation.setText(name);
		super.paint(g);
	}
}
