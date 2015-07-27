package net.aegistudio.resonance.jui.pianoroll;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;

import javax.swing.JFrame;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.channel.Note;
import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.scroll.RowPanel;
import net.aegistudio.scroll.ScrollPane;

@SuppressWarnings("serial")
public class PianoRoll extends JFrame
{
	protected final PianoRollModel model;
	
	protected final ScrollPane pianoRoll;
	
	public boolean activated = true;
	
	protected KeyboardStrip[] keys = new KeyboardStrip[128];
	
	public PianoRoll(PianoRollModel model)
	{
		this.model = model;
		MeasureRuler ruler = new MeasureRuler();
		RowPanel rowPanel = new RowPanel();
		
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
				
				while(activated) try
				{
					PianoRoll.this.repaint();
					Thread.sleep(20L);
				}
				catch(Exception e){
					
				}
			}
		}.start();
		
	}
}
