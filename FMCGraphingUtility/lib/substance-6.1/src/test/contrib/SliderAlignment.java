package test.contrib;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.DustSkin;

public class SliderAlignment extends JFrame {

	public SliderAlignment() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel buttons = new JPanel();
		buttons.add(new JButton(new AbstractAction("Metal") {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeLaF(e.getActionCommand());
			}
		}));
		buttons.add(new JButton(new AbstractAction("Motif") {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeLaF(e.getActionCommand());
			}
		}));
		buttons.add(new JButton(new AbstractAction("Nimbus") {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeLaF(e.getActionCommand());
			}
		}));
		buttons.add(new JButton(new AbstractAction("Substance") {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeLaF(e.getActionCommand());
			}
		}));

		JSlider slider = new JSlider(JSlider.VERTICAL);
		JPanel p = new JPanel();
		p.add(slider);
		getContentPane().add(p, BorderLayout.CENTER);
		getContentPane().add(buttons, BorderLayout.SOUTH);

		JComponent glass = new JComponent() {

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.RED);
				g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
			}

		};
		setGlassPane(glass);
		glass.setVisible(true);

		pack();
		setLocationRelativeTo(null);
	}

	public void changeLaF(final String laf) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					if (laf.equals("Metal")) {
						UIManager
								.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
					} else if (laf.equals("Motif")) {
						UIManager
								.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
					} else if (laf.equals("Windows")) {
						UIManager
								.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					} else if (laf.equals("Nimbus")) {
						UIManager
								.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					} else if (laf.equals("Substance")) {
						UIManager
								.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceDustLookAndFeel");
					}
					SwingUtilities.updateComponentTreeUI(SliderAlignment.this
							.getRootPane());
				} catch (Exception e) {
				}
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SubstanceLookAndFeel.setSkin(new DustSkin());
				new SliderAlignment().setVisible(true);
			}
		});
	}

}
