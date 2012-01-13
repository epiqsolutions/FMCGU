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

import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.BusinessBlueSteelSkin;

/**
 * Test application that shows the use of the
 * {@link SubstanceLookAndFeel#getDecorationType(java.awt.Component)} API called
 * on different components.
 * 
 * @author Kirill Grouchnikov
 * @see SubstanceLookAndFeel#getDecorationType(java.awt.Component)
 */
public class GetDecorationType extends JFrame {
	/**
	 * Creates the main frame for <code>this</code> sample.
	 */
	public GetDecorationType() {
		super("Get decoration type");

		this.setLayout(new BorderLayout());

		final JTabbedPane tabs = new JTabbedPane();
		SubstanceLookAndFeel.setDecorationType(tabs, DecorationAreaType.HEADER);

		JPanel tab1 = new JPanel(new FlowLayout());
		tab1.add(new JTextField("sample"));
		final JComboBox combo = new JComboBox(new Object[] { "sample" });
		tab1.add(combo);
		SubstanceLookAndFeel
				.setDecorationType(tab1, DecorationAreaType.GENERAL);

		JPanel tab2 = new JPanel(new FlowLayout());
		tab2.add(new JTextField("sample2"));
		tab2.add(new JComboBox(new Object[] { "sample2" }));
		SubstanceLookAndFeel
				.setDecorationType(tab2, DecorationAreaType.GENERAL);

		tabs.addTab("tab1", tab1);
		tabs.addTab("tab2", tab2);

		this.add(tabs, BorderLayout.CENTER);

		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton getTypes = new JButton("Get types");
		getTypes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						DecorationAreaType tabsType = SubstanceLookAndFeel
								.getDecorationType(tabs);
						DecorationAreaType comboType = SubstanceLookAndFeel
								.getDecorationType(combo);
						JOptionPane.showMessageDialog(GetDecorationType.this,
								"Tabbed pane: " + tabsType.getDisplayName()
										+ "\n" + "Combo box: "
										+ comboType.getDisplayName());

					}
				});
			}
		});
		controlPanel.add(getTypes);
		this.add(controlPanel, BorderLayout.SOUTH);

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
				SubstanceLookAndFeel.setSkin(new BusinessBlueSteelSkin());
				UIManager.put("TabbedPane.contentOpaque", Boolean.TRUE);
				new GetDecorationType().setVisible(true);
			}
		});
	}
}
