package net.aegistudio.resonance.jui.resource;

public interface ResourceModel {
	
	public void init(ScoreCatalog scoreCatalog);
	
	// Gross Interface For Resourcing.
	public Object getCurrentResource();
	
	// Required For Score Resouce Managing
	public void createScore();
	public void removeScore(ScoreEntry entry);
	public void renameScore(ScoreEntry entry, String oldName, String newName) throws Exception;
	
	
}
