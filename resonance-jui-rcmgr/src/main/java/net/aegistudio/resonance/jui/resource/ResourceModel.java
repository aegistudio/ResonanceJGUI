package net.aegistudio.resonance.jui.resource;

import java.io.File;
import java.util.Collection;

public interface ResourceModel extends ResourceProvider {
	public void init(ScoreCatalog scoreCatalog);
	
	// Required For Score Resource Managing
	public Collection<? extends ScoreResource> allScores();
	public void createScore();
	public void removeScore(ScoreEntry entry) throws Exception;
	public void requestUseScore(ScoreEntry entry);

	public void importScore(File file) throws Exception;					//Import Score From Midi File!
	public void exportScore(ScoreEntry entry, File file) throws Exception;	//Import Score To Midi0 File!
}