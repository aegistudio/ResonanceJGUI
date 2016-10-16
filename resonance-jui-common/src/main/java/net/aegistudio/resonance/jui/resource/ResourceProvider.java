package net.aegistudio.resonance.jui.resource;

public interface ResourceProvider {
	public <E extends Resource<?>> E getCurrentResource(Class<E> request);
}