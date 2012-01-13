package test;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.MagellanSkin;

public class ColoredSpinner extends JFrame {

	public ColoredSpinner() {
		this.setLayout(new FlowLayout());
		JSpinner numberEnSpinner = new JSpinner(new SpinnerNumberModel(0, 0,
				100, 5));
		numberEnSpinner.setBackground(new Color(255, 128, 128));
		add(numberEnSpinner);

		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SubstanceLookAndFeel.setSkin(new MagellanSkin());
				new ColoredSpinner().setVisible(true);
			}
		});
	}

}
