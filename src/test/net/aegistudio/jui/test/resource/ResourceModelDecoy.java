package net.aegistudio.jui.test.resource;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.NamedHolder;
import net.aegistudio.resonance.channel.Note;
import net.aegistudio.resonance.channel.Score;
import net.aegistudio.resonance.jui.pianoroll.PianoRoll;
import net.aegistudio.resonance.jui.pianoroll.PianoRollLogic;
import net.aegistudio.resonance.jui.resource.RenamableEntry;
import net.aegistudio.resonance.jui.resource.ResourceModel;
import net.aegistudio.resonance.jui.resource.ScoreCatalog;
import net.aegistudio.resonance.jui.resource.ScoreEntry;

public class ResourceModelDecoy implements ResourceModel
{
	public NamedHolder<Score> scoreHolder = new NamedHolder<Score>("score", false)
	{
		@Override
		protected Score newObject(Class<?> clazz) {
			return new Score();
		}
	};
	{
		Score theScore = scoreHolder.create("TestScore");
		theScore.addNote(0, new Note((byte)30, (byte)127, 4.0));
	}
	
	Object currentResource = null;
	RenamableEntry currentEntry = null;
	
	@Override
	public Object getCurrentResource() {
		return currentResource;
	}

	ScoreCatalog scoreCatalog;
	
	@Override
	public void init(ScoreCatalog scoreCatalog) {
		System.out.println("ResourceModelDecoy.init");
		this.scoreCatalog = scoreCatalog;
	}

	@Override
	public void createScore() {
		System.out.println("ResourceModelDecoy.createScore");
		String newName = this.getNextAvailableScoreName();
		scoreHolder.create(newName);
		KeywordEntry<String, Score> entry = scoreHolder.getEntry(newName);
		scoreCatalog.addOffspring(new ScoreEntry(this, entry));
		scoreCatalog.setFold(false);
	}

	public String getNextAvailableScoreName()
	{
		int scoreIndex = 1;
		while(true)
			if(scoreHolder.get("Score " + scoreIndex) == null)
				return "Score " + scoreIndex;
			else scoreIndex ++;
	}

	@Override
	public void removeScore(ScoreEntry entry) {
		System.out.println("ResourceModelDecoy.remove");
		scoreHolder.remove(entry.score.getKeyword());
		scoreCatalog.removeOffspring(entry);
	}

	@Override
	public void renameScore(ScoreEntry entry, String oldName, String newName) throws Exception
	{
		System.out.println("ResourceModelDecoy.rename");
		try
		{
			scoreHolder.rename(oldName, newName);
		}
		catch(RuntimeException e)
		{
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public Collection<? extends KeywordEntry<String, Score>> allScores() {
		return scoreHolder.allEntries();
	}

	public final HashMap<Score, PianoRoll> scoreMapper = new HashMap<Score, PianoRoll>();
	
	@Override
	public void requestScoreEdit(ScoreEntry entry) {
		synchronized(scoreMapper)
		{
			PianoRoll pianoRoll = scoreMapper.get(entry.score.getValue());
			if(pianoRoll != null && pianoRoll.activated)
				pianoRoll.requestFocus();
			else
			{
				pianoRoll = new PianoRoll(new PianoRollLogic(entry.score));
				scoreMapper.put(entry.score.getValue(), pianoRoll);
				pianoRoll.setVisible(true);
			}
		}
		cleanupScoreEdit();
	}
	
	public void cleanupScoreEdit()
	{
		synchronized(scoreMapper){
			Iterator<Entry<Score, PianoRoll>> entries = scoreMapper.entrySet().iterator();
			while(entries.hasNext())
			{
				Entry<Score, PianoRoll> entry = entries.next();
				if(!entry.getValue().activated)
					entries.remove();
			}
		}
	}

	@Override
	public void requestUseScore(ScoreEntry entry) {
		this.useResource(entry, entry.score);
	}
	
	public void useResource(RenamableEntry entry, Object resource){
		if(currentEntry != null)
			currentEntry.setUsed(false);
		
		currentEntry = entry;
		currentResource = resource;
		
		entry.setUsed(true);
	}

	@Override
	public void importScore(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportScore(ScoreEntry entry, File file) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
