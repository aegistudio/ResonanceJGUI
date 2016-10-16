package net.aegistudio.resonance.jui.resource;

public interface ResourceEditor<E> {
	public <F extends Resource<E>> void begin(F resource);
	
	public <F extends Resource<E>> void end(F resource);
}
