package net.aegistudio.resonance.jui;

import java.awt.Color;
import java.awt.Point;

import javax.sound.sampled.AudioSystem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.UIManager;

import net.aegistudio.resonance.Encoding;
import net.aegistudio.resonance.Environment;
import net.aegistudio.resonance.Resonance;
import net.aegistudio.resonance.device.MixerDevice;
import net.aegistudio.resonance.io.OutputController;
import net.aegistudio.resonance.io.OutputFacade;
import net.aegistudio.resonance.jui.arranger.Arranger;
import net.aegistudio.resonance.jui.arranger.ArrangerLogic;
import net.aegistudio.resonance.jui.arranger.ArrangerModel;
import net.aegistudio.resonance.jui.resource.ResourceLogic;
import net.aegistudio.resonance.jui.resource.ResourceManager;
import net.aegistudio.resonance.jui.resource.ResourceModel;
import net.aegistudio.resonance.music.MusicController;

@SuppressWarnings("serial")
public class Main extends JFrame
{
	public final Resonance resonance;

	public final JDesktopPane desktopPane;
	
	//public final JButton showArrangerButton;
	public final Arranger arranger;
	
	public final ResourceManager resourceManager;
	
	//public final JButton showMixerButton;
	
	//public final JButton showResourceManagerButton;
	
	public static final Color mimicBackground = new Color(0, 95, 120);
	
	public Main(Resonance resonance, ArrangerModel arrangerModel, ResourceModel resourceModel) throws Exception
	{
		super();
		this.resonance = resonance;
		super.setTitle("Resonance");
		super.setSize(1280, 768);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);	// Should add confirm message if closing!
		
		
		desktopPane = new JDesktopPane();
		super.add(desktopPane);
		
		Point mainLocation = new Point(200, 100);
		this.setLocation(mainLocation);
		
		this.arranger = new Arranger(arrangerModel);
		this.arranger.setLocation(mainLocation.x + 5, mainLocation.y + 50);
		this.arranger.setVisible(true);
		
		this.resourceManager = new ResourceManager(resourceModel);
		this.resourceManager.setLocation(mainLocation.x - 5 + getWidth() - this.resourceManager.getWidth(), mainLocation.y + 50);
		this.resourceManager.setVisible(true);
		
		Playback playback = new Playback(resonance, arranger.ruler);
		playback.setLocation(mainLocation.x + 5, mainLocation.y + this.arranger.getHeight() + 105);
		playback.setVisible(true);
		
		/*
		this.showArrangerButton = new SubwindowShowButton(arranger);
		this.showArrangerButton.setBounds(10, this.desktopPane.getHeight() - 110, 80,  80);
		this.showArrangerButton.setToolTipText("Arranger");
		this.showArrangerButton.setIcon(new ImageIcon(ImageIO.read(new File("res/arranger.png")).getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
		this.desktopPane.add(showArrangerButton);
		
		this.showMixerButton = new SubwindowShowButton(null);
		this.showMixerButton.setBounds(100, this.desktopPane.getHeight() - 110, 80, 80);
		this.showMixerButton.setToolTipText("Mixer");
		this.showMixerButton.setIcon(new ImageIcon(ImageIO.read(new File("res/mixer.png")).getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
		this.desktopPane.add(showMixerButton);
		
		this.showResourceManagerButton = new SubwindowShowButton(resourceManager);
		this.showResourceManagerButton.setBounds(190, this.desktopPane.getHeight() - 110, 80, 80);
		this.showResourceManagerButton.setToolTipText("Resource Manager");
		this.desktopPane.add(showResourceManagerButton);
		*/
	}
	
	public static void main(String[] arguments) throws Exception
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		
		OutputFacade outputLayer = new OutputController();
		MusicController musicController = new MusicController();
		Resonance resonance = new Resonance(outputLayer, musicController.dataflowController, musicController);
		
		ResourceModel resourceModel = new ResourceLogic(musicController.getScoreHolder());
		ArrangerModel arrangerModel = new ArrangerLogic(musicController, resourceModel);
		
		Main main = new Main(resonance, arrangerModel, resourceModel);
		main.setVisible(true);
		
		resonance.setEnvironment(new Environment(44100.0f, 2, new Encoding(Encoding.BITDEPTH_BIT32 | Encoding.WORDTYPE_INT | Encoding.ENDIAN_BIG),
				128, 16), new MixerDevice(AudioSystem.getMixerInfo()[0]));
	}
}
