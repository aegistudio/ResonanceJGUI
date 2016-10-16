package net.aegistudio.resonance.jui.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import net.aegistudio.resonance.music.*;
import net.aegistudio.resonance.music.channel.*;
import net.aegistudio.resonance.music.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.music.channel.MidiConverter;
import net.aegistudio.resonance.jui.MainControl;

public class ResourceLogic implements ResourceModel {
	NamedHolder<Score> scoreHolder;
	MainControl mainControl;
	ResourceEditorSet resourceEditorSet;
	
	public ResourceLogic(MainControl mainControl, NamedHolder<Score> scoreHolder, ResourceEditorSet editorSet) {
		this.scoreHolder = scoreHolder;
		this.mainControl = mainControl;
		this.resourceEditorSet = editorSet;
	}
	
	Resource<?> currentResource = null;
	RenamableEntry currentEntry = null;
	
	@SuppressWarnings("unchecked")
	@Override
	public <E extends Resource<?>> E getCurrentResource(Class<E> request) {
		if(currentResource == null) return null;
		if(request.isAssignableFrom(currentResource.getClass()))
			return (E) currentResource;
		return null;
	}
	
	private void removeResource(Resource<?> resource) {
		if(resource == currentResource) useResource(null, null);
		mainControl.getHistory().abandon(a -> {
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

	private ScoreResource getScoreResource(KeywordEntry<String, Score> entry) {
		ScoreResource resource = new ScoreResource(mainControl.getHistory(), scoreHolder, entry);
		resource.setEditor(resourceEditorSet.scoreEditors.get(0));
		return resource;
	}
	
	@Override
	public void createScore() {
		String newName = this.getNextAvailableScoreName();
		scoreHolder.create(newName);
		KeywordEntry<String, Score> entry = scoreHolder.getEntry(newName);
		scoreCatalog.addOffspring(new ScoreEntry(mainControl.getTheme(), 
				this, getScoreResource(entry)));
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
	public void removeScore(ScoreEntry entry) throws Exception {
		entry.score.remove();
		scoreCatalog.removeOffspring(entry);
		removeResource(entry.score);
	}

	@Override
	public Collection<? extends ScoreResource> allScores() {
		ArrayList<ScoreResource> result = new ArrayList<>();
		scoreHolder.allEntries().forEach(e -> result.add(getScoreResource(e)));
		return result;
	}
	
	@Override
	public void requestUseScore(ScoreEntry entry) {
		useResource(entry, entry.score);
	}
	
	public void useResource(RenamableEntry entry, Resource<?> resource) {
		if(currentEntry != null)
			currentEntry.setUsed(false);
		
		currentEntry = entry;
		currentResource = resource;
		
		if(currentEntry != null)
			currentEntry.setUsed(true);
	}

	MidiConverter midiConverter = new MidiConverter();
	@Override
	public void importScore(File file) throws Exception {
		Sequence sequence = MidiSystem.getSequence(file);
		int ticksPerQuarter = sequence.getResolution();
		Track[] tracks = sequence.getTracks();
		String truncatedName = file.getName();
		if(truncatedName.endsWith(".mid")) 
			truncatedName = truncatedName.substring(0, truncatedName.length() - 4);
		
		for(Track track : tracks) {
			String scoreName = truncatedName;
			Score score = scoreHolder.get(scoreName);
			int index = 1;
			while(score != null) {
				scoreName = String.format("%s#%d", truncatedName, index);
				score = scoreHolder.get(scoreName);
				index ++;
			}
			score = scoreHolder.create(scoreName);
			midiConverter.decapsulateTrack(score, track, ticksPerQuarter);
			
			KeywordEntry<String, Score> entry = scoreHolder.getEntry(scoreName);
			scoreCatalog.addOffspring(new ScoreEntry(mainControl.getTheme(), 
					this, getScoreResource(entry)));
			scoreCatalog.setFold(false);
		}
	}

	@Override
	public void exportScore(ScoreEntry entry, File file) throws Exception {
		Sequence sequence = new Sequence(Sequence.PPQ, 480);
		int ticksPerQuarterNote = sequence.getResolution();
		Track midi0Track = sequence.createTrack();
		midiConverter.encapsulateTrack(midi0Track, entry.score.get().getValue(), ticksPerQuarterNote);
		MidiSystem.write(sequence, 0, file);
	}
}
