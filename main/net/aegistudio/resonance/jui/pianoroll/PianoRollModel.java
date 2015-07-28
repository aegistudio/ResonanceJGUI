package net.aegistudio.resonance.jui.pianoroll;

import java.util.Collection;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.channel.Note;
import net.aegistudio.resonance.jui.measure.MeasureRuler;

public interface PianoRollModel{
	public void init(KeyboardStrip[] keys, MeasureRuler ruler);
	
	public Collection<KeywordEntry<Double, Note>> allNotes();
	
	public void remove(NoteComponent noteComponent, KeywordEntry<Double, Note> note);
	
	public KeywordEntry<Double, Note> trim(KeywordEntry<Double, Note> note, double offset, double sizeDiff);
	
	public KeywordEntry<Double, Note> velocitize(KeywordEntry<Double, Note> note, byte velocity);
	
	public NoteComponent create(NoteStrip key, double offset, double size);

	public String getScoreName();
}
