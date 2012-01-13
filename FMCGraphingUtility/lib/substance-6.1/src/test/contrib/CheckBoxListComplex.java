package test.contrib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel;

/**
 * The selection model and the selection of the checkboxes are handled
 * separately
 */
public class CheckBoxListComplex<I> extends JList {
	private final CheckBoxListCellRenderer renderer = new CheckBoxListCellRenderer(
			false);
	private final Set<I> activedItems = new HashSet<I>();

	public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager
							.setLookAndFeel(new SubstanceModerateLookAndFeel());
					JFrame a = new JFrame();
					a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					CheckBoxListComplex<String> list = new CheckBoxListComplex<String>();
					DefaultListModel model = new DefaultListModel();
					model.addElement("string 1");
					model.addElement("string 2");
					model.addElement("string 3");
					model.addElement("string 4");
					model.addElement("string 5");
					model.addElement("string 6");
					model.addElement("string 7");
					model.addElement("string 8");
					model.addElement("string 9");
					model.addElement("string 10");
					model.addElement("string 11");
					model.addElement("string 12");
					list.setModel(model);
					a.add(new JScrollPane(list));
					a.setLocationRelativeTo(null);
					a.pack();
					a.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	public CheckBoxListComplex() {
		super.setCellRenderer(renderer);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Rectangle bounds = getCellBounds(0, 0);
				if (bounds == null)
					return;
				// clicked on the checkbox
				int x = e.getPoint().x;
				int checkboxsize = 25;
				if (x + checkboxsize < bounds.width)
					return;
				// choose line
				toggleActived(e.getPoint().y / bounds.height);
				repaint();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void toggleActived(Integer index) {
		if (index >= getModel().getSize())
			// clicked on empty space
			return;
		I item = (I) getModel().getElementAt(index);
		if (activedItems.contains(item))
			activedItems.remove(item);
		else
			activedItems.add(item);
	}

	@Override
	public void clearSelection() {
		super.clearSelection();
		activedItems.clear();
	}

	@Override
	public void setCellRenderer(ListCellRenderer cellRenderer) {
		if (renderer == null)
			// in JList initialization
			super.setCellRenderer(cellRenderer);
		else
			renderer.setCellRenderer(cellRenderer);
	}

	private class CheckBoxListCellRenderer extends JPanel implements
			ListCellRenderer {
		private ListCellRenderer textRenderer = new DefaultListCellRenderer();
		private final JCheckBox checkbox = new JCheckBox();

		public CheckBoxListCellRenderer(boolean checkBoxToTheLeft) {
			super(new BorderLayout());
			add(checkbox, checkBoxToTheLeft ? BorderLayout.WEST
					: BorderLayout.EAST);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			checkbox.setSelected(activedItems.contains(value));
			// as the renderer is added to a container, and the module is loaded
			// several times, it gets
			// removed from here, so we need to add it everytime (or create many
			// more renderers)
			add(textRenderer.getListCellRendererComponent(list, value, index,
					isSelected, cellHasFocus), BorderLayout.CENTER);
			for (Component comp : getComponents()) {
				comp.setForeground(activedItems.contains(value) ? Color.red
						: isSelected ? getSelectionForeground() : list
								.getForeground());
				comp.setBackground(isSelected ? getSelectionBackground() : list
						.getBackground());
				comp.setEnabled(list.isEnabled());
				comp.setFont(list.getFont());
			}
			return this;
		}

		public void setCellRenderer(ListCellRenderer cellRenderer) {
			if (cellRenderer == null)
				throw new IllegalArgumentException();
			textRenderer = cellRenderer;
		}
	}
}