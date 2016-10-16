package net.aegistudio.resonance.jui.resource;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

import net.aegistudio.resonance.jui.Theme;
import net.aegistudio.resonance.jui.util.FoldableComponent;

@SuppressWarnings("serial")
public class ScoreCatalog extends FoldableComponent {
	ScorePopupMenu scoreMenu;
	
	public ScoreCatalog(Theme theme, ResourceModel resModel) {
		super(theme, new JLabel("Score") {{
			theme.configure(this, "resource.score");
		}});
		
		this.scoreMenu = new ScorePopupMenu(theme, resModel, null);
		
		super.foldingObject.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if(me.getButton() == MouseEvent.BUTTON3)
					scoreMenu.show(foldingObject, me.getX(), me.getY());
			}
		});
		
		for(ScoreResource entry : resModel.allScores())
			this.addOffspring(new ScoreEntry(theme, resModel, entry));
		this.setFold(false);
	}
}
