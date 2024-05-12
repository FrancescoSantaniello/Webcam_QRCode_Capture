package app.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import com.google.zxing.NotFoundException;
import app.models.QRCode;
import app.window.panels.decode.DecodePanel;
import app.window.panels.decode.ViewPanel;
import app.window.panels.encode.EncodePanel;


public class MainWindow extends JFrame{

	public static final Font FONT = new Font("Arial",Font.PLAIN,20);
	
	private final JTabbedPane tabPane = new JTabbedPane();
	private final DecodePanel decodePanel = new DecodePanel(this);
	private final EncodePanel encodePanel = new EncodePanel(this);
	
	private Thread threadWebcamCapture = new Thread();
	
	private Color bgColorQrCode = Color.WHITE;
	private Color qrCodeColor = Color.BLACK;
	
	public Thread getThreadWebcamCapture() {
		return threadWebcamCapture;
	}
	
	public void setRunnableTherad(Runnable run) {
		threadWebcamCapture = null;
		threadWebcamCapture = new Thread(run);
	}
	
	public Color getBgColoQrCode() {
		return bgColorQrCode;
	}
	
	public Color getColorQrCode() {
		return qrCodeColor;
	}
	
	public void setBgColoQrCode(Color color) {
		if (color == null)
			return;
		
		bgColorQrCode = color;
	}
	
	public void setColorQrCode(Color color) {
		if (color == null)
			return;
		
		qrCodeColor = color;
	} 
	

	private boolean isValidURL(String text){
		try {
			new URL(text);
		}
		catch(Exception ex) {
			return false;
		}
		
		return true;
	}
	
	public void startWebcamCapture(int webcam) {
		final VideoCapture capture = new VideoCapture(webcam);
		final Mat frame = new Mat();
		
	    capture.set(Videoio.CAP_PROP_FRAME_WIDTH, getWidth() / 4);
	    capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, getHeight() / 4);
		
		while (capture.isOpened() && !threadWebcamCapture.isInterrupted()) {            
            
			try {
           	
				try {
					capture.read(frame); 
				}
				catch(Exception ex) {
					showErrorMessage("Webcam non valida");
					return;
				}
				
    			final MatOfByte buf = new MatOfByte(); 
    			Imgcodecs.imencode(".png", frame, buf);
    			
                ImageIcon image = new ImageIcon(buf.toArray());   
                decodePanel.getViewPanel().getLabelViewWebcamCapture().setIcon(image); 
            	
            	QRCode qrCode = QRCode.decode(image, bgColorQrCode, qrCodeColor);
            	
            	if (qrCode != null){
            		if (qrCode.getImage() != null)
            			decodePanel.getViewPanel().getLabelViewQrCode().setIcon(new ImageIcon(qrCode.getImage()));
            		
            		ViewPanel.getLabel2().setVisible(true);
            		
            		decodePanel.getTextArea().setText((qrCode.getText() == null) ? "" : qrCode.getText());
            	
            		if (isValidURL(DecodePanel.getTextAreaText())) {
            			int chooice = JOptionPane.showConfirmDialog(null, "Questo QR Code contiene un link\nVuoi andare visitare il sito in questione?", getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            			
            			if (chooice == 0) 
            				redirect(DecodePanel.getTextAreaText());
            		}
            		
            	}
            }
            catch(NotFoundException ex) {}
            catch(Exception ex) {
            	showErrorMessage(ex.getMessage());
            	return;
            }
            
        }
		
	}
	
	private void redirect(String url) throws IOException {
		ProcessBuilder builder = new ProcessBuilder("cmd ","/c start " + url);
		builder.start();
	}
	
	public static void showErrorMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Webcam QR Code Capture", JOptionPane.ERROR_MESSAGE);
	}
	
	private void initComponents() {	
		tabPane.setFont(FONT);
		
		tabPane.add(decodePanel,"Decodifica");
		tabPane.add(encodePanel, "Codifica");
		
		add(tabPane);
	}
	
	public MainWindow() {
		super("Webcam QR Code Capture");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
        setSize((int)Math.round(screenSize.width / 2), (int)Math.round(screenSize.height / 1.1));
        
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		initComponents();
		
		setVisible(true);
	}
}
