package test.contrib;

import java.awt.EventQueue;

import javax.swing.*;

import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel;

public class Snippet {
	public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager
							.setLookAndFeel(new SubstanceModerateLookAndFeel());
					AnimationConfigurationManager instance = AnimationConfigurationManager
							.getInstance();
					instance.setTimelineDuration(0);
				} catch (UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}
				JFrame f = new JFrame();
				JButton b = new JButton("Button");
				f.add(b);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setLocationRelativeTo(null);
				f.pack();
				f.setVisible(true);
			}
		});
	}
}