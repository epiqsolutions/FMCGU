/*
 * Copyright (c) 2005-2010 Substance Kirill Grouchnikov. All Rights Reserved.
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
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.skin.GeminiSkin;
import org.pushingpixels.substance.api.skin.SkinChangeListener;

import test.Check;
import test.SubstanceLogo;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class SampleFrame extends JFrame {
	protected JButton prev;
	private JTabbedPane tabbed;
	private JTree tree;
	private JList list;

	private static class MyListModel extends AbstractListModel {
		protected List<String> model;

		public MyListModel() {
			super();
			this.model = new ArrayList<String>();
			this.model.add("Ohio State [Buckeyes]");
			this.model.add("Auburn [Tigers]");
			this.model.add("University of South California [Trojans]");
			this.model.add("West Virginia [Mountaineers]");
			this.model.add("Florida [Gators]");
			this.model.add("Michigan [Wolverines]");
			this.model.add("Texas [Longhorns]");
			this.model.add("Louisville [Cardinals]");
			this.model.add("Louisiana State University [Tigers]");
			this.model.add("Georgia [Bulldogs]");
			this.model.add("Virginia Tech [Hokies]");
			this.model.add("Notre Dame [Fighting Irish]");
			this.model.add("Iowa [Hawkeyes]");
			this.model.add("Oregon [Ducks]");
			this.model.add("Tennessee [Volunteers]");
			this.model.add("Oklahoma [Sooners]");
			this.model.add("Texas Christian University [Horned Frogs]");
		}

		public Object getElementAt(int index) {
			return this.model.get(index);
		}

		public int getSize() {
			return this.model.size();
		}
	}

	public SampleFrame() {
		super("Test application");
		this.setLayout(new BorderLayout());
		this.tabbed = new JTabbedPane();

		this.add(Check.getToolbar("", 16, false), BorderLayout.NORTH);
		this.add(this.tabbed, BorderLayout.CENTER);
		// this.tabbed.putClientProperty(LafWidget.TABBED_PANE_PREVIEW_PAINTER,
		// new DefaultTabPreviewPainter());

		JPanel transPanel = new JPanel();
		transPanel.setLayout(new BorderLayout());

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 0));
		transPanel.add(buttons, BorderLayout.SOUTH);

		// for the first movie, change the following line to
		// use the BorderLayout
		final JPanel mainPanel = new JPanel(new FlowLayout());
		final JPanel mainPanel2 = new JPanel(new FlowLayout());

		final JPanel centerPanel = new JPanel(new GridLayout(2, 1));
		centerPanel.add(mainPanel);
		centerPanel.add(mainPanel2);

		final JButton b1 = new JButton("1");
		final JButton b2 = new JButton("2");
		final JButton b3 = new JButton("3");

		final JButton b4 = new JButton("4");
		final JButton b5 = new JButton("5");
		final JButton b6 = new JButton("6");

		final JButton add1 = new JButton("add");
		final JButton add2 = new JButton("add");
		add1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						mainPanel.add(b1);
						mainPanel.add(b2);
						mainPanel.add(b3);
						mainPanel.revalidate();
						add1.setVisible(false);
					}
				});
			}
		});
		add2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						mainPanel2.add(b4);
						mainPanel2.add(b5);
						mainPanel2.add(b6);
						mainPanel2.revalidate();
						add2.setVisible(false);
					}
				});
			}
		});
		mainPanel.add(add1);
		mainPanel2.add(add2);

		final JCheckBox cb = new JCheckBox("border layout");
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cb.isSelected()) {
					mainPanel.setLayout(new BorderLayout());
					mainPanel2.setLayout(new BorderLayout());
				} else {
					mainPanel.setLayout(new FlowLayout());
					mainPanel2.setLayout(new FlowLayout());
				}
				mainPanel.revalidate();
				mainPanel.doLayout();
				mainPanel.repaint();
				mainPanel2.revalidate();
			}
		});
		// buttons.add(cb);

		transPanel.add(centerPanel, BorderLayout.CENTER);

		final JCheckBox cb1 = new JCheckBox("1");
		cb1.setSelected(true);
		final JCheckBox cb2 = new JCheckBox("2");
		cb2.setSelected(true);
		final JCheckBox cb3 = new JCheckBox("3");
		cb3.setSelected(true);
		final JCheckBox cb4 = new JCheckBox("4");
		cb4.setSelected(true);
		final JCheckBox cb5 = new JCheckBox("5");
		cb5.setSelected(true);
		final JCheckBox cb6 = new JCheckBox("6");
		cb6.setSelected(true);
		buttons.add(cb1);
		buttons.add(cb2);
		buttons.add(cb3);
		buttons.add(cb4);
		buttons.add(cb5);
		buttons.add(cb6);

		JButton showHide = new JButton("Toggle");
		showHide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				b1.setVisible(cb1.isSelected());
				b2.setVisible(cb2.isSelected());
				b3.setVisible(cb3.isSelected());
				b4.setVisible(cb4.isSelected());
				b5.setVisible(cb5.isSelected());
				b6.setVisible(cb6.isSelected());
				mainPanel.doLayout();
				mainPanel2.doLayout();
			}
		});
		buttons.add(showHide);

		this.tabbed.addTab("Regular", transPanel);

		JPanel samplePanel = new JPanel(new BorderLayout());
		FormLayout lm = new FormLayout("fill:default:grow(1), 2dlu,"
				+ "fill:default:grow(1)",
				"pref, 3dlu, pref, 3dlu, pref, 3dlu, pref");
		PanelBuilder builder = new PanelBuilder(lm);
		builder.setBorder(new EmptyBorder(0, 2, 0, 2));
		CellConstraints cc = new CellConstraints();

		JCheckBox cbes = new JCheckBox("Enabled selected");
		cbes.setSelected(true);
		JCheckBox cbds = new JCheckBox("Disabled selected");
		cbds.setSelected(true);
		cbds.setEnabled(false);
		JCheckBox cbeu = new JCheckBox("Enabled unselected");
		JRadioButton rb1 = new JRadioButton("Enabled selected");
		rb1.setSelected(true);
		JRadioButton rb2 = new JRadioButton("Disabled selected");
		rb2.setSelected(true);
		rb2.setEnabled(false);
		JRadioButton rb3 = new JRadioButton("Enabled unselected");

		builder.add(cbes, cc.xy(1, 1));
		builder.add(rb1, cc.xy(3, 1));
		builder.add(cbds, cc.xy(1, 3));
		builder.add(rb2, cc.xy(3, 3));
		builder.add(cbeu, cc.xy(1, 5));
		builder.add(rb3, cc.xy(3, 5));

		JComboBox combo = new JComboBox(new Object[] { "item1" });
		combo.setSelectedIndex(0);
		JTextField text = new JTextField("Text field");
		// text.setEditable(false);

		builder.add(combo, cc.xy(1, 7));
		builder.add(text, cc.xy(3, 7));

		JPanel contentPanel = builder.getPanel();
		contentPanel.setPreferredSize(new Dimension(contentPanel
				.getPreferredSize().width,
				contentPanel.getPreferredSize().height + 100));
		// contentPanel.setBorder(null);

		contentPanel.setOpaque(false);
		final JScrollPane scroll = new JScrollPane(contentPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);

		final JPanel buttons2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5,
				2));
		this.prev = new JButton("prev");
		JButton cancel = new JButton("cancel");
		cancel.setEnabled(false);
		final JButton ok = new JButton("OK");
		// ok.putClientProperty(SubstanceLookAndFeel.PAINT_ACTIVE_PROPERTY,
		// Boolean.TRUE);
		buttons2.add(this.prev);
		buttons2.add(cancel);
		buttons2.add(ok);
		this.getRootPane().setDefaultButton(ok);
		// ok.requestFocusInWindow();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				ok.requestFocusInWindow();
			}
		});

		samplePanel.add(scroll, BorderLayout.CENTER);
		samplePanel.add(buttons2, BorderLayout.SOUTH);

		this.tabbed.addTab("Sample", samplePanel);

		JPanel samplePanel2 = new JPanel(new BorderLayout());
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		DefaultMutableTreeNode son1 = new DefaultMutableTreeNode("son1");
		DefaultMutableTreeNode son2 = new DefaultMutableTreeNode("son2");
		DefaultMutableTreeNode son3 = new DefaultMutableTreeNode("son3");
		DefaultMutableTreeNode gson11 = new DefaultMutableTreeNode("gson11");
		DefaultMutableTreeNode gson12 = new DefaultMutableTreeNode("gson12");
		DefaultMutableTreeNode gson21 = new DefaultMutableTreeNode("gson21");
		DefaultMutableTreeNode gson22 = new DefaultMutableTreeNode("gson22");
		DefaultMutableTreeNode gson31 = new DefaultMutableTreeNode("gson31");
		DefaultMutableTreeNode gson32 = new DefaultMutableTreeNode("gson32");
		DefaultMutableTreeNode ggson111 = new DefaultMutableTreeNode("ggson111");
		DefaultMutableTreeNode ggson112 = new DefaultMutableTreeNode("ggson112");
		DefaultMutableTreeNode ggson113 = new DefaultMutableTreeNode("ggson113");

		gson11.add(ggson111);
		gson11.add(ggson112);
		gson11.add(ggson113);
		son1.add(gson11);
		son1.add(gson12);
		son2.add(gson21);
		son2.add(gson22);
		son3.add(gson31);
		son3.add(gson32);
		root.add(son1);
		root.add(son2);
		root.add(son3);

		this.tree = new JTree(root);
		this.tree.setBorder(new EmptyBorder(0, 0, 0, 0));
		JScrollPane jspTree = new JScrollPane(this.tree);
		// TransitionLayoutManager.getInstance().track(jspTree, true);
		// jspTree.setBorder(new EmptyBorder(0, 0, 0, 0));

		this.list = new JList(new MyListModel());
		this.list.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.list
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane jspList = new JScrollPane(this.list);
		// jspList.setBorder(new EmptyBorder(0, 0, 0, 0));

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspTree,
				jspList);
		split.setDividerLocation(130);
		samplePanel2.add(split, BorderLayout.CENTER);
		this.tabbed.add("Renderers", samplePanel2);

		this.tabbed.setSelectedComponent(samplePanel);
		this.tabbed.setOpaque(false);
		this.tabbed.setBorder(new EmptyBorder(0, 2, 2, 2));
		JMenuBar jmb = new JMenuBar();

		if (UIManager.getLookAndFeel() instanceof SubstanceLookAndFeel) {
			// jmb.add(SampleMenuFactory.getThemeMenu());
			jmb.add(SampleMenuFactory.getSkinMenu());
		}

		JMenu testMenu = SampleMenuFactory.getTestMenu();
		jmb.add(testMenu);
		this.setJMenuBar(jmb);

		this.setResizable(true);

		this.synchronize();
		SubstanceLookAndFeel
				.registerSkinChangeListener(new SkinChangeListener() {
					@Override
					public void skinChanged() {
						SampleFrame.this.synchronize();
					}
				});
		this.getRootPane().setDefaultButton(ok);
	}

	protected void synchronize() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (SubstanceLookAndFeel.isCurrentLookAndFeel()) {
					SampleFrame.this
							.setIconImage(SubstanceLogo
									.getLogoImage(SubstanceLookAndFeel
											.getCurrentSkin(
													SampleFrame.this
															.getRootPane())
											.getColorScheme(
													DecorationAreaType.PRIMARY_TITLE_PANE,
													ColorSchemeAssociationKind.FILL,
													ComponentState.ENABLED)));
				}
			}
		});
	}

	public static void main(String... args) throws Exception {

		// UIDefaults uid = UIManager.getLookAndFeelDefaults();
		// Font segoe = new Font("Segoe UI", Font.PLAIN, 12);
		// for (Object key : uid.keySet()) {
		// if (key instanceof String) {
		// String skey = (String) key;
		// if (skey.endsWith(".font")) {
		// System.out.println(skey);
		// uid.put(skey, segoe);
		// }
		// }
		// }

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (System.getProperty("swing.defaultlaf") == null) {
					SubstanceLookAndFeel.setSkin(new GeminiSkin());
				}
				JFrame.setDefaultLookAndFeelDecorated(true);
				SampleFrame sf = new SampleFrame();
				sf.setSize(338, 245);
				sf.setLocationRelativeTo(null);
				sf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				sf.setVisible(true);
			}
		});

		// h(sf, 0);
	}

	public static void h(Component comp, int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print("   ");
		Rectangle r = comp.getBounds();
		System.out.println(comp.getClass().getSimpleName() + " [" + r.x + ","
				+ r.y + " : " + (r.x + r.width) + "," + (r.y + r.height) + "]");
		if (comp instanceof Container) {
			Container cont = (Container) comp;
			for (int i = 0; i < cont.getComponentCount(); i++) {
				h(cont.getComponent(i), depth + 1);
			}
		}
	}

	public void switchToLastTab() {
		this.tabbed.setSelectedIndex(2);
		this.list.setSelectedIndices(new int[] { 1, 4 });
		this.tree.setSelectionRow(1);
	}
}
