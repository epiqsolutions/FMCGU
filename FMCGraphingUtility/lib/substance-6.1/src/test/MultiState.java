package test;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.LightAquaColorScheme;
import org.pushingpixels.substance.api.colorscheme.SunGlareColorScheme;
import org.pushingpixels.substance.api.colorscheme.SunsetColorScheme;
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.MatteDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.ClassicFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

public class MultiState extends JFrame {
	public MultiState() {
		JButton button = new JButton("sample");

		this.setLayout(new FlowLayout());

		button.getModel().setPressed(true);
		button.getModel().setArmed(true);

		this.add(button);
		this.setSize(300, 100);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static class MySkin extends SubstanceSkin {
		@Override
		public String getDisplayName() {
			return "My";
		}

		public MySkin() {
			SubstanceColorScheme active = new SunGlareColorScheme();
			SubstanceColorScheme enabled = new LightAquaColorScheme();
			SubstanceColorScheme pressed = new SunsetColorScheme();

			SubstanceColorSchemeBundle bundle = new SubstanceColorSchemeBundle(
					active, enabled, enabled);

			bundle.registerColorScheme(pressed,
					ComponentState.PRESSED_UNSELECTED);
			bundle.registerColorScheme(active.shade(0.3),
					ColorSchemeAssociationKind.BORDER,
					ComponentState.ROLLOVER_UNSELECTED);
			bundle.registerColorScheme(enabled.shade(0.3),
					ColorSchemeAssociationKind.BORDER, ComponentState.ENABLED);
			bundle.registerColorScheme(pressed.shade(0.3),
					ColorSchemeAssociationKind.BORDER,
					ComponentState.PRESSED_UNSELECTED);

			this.registerDecorationAreaSchemeBundle(bundle, enabled.tint(1.0),
					DecorationAreaType.NONE);

			this.fillPainter = new ClassicFillPainter();
			this.borderPainter = new ClassicBorderPainter();
			this.decorationPainter = new MatteDecorationPainter();
			this.buttonShaper = new ClassicButtonShaper();
			this.highlightPainter = new ClassicHighlightPainter();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SubstanceLookAndFeel.setSkin(new MySkin());
				new MultiState().setVisible(true);
			}
		});
	}

}
