package test.contrib;

import java.awt.Window;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.pushingpixels.substance.api.skin.SubstanceDustCoffeeLookAndFeel;

public class NonOpaqueFrame {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager
							.setLookAndFeel(new SubstanceDustCoffeeLookAndFeel());
				} catch (final UnsupportedLookAndFeelException e) {
				}
				JFrame f = new JFrame();
				f.setUndecorated(true);

				try {
					Class<?> c = Class.forName("com.sun.awt.AWTUtilities");
					Method m = c.getMethod("setWindowOpaque", Window.class,
							boolean.class);
					m.invoke(null, f, false);
				} catch (Exception e) {
					e.printStackTrace();
				}

				f.setSize(300, 200);
				f.getContentPane().add(new JLabel("some label"));

				f.setVisible(true);
			}
		});

	}
}
