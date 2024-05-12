package app.window.panels.decode;

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
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.videoio.VideoCapture;
import app.window.MainWindow;

public class NorthPanel extends JPanel{
	private final GridBagConstraints layout = new GridBagConstraints();
	private final MainWindow _win;
	
	private JComboBox<Integer> comboBoxWebcam = new JComboBox<Integer>(getIndexWebcam());
	private final JButton buttonStartCapture = new JButton("Avvia cattura");
	
	private final JButton buttonSaveQrCodeImage = new JButton("Salva il QR Code");
	private final JButton buttonSaveTextQrCode = new JButton("Salva il contenuto");
	private final JButton buttonChangeQrCodeBgColor = new JButton("Colore di sfondo");
	private final JButton buttonChageQrCodeColor = new JButton("Colore QR Code");
	
	
	public JComboBox<Integer> getComboBoxWebcam() {
		return comboBoxWebcam;
	}

	private Vector<Integer> getIndexWebcam() {
		Vector<Integer> list = new Vector<>();
		
		int index = -1;
		
		try {
			while(new VideoCapture(++index).isOpened())
				list.add(index + 1);
		}
		catch (Exception e) {}
		
		return list;
	}
	
	
	private void initComponents() {
		final JLabel label1 = new JLabel("Seleziona Webcam");
		final JLabel label2 = new JLabel("Opzioni QR code");
		
		label2.setFont(MainWindow.FONT);
		label1.setFont(MainWindow.FONT);
		buttonStartCapture.setFont(MainWindow.FONT);
		comboBoxWebcam.setFont(MainWindow.FONT);
		buttonSaveQrCodeImage.setFont(MainWindow.FONT);
		buttonSaveTextQrCode.setFont(MainWindow.FONT);
		buttonChageQrCodeColor.setFont(MainWindow.FONT);
		buttonChangeQrCodeBgColor.setFont(MainWindow.FONT);
		
		buttonChageQrCodeColor.setIcon(createIconFromColor(Color.BLACK, 65, 30));
		buttonChangeQrCodeBgColor.setIcon(createIconFromColor(Color.WHITE, 65, 30));
		
		layout.insets = new Insets(7, 7, 7, 7);
		
		add(label1, layout);
		
		layout.gridy = 1;
		add(comboBoxWebcam, layout);
	
		layout.gridx = 1;
		add(buttonStartCapture,layout);
		
		layout.gridx = 2;
		add(buttonSaveTextQrCode,layout);
		
		layout.gridx = 3;
		add(buttonSaveQrCodeImage,layout);
		
		layout.gridx = 0;
		layout.gridy = 2;
		add(label2,layout);
		
		layout.gridy = 3;
		add(buttonChageQrCodeColor,layout);
		
		layout.gridx = 1;
		add(buttonChangeQrCodeBgColor, layout);
		
		if (comboBoxWebcam.getItemCount() <= 0) {
			MainWindow.showErrorMessage("Nessuna webcam collegata");
		}
		
	}
	
	private void initListeners() {
		buttonStartCapture.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (comboBoxWebcam.getSelectedIndex() < 0) {
					MainWindow.showErrorMessage("Scegliere una webcam");
					return;
				}
				

				_win.getThreadWebcamCapture().stop();
				_win.setRunnableTherad(() -> _win.startWebcamCapture(comboBoxWebcam.getSelectedIndex()));
				_win.getThreadWebcamCapture().start();
			}
		});
		
		buttonSaveQrCodeImage.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ViewPanel.getIconLabelViewQrCode() == null)
					return;
				
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.addChoosableFileFilter(new FileNameExtensionFilter("Immagine","png"));
					chooser.setPreferredSize(new Dimension(600,500));
					
					chooser.showSaveDialog(null);
					
					File file = chooser.getSelectedFile();
					if (file != null) {
						
						ImageIcon image = (ImageIcon)ViewPanel.getIconLabelViewQrCode();
		
				        BufferedImage bufferedImage = new BufferedImage(image.getIconWidth(),image.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
		
				        Graphics g = bufferedImage.createGraphics();
					    image.paintIcon(null, g, 0,0);
					    g.dispose();
						
					    ImageIO.write(bufferedImage, "png", file);
					    
						JOptionPane.showMessageDialog(null, "QR Code salvato con successo");
					}
				}
				catch(Exception ex) {
					MainWindow.showErrorMessage(ex.getMessage());
				}
			}
		});
		
		buttonSaveTextQrCode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (DecodePanel.getTextAreaText() == null || DecodePanel.getTextAreaText().isBlank())
					return;
				
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setPreferredSize(new Dimension(600,500));
							
					chooser.showSaveDialog(null);
					
					File file = chooser.getSelectedFile();
					if (file != null) {
						Files.writeString(Path.of(file.getAbsolutePath()), DecodePanel.getTextAreaText());
						
						JOptionPane.showMessageDialog(null, "Contenuto del QR Code salvato con successo");
					}
				}
				catch(Exception ex) {
					MainWindow.showErrorMessage(ex.getMessage());
				}
			}
		});
		
		buttonChageQrCodeColor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser color = new JColorChooser();
				Color c = color.showDialog(color, "Scegli il colore del QR Code", Color.BLACK);
				
				if (c != null) {
					_win.setColorQrCode(c);
					buttonChageQrCodeColor.setIcon(createIconFromColor(c, 65,30));
				}
			}
		});
		
		buttonChangeQrCodeBgColor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser color = new JColorChooser();
				Color c = color.showDialog(color, "Scegli il colore di sfondo del QR Code", Color.WHITE);
				
				if (c != null) {
					_win.setBgColoQrCode(c);
					buttonChangeQrCodeBgColor.setIcon(createIconFromColor(c, 65,30));
				}
			}
		});
	}
	
    private ImageIcon createIconFromColor(Color color, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return new ImageIcon(image);
    }
	
	public NorthPanel(MainWindow win) {
		super(new GridBagLayout());
		
		if (win == null)
			throw new IllegalArgumentException("Finestra non valida");
		_win = win;
		
		setBackground(Color.GRAY);

		initComponents();
		initListeners();
	}
}
