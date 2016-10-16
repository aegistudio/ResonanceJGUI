package net.aegistudio.resonance.jui.resource;

import net.aegistudio.resonance.jui.History;
import net.aegistudio.resonance.jui.Observer;

public abstract class Resource<E> {
	History history;
	public Resource(History history) {
		this.history = history;
	}
	
	public abstract E get();
	
	public abstract String name();
	
	public final Observer<E> remove = new Observer<>();
	public void remove() throws Exception {
		subremove();
		history.abandon(a -> {
			if(a instanceof ResourceRelatedAction)
				return ((ResourceRelatedAction)a).getResource().equals(this);
			return false;
		});
		remove.notify(get());
	}	
	protected abstract void subremove() throws Exception;

	boolean editing = false;
	ResourceEditor<E> current = null;
	
	public void setEditor(ResourceEditor<E> editor) {
		if(editor == current) return;
		
		if(editing) current.end(this);
		current = editor;
		if(editing) current.begin(this);
	}
	
	public void edit() {
		current.begin(this);
		editing = true;
	}
	
	public void unedit() {
		current.end(this);
		editing = false;
	}
	
	public final Observer<E> rename = new Observer<>();
	public void rename(String newName) throws Exception {
		final String oldName = name();
		try {
			history.push(new ResourceRelatedAction() {

				@Override
				public void redo() {
					subrename(newName);
					rename.notify(get());
				}

				@Override
				public void undo() {
					subrename(oldName);	
					rename.notify(get());
				}

				@Override
				public Resource<?> getResource() {
					return Resource.this;
				}
			});
		}
		catch(Throwable t) {
			throw new Exception(t);
		}
	}
	
	protected abstract void subrename(String newName);
}