/*
 * Copyright 2005-2008 Kirill Grouchnikov, based on work by
 * Sun Microsystems, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package test.contrib;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel;

public class TestJPopupMenu extends JFrame {

	public TestJPopupMenu() {
		this.setLayout(new BorderLayout());

		JPopupMenu jPopupMenu1 = new JPopupMenu();
		JCheckBoxMenuItem jCheckBoxMenuItem1 = new JCheckBoxMenuItem();
		JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem();
		JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem();

		jCheckBoxMenuItem1.setSelected(true);
		jCheckBoxMenuItem1.setText("Item1");
		jCheckBoxMenuItem1.setName("jCheckBoxMenuItem1");
		jPopupMenu1.add(jCheckBoxMenuItem1);

		jCheckBoxMenuItem2.setSelected(true);
		jCheckBoxMenuItem2.setText("Item2");
		jCheckBoxMenuItem2.setName("jCheckBoxMenuItem2");
		jPopupMenu1.add(jCheckBoxMenuItem2);

		jCheckBoxMenuItem3.setSelected(true);
		jCheckBoxMenuItem3.setText("Item3");
		jCheckBoxMenuItem3.setName("jCheckBoxMenuItem3");
		jPopupMenu1.add(jCheckBoxMenuItem3);

		JPanel controls = new JPanel(new FlowLayout());
		final JButton systemLaf = new JButton("System LAF");
		final JButton metalLaf = new JButton("Metal LAF");
		metalLaf.setEnabled(false);

		systemLaf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						try {
							systemLaf.setEnabled(false);
							metalLaf.setEnabled(true);
							UIManager.setLookAndFeel(UIManager
									.getSystemLookAndFeelClassName());
							SwingUtilities
									.updateComponentTreeUI(TestJPopupMenu.this);
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
				});
			}
		});

		metalLaf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						try {
							systemLaf.setEnabled(true);
							metalLaf.setEnabled(false);
							UIManager.setLookAndFeel(new MetalLookAndFeel());
							SwingUtilities
									.updateComponentTreeUI(TestJPopupMenu.this);
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
				});
			}
		});
		controls.add(systemLaf);
		controls.add(metalLaf);

		controls.setComponentPopupMenu(jPopupMenu1);

		this.add(controls, BorderLayout.CENTER);

		this.setSize(400, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws Exception {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(new SubstanceRavenLookAndFeel());
					new TestJPopupMenu().setVisible(true);
				} catch (UnsupportedLookAndFeelException ex) {
					Logger.getLogger(TestJPopupMenu.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		});
	}
}
