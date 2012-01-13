package test.issues;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.BusinessSkin;

public class Issue484 extends JFrame {

	public Issue484() {
		super("Substance JComboBox Test");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		SubstanceLookAndFeel.setSkin(new BusinessSkin());

		JComboBox comboBox = new JComboBox();
		comboBox.addItem("A");
		comboBox.addItem("B");

		comboBox.setRenderer(new MyRenderer());
		add(comboBox);

		pack();
	}

	// Simple renderer to demonstrate the problem
	private class MyRenderer extends JTextField implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setText(value.toString());
			return this;
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Issue484 scb = new Issue484();
				scb.setVisible(true);
			}
		});
	}
}