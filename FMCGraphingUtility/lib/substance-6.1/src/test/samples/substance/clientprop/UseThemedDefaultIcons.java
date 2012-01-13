/*
 * Copyright (c) 2005-2010 Substance Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of Substance Kirill Grouchnikov nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package test.samples.substance.clientprop;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.GraphiteSkin;

/**
 * Test application that shows the use of the
 * {@link SubstanceLookAndFeel#USE_THEMED_DEFAULT_ICONS} client property.
 * 
 * @author Kirill Grouchnikov
 * @see SubstanceLookAndFeel#USE_THEMED_DEFAULT_ICONS
 */
public class UseThemedDefaultIcons extends JFrame {
	/**
	 * Creates the main frame for <code>this</code> sample.
	 */
	public UseThemedDefaultIcons() {
		super("Use themed default icons");

		this.setLayout(new BorderLayout());

		final JPanel panel = new JPanel(new FlowLayout());
		panel
				.add(new JButton(
						"cut",
						new ImageIcon(
								UseThemedDefaultIcons.class
										.getResource("/org/pushingpixels/lafwidget/text/edit-cut.png"))));
		panel
				.add(new JButton(
						"copy",
						new ImageIcon(
								UseThemedDefaultIcons.class
										.getResource("/org/pushingpixels/lafwidget/text/edit-copy.png"))));
		panel
				.add(new JButton(
						"paste",
						new ImageIcon(
								UseThemedDefaultIcons.class
										.getResource("/org/pushingpixels/lafwidget/text/edit-paste.png"))));
		this.add(panel, BorderLayout.CENTER);

		JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		final JCheckBox useThemedIcons = new JCheckBox("use themed icons");
		useThemedIcons.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// based on the checkbox selection status, set the
						// property
						UIManager.put(
								SubstanceLookAndFeel.USE_THEMED_DEFAULT_ICONS,
								useThemedIcons.isSelected() ? Boolean.TRUE
										: null);
						UseThemedDefaultIcons.this.repaint();
					}
				});
			}
		});
		controls.add(useThemedIcons);
		this.add(controls, BorderLayout.SOUTH);

		this.setSize(400, 200);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * The main method for <code>this</code> sample. The arguments are ignored.
	 * 
	 * @param args
	 *            Ignored.
	 */
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SubstanceLookAndFeel.setSkin(new GraphiteSkin());
				new UseThemedDefaultIcons().setVisible(true);
			}
		});
	}
}
