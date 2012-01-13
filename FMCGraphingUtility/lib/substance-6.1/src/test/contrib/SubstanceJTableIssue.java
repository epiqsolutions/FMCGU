package test.contrib;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

public class SubstanceJTableIssue {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager
							.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel");
				} catch (Exception e) {
					System.out
							.println("Substance Graphite failed to initialize");
				}
				JTable table = new JTable(3, 3);
				// table.setBackground(Color.RED); // <-- works
				table.setBackground(null); // <-- throws NPE

				JFrame main = new JFrame("SubstanceJTableIssue");
				main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				main.setLayout(new BorderLayout());
				main.add(new JScrollPane(table), BorderLayout.CENTER);
				main.setSize(new Dimension(640, 460));
				main.setVisible(true);

			}
		});
	}

}
