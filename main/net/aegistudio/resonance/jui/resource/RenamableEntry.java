package net.aegistudio.resonance.jui.resource;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import net.aegistudio.util.FoldableComponent;
import net.aegistudio.util.RenamableLabel;

@SuppressWarnings("serial")
public abstract class RenamableEntry extends FoldableComponent
{
	
	public static long waitInterval = 200L;
	
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
			
			int clickCountDenotation = 0;
			public void mouseClicked(MouseEvent me)
			{
				if(me.getButton() == MouseEvent.BUTTON1)
				{
					if(me.getClickCount() == 1)
					{
						clickCountDenotation = 1;
						new Thread()
						{
							public void run()
							{
								try {
									Thread.sleep(waitInterval);
									if(clickCountDenotation == 1)
										doUse();
									clickCountDenotation = 0;
								} catch (InterruptedException e) {
								
								}
								
							}
						}.start();
					}
					else if(me.getClickCount() == 2)
					{
						clickCountDenotation = 0;
						doEdit();
					}
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
}
