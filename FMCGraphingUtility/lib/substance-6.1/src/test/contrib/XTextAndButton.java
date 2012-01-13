package test.contrib;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;

public class XTextAndButton {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				startGUI();
			}
		});
	}

	private static void startGUI() {
		setLaF();
		JPanel panel = new JPanel();
		panel.add(new JLabel("This is just a test of some text"));
		panel.add(new JButton("ButtonTest"));
		panel.add(new JButton("ButtonTest"));
		panel.add(new JButton("ButtonTest"));
		panel.add(new JButton("ButtonTest"));
		panel.add(new JButton("ButtonTest"));
		panel.add(new JButton("ButtonTest"));
		panel.add(new JButton("ButtonTest"));

		JFrame frame = new JFrame("TestFrame");
		frame.add(panel);

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(300, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private static void setLaF() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		try {
			UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}
	}
}
