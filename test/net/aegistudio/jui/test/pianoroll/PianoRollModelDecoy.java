package net.aegistudio.jui.test.pianoroll;

import java.util.Collection;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.channel.Note;
import net.aegistudio.resonance.channel.Score;
import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.pianoroll.KeyboardStrip;
import net.aegistudio.resonance.jui.pianoroll.NoteComponent;
import net.aegistudio.resonance.jui.pianoroll.NoteStrip;
import net.aegistudio.resonance.jui.pianoroll.PianoRollModel;

public class PianoRollModelDecoy implements PianoRollModel
{
	Score score = new Score();
	{
		score.addNote(0, new Note((byte)0, (byte)127, 1.0));
		score.addNote(1, new Note((byte)1, (byte)64, 1.0));
	}
	
	@Override
	public Collection<KeywordEntry<Double, Note>> allNotes() {
		return score.getAllNotes().all();
	}

	@Override
	public KeywordEntry<Double, Note> velocitize(KeywordEntry<Double, Note> note, byte velocity) {
		int newVelocity = velocity;
		if(newVelocity > 127) note.getValue().velocity = 127;
		else if(newVelocity < 0) note.getValue().velocity = 0;
		else note.getValue().velocity = (byte) newVelocity;
		return note;
	}
	
	@Override
	public KeywordEntry<Double, Note> trim(KeywordEntry<Double, Note> note, double offset, double sizeDiff) {
		double newDuration = note.getValue().duration + sizeDiff;
		if(newDuration <= 0)
			return note;
		score.removeNote(note);
		note.getValue().duration = newDuration;
		double newOffset = note.getKeyword() + offset;
		if(newOffset < 0) newOffset = 0;
		return score.addNote(newOffset, note.getValue());
	}

	KeyboardStrip[] keys;
	MeasureRuler ruler;
	@Override
	public void init(KeyboardStrip[] keys, MeasureRuler ruler) {
		this.keys = keys;
		this.ruler = ruler;
	}

	@Override
	public void remove(NoteComponent noteComponent, KeywordEntry<Double, Note> note) {
		score.removeNote(note);
		noteComponent.parent.remove(noteComponent);
		noteComponent.parent.recalculateMeasure();
	}

	@Override
	public NoteComponent create(NoteStrip key, double offset, double size) {
		if(size <= 0 || offset < 0) return null;
		KeywordEntry<Double, Note> target = score.addNote(offset, new Note((byte)key.note, (byte)127, size));
		return new NoteComponent(this, key, target, ruler);
	}

}
