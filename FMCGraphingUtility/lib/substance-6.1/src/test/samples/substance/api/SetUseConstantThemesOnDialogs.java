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
package test.samples.substance.api;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.NebulaSkin;

/**
 * Test application that shows the use of the
 * {@link SubstanceLookAndFeel#setToUseConstantThemesOnDialogs(boolean)} API.
 * 
 * @author Kirill Grouchnikov
 * @see SubstanceLookAndFeel#setToUseConstantThemesOnDialogs(boolean)
 */
public class SetUseConstantThemesOnDialogs extends JFrame {
	/**
	 * Creates the main frame for <code>this</code> sample.
	 */
	public SetUseConstantThemesOnDialogs() {
		super("Use constant themes on dialogs");

		this.setLayout(new BorderLayout());

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		String packageName = SetUseConstantThemesOnDialogs.class.getPackage()
				.getName();

		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton bopi = new JButton("Info", new ImageIcon(cl
				.getResource(packageName.replace('.', '/')
						+ "/dialog-information.png")));
		bopi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						SetUseConstantThemesOnDialogs.this,
						"Sample info message", "Sample title",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		buttonPanel.add(bopi);

		JButton bope = new JButton("Show", new ImageIcon(cl
				.getResource(packageName.replace('.', '/')
						+ "/dialog-error.png")));
		bope.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						SetUseConstantThemesOnDialogs.this,
						"Sample error message", "Sample title",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		buttonPanel.add(bope);

		JButton bopw = new JButton("Show", new ImageIcon(cl
				.getResource(packageName.replace('.', '/')
						+ "/dialog-warning.png")));
		bopw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						SetUseConstantThemesOnDialogs.this,
						"Sample warning message", "Sample title",
						JOptionPane.WARNING_MESSAGE);
			}
		});
		buttonPanel.add(bopw);

		JButton bopq = new JButton("Show", new ImageIcon(cl
				.getResource(packageName.replace('.', '/')
						+ "/help-browser.png")));
		bopq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						SetUseConstantThemesOnDialogs.this,
						"Sample question message", "Sample title",
						JOptionPane.QUESTION_MESSAGE);
			}
		});
		buttonPanel.add(bopq);

		this.add(buttonPanel, BorderLayout.CENTER);

		JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		final JCheckBox useConstantThemesOnDialogs = new JCheckBox(
				"Use constant themes on dialogs");
		useConstantThemesOnDialogs.setSelected(SubstanceLookAndFeel
				.isToUseConstantThemesOnDialogs());
		useConstantThemesOnDialogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						SubstanceLookAndFeel
								.setToUseConstantThemesOnDialogs(useConstantThemesOnDialogs
										.isSelected());
					}
				});
			}
		});
		controls.add(useConstantThemesOnDialogs);
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
		JDialog.setDefaultLookAndFeelDecorated(true);
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SubstanceLookAndFeel.setSkin(new NebulaSkin());
				new SetUseConstantThemesOnDialogs().setVisible(true);
			}
		});
	}
}
