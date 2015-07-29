package net.aegistudio.resonance.jui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
		super.setSize(755, 80);
		super.setLayout(null);
		super.setTitle("Playback");
		
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent ke)
			{
				if(ke.getKeyCode() == KeyEvent.VK_SPACE)
				{
					if(play.isVisible())
						play.doClick();
					else pause.doClick();
				}
			}
		});
		
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
			JMenuItem endlessItem, stopAtTheEndItem, loopItem;
			protected void enableAll()
			{
				endlessItem.setEnabled(true);
				stopAtTheEndItem.setEnabled(true);
				loopItem.setEnabled(true);
			}
			
			{
				setToolTipText("Endless");
				setIcon(endless);
				JMenuItem playModeItem = new JMenuItem("Play Mode");
				playModeItem.setForeground(Color.GREEN.darker());
				playModeMenu.setEnabled(false);
				playModeMenu.add(playModeItem);
				playModeMenu.addSeparator();
				
				endlessItem = new JMenuItem("Endless");
				endlessItem.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						enableAll();
						endlessItem.setEnabled(false);
						mode = 0;
						playMode.setIcon(endless);
						playMode.setToolTipText("Endless");
					}
				});
				endlessItem.setEnabled(false);
				playModeMenu.add(endlessItem);
				
				stopAtTheEndItem = new JMenuItem("Stop At The End");
				stopAtTheEndItem.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						enableAll();
						stopAtTheEndItem.setEnabled(false);
						mode = 1;
						playMode.setIcon(stopAtTheEnd);
						playMode.setToolTipText("Stop At The End");
					}
				});
				playModeMenu.add(stopAtTheEndItem);

				
				loopItem = new JMenuItem("Loop");
				loopItem.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						enableAll();
						loopItem.setEnabled(false);
						mode = 2;
						playMode.setIcon(loop);
						playMode.setToolTipText("Loop");
					}
				});
				playModeMenu.add(loopItem);
				
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
		
		JLabel bpmLabel = new JLabel("BPM")
		{
			public void paint(Graphics g)
			{
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getWidth(), getHeight());
				super.paint(g);
			}
		};
		bpmLabel.setHorizontalAlignment(JLabel.CENTER);
		bpmLabel.setToolTipText("Beats Per Minute");
		bpmLabel.setLocation(650, 10);
		bpmLabel.setSize(100, 17);
		bpmLabel.setForeground(Color.WHITE);
		super.add(bpmLabel);
		
		JLabel bpmSetter = new JLabel()
		{
			public void paint(Graphics g)
			{
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getWidth(), getHeight());
				
				setText(String.format("%.3f", resonance.musicFacade.getBeatsPerMinute()));
				
				super.paint(g);
			}
		};
		bpmSetter.setHorizontalAlignment(JLabel.CENTER);
		bpmSetter.setLocation(650, 27);
		bpmSetter.setSize(100, 23);
		bpmSetter.setForeground(Color.WHITE);
		super.add(bpmSetter);
		
		MouseAdapter bpmSetting = new MouseAdapter(){
			int initialY;
			double initialBPM;
			protected void updateBPM(MouseEvent me)
			{
				resonance.musicFacade.
					setBeatsPerMinute(Math.max(0.001f, (float) (initialBPM + (-1.0 * (me.getY() - initialY)) / 10)));
			}
			public void mouseDragged(MouseEvent me)
			{
				updateBPM(me);
			}
			public void mouseReleased(MouseEvent me)
			{
				updateBPM(me);
				bpmSetter.removeMouseMotionListener(this);
			}
			public void mousePressed(MouseEvent me)
			{
				initialY = me.getY();
				initialBPM = resonance.musicFacade.getBeatsPerMinute();
				bpmSetter.addMouseMotionListener(this);
			}
			
			public void mouseClicked(MouseEvent me)
			{
				if(me.getClickCount() == 2 && me.getButton() == MouseEvent.BUTTON1)
				{
					String input = String.format("%.3f", resonance.musicFacade.getBeatsPerMinute());
					while(true)
					{
						String errorMessage = "The beat(s) per minute you entered is not recognized!";
						input = JOptionPane.showInputDialog(bpmSetter, "", "Customize Beat(s) Per Minute", JOptionPane.QUESTION_MESSAGE);
						if(input == null) return;
						else try
						{
							float bpm = Float.parseFloat(input);
							if(bpm <= 0)
							{
								errorMessage = "The beat(s) per minute you entered should be greater than zero!";
								throw new RuntimeException();
							}
							resonance.musicFacade.setBeatsPerMinute(bpm);
							break;
						}
						catch(RuntimeException e)
						{
							JOptionPane.showConfirmDialog(bpmSetter, errorMessage
									, "Not recognized beat(s) per minute!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
						}
 
					}
				}
			}
		};
		bpmSetter.addMouseListener(bpmSetting);
		
		new Thread()
		{
			public void run()
			{
				while(true) try
				{
					if(mode == 1)
					{
						if(resonance.musicFacade.getBeatPosition() >= resonance.musicFacade.getLength())
						{
							resonance.stop();
							pause.setVisible(false);
							play.setVisible(true);
						}
					}
					else if(mode == 2)
					{
						if(resonance.musicFacade.getBeatPosition() > resonance.musicFacade.getLength())
							resonance.musicFacade.setBeatPosition(0);
					}
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
