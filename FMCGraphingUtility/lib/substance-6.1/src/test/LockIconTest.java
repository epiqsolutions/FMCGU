package test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.pushingpixels.substance.api.colorscheme.AquaColorScheme;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;

public class LockIconTest extends JFrame {
	class LockIconPanel extends JPanel {
		List<Icon> icons = new ArrayList<Icon>();

		public LockIconPanel() {
			for (int i = 11; i < 30; i++) {
				JLabel label = new JLabel();
				label.setFont(new Font("Lucida Grande", Font.PLAIN, i));
				icons.add(SubstanceImageCreator.getSmallLockIcon(
						new AquaColorScheme(), label));
			}
			JLabel label = new JLabel();
			label.setFont(new Font("Lucida Grande", Font.PLAIN, 72));
			icons.add(SubstanceImageCreator.getSmallLockIcon(
					new AquaColorScheme(), label));
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			int x = 1;
			for (Icon icon : this.icons) {
				int w = icon.getIconWidth();
				int h = icon.getIconHeight();
				g.setColor(new Color(255, 128, 128));
				g.fillRect(x, 2, w, h);
				icon.paintIcon(this, g, x, 2);
				x += w + 3;
			}
		}
	}

	public LockIconTest() {
		this.add(new LockIconPanel(), BorderLayout.CENTER);

		this.setSize(500, 200);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new LockIconTest().setVisible(true);
			}
		});
	}

}
