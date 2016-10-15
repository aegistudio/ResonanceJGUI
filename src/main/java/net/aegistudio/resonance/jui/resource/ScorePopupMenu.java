package net.aegistudio.resonance.jui.resource;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.aegistudio.resonance.jui.Main;
import net.aegistudio.resonance.jui.Overwrite;

@SuppressWarnings("serial")
public class ScorePopupMenu extends JPopupMenu
{
	JMenuItem score;
	
	JMenuItem createScore = new JMenuItem("Create Score");
	JMenuItem eraseScore = new JMenuItem("Erase Current Score");
	JMenuItem renameScore = new JMenuItem("Rename Score");

	JMenuItem importScore = new JMenuItem("Import Score");
	JMenuItem exportScore = new JMenuItem("Export Score");
	
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
		
		Main.getMain().theme.configure(createScore, "resource.score");
		createScore.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resModel.createScore();
			}
		});
		super.add(createScore);

		Main.getMain().theme.configure(eraseScore, "common.erase");
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
		
		importScore.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser midiChooser = new JFileChooser();
				midiChooser.changeToParentDirectory();
				FileNameExtensionFilter midiFilter 
					= new FileNameExtensionFilter("Sequence (*.mid)", "mid");
				midiChooser.setFileFilter(midiFilter);
				int returnVal = midiChooser.showOpenDialog(ScorePopupMenu.this.getInvoker());
			    if(returnVal == JFileChooser.APPROVE_OPTION) try {
			    	resModel.importScore(midiChooser.getSelectedFile());
			    }
			    catch(Exception e) {
			    	JOptionPane.showConfirmDialog(ScorePopupMenu.this.getInvoker(),
							String.format("Cannot import score from this file! Caused by: \n%s", e.getMessage()),
							"Import failure!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			    }
			}
			
		});
		super.add(importScore);

		if(scoreEntry == null) exportScore.setEnabled(false);
		exportScore.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser midiChooser = new JFileChooser();
				midiChooser.changeToParentDirectory();
				FileNameExtensionFilter midiFilter 
					= new FileNameExtensionFilter("Midi0 Sequence (*.mid)", "mid");
				midiChooser.setFileFilter(midiFilter);
				midiChooser.setSelectedFile(new File(scoreEntry.score.getKeyword().concat(".mid")));
				int returnVal = midiChooser.showSaveDialog(ScorePopupMenu.this.getInvoker());
			    if(returnVal == JFileChooser.APPROVE_OPTION) try {
			    	if(new Overwrite(midiChooser.getSelectedFile()).ask(ScorePopupMenu.this.getInvoker()))
			    		resModel.exportScore(scoreEntry, midiChooser.getSelectedFile());
			    }
			    catch(Exception e) {
			    	JOptionPane.showConfirmDialog(ScorePopupMenu.this.getInvoker(),
							String.format("Cannot export score to this file! Caused by: \n%s", e.getMessage()),
							"Export failure!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			    }
			}
			
		});
		super.add(exportScore);
		
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
