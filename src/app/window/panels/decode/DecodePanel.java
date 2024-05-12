package app.window.panels.decode;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import app.window.MainWindow;

public class DecodePanel extends JPanel{

	private final GridBagConstraints layout = new GridBagConstraints();
	private final NorthPanel optionPanel;
	private final ViewPanel viewPanel = new ViewPanel();
	private static JTextArea textArea = new JTextArea(10,40);
	
	public static String getTextAreaText() {
		return textArea.getText();
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}

	public NorthPanel getOptionPanel() {
		return optionPanel;
	}

	public ViewPanel getViewPanel() {
		return viewPanel;
	}
	
	private void initComponentes() {
		final JScrollPane scrollPane = new JScrollPane(textArea);
		final JLabel label1 = new JLabel("Contenuto QR Code");
		
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
        label1.setFont(MainWindow.FONT);
		textArea.setFont(MainWindow.FONT);
		textArea.setEditable(false);
		
		layout.gridx = 0;
		layout.gridy = 0;
		layout.anchor = GridBagConstraints.NORTHWEST;
		layout.insets = new Insets(7, 7, 7, 7);
		
		add(optionPanel, layout);
		
		layout.gridy = 1;
		add(viewPanel, layout);
		
		layout.gridy = 2;
		layout.gridx = 0;
		add(label1, layout)	;
		
		layout.gridy = 3;
		add(scrollPane, layout);
	}
	
	public DecodePanel(MainWindow win){
		super(new GridBagLayout());
		
		optionPanel = new NorthPanel(win);
		
		setBackground(Color.GRAY);
		
		initComponentes();
	}
}
