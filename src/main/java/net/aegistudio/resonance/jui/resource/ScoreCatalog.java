package net.aegistudio.resonance.jui.resource;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import net.aegistudio.resonance.music.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.music.channel.Score;
import net.aegistudio.util.FoldableComponent;


@SuppressWarnings("serial")
public class ScoreCatalog extends FoldableComponent {
	ScorePopupMenu scoreMenu;
	
	public ScoreCatalog(ResourceModel resModel) {
		super(new JLabel("Score") {{
			setIcon(new ImageIcon("res/midi.png"));
		}});
		
		this.scoreMenu = new ScorePopupMenu(resModel, null);
		
		super.foldingObject.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				if(me.getButton() == MouseEvent.BUTTON3)
					scoreMenu.show(foldingObject, me.getX(), me.getY());
			}
		});
		
		for(KeywordEntry<String, Score> entry : resModel.allScores())
			this.addOffspring(new ScoreEntry(resModel, entry));
		this.setFold(false);
	}
}
