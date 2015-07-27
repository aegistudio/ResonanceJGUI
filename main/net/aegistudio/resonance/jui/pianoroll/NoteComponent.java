package net.aegistudio.resonance.jui.pianoroll;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.aegistudio.resonance.KeywordArray.KeywordEntry;
import net.aegistudio.resonance.channel.Note;
import net.aegistudio.resonance.jui.measure.Measurable;
import net.aegistudio.resonance.jui.measure.MeasureRuler;

@SuppressWarnings("serial")
public class NoteComponent extends Component implements Measurable
{
	protected KeywordEntry<Double, Note> note;
	
	public final NoteStrip parent;
	
	int cornerSize = 8;
	int processState = 0;
	int potentialState = 0;
	
	byte velocity;
	
	public static Cursor move = new Cursor(Cursor.MOVE_CURSOR);
	public static Cursor trimLeft = new Cursor(Cursor.E_RESIZE_CURSOR);
	public static Cursor trimRight = new Cursor(Cursor.W_RESIZE_CURSOR);
	
	public NoteComponent(PianoRollModel model, final NoteStrip parent, KeywordEntry<Double, Note> note, final MeasureRuler ruler)
	{
		this.parent = parent;
		this.note = note;
		velocity = note.getValue().velocity;
		
		MouseAdapter shaping = new MouseAdapter()
		{
			Cursor defaultCursor;
			
			public void mouseEntered(MouseEvent me)
			{
				defaultCursor = getCursor();
				addMouseMotionListener(this);
			}
			
			public void mouseMoved(MouseEvent me)
			{
				int x = me.getX();
				if(x < cornerSize)
				{
					setCursor(trimLeft);
					potentialState = 1;
				}
				else if(x >= getWidth() - cornerSize)
				{
					setCursor(trimRight);
					potentialState = 2;
				}
				else
				{
					setCursor(move);
					potentialState = 0;
				}
			}
			
			public void mouseExited(MouseEvent me)
			{
				setCursor(defaultCursor);
				removeMouseListener(this);
			}
		};
		super.addMouseListener(shaping);
		
		MouseAdapter response = new MouseAdapter()
		{
			Dimension size;
			Point notePosition;
			Point screenPosition;
			byte velocityOrigin;
			public void mousePressed(MouseEvent me)
			{
				if(me.getButton() == MouseEvent.BUTTON1)
				{
					processState = potentialState;
					size = getPreferredSize();
					notePosition = getLocation();
					screenPosition = me.getLocationOnScreen();
					velocityOrigin = velocity;
					addMouseMotionListener(this);
				}
			}
			
			public void mouseClicked(MouseEvent me)
			{
				if(me.getButton() == MouseEvent.BUTTON3)
					model.remove(NoteComponent.this, NoteComponent.this.note);
			}
			
			public void mouseDragged(MouseEvent me)
			{
				if(processState == 0)
				{
					setLocation(notePosition.x + me.getLocationOnScreen().x - screenPosition.x, notePosition.y);
					
					int velocityResult = velocityOrigin - (me.getLocationOnScreen().y - screenPosition.y);
					velocity = (byte) Math.max(Math.min(velocityResult, 127), 0);
				}
				else if(processState == 1)
				{
					// Trim Left
					int noteLocationX = notePosition.x + me.getLocationOnScreen().x - screenPosition.x;
					int noteLength = size.width - (me.getLocationOnScreen().x - screenPosition.x);
					if(noteLength > 0)
					{
						setLocation(noteLocationX, notePosition.y);
						setPreferredSize(new Dimension(noteLength, size.height));
					}
				}
				else if(processState == 2)
				{
					// Trim Right
					int noteLength = size.width + (me.getLocationOnScreen().x - screenPosition.x);
					if(noteLength > 0)
						setPreferredSize(new Dimension(noteLength, size.height));
				}
			}
			
			public void mouseReleased(MouseEvent me)
			{

				if(me.getButton() == MouseEvent.BUTTON1)
				{
					removeMouseMotionListener(this);
					if(processState == 0)
					{
						NoteComponent.this.note = model.trim(NoteComponent.this.note, ruler.getBeat(getLocation().x - notePosition.x), 0);
						NoteComponent.this.note = model.velocitize(NoteComponent.this.note, velocity);
						velocity = NoteComponent.this.note.getValue().velocity;
					}
					else if(processState == 1 || processState == 2) 
					{
						NoteComponent.this.note = model.trim(NoteComponent.this.note,
								ruler.getBeat(getLocation().x - notePosition.x), ruler.getBeat(getPreferredSize().width - size.width));
					}
					parent.recalculateMeasure();
				}
			}
		};
		super.addMouseListener(response);
	}
	
	public void paint(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawRect(0, 1, getWidth() - 1, getHeight() - 3);
		int remainderHeight = (int)((1.0 * velocity) / 128 * getHeight());
	
		g.fillRect(0, getHeight() - remainderHeight, getWidth(), remainderHeight);
	}
	
	@Override
	public double start() {
		return note.getKeyword();
	}

	@Override
	public double duration() {
		return note.getValue().getLength();
	}
	
}
