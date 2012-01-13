/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.contrib;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

/**
 * Test application to demonstrate a memory leak in Substance. The test works by
 * building a JFrame with a JTabbedPane, then continually adding and removing a
 * panel to the tabbed pane.
 * 
 * @author peter
 */
public class Main {

	private static final int DELAY = 100;

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				Main main = new Main();
				main.startGui();
			}
		});
	}

	private JTabbedPane tabbedPane;
	private int addCounter = 0;

	private void startGui() {

		activateSubstance();

		tabbedPane = new JTabbedPane();

		JFrame mainFrame = new JFrame("Testing");
		mainFrame.getContentPane().setLayout(new BorderLayout());
		mainFrame.getContentPane().add(tabbedPane);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		mainFrame.setBounds((screenSize.width - 800) / 2,
				(screenSize.height - 600) / 2, 800, 600);
		mainFrame.setVisible(true);

		Timer addTimer = new Timer(DELAY, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JPanel tab = new MyTabPanel();

				/*
				 * ==============================================================
				 * ======= Commenting this line out means the test runs for ever
				 * (Yes I tested it ;-)
				 * ==========================================
				 * ===========================
				 */
				tab.putClientProperty(SubstanceLookAndFeel.WINDOW_MODIFIED,
						true);

				tabbedPane.addTab("Tab " + addCounter, tab);

				addCounter++;
			}
		});
		addTimer.start();

		Timer subtractTimer = new Timer(DELAY, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (tabbedPane.getTabCount() > 5) {
					tabbedPane.remove(0);
				}
			}
		});
		subtractTimer.start();
	}

	/**
	 * A heavy JPanel which simulates using a lot of resources.
	 */
	static class MyTabPanel extends JPanel {

		private int[] junk;

		public MyTabPanel() {
			junk = new int[1000000]; // May have to fine tune this depending on
										// JVM args
		}

		@Override
		protected void finalize() throws Throwable {
			super.finalize();
			System.out.println("-------------- Finalize MyTabPanel");
		}

	}

	private void activateSubstance() {
		try {
			UIManager
					.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());

			UIManager.put(LafWidget.TEXT_EDIT_CONTEXT_MENU, Boolean.TRUE);
			UIManager.put(LafWidget.TEXT_SELECT_ON_FOCUS, Boolean.TRUE);

			UIManager
					.put(SubstanceLookAndFeel.SHOW_EXTRA_WIDGETS, Boolean.TRUE);
		} catch (Exception ex) {
			// oh well.
			ex.printStackTrace();
		}
	}
}
