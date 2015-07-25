package net.aegistudio.resonance.measure;

import java.awt.Component;

@SuppressWarnings("serial")

public abstract class Measurable extends Component{
	public abstract double start();
	
	public abstract double duration();
}
