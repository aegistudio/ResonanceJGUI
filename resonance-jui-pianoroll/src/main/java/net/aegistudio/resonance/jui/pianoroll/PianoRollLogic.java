package net.aegistudio.resonance.jui.pianoroll;

import java.awt.Component;
import java.util.Collection;

import net.aegistudio.resonance.music.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.music.channel.Note;
import net.aegistudio.resonance.music.channel.Score;
import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.resource.ScoreResource;

public class PianoRollLogic implements PianoRollModel
{
	public final ScoreResource targetScore;
	public final Score score;
	public PianoRollLogic(ScoreResource targetScore) {
		this.targetScore = targetScore;
		this.score = this.targetScore.get().getValue();
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
	
	public String getScoreName() {
		return this.targetScore.name();
	}
	
	public void batchMove(int delta) {
		for(KeyboardStrip strip : keys)
			for(Component component : strip.noteStrip.getComponents())
				if(component instanceof NoteComponent)
					strip.noteStrip.remove(component);
		for(KeywordEntry<Double, Note> entry : score.getAllNotes().all()) {
				entry.getValue().pitch = (byte) ((entry.getValue().pitch + delta) % 128);
				if(entry.getValue().pitch < 0) entry.getValue().pitch += 128;
				keys[entry.getValue().pitch].noteStrip.add(new NoteComponent(this,
						keys[entry.getValue().pitch].noteStrip, entry, ruler));	
		}
		for(KeyboardStrip strip : keys)
			strip.noteStrip.recalculateMeasure();
	}

	@Override
	public void close() {
		targetScore.unedit();
	}
}
