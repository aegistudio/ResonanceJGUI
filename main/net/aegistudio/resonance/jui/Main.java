package net.aegistudio.resonance.jui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;

import net.aegistudio.resonance.Resonance;
import net.aegistudio.resonance.jui.arranger.Arranger;
import net.aegistudio.resonance.jui.arranger.ArrangerModel;
import net.aegistudio.resonance.jui.resource.ResourceManager;
import net.aegistudio.resonance.jui.resource.ResourceModel;

@SuppressWarnings("serial")
public class Main extends JFrame
{
	public final Resonance resonance;

	public final JDesktopPane desktopPane;
	
	public final JButton showArrangerButton;
	public final Arranger arranger;
	
	public final ResourceManager resourceManager;
	
	public final JButton showMixerButton;
	
	public final JButton showResourceManagerButton;
	
	public static final Color mimicBackground = new Color(0, 95, 120);
	
	public Main(Resonance resonance, ArrangerModel arrangerModel, ResourceModel resourceModel) throws Exception
	{
		super();
		this.resonance = resonance;
		super.setTitle("Resonance Java Gui");
		super.setSize(1280, 768);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);	// Should add confirm message if closing!
		super.setResizable(false);
		
		super.setLayout(null);
		
		desktopPane = new JDesktopPane();
		desktopPane.setLocation(0, 0);
		desktopPane.setSize(super.getWidth(), super.getHeight() - desktopPane.getHeight());
		super.add(desktopPane);
		
		Point mainLocation = new Point();
		mainLocation.x = 50;
		mainLocation.y = 100;
		this.setLocation(mainLocation);
		
		this.arranger = new Arranger(arrangerModel);
		this.arranger.setLocation(mainLocation.x + 5, mainLocation.y + 50);
		this.arranger.setVisible(true);
		
		this.resourceManager = new ResourceManager(resourceModel);
		this.resourceManager.setLocation(mainLocation.x + 5 + getWidth() - this.resourceManager.getWidth(), mainLocation.y + 50);
		this.resourceManager.setVisible(true);
		
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
	}
}
