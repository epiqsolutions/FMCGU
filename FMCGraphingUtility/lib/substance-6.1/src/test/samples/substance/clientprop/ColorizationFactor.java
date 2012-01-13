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

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.BusinessBlackSteelSkin;

/**
 * Test application that shows the use of the
 * {@link SubstanceLookAndFeel#COLORIZATION_FACTOR} client property.
 * 
 * @author Kirill Grouchnikov
 * @see SubstanceLookAndFeel#COLORIZATION_FACTOR
 */
public class ColorizationFactor extends JFrame {
	/**
	 * Creates the main frame for <code>this</code> sample.
	 */
	public ColorizationFactor() {
		super("Colorization factor");

		this.setLayout(new BorderLayout());

		final JPanel panel = new JPanel(new FlowLayout());
		JButton button = new JButton("sample");
		button.setBackground(Color.yellow);
		button.setForeground(Color.red);
		panel.add(button);
		JCheckBox checkbox = new JCheckBox("sample");
		checkbox.setSelected(true);
		checkbox.setBackground(Color.green.brighter());
		checkbox.setForeground(Color.blue.darker());
		panel.add(checkbox);
		JRadioButton radiobutton = new JRadioButton("sample");
		radiobutton.setSelected(true);
		radiobutton.setBackground(Color.yellow);
		radiobutton.setForeground(Color.green.darker());
		panel.add(radiobutton);

		this.add(panel, BorderLayout.CENTER);

		JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		final JSlider colorizationSlider = new JSlider(0, 100, 50);
		colorizationSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				double val = colorizationSlider.getValue() / 100.0;
				panel.putClientProperty(
						SubstanceLookAndFeel.COLORIZATION_FACTOR, new Double(
								val));
				panel.repaint();
			}
		});
		controls.add(colorizationSlider);

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
				SubstanceLookAndFeel.setSkin(new BusinessBlackSteelSkin());
				new ColorizationFactor().setVisible(true);
			}
		});
	}
}
