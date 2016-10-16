package net.aegistudio.resonance.jui.pianoroll;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.aegistudio.resonance.jui.resource.Resource;
import net.aegistudio.resonance.jui.resource.ResourceEditor;
import net.aegistudio.resonance.jui.resource.ScoreResource;
import net.aegistudio.resonance.music.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.music.channel.Score;

public class PianoRollEditor implements ResourceEditor<KeywordEntry<String, Score>>{
	public final HashMap<ScoreResource, PianoRoll> scoreMapper = new HashMap<ScoreResource, PianoRoll>();
	
	@Override
	public <F extends Resource<KeywordEntry<String, Score>>> void begin(F resource) {
		synchronized(scoreMapper) {
			PianoRoll pianoRoll = scoreMapper.get((ScoreResource)resource);
			if(pianoRoll != null && pianoRoll.activated)
				pianoRoll.requestFocus();
			else {
				pianoRoll = new PianoRoll(new PianoRollLogic((ScoreResource)resource));
				scoreMapper.put((ScoreResource)resource, pianoRoll);
				pianoRoll.setVisible(true);
			}
		}
		cleanupScoreEdit();
	}

	@Override
	public <F extends Resource<KeywordEntry<String, Score>>> void end(F resource) {
		PianoRoll pianoRoll = scoreMapper.get((ScoreResource)resource);
		if(pianoRoll != null) {
			pianoRoll.setVisible(false);
			pianoRoll.activated = false;
		}
	}
	
	public void cleanupScoreEdit() {
		synchronized(scoreMapper){
			Iterator<Entry<ScoreResource, PianoRoll>> entries
				= scoreMapper.entrySet().iterator();
			
			while(entries.hasNext()) {
				Entry<ScoreResource, PianoRoll> entry = entries.next();
				if(!entry.getValue().activated)
					entries.remove();
			}
		}
	}
}
