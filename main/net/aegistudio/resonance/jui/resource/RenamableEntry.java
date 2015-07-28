package net.aegistudio.resonance.jui.resource;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import net.aegistudio.util.FoldableComponent;
import net.aegistudio.util.RenamableLabel;

@SuppressWarnings("serial")
public abstract class RenamableEntry extends FoldableComponent
{
	
	public RenamableEntry(String icon, String initialName) {
		super(null);
		
		super.foldingObject = new RenamableLabel(initialName)
		{
			{
				nameLabel.setIcon(new ImageIcon("res/midi.png"));
				nameLabel.removeMouseListener(doubleClickRename);
				setSize(200, 22);
			}
			
			@Override
			protected void submit(String oldName, String newName) throws Exception {
				doSubmit(oldName, newName);
			}
		};
		super.add(foldingObject);
		super.recalculateSize();
		
		super.foldingObject.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				if(me.getButton() == MouseEvent.BUTTON3)
					doShowMenu(RenamableEntry.this, me.getX(), me.getY());
			}
			
			public void mouseClicked(MouseEvent me)
			{
				if(me.getButton() == MouseEvent.BUTTON1)
				{
					if(me.getClickCount() == 1)
						doUse();
					else if(me.getClickCount() == 2)
						doEdit();
					
				}
			}
		});
	}

	protected abstract void doSubmit(String oldName, String newName) throws Exception;
	
	protected abstract void doShowMenu(Component c, int x, int y);
	
	protected abstract void doUse();
	
	protected abstract void doEdit();
	
	public void doRename(){
		((RenamableLabel)super.foldingObject).beginRename();
	}
	
	public void setUsed(boolean isUsed){
		setBackground(isUsed? Color.BLUE : null);
		setForeground(isUsed? Color.WHITE : Color.BLACK);
	}
}
