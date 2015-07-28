package net.aegistudio.resonance.jui.resource;

import java.awt.Component;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.channel.Score;
import net.aegistudio.util.RenamableLabel;

@SuppressWarnings("serial")
public class ScoreEntry extends RenamableEntry{

	ScorePopupMenu scoreMenu;
	public KeywordEntry<String, Score> score;
	public ResourceModel resModel;
	
	public ScoreEntry(ResourceModel resModel, KeywordEntry<String, Score> score) {
		super("res/midi.png", score.getKeyword());
		this.score = score;
		this.resModel = resModel;
		this.scoreMenu = new ScorePopupMenu(resModel, this);
	}

	public void doRename(){
		((RenamableLabel)foldingObject).beginRename();
	}
	
	public void doEdit(){
		resModel.requestScoreEdit(this);
	}
	
	public void doUse(){
		resModel.requestUseScore(this);
	}
	
	@Override
	protected void doSubmit(String oldName, String newName) throws Exception {
		resModel.renameScore(this, oldName, newName);
	}

	@Override
	protected void doShowMenu(Component c, int x, int y) {
		this.scoreMenu.show(c, x, y);
	}

}
