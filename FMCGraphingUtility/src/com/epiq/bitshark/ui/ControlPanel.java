/*  +--------------------------+
 *  | FMC-1RX Graphing Utility |
 *  +--------------------------+
 *
 *  Copyright (C) 2011 Epiq Solutions Inc.
 *                     epiq-solutions.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  ===========================================================================
 *
 *  Revision History
 *  ----------------
 *  01/01/2011 : Created file
 *  01/09/2011 : Changed package name
 *  01/12/2012 : Added support for embedded controller 1.15
 */

package com.epiq.bitshark.ui;

import com.epiq.bitshark.client.ConnectionRequester;
import com.epiq.bitshark.Frequency;
import com.epiq.bitshark.client.FMCUartClient;
import com.epiq.bitshark.client.test.FMCTestClient;
import gnu.io.CommPortIdentifier;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;

/**
 * JPanel that contains controls for the FMC-1RX graphing utility.
 *
 * This class was originally generated code, so it still needs some
 * work to make it human readable.
 * 
 * @author Epiq Solutions
 */
public class ControlPanel extends JPanel {

    private javax.swing.JLabel basebandGainLabel;
    private javax.swing.JPanel basebandGainPanel;
    private javax.swing.JSpinner basebandGainSpinner;
    private javax.swing.JComboBox bbFilterBandwidthComboBox;
    private javax.swing.JLabel bbLine1Label;
    private javax.swing.JLabel bbLine2Label;
    private javax.swing.JPanel bbSpacerPanel;
    private javax.swing.JFormattedTextField centerFrequencyFormattedTextField;
    private javax.swing.JLabel centerFrequencyLabel;
    private javax.swing.JPanel centerFrequencyPanel;
    private javax.swing.JPanel cmdPanel;
    private javax.swing.JComboBox comPortComboBox;
    private javax.swing.JButton connectButton;
    private javax.swing.JPanel connectButtonPanel;
    private javax.swing.JPanel connectionPanel;
    private javax.swing.JButton disconnectButton;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JPanel frequencyPanel;
    private javax.swing.JPanel gainPanel;
    private javax.swing.JPanel mainBottomPanel;
    private javax.swing.JLabel portLabel;
    private javax.swing.JPanel portPanel;
    private javax.swing.JPanel portRightSpacerPanel;
    private javax.swing.JLabel rfGainLabel;
    private javax.swing.JPanel rfGainPanel;
    private javax.swing.JSpinner rfGainSpinner;
    private javax.swing.JFormattedTextField sampleRateFormattedTextField;
    private javax.swing.JLabel sampleRateLabel;
    private javax.swing.JPanel sampleRatePanel;
    private javax.swing.JButton showCommandPromptButton;
    private JCheckBox iqCalCheckBox = null;
    
    private java.util.Timer portPollTimer = new java.util.Timer();
    private ExecutorService commandPool = Executors.newSingleThreadExecutor();
    private FMCUartClient client = null;
    private IQPoller iqPoll = null;

    protected CommandLineFrame commandLineFrame = null;

    protected ConnectionRequester connectionRequester = null;
    protected IQHandler iqHandler = null;

    public ControlPanel(final ConnectionRequester connectionRequester,
                        final IQHandler iqHandler) {

        this.connectionRequester = connectionRequester;
        this.iqHandler = iqHandler;
        this.iqPoll = new IQPoller(iqHandler);

        initComponents();

        handleDisconnected();
        startPortPollTimer();        
    }

    private void initComponents() {
        mainBottomPanel = new javax.swing.JPanel();
        mainBottomPanel.setLayout(new HorizontalLayout(10));

        mainBottomPanel.add(initConnectionPanel());
        mainBottomPanel.add(new JSeparator(SwingConstants.VERTICAL));
        mainBottomPanel.add(initFrequencyPanel());
        mainBottomPanel.add(new JSeparator(SwingConstants.VERTICAL));
        mainBottomPanel.add(initGainPanel());
        mainBottomPanel.add(new JSeparator(SwingConstants.VERTICAL));
        mainBottomPanel.add(initFilterPanel());
        mainBottomPanel.add(new JSeparator(SwingConstants.VERTICAL));
        mainBottomPanel.add(initCalPanel());
        mainBottomPanel.add(new JSeparator(SwingConstants.VERTICAL));
        mainBottomPanel.add(initCmdPanel());

        this.setLayout(new BorderLayout());
        this.add(mainBottomPanel, BorderLayout.CENTER);
    }

    protected void initCommPortComboBox() {
        comPortComboBox = new javax.swing.JComboBox();
        comPortComboBox.setModel(new DefaultComboBoxModel(new CommPortIdentifier[0]));
        comPortComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list,
                                                          Object value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus) {
                Component c
                        = super.getListCellRendererComponent(list,
                                                             value,
                                                             index,
                                                             isSelected,
                                                             cellHasFocus);

                if(FMCTestClient.SHOW_TEST_SERVER) {
                    if(value == null) {
                        ((JLabel)c).setText("Test Server");
                        return c;
                    }
                }

                if(!(value instanceof CommPortIdentifier)) {
                    return c;
                }

                CommPortIdentifier cpi = (CommPortIdentifier)value;

                JLabel l = (JLabel)c;

                if(cpi != null) {
                    if(cpi.isCurrentlyOwned()) {
                        l.setText(cpi.getName() + " (owned)");
                    } else {
                        l.setText(cpi.getName());
                    }
                }

                return c;
            }

        });
    }

    protected JPanel initConnectionPanel() {

        connectionPanel = new javax.swing.JPanel();
        disconnectButton = new javax.swing.JButton();
        connectButton = new javax.swing.JButton();
        connectButtonPanel = new javax.swing.JPanel();
        portRightSpacerPanel = new javax.swing.JPanel();
        portPanel = new javax.swing.JPanel();
        portLabel = new javax.swing.JLabel();

        connectionPanel.setPreferredSize(new java.awt.Dimension(200, 100));
        connectionPanel.setLayout(new VerticalLayout(5));

        portPanel.setLayout(new java.awt.BorderLayout());

        portLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        portLabel.setText("Port:");
        portLabel.setPreferredSize(new java.awt.Dimension(40, 20));
        portPanel.add(portLabel, java.awt.BorderLayout.WEST);

        initCommPortComboBox();
        portPanel.add(comPortComboBox, java.awt.BorderLayout.CENTER);

        portRightSpacerPanel.setPreferredSize(new java.awt.Dimension(5, 0));
        portRightSpacerPanel.setLayout(new java.awt.BorderLayout());
        portPanel.add(portRightSpacerPanel, java.awt.BorderLayout.EAST);

        connectionPanel.add(portPanel);

        connectButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        disconnectButton.setText("Disconnect");
        disconnectButton.setEnabled(false);
        disconnectButton.setPreferredSize(new java.awt.Dimension(95, 25));
        disconnectButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectButtonActionPerformed(evt);
            }
        });
        connectButtonPanel.add(disconnectButton);

        connectButton.setText("Connect");
        connectButton.setPreferredSize(new java.awt.Dimension(95, 25));
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });
        connectButtonPanel.add(connectButton);

        connectionPanel.add(connectButtonPanel);

        return connectionPanel;
    }

    protected JPanel initFrequencyPanel() {
        frequencyPanel = new javax.swing.JPanel();

        centerFrequencyPanel = new javax.swing.JPanel();
        centerFrequencyLabel = new javax.swing.JLabel();
        centerFrequencyFormattedTextField = new javax.swing.JFormattedTextField();
        sampleRatePanel = new javax.swing.JPanel();
        sampleRateLabel = new javax.swing.JLabel();
        sampleRateFormattedTextField = new javax.swing.JFormattedTextField();

        frequencyPanel.setPreferredSize(new java.awt.Dimension(210, 60));
        frequencyPanel.setLayout(new VerticalLayout(5));

        centerFrequencyPanel.setLayout(new java.awt.BorderLayout());

        centerFrequencyLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        centerFrequencyLabel.setText("Center Frequency:");
        centerFrequencyLabel.setPreferredSize(new java.awt.Dimension(110, 14));
        centerFrequencyPanel.add(centerFrequencyLabel, java.awt.BorderLayout.WEST);
        centerFrequencyPanel.add(centerFrequencyFormattedTextField, java.awt.BorderLayout.CENTER);

        centerFrequencyFormattedTextField.setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {
            FrequencyTextFieldFormatter formatter
                    = new FrequencyTextFieldFormatter((long)300e6,
                                                      (long)4e9,
                                                      (long)1e3);
            @Override
            public AbstractFormatter getFormatter(JFormattedTextField tf) {
                return formatter;
            }
        });
                
        centerFrequencyFormattedTextField.addPropertyChangeListener(
            "value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setCenterFrequency((Frequency)evt.getNewValue());
            }
        });

        frequencyPanel.add(centerFrequencyPanel);

        sampleRatePanel.setLayout(new java.awt.BorderLayout());

        sampleRateLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        sampleRateLabel.setText("Sample Rate:");
        sampleRateLabel.setPreferredSize(new java.awt.Dimension(110, 14));
        sampleRatePanel.add(sampleRateLabel, java.awt.BorderLayout.WEST);
        sampleRatePanel.add(sampleRateFormattedTextField, java.awt.BorderLayout.CENTER);

        sampleRateFormattedTextField.setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {
            FrequencyTextFieldFormatter formatter
                    = new FrequencyTextFieldFormatter((long)5e6,
                                                      (long)105e6,
                                                      (long)1);

            @Override
            public AbstractFormatter getFormatter(JFormattedTextField tf) {
                return formatter;
            }

        });

        sampleRateFormattedTextField.addPropertyChangeListener(
            "value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setSampleRate((Frequency)evt.getNewValue());
            }
        });

        frequencyPanel.add(sampleRatePanel);

        return frequencyPanel;
    }

    protected JPanel initGainPanel() {
        gainPanel = new javax.swing.JPanel();
        rfGainPanel = new javax.swing.JPanel();
        rfGainLabel = new javax.swing.JLabel();
        rfGainSpinner = new javax.swing.JSpinner();
        basebandGainPanel = new javax.swing.JPanel();
        basebandGainLabel = new javax.swing.JLabel();
        basebandGainSpinner = new javax.swing.JSpinner();

        gainPanel.setPreferredSize(new java.awt.Dimension(150, 60));
        gainPanel.setLayout(new VerticalLayout(5));

        rfGainPanel.setLayout(new java.awt.BorderLayout());

        rfGainLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        rfGainLabel.setText("RF Gain:");
        rfGainLabel.setPreferredSize(new java.awt.Dimension(95, 14));
        rfGainPanel.add(rfGainLabel, java.awt.BorderLayout.WEST);

        SpinnerNumberModel rfGainModel
                = new SpinnerNumberModel(0.0f,  // value
                                         0.0f,  // min
                                         31.5f, // max
                                         0.5f) {  // step size
            @Override
            public void setValue(Object value) {

                if(value instanceof Number) {
                    if( (((Number)value).floatValue() * 10) % 5 != 0 ) {
                        throw new IllegalArgumentException("Invalid value");
                    }
                 }

                super.setValue(value);
            }
        };

        rfGainSpinner.setModel(rfGainModel);

        rfGainSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rfGainSpinnerStateChanged(evt);
            }
        });
        rfGainPanel.add(rfGainSpinner, java.awt.BorderLayout.CENTER);

        gainPanel.add(rfGainPanel);

        basebandGainPanel.setLayout(new java.awt.BorderLayout());

        basebandGainLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        basebandGainLabel.setText("Baseband Gain:");
        basebandGainLabel.setPreferredSize(new java.awt.Dimension(95, 14));
        basebandGainPanel.add(basebandGainLabel, java.awt.BorderLayout.WEST);

        SpinnerNumberModel basebandGainModel
                = new SpinnerNumberModel(0.0f,  // value
                                         0.0f,  // min
                                         6.0f, // max
                                         3.0f) {  // step size
            @Override
            public void setValue(Object value) {

                if(value instanceof Number) {
                    if( (((Number)value).floatValue() * 10) % 30 != 0 ) {
                        throw new IllegalArgumentException("Invalid value");
                    }
                 }

                super.setValue(value);
            }
        };
        basebandGainSpinner.setModel(basebandGainModel);
        
        basebandGainSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                basebandGainSpinnerStateChanged(evt);
            }
        });
        basebandGainPanel.add(basebandGainSpinner, java.awt.BorderLayout.CENTER);

        gainPanel.add(basebandGainPanel);

        return gainPanel;

    }

    protected JPanel initFilterPanel() {
        filterPanel = new javax.swing.JPanel();
        bbLine1Label = new javax.swing.JLabel();
        bbLine2Label = new javax.swing.JLabel();
        bbSpacerPanel = new javax.swing.JPanel();
        bbFilterBandwidthComboBox = new javax.swing.JComboBox();

        filterPanel.setPreferredSize(new java.awt.Dimension(120, 55));
        filterPanel.setLayout(new org.jdesktop.swingx.VerticalLayout());

        bbLine1Label.setFont(new java.awt.Font("Tahoma", 1, 10));
        bbLine1Label.setText("Baseband Low-Pass");
        filterPanel.add(bbLine1Label);

        bbLine2Label.setFont(new java.awt.Font("Tahoma", 1, 10));
        bbLine2Label.setText("Filter Bandwidth:");
        filterPanel.add(bbLine2Label);

        bbSpacerPanel.setPreferredSize(new java.awt.Dimension(0, 5));
        bbSpacerPanel.setLayout(new java.awt.BorderLayout());
        filterPanel.add(bbSpacerPanel);

        bbFilterBandwidthComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "330 kHz", "660 kHz", "1 MHz", "2 MHz", "3 MHz", "4 MHz", "5 MHz", "6 MHz", "7 MHz", "8 MHz", "9 MHz", "10 MHz", "11 MHz", "12 MHz", "13 MHz", "14 MHz", "15 MHz", "16 MHz", "17 MHz", "18 MHz", "19 MHz", "20 MHz", "21 MHz", "22 MHz", "23 MHz", "24 MHz", "25 MHz", "26 MHz", "27 MHz", "28 MHz" }));
        bbFilterBandwidthComboBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bbFilterBandwidthComboBoxActionPerformed(evt);
            }
        });
        filterPanel.add(bbFilterBandwidthComboBox);

        return filterPanel;
    }

    protected JPanel initCmdPanel() {

        commandLineFrame = new CommandLineFrame();

        cmdPanel = new javax.swing.JPanel();
        showCommandPromptButton = new javax.swing.JButton();

        cmdPanel.setLayout(new java.awt.GridBagLayout());

        showCommandPromptButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/epiq/bitshark/ui/kterm.png")));
        showCommandPromptButton.setMargin(new java.awt.Insets(2, 2, -2, 2));
        showCommandPromptButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showCommandPromptButtonActionPerformed(evt);
            }
        });
        cmdPanel.add(showCommandPromptButton, new java.awt.GridBagConstraints());

        return cmdPanel;
    }

    protected JPanel initCalPanel() {
        JPanel calPanel = new JPanel();

        calPanel.setLayout(new VerticalLayout(3));
        Font f = new Font("Tahoma", Font.BOLD, 11);
        iqCalCheckBox = new JCheckBox("Use IQ");
        iqCalCheckBox.setFont(f);
        iqCalCheckBox.setFocusPainted(false);
        iqCalCheckBox.setSelected(true);
        iqCalCheckBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iqCalCheckBoxActionPerformed(evt);
            }
        });
        final JLabel line2 = new JLabel("Calibration");
        line2.setFont(f);
        calPanel.add(iqCalCheckBox);
        calPanel.add(line2);

        iqCalCheckBox.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                line2.setEnabled(iqCalCheckBox.isEnabled());
            }
        });

        JPanel holder = new JPanel();
        holder.setLayout(new GridBagLayout());
        holder.add(calPanel);

        return holder;
    }

    protected void handleConnected() {

        this.connectionRequester.handleConnected(client);

        commandLineFrame.setClient(client);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                centerFrequencyFormattedTextField.setEnabled(true);
                sampleRateFormattedTextField.setEnabled(true);
                rfGainSpinner.setEnabled(true);
                basebandGainSpinner.setEnabled(true);
                bbFilterBandwidthComboBox.setEnabled(true);
                iqCalCheckBox.setEnabled(true);
            }
        };

        if(SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }

        final double version = client.getVersion();
        
        // this was added because version 1.15 of the board allows a lower
        // bound frequency of 200 MHz
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                long freqLowerBound = (long)300e6;
                
                // should this be version >= 1.15
                if(version == 1.15) {
                    freqLowerBound = (long)200e6;
                }
                
                AbstractFormatter af = centerFrequencyFormattedTextField.getFormatterFactory()
                                                                       .getFormatter(centerFrequencyFormattedTextField);
                if(af instanceof FrequencyTextFieldFormatter) {
                    FrequencyTextFieldFormatter ftff = (FrequencyTextFieldFormatter)af;
                    ftff.setLowerBound(freqLowerBound);
                }
            }
        });
        
        int fails = 0;
        boolean success = false;

        do {
            try {

                final long center = client.getCenterFrequency();
                final int rate = client.getSampleRate();

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        centerFrequencyFormattedTextField.setValue(new Frequency(center));
                        sampleRateFormattedTextField.setValue(new Frequency(rate));
                    }
                });

                success = true;

            } catch(Exception e) {
                System.err.println("Error with getting center or rate");
                e.printStackTrace();

                ++fails;
            }
        } while(!success && fails < 3);

        try {
            final long bbFilterBw = client.getBasebandFilterBw() / 2;

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    DefaultComboBoxModel bbfModel = (DefaultComboBoxModel)bbFilterBandwidthComboBox.getModel();
                    for(int i = 0; i < bbfModel.getSize(); ++i) {
                        String v = (String)bbfModel.getElementAt(i);
                        if(Frequency.parse(v).getValue() == bbFilterBw) {
                            bbfModel.setSelectedItem(v);
                            break;
                        }
                    }
                }
            });
        } catch(Exception e) {
            System.err.println("error with getting bbw");
            e.printStackTrace();
        }

        try {
            final float rfGain = client.getRfGain();
            final float bbGain = client.getBasebandGain();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ControlPanel.this.rfGainSpinner.setValue(new Double(rfGain));
                    ControlPanel.this.basebandGainSpinner.setValue(new Double(bbGain));
                }
            });
        } catch(Exception e) {
            System.err.println("error getting RF Gain or Baseband Gain");
            e.printStackTrace();
        }
    }

    protected void handleDisconnected() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                centerFrequencyFormattedTextField.setEnabled(false);
                sampleRateFormattedTextField.setEnabled(false);
                rfGainSpinner.setEnabled(false);
                basebandGainSpinner.setEnabled(false);
                bbFilterBandwidthComboBox.setEnabled(false);
                iqCalCheckBox.setEnabled(false);
            }
        };

        if(SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }

        this.connectionRequester.handleDisconnected();
    }

    protected void setCenterFrequency(final Frequency centerFrequency) {

        if(centerFrequency == null) return;

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    client.setCenterFrequency(centerFrequency.getValue());
                    handleCenterSampleRateChanged();
                } catch(Exception e) {
                    System.err.println("Error Setting Center Frequency: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };

        commandPool.submit(r);
    }

    protected void setSampleRate(final Frequency sampleRate) {

        if(sampleRate == null) return;

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    client.setSampleRate((int)sampleRate.getValue());
                    handleCenterSampleRateChanged();
                } catch(Exception e) {
                    System.err.println("Error Setting Sample Rate: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };

        commandPool.submit(r);
    }

    protected void handleCenterSampleRateChanged() {
        if(centerFrequencyFormattedTextField.getValue() != null &&
           sampleRateFormattedTextField.getValue() != null) {
                ControlPanel.this.iqHandler.setConfiguration(
                    ((Frequency)centerFrequencyFormattedTextField.getValue()).getValue(),
                    (int)((Frequency)sampleRateFormattedTextField.getValue()).getValue());
        }
    }

    protected void startPortPollTimer() {
        portPollTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {

                        if(ControlPanel.this.client != null &&
                           ControlPanel.this.client.isConnected()) {
                            return;
                        }

                        final CommPortIdentifier [] port = FMCUartClient.getCommPortList();
                        final DefaultComboBoxModel model = new DefaultComboBoxModel(port);

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {

                                if(comPortComboBox.isPopupVisible()) return;

                                Object selected = comPortComboBox.getSelectedItem(); // remember user selection
                                comPortComboBox.setModel(model);

                                if(selected != null) {
                                    CommPortIdentifier s = (CommPortIdentifier)selected;
                                    for(int i = 0; i < model.getSize(); ++i) {
                                        Object o = model.getElementAt(i);

                                        if(o != null) {
                                            if( ((CommPortIdentifier)o).getName().equals(s.getName()) ) {
                                                comPortComboBox.setSelectedItem(o);
                                                break;
                                            }
                                        }
                                    }
                                }

                                //comPortComboBox.setSelectedItem(selected); // restore user selection

                            }
                        });
                    }
                },
                2000, // delay
                1000  // period
                );
    }

    private void iqCalCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        final boolean enable = iqCalCheckBox.isSelected();
        commandPool.submit(new Runnable() {
            @Override
            public void run() {
                client.setIqCalEnabled(enable);
            }
        });
    }

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        final CommPortIdentifier commPort = (CommPortIdentifier)comPortComboBox.getSelectedItem();
        commandPool.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    if(commPort != null) {
                        client = new FMCUartClient(commPort);
                    } else {
                        if(FMCTestClient.SHOW_TEST_SERVER) {
                            client = new FMCTestClient();
                        } else {
                            return;
                        }
                    }

                    handleConnected();

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            comPortComboBox.setEnabled(false);
                            connectButton.setEnabled(false);
                            disconnectButton.setEnabled(true);
                        }
                    });

                    iqPoll.start(client);

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void disconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        commandPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    client.disconnect();

                    handleDisconnected();

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            comPortComboBox.setEnabled(true);
                            connectButton.setEnabled(true);
                            disconnectButton.setEnabled(false);
                        }
                    });

                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    client = null;
                    iqPoll.stop();

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
//                            ControlPanel.this.graphingPanel.clear();
                            iqHandler.handleIqEnded();
                        }
                    });
                }
            }
        });
    }

    private void rfGainSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {

        final float rfGain = ((Number)rfGainSpinner.getValue()).floatValue();
        commandPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    client.setRfGain((float)rfGain);
                } catch(Exception e) {
                    System.err.println("Error setting RF Gain: " + e.getMessage());
                }
            }
        });

    }

    private void basebandGainSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {
        final int bbGain = ((Number)basebandGainSpinner.getValue()).intValue();
        commandPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    client.setBasebandGain(bbGain);
                } catch(Exception e) {
                    System.err.println("Error setting baseband gain: " + e.getMessage());
                }
            }
        });
    }

    private void bbFilterBandwidthComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        String bwStr = (String)bbFilterBandwidthComboBox.getSelectedItem();
        final Frequency bw = Frequency.parse(bwStr);

        commandPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    client.setBasebandFilterBw((int)bw.getValue() * 2);
                } catch(Exception e) {
                    System.err.println("Error setting baseband low-pass filter bandwidth: " + e.getMessage());
                }
           }
        });
    }

    private void showCommandPromptButtonActionPerformed(java.awt.event.ActionEvent evt) {
        commandLineFrame.setLocationRelativeTo(null);
        commandLineFrame.setVisible(true);
    }

}
