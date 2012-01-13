package test.contrib;

import java.awt.*;
import java.math.BigDecimal;

import javax.swing.*;

import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

public class Test {

	private static JPanel getTestPanel() {
		JPanel panel = new Test().new APanel();
		SubstanceLookAndFeel.setDecorationType(panel,
				DecorationAreaType.GENERAL);
		return panel;
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
				frame.setSize(800, 300);
				Dimension paneSize = frame.getSize();
				Dimension screenSize = frame.getToolkit().getScreenSize();
				frame.setLocation((screenSize.width - paneSize.width) / 2,
						(screenSize.height - paneSize.height) / 2);
				frame.setTitle("Test");
				frame.setVisible(true);
			}
		});
	}

	class APanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public APanel() {
			super();
			setLayout(new GridBagLayout());

			initGUI();
		}

		protected void initGUI() {
			JPanel datosPanel = new JPanel(new BorderLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.weightx = 1.0;
			constraints.weighty = 1.0;
			constraints.fill = GridBagConstraints.BOTH;
			constraints.anchor = GridBagConstraints.NORTHWEST;
			this.add(datosPanel, constraints);

			datosPanel.add(initPanelSuperior(), BorderLayout.NORTH);
			datosPanel.add(initPanelInferior(), BorderLayout.SOUTH);
		}

		private JPanel initPanelSuperior() {
			JPanel panelSuperior = new JPanel(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(5, 5, 5, 5);
			int gridx = 0;
			// Fila 1
			gridx = 0;
			{
				JLabel nro_cuentaLabel = new JLabel("Right");
				constraints.gridx = gridx;
				constraints.gridwidth = 2;
				gridx += 2;
				panelSuperior.add(nro_cuentaLabel, constraints);
				constraints.gridwidth = 1;

				JFormattedTextField nro_cuentaTextField = new JFormattedTextField();
				constraints.gridx = gridx++;
				constraints.insets = new Insets(0, 0, 0, 5);
				panelSuperior.add(nro_cuentaTextField, constraints);

				gridx++;
			}
			constraints.insets = new Insets(0, 0, 0, 5);
			{
				JLabel nombre_razon_socialLabel = new JLabel("Right");
				constraints.gridx = gridx++;
				constraints.anchor = GridBagConstraints.EAST;
				panelSuperior.add(nombre_razon_socialLabel, constraints);

				JFormattedTextField nombre_razon_socialTextField = new JFormattedTextField();
				constraints.gridx = gridx;
				constraints.gridwidth = 6;
				gridx += 6;
				constraints.weightx = 1.0;
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.insets = new Insets(0, 0, 0, 5);
				constraints.anchor = GridBagConstraints.WEST;
				panelSuperior.add(nombre_razon_socialTextField, constraints);
			}
			constraints.weightx = 0;
			constraints.fill = GridBagConstraints.NONE;
			constraints.insets = new Insets(0, 5, 0, 5);
			constraints.gridwidth = 1;
			{
				JLabel saldo_cta_cteLabel = new JLabel("Wrong");
				constraints.gridx = gridx++;
				constraints.anchor = GridBagConstraints.EAST;
				panelSuperior.add(saldo_cta_cteLabel, constraints);

				JFormattedTextField saldo_cta_cteTextField = new JFormattedTextField();
				saldo_cta_cteTextField.setEditable(false);
				saldo_cta_cteTextField.setColumns(11);
				constraints.gridx = gridx++;
				constraints.insets = new Insets(0, 0, 0, 5);
				constraints.anchor = GridBagConstraints.WEST;
				panelSuperior.add(saldo_cta_cteTextField, constraints);
			}
			// Fila 2
			constraints.gridy = 1;
			constraints.insets = new Insets(8, 5, 8, 5);
			gridx = 0;
			{
				JLabel cond_ivaLabel = new JLabel("Wrong");
				constraints.gridx = gridx++;
				constraints.anchor = GridBagConstraints.EAST;
				panelSuperior.add(cond_ivaLabel, constraints);

				JTextField cond_ivaTextField = new JTextField();
				cond_ivaTextField.setEditable(false);
				cond_ivaTextField.setColumns(11);
				constraints.gridx = gridx;
				constraints.gridwidth = 3;
				gridx += 3;
				constraints.insets = new Insets(0, 0, 0, 5);
				constraints.anchor = GridBagConstraints.WEST;
				panelSuperior.add(cond_ivaTextField, constraints);
			}
			constraints.gridwidth = 1;
			constraints.insets = new Insets(0, 0, 0, 5);
			{
				JLabel direccionLabel = new JLabel("Wrong");
				constraints.gridx = gridx++;
				constraints.anchor = GridBagConstraints.EAST;
				panelSuperior.add(direccionLabel, constraints);
			}
			{
				JPanel panel = new JPanel(new GridBagLayout());
				constraints.gridx = gridx;
				constraints.gridwidth = 8;
				gridx += 8;
				constraints.weightx = 1.0;
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.insets = new Insets(0, 0, -2, 0);
				constraints.anchor = GridBagConstraints.WEST;
				panelSuperior.add(panel, constraints);

				GridBagConstraints constraints2 = new GridBagConstraints();
				JTextField direccionTextField = new JTextField();
				direccionTextField.setEditable(false);
				constraints2.gridx = 0;
				constraints2.weightx = 1.0;
				constraints2.fill = GridBagConstraints.HORIZONTAL;
				constraints2.insets = new Insets(-1, 0, 0, 10);
				panel.add(direccionTextField, constraints2);
			}
			return panelSuperior;
		}

		private JPanel initPanelInferior() {
			JPanel panelInferior = new JPanel(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();

			// Columna 1
			JPanel panelInferiorColumna1 = new JPanel(new GridBagLayout());
			constraints.weightx = 1.0;
			constraints.weighty = 1.0;
			constraints.fill = GridBagConstraints.BOTH;
			constraints.anchor = GridBagConstraints.NORTHWEST;
			constraints.insets = new Insets(-4, 0, 0, 0);
			panelInferior.add(panelInferiorColumna1, constraints);

			// Fila 1
			JPanel panelImporteNetoIVA = new JPanel(new FlowLayout(
					FlowLayout.LEFT));
			constraints.anchor = GridBagConstraints.WEST;
			panelInferiorColumna1.add(panelImporteNetoIVA, constraints);

			panelImporteNetoIVA.add(new JLabel("Right"));

			JFormattedTextField importe_netoTextField = new JFormattedTextField();
			importe_netoTextField.setEditable(false);
			panelImporteNetoIVA.add(importe_netoTextField);
			importe_netoTextField.setValue(new BigDecimal(123456));

			panelImporteNetoIVA.add(getAreaVacia(8, 0));

			panelImporteNetoIVA.add(new JLabel("Right"));

			JFormattedTextField importe_ivaTextField = new JFormattedTextField();
			importe_ivaTextField.setEditable(false);
			panelImporteNetoIVA.add(importe_ivaTextField);
			importe_ivaTextField.setValue(new BigDecimal(123456));

			// Fila 2
			JPanel panelConcepNoGravImpExento = new JPanel(new FlowLayout(
					FlowLayout.LEFT));
			constraints.gridy = 1;
			constraints.insets = new Insets(0, 5, 0, 5);
			panelInferiorColumna1.add(panelConcepNoGravImpExento, constraints);

			panelConcepNoGravImpExento.add(new JLabel("Right"));

			JFormattedTextField conceptos_no_gravadosTextField = new JFormattedTextField();
			panelConcepNoGravImpExento.add(conceptos_no_gravadosTextField);
			conceptos_no_gravadosTextField.setValue(new BigDecimal(123456));

			panelConcepNoGravImpExento.add(getAreaVacia(8, 0));

			panelConcepNoGravImpExento.add(new JLabel("Right"));

			JFormattedTextField importe_exentoTextField = new JFormattedTextField();
			panelConcepNoGravImpExento.add(importe_exentoTextField);
			importe_exentoTextField.setValue(new BigDecimal(123456));

			// Columna 2
			JPanel panelInferiorColumna2 = new JPanel(new GridBagLayout());
			constraints = new GridBagConstraints();
			constraints.gridx = 1;
			constraints.weighty = 1.0;
			constraints.fill = GridBagConstraints.VERTICAL;
			constraints.anchor = GridBagConstraints.NORTH;
			constraints.insets = new Insets(0, -1, 0, -1);
			panelInferior.add(panelInferiorColumna2, constraints);

			constraints = new GridBagConstraints();
			constraints.gridwidth = 3;
			constraints.weighty = 0.5;
			constraints.anchor = GridBagConstraints.NORTH;
			constraints.insets = new Insets(3, 5, 3, 5);
			panelInferiorColumna2.add(new JPanel(), constraints);

			constraints.gridwidth = 1;
			constraints.gridy = 1;
			constraints.insets = new Insets(0, 0, 3, 0);
			panelInferiorColumna2.add(getAreaVacia(0, 23), constraints);

			JFormattedTextField contadoChequeTextField = new JFormattedTextField();
			contadoChequeTextField.setColumns(6);
			constraints.gridx = 1;
			constraints.insets = new Insets(3, 5, 0, -5);
			panelInferiorColumna2.add(contadoChequeTextField, constraints);
			contadoChequeTextField.setVisible(false);
			contadoChequeTextField.setValue(new BigDecimal(123456));

			// Columna 3
			JPanel panelInferiorColumna3 = new JPanel(new GridBagLayout());
			constraints = new GridBagConstraints();
			constraints.weighty = 1.0;
			constraints.fill = GridBagConstraints.BOTH;
			constraints.anchor = GridBagConstraints.NORTHEAST;
			panelInferior.add(panelInferiorColumna3, constraints);

			// Fila 1
			JPanel panelImporteTotal = new JPanel(new FlowLayout(
					FlowLayout.RIGHT, 5, 2));
			constraints.anchor = GridBagConstraints.EAST;
			constraints.insets = new Insets(0, 5, 0, 0);
			panelInferiorColumna3.add(panelImporteTotal, constraints);

			panelImporteTotal.add(new JLabel("Wrong"));

			panelImporteTotal.add(getAreaVacia(4, 0));

			JFormattedTextField importe_totalTextField = new JFormattedTextField();
			importe_totalTextField.setEditable(false);
			panelImporteTotal.add(importe_totalTextField);
			importe_totalTextField.setFont(new Font("Dialog", 1, 16));
			importe_totalTextField.setValue(new BigDecimal(123456));

			// Fila 2
			JPanel panelPagoVuelto = new JPanel(new FlowLayout(
					FlowLayout.RIGHT, 5, 2));
			constraints.gridy = 1;
			panelInferiorColumna3.add(panelPagoVuelto, constraints);

			panelPagoVuelto.add(new JLabel("Wrong"));

			JFormattedTextField pagoTextField = new JFormattedTextField();
			pagoTextField.setColumns(7);
			panelPagoVuelto.add(pagoTextField);

			panelPagoVuelto.add(new JLabel("Wrong"));

			JFormattedTextField vueltoTextField = new JFormattedTextField();
			vueltoTextField.setEditable(false);
			vueltoTextField.setColumns(7);
			panelPagoVuelto.add(vueltoTextField);

			return panelInferior;
		}

		private Component getAreaVacia(int width, int height) {
			return Box.createRigidArea(new Dimension(width, height));
		}
	}
}
