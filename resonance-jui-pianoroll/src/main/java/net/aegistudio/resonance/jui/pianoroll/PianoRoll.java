package net.aegistudio.resonance.jui.pianoroll;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;

import javax.swing.JFrame;

import net.aegistudio.resonance.music.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.music.channel.Note;
import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.scroll.RowPanel;
import net.aegistudio.resonance.jui.scroll.ScrollPane;

@SuppressWarnings("serial")
public class PianoRoll extends JFrame
{
	protected final PianoRollModel model;
	
	protected final ScrollPane pianoRoll;
	
	public boolean activated = true;
	
	protected KeyboardStrip[] keys = new KeyboardStrip[128];
	
	public PianoRoll(PianoRollModel model) {
		this.model = model;
		MeasureRuler ruler = new MeasureRuler();
		RowPanel rowPanel = new RowPanel();
		
		super.setAlwaysOnTop(true);
		super.setTitle("Piano Roll");
		super.setSize(800, 500);
		
		pianoRoll = new ScrollPane(ruler, rowPanel){{
				super.viewPanel.setBackground(Color.WHITE);
		}};
		super.add(pianoRoll);
		
		super.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				activated = false;
				dispose();
			}
		});
		
		rowPanel.setSectionWidth(KeyboardStrip.sectionWidth);
		
		for(int i = keys.length - 1; i >= 0; i --)
			rowPanel.addRowContent(keys[i] = new KeyboardStrip(model, i, ruler));
		
		model.init(keys, ruler);
		
		Collection<KeywordEntry<Double, Note>> notes = model.allNotes();
		for(KeywordEntry<Double, Note> note : notes) {
			int pitch = note.getValue().pitch;
			keys[pitch].getMainScroll().add(new NoteComponent(model, keys[pitch].getMainScroll(), note, ruler));
		}
		
		this.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent ke)
			{
				if(ke.isControlDown())
				{
					if(ke.getKeyCode() == KeyEvent.VK_UP)
						model.batchMove(1);
					else if(ke.getKeyCode() == KeyEvent.VK_DOWN)
						model.batchMove(-1);
				}
			}
		});
		
		new Thread()
		{
			public void run(){
				while(activated && !PianoRoll.this.isVisible())
					try {
						Thread.sleep(100L);
					} catch (Exception e) {
						
					}
				
				pianoRoll.getVerticalScrollBar()
					.setValue((127 - 4 * 12) * KeyboardStrip.sectionHeight);
				
				String scoreName = new String();
				while(activated) try
				{
					PianoRoll.this.repaint();
					
					if(scoreName != model.getScoreName())
					{
						scoreName = model.getScoreName();
						if(scoreName == null) {
							setVisible(false);
							activated = false;
						}
						else setTitle("Piano Roll - " + scoreName);
					}
					
					Thread.sleep(20L);
				}
				catch(Exception e){
					
				}
			}
		}.start();
		
	}
}
