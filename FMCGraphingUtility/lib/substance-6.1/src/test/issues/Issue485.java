package test.issues;

import java.awt.EventQueue;

import javax.swing.*;

import org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel;

public class Issue485 {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager
							.setLookAndFeel(new SubstanceBusinessLookAndFeel());
				} catch (UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				JFrame f = new JFrame();
				JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT,
						JTabbedPane.SCROLL_TAB_LAYOUT);
				tabbedPane.add("Test1", new JButton("foo1"));
				tabbedPane.add("Test2", new JButton("foo2"));
				f.getContentPane().add(tabbedPane);
				f.setSize(400, 400);
				f.setVisible(true);
			}
		});
	}
}
