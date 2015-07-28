package net.aegistudio.resonance.jui.resource;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class ScorePopupMenu extends JPopupMenu
{
	JMenuItem score;
	
	JMenuItem createScore = new JMenuItem("Create Score");
	JMenuItem eraseScore = new JMenuItem("Erase Current Score");
	JMenuItem renameScore = new JMenuItem("Rename Score");
	
	JMenuItem editScore = new JMenuItem("Edit Current Score");
	JMenuItem useScore = new JMenuItem("Use Score");
	
	protected final ScoreEntry scoreEntry;
	public ScorePopupMenu(ResourceModel resModel, ScoreEntry scoreEntry){
		this.scoreEntry = scoreEntry;
		
		if(this.scoreEntry == null)
			score = new JMenuItem("Score");
		else score = new JMenuItem(scoreEntry.score.getKeyword());
		
		score.setForeground(Color.BLUE.darker());
		score.setEnabled(false);
		super.add(score);
		
		super.addSeparator();
		
		createScore.setIcon(new ImageIcon("res/midi.png"));
		createScore.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resModel.createScore();
			}
		});
		super.add(createScore);
		
		eraseScore.setIcon(new ImageIcon("res/erase.png"));
		eraseScore.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int answer = JOptionPane.showConfirmDialog(scoreEntry,
						String.format("Are you sure to erase the score %s (Can't be undo)?", scoreEntry.score.getKeyword()),
						"Erasure confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(answer == JOptionPane.YES_OPTION) resModel.removeScore(scoreEntry);
			}
		});
		if(scoreEntry == null) eraseScore.setEnabled(false);
		super.add(eraseScore);

		if(scoreEntry == null) renameScore.setEnabled(false);
		renameScore.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(scoreEntry != null)
					scoreEntry.doRename();
			}	
		});
		super.add(renameScore);
		
		super.addSeparator();
		
		if(scoreEntry == null) editScore.setEnabled(false);
		editScore.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				scoreEntry.doEdit();
			}
		});
		super.add(editScore);

		if(scoreEntry == null) useScore.setEnabled(false);
		useScore.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				scoreEntry.doUse();
			}	
		});
		super.add(useScore);
	}
}
