package test.contrib;

import java.awt.*;

import javax.swing.*;

import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.SubstanceConstants.TabContentPaneBorderKind;
import org.pushingpixels.substance.api.colorscheme.BaseColorScheme;
import org.pushingpixels.substance.api.skin.GeminiSkin;
import org.pushingpixels.substance.internal.ui.SubstanceTabbedPaneUI;

public class TabColor extends JFrame {
	private static final DecorationAreaType DECO_BG_WHITE = new DecorationAreaType(
			"WHITE_BG");

	public TabColor() {
		super("Selected tab");

		JTabbedPane jtp = new JTabbedPane();
		jtp.setUI(new SubstanceTabbedPaneUI() {
			@Override
			protected Insets getContentBorderInsets(int tabPlacement) {
				return new Insets(1, 1, 1, 1);
			}
		});
		jtp.putClientProperty(
				SubstanceLookAndFeel.TABBED_PANE_CONTENT_BORDER_KIND,
				TabContentPaneBorderKind.SINGLE_FULL);

		JPanel firstPanel = new JPanel();
		JPanel secondPanel = new JPanel();
		JPanel thirdPanel = new JPanel();
		SubstanceLookAndFeel.setDecorationType(firstPanel, DECO_BG_WHITE);
		jtp.addTab("tab0", firstPanel);
		jtp.addTab("tab1", secondPanel);
		jtp.addTab("tab2", thirdPanel);

		SubstanceLookAndFeel.setDecorationType((JComponent) getContentPane(),
				DECO_BG_WHITE);
		SubstanceLookAndFeel.setDecorationType(jtp, DecorationAreaType.NONE);
		this.getContentPane().add(jtp, BorderLayout.CENTER);
		this.setSize(300, 200);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Is used for GENERAL decoration areas. With substance 6.0 this can be
	 * removed cause of build in decoration areas, with the availability of self
	 * defining.
	 */
	private static class WhiteColorScheme extends BaseColorScheme {
		public WhiteColorScheme() {
			super("WhiteColorScheme", false);
		}

		@Override
		public Color getDarkColor() {
			return Color.WHITE;
		}

		@Override
		public Color getExtraLightColor() {
			return Color.WHITE;
		}

		@Override
		public Color getForegroundColor() {
			return Color.WHITE;
		}

		@Override
		public Color getLightColor() {
			return Color.WHITE;
		}

		@Override
		public Color getMidColor() {
			return Color.WHITE;
		}

		@Override
		public Color getUltraDarkColor() {
			return Color.WHITE;
		}

		@Override
		public Color getUltraLightColor() {
			return Color.WHITE;
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SubstanceLookAndFeel.setSkin(new GeminiSkin());
				SubstanceSkin skin = SubstanceLookAndFeel.getCurrentSkin();
				skin.registerAsDecorationArea(new WhiteColorScheme(),
						DECO_BG_WHITE);
				new TabColor().setVisible(true);
			}
		});
	}

}