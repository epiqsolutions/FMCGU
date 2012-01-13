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
package test.samples.lafwidget.clientprop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.tabbed.DefaultTabPreviewPainter;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.BusinessBlackSteelSkin;

/**
 * Test application that shows the use of the
 * {@link LafWidget#TABBED_PANE_PREVIEW_PAINTER} client property.
 * 
 * @author Kirill Grouchnikov
 * @see LafWidget#TABBED_PANE_PREVIEW_PAINTER
 */
public class TabbedPanePreviewPainter extends JFrame {
	/**
	 * Creates the main frame for <code>this</code> sample.
	 */
	public TabbedPanePreviewPainter() {
		super("Tabbed pane preview painter");

		this.setLayout(new BorderLayout());

		final JTabbedPane jtp = new JTabbedPane();
		String packageName = TabbedPanePreviewPainter.class.getPackage()
				.getName();
		jtp.addTab("First", new ImageIcon(TabbedPanePreviewPainter.class
				.getClassLoader().getResource(
						packageName.replace('.', '/') + "/flag_mexico.png")),
				new JPanel() {
					@Override
					protected void paintComponent(Graphics g) {
						g.setColor(new Color(255, 200, 200));
						g.fillRect(0, 0, getWidth(), getHeight());
					}
				});
		jtp.addTab("Second", new ImageIcon(TabbedPanePreviewPainter.class
				.getClassLoader().getResource(
						packageName.replace('.', '/') + "/flag_sweden.png")),
				new JPanel() {
					@Override
					protected void paintComponent(Graphics g) {
						g.setColor(new Color(200, 255, 200));
						g.fillRect(0, 0, getWidth(), getHeight());
					}
				});
		jtp.addTab("Third",
				new ImageIcon(TabbedPanePreviewPainter.class.getClassLoader()
						.getResource(
								packageName.replace('.', '/')
										+ "/flag_hong_kong.png")),
				new JPanel() {
					@Override
					protected void paintComponent(Graphics g) {
						g.setColor(new Color(200, 200, 255));
						g.fillRect(0, 0, getWidth(), getHeight());
					}
				});

		this.add(jtp, BorderLayout.CENTER);

		JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		final JCheckBox hasPreview = new JCheckBox("Has preview");
		hasPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jtp
						.putClientProperty(
								LafWidget.TABBED_PANE_PREVIEW_PAINTER,
								hasPreview.isSelected() ? new DefaultTabPreviewPainter()
										: null);
				jtp.revalidate();
				jtp.repaint();
			}
		});

		controls.add(hasPreview);
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
		JDialog.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SubstanceLookAndFeel.setSkin(new BusinessBlackSteelSkin());
				new TabbedPanePreviewPainter().setVisible(true);
			}
		});
	}
}
