package app;

import javax.swing.SwingUtilities;

import org.opencv.core.Core;

import app.window.MainWindow;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(MainWindow::new);
	}
}
