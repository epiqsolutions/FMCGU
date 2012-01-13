package test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.plaf.basic.BasicStatusBarUI;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.MagellanSkin;
import org.pushingpixels.substance.api.skin.SkinChangeListener;

import test.check.SampleMenuFactory;

public class TestTextFields extends JFrame {
	public TestTextFields() {
		super("Text fields galore");

		this.setLayout(new BorderLayout());

		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(true);
		toolbar.add(new JLabel("In a toolbar"));
		toolbar.add(Box.createHorizontalStrut(8));
		toolbar.add(new JTextField("sample", 9));
		this.add(toolbar, BorderLayout.NORTH);

		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		left.setBorder(new EmptyBorder(0, 4, 0, 4));
		left.add(Box.createVerticalStrut(30));
		left.add(new JLabel("In a sidebar"));
		left.add(new JTextField("sample", 9));
		SubstanceLookAndFeel
				.setDecorationType(left, DecorationAreaType.GENERAL);
		this.add(left, BorderLayout.WEST);

		JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
		center.setBorder(new EmptyBorder(40, 0, 0, 0));
		center.add(new JLabel("General"));
		center.add(new JTextField("sample", 9));
		this.add(center, BorderLayout.CENTER);

		JXStatusBar statusBar = new JXStatusBar();
		statusBar.putClientProperty(BasicStatusBarUI.AUTO_ADD_SEPARATOR, false);
		statusBar.add(new JLabel("In a status bar"));
		statusBar.add(Box.createHorizontalStrut(8));
		statusBar.add(new JTextField("sample", 9));
		// statusBar.add(new JButton("sample"));
		this.add(statusBar, BorderLayout.SOUTH);

		this.setIconImage(SubstanceLogo
				.getLogoImage(SubstanceLookAndFeel.getCurrentSkin(
						this.getRootPane())
						.getColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE,
								ColorSchemeAssociationKind.FILL,
								ComponentState.ENABLED)));
		SubstanceLookAndFeel
				.registerSkinChangeListener(new SkinChangeListener() {
					@Override
					public void skinChanged() {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								setIconImage(SubstanceLogo
										.getLogoImage(SubstanceLookAndFeel
												.getCurrentSkin(getRootPane())
												.getColorScheme(
														DecorationAreaType.PRIMARY_TITLE_PANE,
														ColorSchemeAssociationKind.FILL,
														ComponentState.ENABLED)));
							}
						});
					}
				});

		JMenuBar jmb = new JMenuBar();
		jmb.add(SampleMenuFactory.getSkinMenu());
		jmb.add(SampleMenuFactory.getTransformMenu());
		this.setJMenuBar(jmb);

		this.setSize(450, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame.setDefaultLookAndFeelDecorated(true);
				SubstanceLookAndFeel.setSkin(new MagellanSkin());
				new TestTextFields().setVisible(true);
			}
		});
	}
}
