package net.aegistudio.resonance.jui.resource;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.NamedHolder;
import net.aegistudio.resonance.channel.Score;
import net.aegistudio.resonance.io.MidiConverter;
import net.aegistudio.resonance.jui.Main;
import net.aegistudio.resonance.jui.pianoroll.PianoRoll;
import net.aegistudio.resonance.jui.pianoroll.PianoRollLogic;

public class ResourceLogic implements ResourceModel{
	NamedHolder<Score> scoreHolder;
	public ResourceLogic(NamedHolder<Score> scoreHolder) {
		this.scoreHolder = scoreHolder;
	}
	
	Object currentResource = null;
	RenamableEntry currentEntry = null;
	
	@Override
	public Object getCurrentResource() {
		return currentResource;
	}

	private void removeResource(Object resource) {
		if(resource == currentResource) useResource(null, null);
		Main.getHistory().abandon(a -> {
			if(a instanceof ResourceRelatedAction)
				return ((ResourceRelatedAction)a).getResource() == resource;
			return false;
		});
	}
	
	ScoreCatalog scoreCatalog;
	
	@Override
	public void init(ScoreCatalog scoreCatalog) {
		this.scoreCatalog = scoreCatalog;
	}

	@Override
	public void createScore() {
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
		scoreHolder.remove(entry.score.getKeyword());
		scoreCatalog.removeOffspring(entry);
		removeResource(entry.score);
	}

	@Override
	public void renameScore(ScoreEntry entry, final String oldName, final String newName) throws Exception
	{
		try {
			
			Main.getHistory().push(new ResourceRelatedAction() {
				
				@Override
				public void redo() {
					scoreHolder.rename(oldName, newName);
					entry.updateName(newName);
				}

				@Override
				public void undo() {
					scoreHolder.rename(newName, oldName);
					entry.updateName(oldName);
				}

				@Override
				public Object getResource() {
					return entry.score;
				}
				
				public String toString() {
					return "Rename score";
				}
			});
		}
		catch(RuntimeException e) {
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
		
		if(currentEntry != null)
			currentEntry.setUsed(true);
	}

	MidiConverter midiConverter = new MidiConverter();
	@Override
	public void importScore(File file) throws Exception{
		Sequence sequence = MidiSystem.getSequence(file);
		int ticksPerQuarter = sequence.getResolution();
		Track[] tracks = sequence.getTracks();
		String truncatedName = file.getName();
		if(truncatedName.endsWith(".mid")) 
			truncatedName = truncatedName.substring(0, truncatedName.length() - 4);
		for(Track track : tracks)
		{
			String scoreName = truncatedName;
			Score score = scoreHolder.get(scoreName);
			int index = 1;
			while(score != null)
			{
				scoreName = String.format("%s#%d", truncatedName, index);
				score = scoreHolder.get(scoreName);
				index ++;
			}
			score = scoreHolder.create(scoreName);
			midiConverter.decapsulateTrack(score, track, ticksPerQuarter);
			
			KeywordEntry<String, Score> entry = scoreHolder.getEntry(scoreName);
			scoreCatalog.addOffspring(new ScoreEntry(this, entry));
			scoreCatalog.setFold(false);
		}
	}

	@Override
	public void exportScore(ScoreEntry entry, File file) throws Exception {
		Sequence sequence = new Sequence(Sequence.PPQ, 480);
		int ticksPerQuarterNote = sequence.getResolution();
		Track midi0Track = sequence.createTrack();
		midiConverter.encapsulateTrack(midi0Track, entry.score.getValue(), ticksPerQuarterNote);
		MidiSystem.write(sequence, 0, file);
	}
}
