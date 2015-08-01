package net.aegistudio.resonance.jui.arranger;

import java.awt.Graphics;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.channel.Note;
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
	
	public void draw(Graphics g)
	{
		String name = "";
		KeywordEntry<String, Score> score = scoreClip.getValue().getScore();
		if(score != null) name = score.getKeyword();
		super.clipDenotation.setText(name);
		
		if(scoreClip.getValue().getNotes() != null)
		{
			int maxPitch = Integer.MIN_VALUE;
			int minPitch = Integer.MAX_VALUE;
			for(KeywordEntry<Double, Note> note : scoreClip.getValue().getNotes())
			{
				maxPitch = Math.max(note.getValue().pitch, maxPitch);
				minPitch = Math.min(note.getValue().pitch, minPitch);
			}
			
			int difference = (maxPitch - minPitch + 1);
			double unit = 1.0 * (getHeight() - clipDenotation.getHeight()) / difference;
			
			g.setColor(getForeground());
			for(KeywordEntry<Double, Note> note : scoreClip.getValue().getNotes())
			{
				int locationX = super.ruler.getWidth(note.getKeyword() + scoreClip.getValue().getOffset());
				int length = super.ruler.getWidth(note.getValue().duration);
				
				int locationY = clipDenotation.getHeight() + (int)((difference - (note.getValue().pitch - minPitch + 1)) * unit);
				g.fillRect(- super.loadDifference + locationX, locationY, length, (int) unit);
			}
		}
	}
}
