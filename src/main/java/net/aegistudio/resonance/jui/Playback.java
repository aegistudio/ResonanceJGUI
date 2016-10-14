package net.aegistudio.resonance.jui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import net.aegistudio.resonance.Resonance;
import net.aegistudio.resonance.jui.history.Action;
import net.aegistudio.resonance.jui.measure.MeasureRuler;
import net.aegistudio.resonance.jui.setting.Theme;
import net.aegistudio.resonance.music.LengthObject;

@SuppressWarnings("serial")
public class Playback extends Subwindow {
	Resonance resonance;
	JButton play, pause, stop;
	JButton playMode;
	
	ImageIcon endless;
	ImageIcon stopAtTheEnd;
	ImageIcon loop;
	
	ImageIcon endlessMin;
	ImageIcon stopAtTheEndMin;
	ImageIcon loopMin;
	
	Runnable mode = () -> {};
	
	public Playback(Theme theme, Resonance resonance, MeasureRuler ruler)
	{
		super();
		super.setSize(755, 80);
		super.setResizable(false);
		super.setLayout(null);
		super.setTitle("Playback");
		
		this.resonance = resonance;
		endless = theme.makeIcon("endless.png");
		stopAtTheEnd = theme.makeIcon("stop_at_the_end.png");
		loop = theme.makeIcon("loop.png");
		
		endlessMin = new ImageIcon(endless.getImage().getScaledInstance(20, 20, Image.SCALE_REPLICATE));
		stopAtTheEndMin = new ImageIcon(stopAtTheEnd.getImage().getScaledInstance(20, 20, Image.SCALE_REPLICATE));
		loopMin = new ImageIcon(loop.getImage().getScaledInstance(20, 20, Image.SCALE_REPLICATE));
		
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
				super.addActionListener(new ActionListener() {
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
				super.addActionListener(new ActionListener() {
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
				super.addActionListener(new ActionListener() {
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
			protected void enableAll() {
				endlessItem.setEnabled(true);
				stopAtTheEndItem.setEnabled(true);
				loopItem.setEnabled(true);
			}
			
			{
				setToolTipText("Endless");
				setIcon(endless);
				JMenuItem playModeItem = new JMenuItem("Play Mode");
				playModeItem.setForeground(Color.GREEN.darker());
				playModeItem.setEnabled(false);
				playModeMenu.add(playModeItem);
				playModeMenu.addSeparator();
				
				endlessItem = new JMenuItem("Endless");
				endlessItem.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						enableAll();
						endlessItem.setEnabled(false);
						mode = () -> {};
						playMode.setIcon(endless);
						playMode.setToolTipText("Endless");
					}
				});
				endlessItem.setIcon(endlessMin);
				endlessItem.setEnabled(false);
				playModeMenu.add(endlessItem);
				
				stopAtTheEndItem = new JMenuItem("Stop At The End");
				stopAtTheEndItem.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						enableAll();
						stopAtTheEndItem.setEnabled(false);
						mode = () -> {
							if(resonance.musicFacade.getBeatPosition() >= ((LengthObject)resonance.musicFacade).getLength())
							{
								resonance.stop();
								pause.setVisible(false);
								play.setVisible(true);
							}
						};
						playMode.setIcon(stopAtTheEnd);
						playMode.setToolTipText("Stop At The End");
					}
				});
				stopAtTheEndItem.setIcon(stopAtTheEndMin);
				playModeMenu.add(stopAtTheEndItem);

				loopItem = new JMenuItem("Loop");
				loopItem.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						enableAll();
						loopItem.setEnabled(false);
						mode = () -> {
							if(resonance.musicFacade.getBeatPosition() > ((LengthObject)resonance.musicFacade).getLength())
								resonance.musicFacade.setBeatPosition(0);
						};;
						playMode.setIcon(loop);
						playMode.setToolTipText("Loop");
					}
				});
				loopItem.setIcon(loopMin);
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
			public void paint(Graphics g) {
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
		
		Component progressBar = new JLabel() {
			public void paint(Graphics g) {
				g.setColor(Color.BLACK);
				g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
				g.drawLine(0, getHeight() / 2 + 1, getWidth(), getHeight() / 2 + 1);
				
				double length = Math.max(((LengthObject)resonance.musicFacade).getLength(), 1.0);
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
						double position = (1.0 * me.getX()) / getWidth() * ((LengthObject)resonance.musicFacade).getLength();
						position = Math.min(Math.max(position, 0), ((LengthObject)resonance.musicFacade).getLength());
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
		
		JLabel bpmSetter = new JLabel() {
			public void paint(Graphics g) {
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
			float initialBPM;
			protected void updateBPM(MouseEvent me) {
				resonance.musicFacade.setBeatsPerMinute(
						Math.max(0.001f, (float) (initialBPM + (-1.0 * (me.getY() - initialY)) / 10)));
			}
			
			public void mouseDragged(MouseEvent me) {
				updateBPM(me);
				repaint();
			}
			
			public void mouseReleased(MouseEvent me) {
				updateBPM(me);
				setBeatsPerMinute(initialBPM, resonance.musicFacade.getBeatsPerMinute());
				bpmSetter.removeMouseMotionListener(this);
			}
			
			public void mousePressed(MouseEvent me) {
				initialY = me.getY();
				initialBPM = resonance.musicFacade.getBeatsPerMinute();
				bpmSetter.addMouseMotionListener(this);
			}
			
			public void mouseClicked(MouseEvent me) {
				if(me.getClickCount() == 2 && me.getButton() == MouseEvent.BUTTON1) {
					initialBPM = resonance.musicFacade.getBeatsPerMinute();
					String input = String.format("%.3f", initialBPM);
					while(true)	{
						String errorMessage = "The beat(s) per minute you entered is not recognized!";
						input = JOptionPane.showInputDialog(bpmSetter, "", "Customize Beat(s) Per Minute", JOptionPane.QUESTION_MESSAGE);
						
						if(input == null) return;
						else try {
							float bpm = Float.parseFloat(input);
							if(bpm <= 0) {
								errorMessage = "The beat(s) per minute you entered should be greater than zero!";
								throw new RuntimeException();
							}
							setBeatsPerMinute(initialBPM, bpm);
							break;
						}
						catch(RuntimeException e) {
							JOptionPane.showConfirmDialog(bpmSetter, errorMessage
									, "Not recognized beat(s) per minute!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
						}
 
					}
				}
			}
		};
		bpmSetter.addMouseListener(bpmSetting);
	}
	
	public void resonanceTick() {
		mode.run();
		repaint();
	}
	
	public void setBeatsPerMinute(float initialBpm, float lastBpm) {
		Main.getHistory().push(new Action() {
			
			public String toString() {
				return "Update BPM";
			}

			@Override
			public void redo() {
				resonance.musicFacade.setBeatsPerMinute(lastBpm);
				repaint();
			}

			@Override
			public void undo() {
				resonance.musicFacade.setBeatsPerMinute(initialBpm);
				repaint();
			}
		});
	}
}
