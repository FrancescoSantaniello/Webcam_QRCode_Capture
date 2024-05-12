package app.window.panels.decode;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.Main;
import app.window.MainWindow;

public class ViewPanel extends JPanel{

	private final JLabel labelViewWebcamCapture = new JLabel();
	private final static JLabel label2 = new JLabel("QR Code");
	private final static JLabel labelViewQrCode = new JLabel();
	private final GridBagConstraints layout = new GridBagConstraints();
	
	private Color bg = Color.WHITE;
	private Color qrColor = Color.BLACK;
	
	public static JLabel getLabel2() {
		return label2;
	}
	
	public static Icon getIconLabelViewQrCode() {
		return labelViewQrCode.getIcon();
	}
	
	public JLabel getLabelViewWebcamCapture() {
		return labelViewWebcamCapture;
	}

	public JLabel getLabelViewQrCode() {
		return labelViewQrCode;
	}

	public void setBcColorQrcode(Color color) {
		if (color == null)
			return;
		bg = color;
	}
	
	public void setQrCodeColor(Color color) {
		if (color == null)
			return;
		
		qrColor = color;
	}
	
	
	public ViewPanel() {
		super(new GridBagLayout());
		
		setBackground(Color.GRAY);
		
		final JLabel label1 = new JLabel("Webcam");
		
		label1.setFont(MainWindow.FONT);
		label2.setFont(MainWindow.FONT);
		label2.setVisible(false);
		
		add(label1,layout);
		
		layout.gridx = 1;
		add(label2,layout);
		
		layout.gridx = 0;
		layout.gridy = 1;
		layout.insets = new Insets(7, 7, 7, 7);
		add(labelViewWebcamCapture,layout);
	
		layout.gridx = 1;
		add(labelViewQrCode, layout);
	}
}
