package net.aegistudio.resonance.jui.resource;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JPopupMenu;

import net.aegistudio.util.RenamableLabel;

@SuppressWarnings("serial")
public class RenamableTreeNode extends ComponentTreeNode<RenamableLabel>
{
	protected JPopupMenu menu;
	
	public RenamableTreeNode(Icon icon, String initialName) {
		super(null);
		super.component = new RenamableLabel(initialName){
			{
				setOpaque(true);
				super.nameLabel.setIcon(icon);
			}
			
			public void setForeground(Color fg)
			{
				super.setForeground(fg);
				if(nameLabel != null)
					nameLabel.setForeground(fg);
			}
			
			@Override
			protected void submit(String oldName, String newName) throws Exception {
				RenamableTreeNode.this.submit(oldName, newName);
			}
			
		};
		super.component.setPreferredSize(new Dimension(200, 25));
	}
	
	/**
	 * Must Be Called Under The MouseClicked Thread.
	 */
	
	protected void doRename(){
		super.component.beginRename();
		while(super.component.isEditing())
			Thread.yield();
	};
	
	protected void submit(String oldName, String newName) throws Exception{
		
	}
	
	protected void menuUnshown()
	{
		
	}
	
	protected void doubleClick()
	{
		
	}
	
	boolean editing = false;
	@Override
	public synchronized void mouseClicked(MouseEvent me) {
		
		if(me.getButton() == MouseEvent.BUTTON1){
			if(me.getClickCount() == 2)
				doRename();
				//doubleClick();
		}
		else if(me.getButton() == MouseEvent.BUTTON3){
			if(menu != null) 
			{
				menu.show(super.component, me.getX() - component.getX(),
						me.getY() - component.getY());
				while(menu.isShowing())
					try {
						Thread.sleep(100L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				menuUnshown();
			}
		}
	}

}
