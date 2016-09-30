package net.aegistudio.resonance.jui.setting;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.sound.sampled.AudioFormat;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import net.aegistudio.resonance.Resonance;
import net.aegistudio.resonance.device.MixerDevice;

public class MetasettingView extends JPanel {
	private static final long serialVersionUID = 1L;

	public JComboBox<MixerDevice> mixerDevices;
	public JComboBox<AudioFormat> mixerFormat;
	public JLabel mixerDetail;
	
	public JSlider bufferSize;
	public JLabel bufferSizeDetail;
	public JLabel bufferSizeWarning;
	
	public JSlider bufferAmount;
	public JLabel bufferAmountDetail;
	public JLabel bufferAmountWarning;
	
	public final MetasettingModel model;

	public static final Dimension LABEL_SIZE = new Dimension(120, 30);
	public static final Dimension BOX_SIZE = new Dimension(200, 30);
	
	public static final Dimension SLIDER_SIZE = new Dimension(155, 30);
	public static final Dimension SLIDER_TAG_SIZE = new Dimension(40, 30);
	
	public MetasettingView(Resonance resonance) {
		model = new MetasettingLogic(resonance);
		super.setLayout(new FlowLayout());
		
		// Mixer device
		JPanel mixerDeviceSection = new JPanel();
		mixerDeviceSection.setLayout(new BorderLayout());
		super.add(mixerDeviceSection);
		
		JLabel deviceLabel = new JLabel("Device");
		deviceLabel.setHorizontalAlignment(JLabel.CENTER);
		deviceLabel.setPreferredSize(LABEL_SIZE);
		mixerDeviceSection.add(deviceLabel, BorderLayout.WEST);
		
		mixerDevices = new JComboBox<MixerDevice>(
				model.getOutputDevices());
		mixerDevices.setPreferredSize(BOX_SIZE);
		mixerDevices.addItemListener(e -> 
			selectDevice((MixerDevice)e.getItem()));
		mixerDeviceSection.add(mixerDevices, BorderLayout.CENTER);
		
		// Mixer format
		JPanel mixerFormatSection = new JPanel();
		mixerFormatSection.setLayout(new BorderLayout());
		super.add(mixerFormatSection);
		
		JLabel formatLabel = new JLabel("Format");
		formatLabel.setHorizontalAlignment(JLabel.CENTER);
		formatLabel.setPreferredSize(LABEL_SIZE);
		mixerFormatSection.add(formatLabel, BorderLayout.WEST);
		
		mixerFormat = new JComboBox<AudioFormat>();
		mixerFormat.setPreferredSize(BOX_SIZE);
		mixerFormat.addItemListener(e -> 
			selectFormat((AudioFormat) e.getItem()));
		mixerFormatSection.add(mixerFormat, BorderLayout.CENTER);
		
		mixerDetail = new JLabel();
		mixerDetail.setHorizontalAlignment(JLabel.RIGHT);
		mixerFormatSection.add(mixerDetail, BorderLayout.SOUTH);
		
		// Output buffer
		JPanel bufferSizeSection = new JPanel();
		bufferSizeSection.setLayout(new BorderLayout());
		super.add(bufferSizeSection);
		
		JLabel bufferSizeLabel = new JLabel(
				"<html><center>Buffer Size</center><p color=\"gray\">(Samples / Frame)</p></html>");
		bufferSizeLabel.setHorizontalAlignment(JLabel.RIGHT);
		bufferSizeLabel.setPreferredSize(LABEL_SIZE);
		bufferSizeSection.add(bufferSizeLabel, BorderLayout.WEST);
		
		this.bufferSize = new JSlider();
		this.bufferSize.setMinimum(0); 
		this.bufferSize.setMaximum(15);
		this.bufferSize.setPreferredSize(SLIDER_SIZE);
		this.bufferSize.addChangeListener(e -> 
			selectBufferSize(bufferSize.getValue()));
		bufferSizeSection.add(bufferSize, BorderLayout.CENTER);
		
		bufferSizeDetail = new JLabel();
		bufferSizeDetail.setPreferredSize(SLIDER_TAG_SIZE);
		bufferSizeDetail.setHorizontalAlignment(JLabel.RIGHT);
		bufferSizeSection.add(bufferSizeDetail, BorderLayout.EAST);
		
		bufferSizeWarning = new JLabel();
		bufferSizeWarning.setPreferredSize(new Dimension(320, 30));
		bufferSizeWarning.setHorizontalAlignment(JLabel.RIGHT);
		super.add(bufferSizeWarning);
		
		// Prerender policy
		JPanel bufferAmountSection = new JPanel();
		bufferAmountSection.setLayout(new BorderLayout());
		super.add(bufferAmountSection);
		
		JLabel bufferAmountLabel = new JLabel("Buffer Amount");
		bufferAmountLabel.setHorizontalAlignment(JLabel.CENTER);
		bufferAmountLabel.setPreferredSize(LABEL_SIZE);
		bufferAmountSection.add(bufferAmountLabel, BorderLayout.WEST);
		
		this.bufferAmount = new JSlider();
		this.bufferAmount.setMinimum(0); 
		this.bufferAmount.setMaximum(8);
		this.bufferAmount.setPreferredSize(SLIDER_SIZE);
		this.bufferAmount.addChangeListener(e -> 
			selectBufferAmount(bufferAmount.getValue()));
		bufferAmountSection.add(bufferAmount, BorderLayout.CENTER);
		
		bufferAmountDetail = new JLabel();
		bufferAmountDetail.setPreferredSize(SLIDER_TAG_SIZE);
		bufferAmountDetail.setHorizontalAlignment(JLabel.RIGHT);
		bufferAmountSection.add(bufferAmountDetail, BorderLayout.EAST);
		
		bufferAmountWarning = new JLabel();
		bufferAmountWarning.setPreferredSize(new Dimension(320, 45));
		bufferAmountWarning.setHorizontalAlignment(JLabel.RIGHT);
		super.add(bufferAmountWarning);
		
		// Confirm and Reset
		JPanel confirmPanel = new JPanel();
		confirmPanel.setLayout(new FlowLayout());
		super.add(confirmPanel);
		
		JButton confirm = new JButton("Confirm");
		confirm.addActionListener(a -> model.applyOutputSetting(
				(MixerDevice) mixerDevices.getSelectedItem(), 
				(AudioFormat) mixerFormat.getSelectedItem(), 
				bufferAmount.getValue(), bufferSize.getValue()));
		super.add(confirm);
		
		JButton reset = new JButton("Reset");
		reset.addActionListener(a -> syncModel());
		super.add(reset);
		
		syncModel();
	}
	
	public void syncModel() {
		this.selectDevice(model.getOutputDeviceInUse());
		this.selectFormat(model.getOutputFormat());
		
		this.selectBufferSize(model.getBufferSize());
		this.selectBufferAmount(model.getBufferAmount());
	}
	
	public void selectDevice(MixerDevice device) {
		if(device == null) return;
		this.mixerDevices.setSelectedItem(device);
		AudioFormat[] formats = model.getSupportedFormat(device);
		this.mixerFormat.setModel(new DefaultComboBoxModel<>(formats));
		if(formats.length > 0) selectFormat(formats[0]);
		else selectFormat(null);
	}
	
	public void selectFormat(AudioFormat format) {
		if(format == null) mixerDetail.setText("");
		else {
			StringBuilder output = new StringBuilder("<html>");
			
			output.append("<p><b>Sample rate: </b>")
				.append(format.getSampleRate()).append(" Hz</p>");

			output.append("<p><b>Word type: </b>")
				.append(format.getSampleSizeInBits()).append("-bit ")
				.append(format.getEncoding()).append("</p>");
			
			String channels;
			if(format.getChannels() == 1) channels = "mono";
			else if(format.getChannels() == 2) channels = "stereo";
			else channels = format.getChannels() + " channels";
			
			output.append("<p><b>Channel: </b>").append(channels).append("</p>");
			
			output.append("</html>");
			
			for(int i = 0; i < mixerFormat.getItemCount() ; i ++) 
				if(mixerFormat.getItemAt(i).toString().equals(format.toString())) {
					mixerFormat.setSelectedIndex(i); break;
				}
			mixerDetail.setText(new String(output));
		}
	}
	
	public void selectBufferSize(int value) {
		this.bufferSize.setValue(value);
		bufferSizeDetail.setText(Integer.toString(1 << value));
		if(value < 4) {
			bufferSizeWarning.setText(
				  "<html><a style=\"text-align: center;\"><b>Warning:</b> Setting buffer size too small may "
				+ "be a challenge to your computer performance.</a></html>");
			bufferSizeWarning.setVisible(true);
		}
		else if(value > 13) {
			bufferSizeWarning.setText(
				  "<html><a style=\"text-align: center;\"><b>Warning:</b> Setting buffer size too large may "
				+ "cause big gap between tape position and output sound.</a></html>");
			bufferSizeWarning.setVisible(true);
		}
		else bufferSizeWarning.setVisible(false);
	}
	
	public void selectBufferAmount(int value) {
		this.bufferAmount.setValue(value);
		bufferAmountDetail.setText(Integer.toString(1 << value));
		if(value < 1) {
			bufferAmountWarning.setText(
				  "<html><a style=\"text-align: center;\"><b>Warning:</b> You're trying to use <b>hardware "
				+ "buffering</b>, what you should ensure your device supports.</a></html>");
			bufferAmountWarning.setVisible(true);
		}
		else if(value > 5) {
			bufferAmountWarning.setText(
				  "<html><a style=\"text-align: center;\"><b>Warning:</b> Setting buffer amount too large may "
				+ "cause big gap between tape position and output sound.</a></html>");
			bufferAmountWarning.setVisible(true);
		}
		else bufferAmountWarning.setVisible(false);
	}
}
