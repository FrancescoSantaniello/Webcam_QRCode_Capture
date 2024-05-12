package app.models;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCode {
	private String _text;
	private BufferedImage _image;
	
	private Color _bgColor = Color.WHITE;
	private Color _qrColor = Color.BLACK;
	
	
	public Color getBgColor() {
		return _bgColor;
	}

	public void setBgColor(Color bgColor) {
		if (bgColor == null)
			return;
		
		_bgColor = bgColor;
	}

	public Color getQrColor() {
		return _qrColor;
	}

	public void setQrColor(Color qrColor) {
		if (qrColor == null)
			return;
		
		_qrColor = qrColor;
	}

	public void setText(String text) {
		_text = text;
	}
	
	public void setImage(BufferedImage image) {
		if (image == null)
			throw new IllegalArgumentException("QRCode non valido");
		_image = image;
	}
	
	public void setImage(Icon icon) {
		setImage(convertIconInBufferedImage(icon));
	}
	
	public BufferedImage getImage() {
		return _image;
	}
	
	public String getText() {
		return _text;
	}
	
	private static BufferedImage convertIconInBufferedImage(Icon icon) {
        BufferedImage buffer = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics g = buffer.getGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return buffer;
	}
	
	public static QRCode decode(Icon icon, Color bg, Color qrCode) throws NotFoundException, WriterException {
		if (icon == null || bg == null || qrCode == null) 
			return null;

        QRCode qrcode = new QRCode();
		
		HybridBinarizer binarizer = new HybridBinarizer(new BufferedImageLuminanceSource(convertIconInBufferedImage(icon)));
        Result result = new MultiFormatReader().decode(new BinaryBitmap(binarizer));
        
        qrcode.setText(result.getText());
        
        qrcode.setImage(encode(result.getText(), bg, qrCode).getImage());
        
        return qrcode;
	}
	
	public static QRCode encode(String text, Color bg, Color qrCode) throws WriterException {
        if (text == null || text.isBlank() || bg == null || qrCode == null)
        	return null;
		
		QRCode qrcode = new QRCode();
		
		qrcode.setBgColor(bg);
		qrcode.setQrColor(qrCode);
		
		QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 200, 200, hintMap);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? qrCode.getRGB() : bg.getRGB());
            }
        }

        qrcode.setImage(image);
        
        return qrcode;
	}
}






