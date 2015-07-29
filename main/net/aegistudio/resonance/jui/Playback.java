package net.aegistudio.resonance.jui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import net.aegistudio.resonance.Resonance;
import net.aegistudio.resonance.jui.measure.MeasureRuler;

@SuppressWarnings("serial")
public class Playback extends JFrame{
	JButton play, pause, stop;
	JButton playMode;
	
	ImageIcon endless = new ImageIcon("res/endless.png");
	ImageIcon stopAtTheEnd = new ImageIcon("res/stop_at_the_end.png");
	ImageIcon loop = new ImageIcon("res/loop.png");
	
	int mode = 0;	//0 = ENDLESS, 1 = STOP_AT_THE_END, 2 = LOOP
	
	public Playback(Resonance resonance, MeasureRuler ruler)
	{
		super();
		super.setSize(650, 80);
		super.setLayout(null);
		super.setTitle("Playback");
		
		play = new JButton()
		{
			{
				setToolTipText("Play");
				setIcon(new ImageIcon("res/play.png"));
			}
			{
				super.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) {
						resonance.play();
						play.setVisible(false);
						pause.setVisible(true);
					}
				});
			}
		};
		play.setSize(40, 40);
		play.setLocation(5, 10);
		super.add(play);
		
		pause = new JButton()
		{
			{
				setToolTipText("Pause");
				setIcon(new ImageIcon("res/pause.png"));
			}
			{
				super.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) {
						resonance.pause();
						pause.setVisible(false);
						play.setVisible(true);
					}
				});
			}
		};
		pause.setSize(40, 40);
		pause.setLocation(5, 10);
		super.add(pause);
		
		stop = new JButton()
		{
			{
				setToolTipText("Stop");
				setIcon(new ImageIcon("res/stop.png"));
			}
			{
				super.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) {
						resonance.stop();
						play.setVisible(true);
						pause.setVisible(false);
					}
				});
			}
		};
		stop.setSize(40, 40);
		stop.setLocation(50, 10);
		super.add(stop);
		
		this.playMode = new JButton()
		{
			JPopupMenu playModeMenu = new JPopupMenu();
			{
				setToolTipText("Endless");
				setIcon(endless);
				JMenuItem playModeItem = new JMenuItem("Play Mode");
				playModeItem.setForeground(Color.GREEN.darker());
				playModeMenu.setEnabled(false);
				playModeMenu.add(playModeItem);
				playModeMenu.addSeparator();
				
				JMenuItem endlessItem = new JMenuItem("Endless");
				endlessItem.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						endlessItem.setEnabled(false);
						mode = 0;
						playMode.setIcon(endless);
						playMode.setToolTipText("Endless");
					}
				});
				playModeMenu.add(endlessItem);
				
				addMouseListener(new MouseAdapter(){
					public void mousePressed(MouseEvent me)
					{
						playModeMenu.show(playMode, me.getX(), me.getY());
					}
				});
			}
		};
		playMode.setSize(40, 40);
		playMode.setLocation(95, 10);
		super.add(playMode);
		
		Component position = new JLabel()
		{
			{
				setToolTipText("Beat Position");
				setForeground(Color.WHITE);
				setHorizontalAlignment(CENTER);
			}
			public void paint(Graphics g)
			{
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getWidth(), getHeight());
				super.paint(g);
				int bpmeasure = ruler.beatPerMeasure;
				double position = resonance.musicFacade.getBeatPosition();
				int measure = (int) (Math.floor(position) / bpmeasure);
				double beat = position - bpmeasure * measure;
				setText(String.format("%d:%.3f", measure + 1, beat + 1));
				super.paint(g);
			}
		};
		position.setSize(100, 40);
		position.setLocation(140, 10);
		super.add(position);
		
		Component progressBar = new JLabel()
		{
			public void paint(Graphics g)
			{
				g.setColor(Color.BLACK);
				g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
				g.drawLine(0, getHeight() / 2 + 1, getWidth(), getHeight() / 2 + 1);
				
				double length = Math.max(resonance.musicFacade.getLength(), 1.0);
				double position = Math.max(resonance.musicFacade.getBeatPosition(), 0.0);
				
				if(position <= length)
				{
					int location = (int) (position / length * getWidth());
					g.fillRect(location, getHeight() / 2 - 5, 5, getHeight() / 2 - 5);
				}
			}
			
			{
				MouseAdapter positional = new MouseAdapter()
				{
					protected void actionProcess(MouseEvent me)
					{
						double position = (1.0 * me.getX()) / getWidth() * resonance.musicFacade.getLength();
						position = Math.min(Math.max(position, 0), resonance.musicFacade.getLength());
						resonance.musicFacade.setBeatPosition(position);
					}
					
					public void mousePressed(MouseEvent me)
					{
						actionProcess(me);
					}
					
					public void mouseDragged(MouseEvent me)
					{
						actionProcess(me);
					}
				};
				addMouseListener(positional);
				addMouseMotionListener(positional);
			}
		};
		
		super.add(progressBar);
		progressBar.setLocation(245, 10);
		progressBar.setSize(400, 40);
		
		new Thread()
		{
			public void run()
			{
				while(true) try
				{
					repaint();
					Thread.sleep(100L);
				}
				catch(Exception e)
				{
					
				}
			}
		}
		.start();
	
	}
}
