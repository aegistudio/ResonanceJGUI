package net.aegistudio.resonance.jui.pianoroll;

import java.awt.Component;
import java.awt.Dimension;

import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.scroll.Content;

public class KeyboardStrip implements Content
{
	protected static final boolean[] scheme = new boolean[]{true, false, true, false, true, true, false, true, false, true, false, true};
	
	protected Component keyComponent;
	protected NoteStrip noteStrip;
	
	public static int sectionHeight = 20;
	public static int sectionWidth = 100;
	
	static int getIndex(int note)
	{
		int value = note % 12;
		if(value < 0) return value + 12;
		else return value;
	}
	
	public KeyboardStrip(PianoRollModel model, int note, MeasureRuler ruler)
	{
		if(!scheme[getIndex(note)])
			keyComponent = new BlackKeyComponent();
		else
		{
			keyComponent = new WhiteKeyComponent(scheme[getIndex(note + 1)], scheme[getIndex(note - 1)]);
			if(getIndex(note) == 0) ((WhiteKeyComponent)keyComponent).label.setText("C" + note / 12);
		}
		
		keyComponent.setPreferredSize(new Dimension(sectionWidth, sectionHeight));
		
		noteStrip = new NoteStrip(model, note, !scheme[getIndex(note)], ruler, keyComponent);
	}
	
	@Override
	public Component getSectionScroll() {
		return keyComponent;
	}

	@Override
	public NoteStrip getMainScroll() {
		return noteStrip;
	}
}
