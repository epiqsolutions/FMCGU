package test.check.decoration;

import java.awt.*;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.tree.*;

import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.MistSilverSkin;

import test.check.SubstanceSkinComboSelector;

public class DecorationTrees extends JFrame {
	private static class TreePanel extends JPanel {
		public TreePanel(String caption, DecorationAreaType decorationAreaType,
				boolean isEnabled) {
			super(new BorderLayout());
			SubstanceLookAndFeel.setDecorationType(this, decorationAreaType);

			JLabel captionLabel = new JLabel(" " + caption);
			Font font = captionLabel.getFont();
			captionLabel.setFont(font.deriveFont(Font.BOLD));
			this.add(captionLabel, BorderLayout.NORTH);

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
			DefaultMutableTreeNode ggson111 = new DefaultMutableTreeNode(
					"ggson111");
			DefaultMutableTreeNode ggson112 = new DefaultMutableTreeNode(
					"ggson112");
			DefaultMutableTreeNode ggson113 = new DefaultMutableTreeNode(
					"ggson113");

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

			JTree tree = new JTree(root);
			expandAll(tree, true);
			tree.setEnabled(isEnabled);

			this.add(tree, BorderLayout.CENTER);
		}

		private static void expandAll(JTree tree, boolean expand) {
			TreeNode root = (TreeNode) tree.getModel().getRoot();

			// Traverse tree from root
			expandAll(tree, new TreePath(root), expand);
		}

		private static void expandAll(JTree tree, TreePath parent,
				boolean expand) {
			// Traverse children
			TreeNode node = (TreeNode) parent.getLastPathComponent();
			if (node.getChildCount() >= 0) {
				for (Enumeration e = node.children(); e.hasMoreElements();) {
					TreeNode n = (TreeNode) e.nextElement();
					TreePath path = parent.pathByAddingChild(n);
					expandAll(tree, path, expand);
				}
			}

			// Expansion or collapse must be done bottom-up
			if (expand) {
				tree.expandPath(parent);
			} else {
				tree.collapsePath(parent);
			}
		}

	}

	public DecorationTrees() {
		super("Trees in decoration areas");

		JPanel treePanel = new JPanel(new GridLayout(2, 4));

		treePanel.add(new TreePanel("Enabled in HEADER",
				DecorationAreaType.HEADER, true));
		treePanel.add(new TreePanel("Disabled in HEADER",
				DecorationAreaType.HEADER, false));
		treePanel.add(new TreePanel("Enabled in NONE", DecorationAreaType.NONE,
				true));
		treePanel.add(new TreePanel("Disabled in NONE",
				DecorationAreaType.NONE, false));
		treePanel.add(new TreePanel("Enabled in GENERAL",
				DecorationAreaType.GENERAL, true));
		treePanel.add(new TreePanel("Disabled in GENERAL",
				DecorationAreaType.GENERAL, false));
		treePanel.add(new TreePanel("Enabled in FOOTER",
				DecorationAreaType.FOOTER, true));
		treePanel.add(new TreePanel("Disabled in FOOTER",
				DecorationAreaType.FOOTER, false));

		this.setLayout(new BorderLayout());
		this.add(treePanel, BorderLayout.CENTER);

		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		controlPanel.add(new SubstanceSkinComboSelector());

		this.add(controlPanel, BorderLayout.SOUTH);

		this.setSize(500, 400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SubstanceLookAndFeel.setSkin(new MistSilverSkin());
				new DecorationTrees().setVisible(true);
			}
		});
	}
}
