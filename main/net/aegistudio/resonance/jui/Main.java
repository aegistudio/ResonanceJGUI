package net.aegistudio.resonance.jui;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
import net.aegistudio.resonance.jui.setting.DefaultTheme;
import net.aegistudio.resonance.jui.setting.Setting;
import net.aegistudio.resonance.jui.setting.Theme;
import net.aegistudio.resonance.music.MusicController;

@SuppressWarnings("serial")
public class Main extends JFrame
{
	public final Theme theme;
	
	public final Resonance resonance;

	public final JDesktopPane desktopPane;
	
	public final Arranger arranger;
	
	public final ResourceManager resourceManager;
	
	public final Playback playback;
	
	public final Setting setting;
	
	public static final Color mimicBackground = new Color(0, 95, 120);
	
	public ArrayList<Subwindow> subwindows = new ArrayList<Subwindow>();
	
	public Main(Theme theme, Resonance resonance, ArrangerModel arrangerModel, ResourceModel resourceModel) throws Exception {
		super();
		this.theme = theme;
		theme.preset();
		
		this.resonance = resonance;
		super.setTitle("Resonance");
		super.setSize(1280, 768);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);	// Should add confirm message if closing!
		
		desktopPane = new JDesktopPane();
		super.add(desktopPane);
		
		Point mainLocation = new Point(200, 100);
		this.setLocation(mainLocation);
		
		this.arranger = new Arranger(arrangerModel);
		//this.arranger.setLocation(mainLocation.x + 5, mainLocation.y + 50);
		this.arranger.setLocation(5, 50);
		this.arranger.setVisible(true);
		desktopPane.add(arranger);
		subwindows.add(arranger);
		
		this.resourceManager = new ResourceManager(resourceModel);
		//this.resourceManager.setLocation(mainLocation.x - 5 + getWidth() - this.resourceManager.getWidth(), mainLocation.y + 50);
		this.resourceManager.setLocation(getWidth() - this.resourceManager.getWidth() - 5, 50);
		this.resourceManager.setVisible(true);
		desktopPane.add(resourceManager);
		subwindows.add(resourceManager);
		
		this.playback = new Playback(theme, resonance, arranger.ruler);
		//this.playback.setLocation(mainLocation.x + 5, mainLocation.y + this.arranger.getHeight() + 105);
		this.playback.setLocation(5, this.arranger.getHeight() + 105);
		this.playback.setVisible(true);
		desktopPane.add(playback);
		subwindows.add(playback);
		
		this.setting = new Setting(resonance);
		this.setting.setVisible(false);
		this.setting.setLocation(5, 50);
		desktopPane.add(setting);
		subwindows.add(setting);
		
		this.setJMenuBar(new MenuBar(this));
	}
	
	static Main main;
	
	public void onMusicLayerTick() {
		subwindows.forEach(w -> w.resonanceTick());
	}
	
	public static MixerDevice[] devices;
	public static Map<MixerDevice, AudioFormat[]> formats = new HashMap<>();
	public static void scanSupportedFormat() {
		// Retrieve devices.
		ArrayList<MixerDevice> devices = new ArrayList<>();
		for(Mixer.Info info : AudioSystem.getMixerInfo())
			devices.add(new MixerDevice(info));
		Main.devices = devices.toArray(new MixerDevice[0]);
		
		// Retrieve format for first device.
		formats.put(Main.devices[0], Main.devices[0].getAvailableFormats());
		
		// Retrieve formats.
		devices.forEach(device -> {
			if(device == Main.devices[0]) return;
			new Thread(() -> formats.put(device, device.getAvailableFormats())).start();
		});
	}
	
	public static void main(String[] arguments) throws Exception {
		OutputFacade outputLayer = new OutputController();
		MusicController musicController = new MusicController() {
			public void tick() {
				super.tick();
				new Thread(() -> main.onMusicLayerTick()).start();
			}
		};
		
		Resonance resonance = new Resonance(outputLayer, musicController.dataflowController, musicController) {
			public void exceptionWhileRendering(RuntimeException re) {
				this.exceptionBus(re);
			}
			
			public void exceptionWhileTaping(RuntimeException re) {
				this.exceptionBus(re);
			}
			
			public void exceptionBus(RuntimeException re) {
				JOptionPane.showConfirmDialog(main.playback, 
						String.format("Error while playing, caused by %s\nPlease change your configuration and try again.", 
								re.getMessage() == null? re.getClass() : re.getMessage()), "Error while playing",
						JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			}
		};
		
		ResourceModel resourceModel = new ResourceLogic(musicController.getScoreHolder());
		ArrangerModel arrangerModel = new ArrangerLogic(musicController, resourceModel);
		
		scanSupportedFormat();
		
		MixerDevice mixerDevice = Main.devices[0];
		resonance.setEnvironment(new Environment(formats.get(Main.devices[0])[0], 128, 16), mixerDevice);
		
		main = new Main(new DefaultTheme(), resonance, arrangerModel, resourceModel);
		main.setVisible(true);
	}
}
