package test.contrib;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class SubstanceJInternalFrameIconTest extends JFrame {

	private static final long serialVersionUID = 7243754225818902724L;

	JDesktopPane desktop = null;
	JRadioButton defaultLAF = null;
	JRadioButton substanceLAF = null;
	JRadioButton metalLAF = null;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Set the look and feel to the system default.
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				new SubstanceJInternalFrameIconTest();
			}
		});
	}

	private SubstanceJInternalFrameIconTest() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		desktop = new JDesktopPane();
		desktop.setLayout(new BorderLayout());
		getContentPane().add(desktop);

		// Show an internal frame.
		JInternalFrame aFrame = new JInternalFrame("JInternalFrame", false,
				true, false);
		aFrame.setTitle("Internal Frame Icon Test");
		aFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		ImageIcon anImage = null;
		URL imgURL = getClass().getResource("./alert.png");
		anImage = new ImageIcon(imgURL);
		aFrame.setFrameIcon(anImage);
		aFrame.setResizable(true);
		aFrame.setPreferredSize(new Dimension(200, 200));
		aFrame.pack();
		aFrame.setVisible(true);
		desktop.add(aFrame);

		// Add the buttons at the bottom of the frame.
		defaultLAF = new JRadioButton("Default LAF");
		defaultLAF.setSelected(true);
		substanceLAF = new JRadioButton("Substance LAF");
		metalLAF = new JRadioButton("Metal LAF");

		ButtonGroup buttons = new ButtonGroup();
		buttons.add(defaultLAF);
		buttons.add(substanceLAF);
		buttons.add(metalLAF);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(defaultLAF);
		buttonPanel.add(substanceLAF);
		buttonPanel.add(metalLAF);

		desktop.add(buttonPanel, BorderLayout.SOUTH);

		// Add the button listener.
		final JFrame thisFrame = this;
		class ButtonListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (arg0.getSource() == defaultLAF) {
						UIManager.setLookAndFeel(UIManager
								.getSystemLookAndFeelClassName());
					} else if (arg0.getSource() == substanceLAF) {
						UIManager
								.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel");
					} else {
						UIManager.setLookAndFeel(new MetalLookAndFeel());
					}
					SwingUtilities.updateComponentTreeUI(thisFrame);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		}
		ButtonListener bl = new ButtonListener();
		defaultLAF.addActionListener(bl);
		substanceLAF.addActionListener(bl);
		metalLAF.addActionListener(bl);

		// Now show the frame.
		setPreferredSize(new Dimension(400, 400));
		setResizable(false);
		pack();
		setVisible(true);
	}
}