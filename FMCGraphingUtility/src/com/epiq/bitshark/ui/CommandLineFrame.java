/*  +--------------------------+
 *  | FMC-1RX Graphing Utility |
 *  +--------------------------+
 *
 *  Copyright (C) 2011 Epiq Solutions
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
 */

package com.epiq.bitshark.ui;

import com.epiq.bitshark.client.FMCUartClient;
import com.epiq.bitshark.client.NckException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Epiq Solutions
 */
public class CommandLineFrame extends JFrame {

    private javax.swing.JTextField commandTextField;
    private javax.swing.JPanel inputLeftSpacer;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JPanel inputTopSpacer;
    private javax.swing.JPanel mainContentPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane responseScrollPane;
    private javax.swing.JTextArea responseTextArea;
    private javax.swing.JButton sendButton;
    private javax.swing.JPanel sendButtonCenterPanel;
    private javax.swing.JPanel sendButtonLeftSpacer;
    private javax.swing.JPanel sendButtonPanel;
    private javax.swing.JPanel sendButtonRightSpacer;
    private javax.swing.JPanel topPanel;

    protected FMCUartClient client = null;
    protected ExecutorService pool = Executors.newSingleThreadExecutor();

    /** Creates new form CommandLineFrame */
    public CommandLineFrame() {
        initComponents();

        this.setIconImage(new ImageIcon(getClass().getResource("kterm16.png")).getImage());
        this.setSize(400, 500);

    }

    /**
     * Sets the client that can be used to issue commands
     * 
     * @param client
     */
    public void setClient(FMCUartClient client) {
        this.client = client;
    }

    /**
     * This was originally generated code, so it may
     * be a little hard to follow. 
     */
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        topPanel = new javax.swing.JPanel();
        sendButtonPanel = new javax.swing.JPanel();
        sendButtonLeftSpacer = new javax.swing.JPanel();
        sendButtonRightSpacer = new javax.swing.JPanel();
        sendButtonCenterPanel = new javax.swing.JPanel();
        sendButton = new javax.swing.JButton();
        inputPanel = new javax.swing.JPanel();
        inputTopSpacer = new javax.swing.JPanel();
        commandTextField = new javax.swing.JTextField();
        inputLeftSpacer = new javax.swing.JPanel();
        mainContentPanel = new javax.swing.JPanel();
        responseScrollPane = new javax.swing.JScrollPane();
        responseTextArea = new javax.swing.JTextArea();

        setTitle("FMC-1RX Command Line");

        mainPanel.setLayout(new java.awt.BorderLayout());

        topPanel.setPreferredSize(new java.awt.Dimension(0, 30));
        topPanel.setLayout(new java.awt.BorderLayout());

        sendButtonPanel.setLayout(new java.awt.BorderLayout());

        sendButtonLeftSpacer.setPreferredSize(new java.awt.Dimension(10, 30));
        sendButtonLeftSpacer.setLayout(new java.awt.BorderLayout());
        sendButtonPanel.add(sendButtonLeftSpacer, java.awt.BorderLayout.WEST);

        sendButtonRightSpacer.setPreferredSize(new java.awt.Dimension(10, 30));
        sendButtonRightSpacer.setLayout(new java.awt.BorderLayout());
        sendButtonPanel.add(sendButtonRightSpacer, java.awt.BorderLayout.EAST);

        sendButtonCenterPanel.setLayout(new java.awt.GridBagLayout());

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });
        sendButtonCenterPanel.add(sendButton, new java.awt.GridBagConstraints());

        sendButtonPanel.add(sendButtonCenterPanel, java.awt.BorderLayout.CENTER);

        topPanel.add(sendButtonPanel, java.awt.BorderLayout.EAST);

        org.jdesktop.swingx.VerticalLayout verticalLayout1 = new org.jdesktop.swingx.VerticalLayout();
        verticalLayout1.setGap(5);
        inputPanel.setLayout(verticalLayout1);

        inputTopSpacer.setPreferredSize(new java.awt.Dimension(0, 0));
        inputTopSpacer.setLayout(new java.awt.BorderLayout());
        inputPanel.add(inputTopSpacer);

        commandTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                commandTextFieldKeyTyped(evt);
            }
        });
        inputPanel.add(commandTextField);

        topPanel.add(inputPanel, java.awt.BorderLayout.CENTER);

        inputLeftSpacer.setPreferredSize(new java.awt.Dimension(10, 0));
        inputLeftSpacer.setLayout(new java.awt.BorderLayout());
        topPanel.add(inputLeftSpacer, java.awt.BorderLayout.WEST);

        mainPanel.add(topPanel, java.awt.BorderLayout.NORTH);

        mainContentPanel.setLayout(new java.awt.BorderLayout());

        responseScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        responseScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        responseTextArea.setColumns(20);
        responseTextArea.setFont(new java.awt.Font("Monospaced", 0, 12));
        responseTextArea.setRows(5);
        responseScrollPane.setViewportView(responseTextArea);

        mainContentPanel.add(responseScrollPane, java.awt.BorderLayout.CENTER);

        mainPanel.add(mainContentPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }

    private void commandTextFieldKeyTyped(java.awt.event.KeyEvent evt) {
        if(evt.getKeyChar() == KeyEvent.VK_ENTER) {
            sendCommand();
        }
    }

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {
        sendCommand();
    }

    protected void sendCommand() {
        final String cmd = commandTextField.getText();

        if(cmd == null || cmd.trim().equalsIgnoreCase("")) {
            return;
        }

        commandTextField.setEnabled(false);

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String reply = client.sendCommand(cmd);

                    responseTextArea.append("> " + cmd + "\n");
                    responseTextArea.append(reply + "\n");

                } catch(IOException ioe) {
                    responseTextArea.append("Command Failed: " + ioe.getMessage() + "\n");
                } catch(NckException ne) {
                    responseTextArea.append("Command Failed: " + ne.getMessage() + "\n");
                } catch(TimeoutException te) {
                    responseTextArea.append("Command Failed: " + te.getMessage() + "\n");
                } catch(Exception e) {
                    responseTextArea.append("Command Failed: " + e.getMessage() + "\n");
                }  finally {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            commandTextField.setEnabled(true);
                            commandTextField.setText("");
                            commandTextField.requestFocusInWindow();

                            responseTextArea.selectAll();
                            int i = responseTextArea.getSelectionEnd();
                            responseTextArea.select(i,i);
                        }
                    });
                }
            }
        });
    }
}
