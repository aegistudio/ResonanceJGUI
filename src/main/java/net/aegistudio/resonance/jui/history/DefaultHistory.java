package net.aegistudio.resonance.jui.history;

import java.util.Stack;
import java.util.function.Predicate;

public class DefaultHistory implements History {
	public Stack<Action> backward = new Stack<Action>();
	public Stack<Action> forward = new Stack<Action>();
	
	@Override
	public void push(Action action) {
		backward.push(action);
		action.redo();
		forward.clear();
	}

	@Override
	public void undo() {
		if(backward.isEmpty()) return;
		Action action = backward.pop();
		action.undo();
		forward.push(action);
	}

	@Override
	public Action toUndo() {
		if(backward.isEmpty()) return null;
		return backward.peek();
	}

	@Override
	public void redo() {
		if(forward.isEmpty()) return;
		Action action = forward.pop();
		action.redo();
		backward.push(action);
	}

	@Override
	public Action toRedo() {
		if(forward.isEmpty()) return null;
		return forward.peek();
	}

	@Override
	public void abandon(Predicate<Action> action) {
		backward.removeIf(action);
		forward.removeIf(action);
	}
}
