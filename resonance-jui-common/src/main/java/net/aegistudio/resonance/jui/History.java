package net.aegistudio.resonance.jui;

import java.util.function.Predicate;

public interface History {
	public void push(Action action);
	
	public void undo();
	public Action toUndo();
	
	public void redo();
	public Action toRedo();
	
	public void abandon(Predicate<Action> action);
}
