package test.contrib;

import java.awt.FlowLayout;
import java.net.URL;

import javax.swing.*;

/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) Phorest, Inc. All rights reserved.
 *
 * http://www.phorest.com/
 *
 * Created: 20 Apr 2010 alan.oleary
 *
 * $Id:$
 */

/**
 * JCheckBoxIssue
 * 
 * @author alan.oleary
 * 
 */
@SuppressWarnings("serial")
public class JCheckBoxIssue extends JPanel {

	// ------------------------------------------------------------------------
	// Constructor(s)
	// ------------------------------------------------------------------------

	/**
	 * JCheckBoxIssue Constructor
	 * 
	 */
	public JCheckBoxIssue() {
		setLayout(new FlowLayout());
		JCheckBox checkBox = new JCheckBox();
		JLabel testLabel = new JLabel("Test");

		checkBox.setIcon(getImageIcon("unlock.png"));
		checkBox.setSelectedIcon(getImageIcon("lock.png"));
		add(testLabel);
		add(checkBox);
	}

	// ------------------------------------------------------------------------
	// API Implementation
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Actions
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Helper Methods
	// ------------------------------------------------------------------------

	/**
	 * getImageIcon
	 * 
	 * @param iconName
	 * @return
	 */
	private ImageIcon getImageIcon(final String iconName) {
		URL url = JCheckBoxIssue.class.getResource(iconName);
		return new ImageIcon(url);
	}

	// ------------------------------------------------------------------------
	// Inner Classes
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Entry Point(s)
	// ------------------------------------------------------------------------

	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				// /*
				try {
					UIManager
							.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				// */

				JFrame frame = new JFrame();

				JCheckBoxIssue issue = new JCheckBoxIssue();
				frame.setContentPane(issue);
				frame.setSize(300, 400);
				frame.setLocationRelativeTo(frame.getParent());
				frame.setVisible(true);
			}
		});
	}
}
