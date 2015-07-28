package net.aegistudio.util;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class RenamableLabel extends JPanel
{
	protected JLabel nameLabel = new JLabel();
	protected JTextField renameField = new JTextField();
	
	protected final MouseAdapter doubleClickRename;
	
	String name;
	boolean isEditing;
	
	protected RenamableLabel(String initialName)
	{
		super.setLayout(null);
		super.setOpaque(true);
		name = initialName;
		this.nameLabel.setText(initialName);
		this.renameField.setText(initialName);
		
		this.nameLabel.addMouseListener(doubleClickRename = new MouseAdapter()
		{
			public void mouseClicked(MouseEvent click)
			{
				if(click.getClickCount() >= 2)
					beginRename();
			}
		});
		
		super.add(this.nameLabel, 0);
		
		renameField.setVisible(false);
		isEditing = false;
		renameField.setFont(nameLabel.getFont());
		renameField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					renameField.setVisible(false);
					nameLabel.setVisible(true);
					isEditing = false;
					if(!renameField.getText().equals(nameLabel.getText())) try
					{
						submit(name, renameField.getText());
						name = renameField.getText();
						nameLabel.setText(name);
					}
					catch(Exception e)
					{
						JOptionPane.showConfirmDialog(RenamableLabel.this, e.getMessage(), "Rename failed!",
								JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
						renameField.setText(nameLabel.getText());
					}
				}
			}			
		});
		super.add(this.renameField, 0);
	}
	
	public void beginRename()
	{
		isEditing = true;
		renameField.setVisible(true);
	}
	
	protected int nameLabelX = 1;
	protected int nameLabelY = -1;
	
	protected int renameFieldX = 0;
	protected int renameFieldY = 0;
	
	public void paint(Graphics g)
	{
		this.nameLabel.setLocation(nameLabelX, nameLabelY);
		this.nameLabel.setSize(getSize());
		if(this.nameLabel.getIcon() == null)
		{
			this.renameField.setLocation(renameFieldX, renameFieldY);
			this.renameField.setSize(getSize());
		}
		else
		{
			this.renameField.setLocation(renameFieldX + getHeight(), renameFieldY);
			this.renameField.setSize(getWidth() - getHeight(), getHeight());
		}
		super.paint(g);
	}
	
	public boolean isEditing()
	{
		return this.isEditing;
	}
	
	protected abstract void submit(String oldName, String newName) throws Exception;
	
	public String getName()
	{
		return name;
	}
}
