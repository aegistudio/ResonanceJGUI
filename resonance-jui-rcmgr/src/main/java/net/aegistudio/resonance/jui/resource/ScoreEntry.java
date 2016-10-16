package net.aegistudio.resonance.jui.resource;

import java.awt.Component;

import net.aegistudio.resonance.jui.Theme;
import net.aegistudio.resonance.jui.util.RenamableLabel;

@SuppressWarnings("serial")
public class ScoreEntry extends RenamableEntry {

	ScorePopupMenu scoreMenu;
	public ScoreResource score;
	public ResourceModel resModel;
	
	public ScoreEntry(Theme theme, ResourceModel resModel, ScoreResource score) {
		super(theme, "resource.score", score.name());
		this.score = score;
		this.resModel = resModel;
		this.scoreMenu = new ScorePopupMenu(theme, resModel, this);
	}

	public void doRename(){
		((RenamableLabel)foldingObject).beginRename();
	}
	
	public void doEdit(){
		score.edit();
	}
	
	public void doUse(){
		resModel.requestUseScore(this);
	}
	
	@Override
	protected void doSubmit(String oldName, String newName) throws Exception {
		score.rename(newName);
	}

	@Override
	protected void doShowMenu(Component c, int x, int y) {
		this.scoreMenu.show(c, x, y);
	}
}
