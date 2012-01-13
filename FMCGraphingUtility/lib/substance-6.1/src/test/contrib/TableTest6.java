package test.contrib;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;

public class TableTest6 {

	private static JPanel getTestPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.5;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;

		setTable(100, panel, constraints);

		constraints.gridx = 1;
		setTable(1000, panel, constraints);

		return panel;
	}

	private static void setTable(int rowCount, JPanel panel,
			GridBagConstraints constraints) {
		JTable table = new JTable();
		DefaultTableModel tableModel = new DefaultTableModel(new String[] {
				"1", "2", "3" }, rowCount) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}

			@Override
			public Object getValueAt(int row, int column) {
				return row + ":" + column;
			}

			@Override
			public Class<?> getColumnClass(int column) {
				if (getRowCount() > 0) {
					Object value = getValueAt(0, column);
					if (value != null)
						return value.getClass();
				}
				return Object.class;
			}
		};
		table.setModel(tableModel);
		table.setAutoCreateRowSorter(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JScrollPane tableScrollPane = new JScrollPane(table);
		panel.add(tableScrollPane, constraints);
		tableScrollPane.setViewportView(table);
		tableScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SubstanceLookAndFeel
						.setSkin("org.pushingpixels.substance.api.skin.OfficeBlue2007Skin");

				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.add(getTestPanel());
				frame.pack();
				frame.setSize(800, 600);
				Dimension paneSize = frame.getSize();
				Dimension screenSize = frame.getToolkit().getScreenSize();
				frame.setLocation((screenSize.width - paneSize.width) / 2,
						(screenSize.height - paneSize.height) / 2);
				frame.setTitle("Table test 6.0");
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});
	}
}
