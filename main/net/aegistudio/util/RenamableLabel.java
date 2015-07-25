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
	JLabel nameLabel = new JLabel();
	JTextField renameField = new JTextField();
	
	String name;
	
	protected RenamableLabel(String initialName)
	{
		super.setLayout(null);
		name = initialName;
		this.nameLabel.setText(initialName);
		this.renameField.setText(initialName);
		
		this.nameLabel.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent click)
			{
				if(click.getClickCount() >= 2)
					beginRename();
			}
		});
		
		super.add(this.nameLabel);
		
		renameField.setVisible(false);
		renameField.setFont(nameLabel.getFont());
		renameField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					renameField.setVisible(false);
					nameLabel.setVisible(true);
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
		super.add(this.renameField);
	}
	
	public void beginRename()
	{
		nameLabel.setVisible(false);
		renameField.setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		this.nameLabel.setLocation(1, -1);
		this.nameLabel.setSize(getSize());
		this.renameField.setLocation(0, 0);
		this.renameField.setSize(getSize());
		super.paint(g);
	}
	
	protected abstract void submit(String oldName, String newName) throws Exception;
	
	public String getName()
	{
		return name;
	}
}
