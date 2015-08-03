package net.aegistudio.resonance.jui.resource;

import java.io.File;
import java.util.Collection;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.channel.Score;

public interface ResourceModel {
	
	public void init(ScoreCatalog scoreCatalog);
	
	// Gross Interface For Resourcing.
	public Object getCurrentResource();
	
	// Required For Score Resouce Managing
	public Collection<? extends KeywordEntry<String, Score>> allScores();
	public void createScore();
	public void removeScore(ScoreEntry entry);
	public void renameScore(ScoreEntry entry, String oldName, String newName) throws Exception;
	public void requestScoreEdit(ScoreEntry entry);
	public void requestUseScore(ScoreEntry entry);

	public void importScore(File file) throws Exception;		//Import Score From Midi File!
}
