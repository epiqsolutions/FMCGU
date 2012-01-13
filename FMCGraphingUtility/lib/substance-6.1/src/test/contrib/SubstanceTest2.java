package test.contrib;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.*;

import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

public class SubstanceTest2 {
	private static JFrame frame;

	private static JPanel getTestPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		SubstanceLookAndFeel.setDecorationType(panel,
				DecorationAreaType.GENERAL);

		final JPanel panel2 = new JPanel(
				new FlowLayout(FlowLayout.CENTER, 5, 2));

		JFormattedTextField tf = new JFormattedTextField(new BigDecimal(123456)) {
			public void setBounds(int x, int y, int width, int height) {
				super.setBounds(x, y, width, height);
				System.out.println(x + ":" + y + ":" + width + ":" + height);
			}
		};
		tf.setFont(tf.getFont().deriveFont(Font.BOLD, 16));
		tf.setEditable(false);
		panel2.add(tf);

		JButton button = new JButton("Minimize and maximize");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				frame.setExtendedState(JFrame.ICONIFIED);
				frame.setExtendedState(JFrame.NORMAL);
				System.out.println(panel2.getPreferredSize());
			}
		});
		panel2.add(button);

		panel.add(panel2, BorderLayout.SOUTH);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				System.out.println(panel2.getPreferredSize());
			}
		});

		return panel;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SubstanceLookAndFeel
						.setSkin("org.pushingpixels.substance.api.skin.OfficeBlue2007Skin");

				frame = new JFrame();
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.setSize(300, 100);
				Dimension paneSize = frame.getSize();
				Dimension screenSize = frame.getToolkit().getScreenSize();
				frame.setLocation((screenSize.width - paneSize.width) / 2,
						(screenSize.height - paneSize.height) / 2);
				frame.setTitle("Test");
				frame.setVisible(true);

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						frame.add(getTestPanel());
						frame.validate();
					}
				});
			}
		});
	}
}