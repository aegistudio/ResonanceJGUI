package net.aegistudio.resonance.jui.resource;

import net.aegistudio.resonance.jui.History;
import net.aegistudio.resonance.music.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.music.NamedHolder;
import net.aegistudio.resonance.music.channel.Score;

public class ScoreResource extends Resource<KeywordEntry<String, Score>> {

	NamedHolder<Score> scoreHolder;
	KeywordEntry<String, Score> score;
	public ScoreResource(History history, NamedHolder<Score> scoreHolder,
			KeywordEntry<String, Score> score) {
		super(history);
		this.scoreHolder = scoreHolder;
		this.score = score;
	}

	@Override
	public KeywordEntry<String, Score> get() {
		return score;
	}

	@Override
	public String name() {
		return score.getKeyword();
	}

	@Override
	protected void subremove() throws Exception {
		scoreHolder.remove(name());
	}

	@Override
	protected void subrename(String newName) {
		scoreHolder.rename(name(), newName);
	}
}
