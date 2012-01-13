/*
 * Copyright (c) 2005-2008 Substance Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of Substance Kirill Grouchnikov nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package test.check;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;

import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.RepeatBehavior;

import test.Check;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Test application panel for testing {@link JTable} component.
 * 
 * @author Kirill Grouchnikov
 */
public class TablePanel extends ControllablePanel implements Deferrable {
	private boolean isInitialized;

	@Override
	public boolean isInitialized() {
		return this.isInitialized;
	}

	/**
	 * The table.
	 */
	private JTable table;

	/**
	 * Custom renderer for columns that contain {@link Color} data.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private static class MyColorTableRenderer extends
			SubstanceDefaultTableCellRenderer {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent
		 * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
		 */
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Color color = (Color) value;
			this.setForeground(color);
			this.setBackground(new Color(255 - color.getRed(), 255 - color
					.getGreen(), 255 - color.getBlue(), color.getAlpha()));
			this.setText("row " + row);
			return this;
		}
	}

	/**
	 * Custom renderer for the columns that contain {@link Float} data.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private static class MyFloatTableRenderer extends
			SubstanceDefaultTableCellRenderer {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent
		 * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
		 */
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			int c = (10 * row) % 255;
			Color color = new Color(c, c, c);
			this.setForeground(new Color(255 - c, 0, 0));
			this.setBackground(color);
			if (value != null) {
				this.setText(value.toString());
			} else {
				this.setText("");
			}
			return this;
		}
	}

	/**
	 * Custom table model.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private static class MyTableModel extends AbstractTableModel {
		/**
		 * The current row count.
		 */
		private int rows;

		/**
		 * The column count.
		 */
		private int cols = 10;

		/**
		 * The table data.
		 */
		private Object[][] data;

		/**
		 * The table column classes.
		 */
		private Class<?>[] columns = new Class<?>[] { String.class,
				JComboBox.class, Boolean.class, Byte.class, Float.class,
				Double.class, String.class, Date.class, Color.class,
				ImageIcon.class };

		/**
		 * Creates the custom table model.
		 * 
		 * @param rows
		 *            Initial number of rows.
		 */
		public MyTableModel(int rows) {
			this.rows = rows;
			this.data = new Object[rows][this.cols];
			Icon[] icons = new Icon[] { Check.getIcon("flag_brazil"),
					Check.getIcon("flag_bulgaria"),
					Check.getIcon("flag_canada"), Check.getIcon("flag_china"),
					Check.getIcon("flag_denmark"),
					Check.getIcon("flag_finland"),
					Check.getIcon("flag_france"),
					Check.getIcon("flag_germany"),
					Check.getIcon("flag_greece"),
					Check.getIcon("flag_hungary"),
					Check.getIcon("flag_israel"), Check.getIcon("flag_italy"),
					Check.getIcon("flag_japan"), Check.getIcon("flag_mexico"),
					Check.getIcon("flag_netherlands"),
					Check.getIcon("flag_norway") };
			for (int i = 0; i < rows; i++) {
				this.data[i][0] = "cell " + i + ":" + 0;
				this.data[i][1] = "predef";
				this.data[i][2] = new Boolean(i % 2 == 0);
				this.data[i][3] = new Byte((byte) i);
				this.data[i][4] = new Float(i);
				this.data[i][5] = new Double(i);
				this.data[i][6] = "cell " + i + ":" + 6;

				Calendar cal = Calendar.getInstance();
				cal.set(2000 + i, 1 + i, 1 + i);
				this.data[i][7] = cal.getTime();

				int comp = i * 20;
				int red = (comp / 3) % 255;
				int green = (comp / 2) % 255;
				int blue = comp % 255;
				this.data[i][8] = new Color(red, green, blue);

				this.data[i][9] = icons[i % icons.length];
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return this.columns[columnIndex];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return this.cols;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			return this.rows;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int col) {
			return this.data[row][col];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
		@Override
		public String getColumnName(int column) {
			return this.getColumnClass(column).getSimpleName();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (rowIndex % 2 == 0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
		 * int, int)
		 */
		@Override
		public void setValueAt(Object value, int row, int col) {
			this.data[row][col] = value;
			this.fireTableCellUpdated(row, col);
		}
	}

	/**
	 * Creates a test panel with table.
	 */
	public TablePanel() {
	}

	public synchronized void initialize() {
		this.table = new JTable(new MyTableModel(20));

		this.table.setTransferHandler(new TransferHandler() {
			@Override
			public boolean canImport(TransferSupport support) {
				return true;
			}

			@Override
			public boolean canImport(JComponent comp,
					DataFlavor[] transferFlavors) {
				return true;
			}

			@Override
			public boolean importData(JComponent comp, Transferable t) {
				return false;
			}

			@Override
			public boolean importData(TransferSupport support) {
				return false;
			}
		});
		this.table.setDropMode(DropMode.ON);

		this.table.setDefaultRenderer(Color.class, new MyColorTableRenderer());
		this.table.setDefaultRenderer(Float.class, new MyFloatTableRenderer());
		final JScrollPane tableScrollpane = new JScrollPane(this.table);
		tableScrollpane.setName("Main table in table panel");

		JComboBox combo = new JComboBox(new Object[] { "aa", "bb", "cc" });
		combo.setBorder(null);
		this.table.getColumnModel().getColumn(1).setCellEditor(
				new DefaultCellEditor(combo));

		this.table
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		// We allow row selection as the default
		this.table.setCellSelectionEnabled(true);
		this.table.setRowSelectionAllowed(true);
		this.table.setColumnSelectionAllowed(false);

		this.table.setShowGrid(false);
		this.table.setDragEnabled(false);
		this.table
				.setTableHeader(new JTableHeader(this.table.getColumnModel()));

		this.setLayout(new BorderLayout());
		this.add(tableScrollpane, BorderLayout.CENTER);

		JLabel instructional = new JLabel("Every odd row is editable");
		instructional.putClientProperty(
				SubstanceLookAndFeel.COLORIZATION_FACTOR, Double.valueOf(1.0));
		this.add(instructional, BorderLayout.NORTH);

		// create a looping animation to change the label foreground
		// from black to blue and back to draw some attention.
		Timeline instructionalTimeline = new Timeline(instructional);
		instructionalTimeline.addPropertyToInterpolate("foreground",
				Color.black, Color.blue);
		instructionalTimeline.setDuration(1000);
		instructionalTimeline.playLoop(RepeatBehavior.REVERSE);

		FormLayout lm = new FormLayout("right:pref, 4dlu, fill:pref:grow", "");
		DefaultFormBuilder builder = new DefaultFormBuilder(lm,
				new ScrollablePanel());

		builder.appendSeparator("Table settings");
		final JCheckBox isEnabled = new JCheckBox("is enabled");
		isEnabled.setSelected(table.isEnabled());
		isEnabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.setEnabled(isEnabled.isSelected());
				// the table header is not repainted on disabling / enabling :(
				table.getTableHeader().repaint();
			}
		});
		builder.append("Enabled", isEnabled);

		JButton changeFirstColumn = new JButton("change 1st column");
		changeFirstColumn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						for (int i = 0; i < table.getModel().getRowCount(); i++) {
							table.getModel().setValueAt(
									Thread.currentThread().getName() + " " + i,
									i, 0);
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							}
						}
					}
				}).start();
			}
		});
		builder.append("Change values", changeFirstColumn);

		final JSlider rowCountSlider = new JSlider(20, 1000, this.table
				.getModel().getRowCount());
		rowCountSlider.setPaintLabels(false);
		rowCountSlider.setPaintTicks(false);
		rowCountSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (rowCountSlider.getValueIsAdjusting())
					return;
				TablePanel.this.table.setModel(new MyTableModel(rowCountSlider
						.getValue()));
			}
		});
		builder.append("Row count", rowCountSlider);

		final JCheckBox areRowsSelectable = new JCheckBox("Rows selectable");
		areRowsSelectable.setSelected(this.table.getRowSelectionAllowed());
		areRowsSelectable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TablePanel.this.table.setRowSelectionAllowed(areRowsSelectable
						.isSelected());
			}
		});
		builder.append("Selectable", areRowsSelectable);

		final JCheckBox areColsSelectable = new JCheckBox("Cols selectable");
		areColsSelectable.setSelected(this.table.getColumnSelectionAllowed());
		areColsSelectable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TablePanel.this.table
						.setColumnSelectionAllowed(areColsSelectable
								.isSelected());
			}
		});
		builder.append("", areColsSelectable);

		final JCheckBox isSorted = new JCheckBox("Sorted");
		final JCheckBox toHideOddModelRows = new JCheckBox("Hide odd rows");
		isSorted.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isSorted.isSelected()) {
					table.setRowSorter(new TableRowSorter(table.getModel()));
					toHideOddModelRows.setEnabled(true);
				} else {
					table.setRowSorter(null);
					toHideOddModelRows.setSelected(false);
					toHideOddModelRows.setEnabled(false);
				}
				table.repaint();
				table.getTableHeader().repaint();
			}
		});
		builder.append("Sorted", isSorted);

		toHideOddModelRows.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TableRowSorter rowSorter = (TableRowSorter) table
						.getRowSorter();
				if (toHideOddModelRows.isSelected()) {
					rowSorter.setRowFilter(new RowFilter() {
						@Override
						public boolean include(Entry entry) {
							return (((Integer) entry.getIdentifier()) % 2 == 0);
						}
					});
				} else {
					rowSorter.setRowFilter(new RowFilter() {
						@Override
						public boolean include(Entry entry) {
							return true;
						}
					});
				}
			}
		});
		builder.append("", toHideOddModelRows);

		final JCheckBox customBackgroundCb = new JCheckBox(
				"Has pink background");
		customBackgroundCb.addActionListener(new ActionListener() {
			Color oldBackColor;

			public void actionPerformed(ActionEvent e) {
				if (customBackgroundCb.isSelected()) {
					oldBackColor = table.getBackground();
					table.setBackground(new Color(255, 128, 128));
				} else {
					table.setBackground(oldBackColor);
				}
			}
		});
		builder.append("Background", customBackgroundCb);

		final JCheckBox watermarkBleed = new JCheckBox("Watermark bleed");
		watermarkBleed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TablePanel.this.table.putClientProperty(
						SubstanceLookAndFeel.WATERMARK_VISIBLE, Boolean
								.valueOf(watermarkBleed.isSelected()));
				tableScrollpane.putClientProperty(
						SubstanceLookAndFeel.WATERMARK_VISIBLE, Boolean
								.valueOf(watermarkBleed.isSelected()));
				tableScrollpane.repaint();
			}
		});
		builder.append("Watermark", watermarkBleed);

		final JCheckBox linesVertical = new JCheckBox("Vertical visible");
		linesVertical.setSelected(this.table.getShowVerticalLines());
		linesVertical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TablePanel.this.table.setShowVerticalLines(linesVertical
						.isSelected());
			}
		});
		builder.append("Lines", linesVertical);
		final JCheckBox linesHorizontal = new JCheckBox("Horizontal visible");
		linesHorizontal.setSelected(this.table.getShowHorizontalLines());
		linesHorizontal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TablePanel.this.table.setShowHorizontalLines(linesHorizontal
						.isSelected());
			}
		});
		builder.append("", linesHorizontal);

		final JComboBox resizeModeCombo = new FlexiComboBox<Integer>(
				JTable.AUTO_RESIZE_OFF, JTable.AUTO_RESIZE_NEXT_COLUMN,
				JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS,
				JTable.AUTO_RESIZE_LAST_COLUMN, JTable.AUTO_RESIZE_ALL_COLUMNS) {
			@Override
			public String getCaption(Integer item) {
				int iv = item;
				switch (iv) {
				case JTable.AUTO_RESIZE_OFF:
					return "off";
				case JTable.AUTO_RESIZE_NEXT_COLUMN:
					return "next";
				case JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS:
					return "subsequent";
				case JTable.AUTO_RESIZE_LAST_COLUMN:
					return "last";
				case JTable.AUTO_RESIZE_ALL_COLUMNS:
					return "all";
				}
				return null;
			}
		};
		resizeModeCombo.setSelectedItem(this.table.getAutoResizeMode());

		resizeModeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selected = (Integer) resizeModeCombo.getSelectedItem();
				TablePanel.this.table.setAutoResizeMode(selected);
			}
		});

		builder.append("Resize mode", resizeModeCombo);

		final JCheckBox hasRollovers = new JCheckBox("Has rollover effect");
		hasRollovers.setSelected(AnimationConfigurationManager.getInstance()
				.isAnimationAllowed(AnimationFacet.ROLLOVER, this.table));
		hasRollovers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (hasRollovers.isSelected()) {
					AnimationConfigurationManager.getInstance()
							.allowAnimations(AnimationFacet.ROLLOVER,
									TablePanel.this.table);
				} else {
					AnimationConfigurationManager.getInstance()
							.disallowAnimations(AnimationFacet.ROLLOVER,
									TablePanel.this.table);
				}
			}
		});
		builder.append("Rollovers", hasRollovers);

		final JCheckBox hasSelectionAnimations = new JCheckBox(
				"Has selection effect");
		hasSelectionAnimations.setSelected(AnimationConfigurationManager
				.getInstance().isAnimationAllowed(AnimationFacet.SELECTION,
						this.table));
		hasSelectionAnimations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (hasSelectionAnimations.isSelected()) {
					AnimationConfigurationManager.getInstance()
							.allowAnimations(AnimationFacet.SELECTION,
									TablePanel.this.table);
				} else {
					AnimationConfigurationManager.getInstance()
							.disallowAnimations(AnimationFacet.SELECTION,
									TablePanel.this.table);
				}
			}
		});
		builder.append("Selections", hasSelectionAnimations);

		builder.appendSeparator("Font settings");
		JButton tahoma12 = new JButton("Tahoma 12");
		tahoma12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.setFont(new Font("Tahoma", Font.PLAIN, 12));
			}
		});
		builder.append("Set font", tahoma12);

		JButton tahoma13 = new JButton("Tahoma 13");
		tahoma13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.setFont(new Font("Tahoma", Font.PLAIN, 13));
			}
		});
		builder.append("Set font", tahoma13);

		this.controlPanel = builder.getPanel();
		this.isInitialized = true;
	}
}