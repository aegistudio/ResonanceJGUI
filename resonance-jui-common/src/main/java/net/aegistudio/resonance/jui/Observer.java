package net.aegistudio.resonance.jui;

import java.util.HashSet;
import java.util.function.Consumer;

public class Observer<E> {
	public final HashSet<Consumer<E>> consumer = new HashSet<>();
	
	public void notify(E e) {
		consumer.forEach(c -> c.accept(e));
	}	
	
	public void subscribe(Consumer<E> e) {
		consumer.add(e);
	}
	
	public void unsubscribe(Consumer<E> e) {
		consumer.remove(e);
	}
}
