package net.aegistudio.jui.test.arranger;

import java.awt.Color;

import javax.swing.JFrame;

import net.aegistudio.resonance.KeywordArray;
import net.aegistudio.resonance.channel.Clip;
import net.aegistudio.resonance.jui.arranger.ClipComponent;
import net.aegistudio.resonance.measure.MeasureRuler;
import net.aegistudio.resonance.plugin.Event;
import net.aegistudio.resonance.serial.Structure;

public class ClipComponentTest {
	public static void main(String[] arguments) throws InterruptedException
	{
		ClipComponent clipComponent = new ClipComponent(new ArrangerModelDecoy(), null, new KeywordArray.DefaultKeywordEntry<Double, Clip>(0.0, new Clip(){

			@Override
			public void load(Structure input) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void save(Structure output) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public double getLength() {
				return 1;
			}

			@Override
			public Event[] getEvents(double begin, double ends, int samplesPerFrame) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Event[] offload(int samplesPerFrame) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public double getOffset() {
				// TODO Auto-generated method stub
				return 0;
			}
		}), new MeasureRuler());
		
		clipComponent.setBackground(Color.GREEN.darker());
		clipComponent.setForeground(Color.YELLOW);
		
		JFrame jframe = new JFrame();
		jframe.setSize(600, 480);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jframe.setLayout(null);
		clipComponent.setSize(200, 100);
		clipComponent.setPreferredSize(clipComponent.getSize());
		clipComponent.setLocation(200, 100);
		jframe.add(clipComponent);
		
		jframe.setVisible(true);
	}
}
