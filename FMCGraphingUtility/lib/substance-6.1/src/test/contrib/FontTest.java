package test.contrib;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;

public class FontTest {

	private static JPanel getTestPanel() {
		final JPanel panel = new JPanel(new FlowLayout());

		JButton button = new JButton("Add text field");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JTextField tf = new JTextField("Hello", 4);
				tf.setFont(tf.getFont().deriveFont(Font.BOLD, 16));
				panel.add(tf);
				panel.doLayout();
			}
		});
		panel.add(button);

		JButton button2 = new JButton("Do layout");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				panel.doLayout();
			}
		});
		panel.add(button2);

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

				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.add(getTestPanel());
				frame.pack();
				frame.setSize(400, 200);
				Dimension paneSize = frame.getSize();
				Dimension screenSize = frame.getToolkit().getScreenSize();
				frame.setLocation((screenSize.width - paneSize.width) / 2,
						(screenSize.height - paneSize.height) / 2);
				frame.setTitle("Test");
				frame.setVisible(true);
			}
		});
	}
}
