package app.window.panels.encode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.zxing.WriterException;

import app.models.QRCode;
import app.window.MainWindow;

public class EncodePanel extends JPanel{
	
	private final MainWindow _win;
	private final GridBagConstraints layout = new GridBagConstraints();
	
	private final JButton buttonChangeBgColorQRCode = new JButton("Colore di sfondo");
	private final JButton buttonChangeColorQRCode = new JButton("Colore QR Code");
	private final JButton buttonSaveQrCode = new JButton("Salva il QR Code");
	private final JButton buttonCreaQrCodeDaFile = new JButton("Crea QR Code dal contenuto di un file");

	private final JTextArea textArea = new JTextArea(20,50);
	
	private final JLabel labelViewQrCode = new JLabel();
	private final JLabel label2 = new JLabel("Contenuto");
	
	private Color colorBgQrCode = Color.WHITE;
	private Color colorQrCode = Color.BLACK;
	
    private ImageIcon createIconFromColor(Color color, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return new ImageIcon(image);
    }
	
	private void initComponents() {
		layout.anchor = GridBagConstraints.NORTHWEST;
		layout.insets = new Insets(7, 7, 7, 7);
		
		final JScrollPane scrollPane = new JScrollPane(textArea);
		
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		final JLabel label1 = new JLabel("Opzioni QR Code");
		
		label1.setFont(MainWindow.FONT);
		label2.setFont(MainWindow.FONT);
		
		buttonChangeBgColorQRCode.setFont(MainWindow.FONT);
		buttonChangeColorQRCode.setFont(MainWindow.FONT);
		buttonChangeColorQRCode.setIcon(createIconFromColor(colorQrCode, 65, 30));
		buttonChangeBgColorQRCode.setIcon(createIconFromColor(colorBgQrCode, 65, 30));
		buttonCreaQrCodeDaFile.setFont(MainWindow.FONT);
		
		buttonSaveQrCode.setFont(MainWindow.FONT);
		textArea.setFont(MainWindow.FONT);
		
		add(label1, layout);
		
		layout.gridy = 1;
		add(buttonChangeBgColorQRCode, layout);
		
		layout.gridx = 1;
		add(buttonChangeColorQRCode, layout);
		
		layout.gridx = 2;
		add(buttonSaveQrCode, layout);
	
		layout.gridx = 0;
		layout.gridy = 2;
		add(label2, layout);
	
		layout.gridx = 1;
		add(buttonCreaQrCodeDaFile, layout);
		
		layout.gridx = 0;
		layout.gridy = 3;
		add(scrollPane, layout);
		
		layout.gridx = 1;
		add(labelViewQrCode, layout);
	}
	
	private void initListeners() {
		buttonChangeColorQRCode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser color = new JColorChooser();
				Color c = color.showDialog(color, "Scegli il colore del QR Code", Color.BLACK);
				
				if (c != null) {
					colorQrCode = c;
					buttonChangeColorQRCode.setIcon(createIconFromColor(c, 65,30));
				}
			}
		});
		
		buttonChangeBgColorQRCode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser color = new JColorChooser();
				Color c = color.showDialog(color, "Scegli il colore di sfondo del QR Code", Color.WHITE);
				
				if (c != null) {
					colorBgQrCode = c;
					buttonChangeBgColorQRCode.setIcon(createIconFromColor(c, 65,30));
				}
			}
		});
		
		buttonSaveQrCode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (labelViewQrCode.getIcon() == null)
					return;
				
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setPreferredSize(new Dimension(600,500));
					
					chooser.showSaveDialog(null);
					
					File file = chooser.getSelectedFile();
					if (file != null){
						
						ImageIcon image = (ImageIcon)labelViewQrCode.getIcon();
		
				        BufferedImage bufferedImage = new BufferedImage(image.getIconWidth(),image.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
		
				        Graphics g = bufferedImage.createGraphics();
					    image.paintIcon(null, g, 0,0);
					    g.dispose();
						
					    ImageIO.write(bufferedImage, "png", file);
					    
						JOptionPane.showMessageDialog(null, "QR code salvato con successo");
					}
				}
				catch(Exception ex) {
					MainWindow.showErrorMessage(ex.getMessage());
				}
			}
		});
		
	     textArea.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				genQrCode(textArea.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				genQrCode(textArea.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				genQrCode(textArea.getText());
			}
		});
	     
	     buttonCreaQrCodeDaFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();

					chooser.addChoosableFileFilter(new FileNameExtensionFilter("File di testo","txt"));
					
					chooser.setPreferredSize(new Dimension(600,500));
					chooser.showSaveDialog(null);

					if (chooser.getSelectedFile() != null) {
						textArea.setText(Files.readString(Path.of(chooser.getSelectedFile().getAbsolutePath())));
						genQrCode(textArea.getText());
					}
				}
				catch(Exception ex) {
					MainWindow.showErrorMessage(ex.getMessage());
				}
			}
		});
	}
	
	private void genQrCode(String text) {
    	if (textArea.getText() == null || textArea.getText().isBlank())
    		return;
    	
    	try {
			QRCode qrCode = QRCode.encode(textArea.getText(), colorBgQrCode, colorQrCode);
			labelViewQrCode.setIcon(new ImageIcon(qrCode.getImage()));
    	}
    	catch(WriterException ex) {}
        catch(Exception ex) {
        	ex.printStackTrace();
        	MainWindow.showErrorMessage(ex.getMessage());
        	return;
        }
	}
	
	public EncodePanel(MainWindow win) {
		super(new GridBagLayout());
	
		if (win == null)
			throw new IllegalArgumentException("Finestra non valida");
		_win = win;
	
		setBackground(Color.GRAY);
		
		initComponents();
		initListeners();
	}
}
