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
 *  01/09/2011 : Changed package name, renamed class to FMCGUWindow
 */

package com.epiq.bitshark.ui;

import com.epiq.bitshark.client.ConnectionRequester;
import com.epiq.bitshark.client.FMCUartClient;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Epiq Solutions
 */
public class FMCGUWindow extends JFrame
                               implements ConnectionRequester
{

    public static final String VERSION = FMCGUWindow.class.getPackage().getImplementationVersion() != null ?
                                            FMCGUWindow.class.getPackage().getImplementationVersion() :
                                            "Development";

    public static final String TITLE = "FMC-1RX Graphing Utility - " + VERSION;

    private ExecutorService commandPool = Executors.newSingleThreadExecutor();

    public FMCGUWindow() {
        
        initComponents();

        handleDisconnected();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setIconImage(new ImageIcon(this.getClass().getResource("icon.png")).getImage());

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(Math.max(dim.getWidth() / 1.5, 955));
        int height = (int)(Math.max(dim.getHeight() / 1.5, 550));
        this.setSize(width, height);
        this.setLocationRelativeTo(null); // center the window
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        MainGraphingPanel graphingPanel = new MainGraphingPanel();
        mainPanel.add(graphingPanel, BorderLayout.CENTER);

        JPanel controlPanelHolder = new JPanel();
        controlPanelHolder.setLayout(new BorderLayout());

        JPanel leftSpacerPanel = new JPanel();
        leftSpacerPanel.setPreferredSize(new Dimension(5, 0));
        controlPanelHolder.add(leftSpacerPanel, BorderLayout.WEST);

        JPanel topSpacerPanel = new JPanel();
        topSpacerPanel.setPreferredSize(new Dimension(0, 10));
        controlPanelHolder.add(topSpacerPanel, BorderLayout.NORTH);

        JPanel bottomSpacerPanel = new JPanel();
        bottomSpacerPanel.setPreferredSize(new Dimension(0, 10));
        controlPanelHolder.add(bottomSpacerPanel, BorderLayout.SOUTH);

        ControlPanel controlPanel = new ControlPanel(this, graphingPanel);
        controlPanel.setPreferredSize(new Dimension(0, 55));
        controlPanelHolder.add(controlPanel, BorderLayout.CENTER);

        mainPanel.add(controlPanelHolder, BorderLayout.SOUTH);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void handleConnected(final FMCUartClient client) {
        commandPool.submit(new Runnable() {
            @Override
            public void run() {
                String newTitle = TITLE + " (Connected)";
                try {
                    newTitle = TITLE + " (" + client.getVersionString() + ")";
                } catch(Exception e) {

                }

                final String title = newTitle;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setTitle(title);
                    }
                });
            }
        });
    }

    @Override
    public void handleDisconnected() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                FMCGUWindow.this.setTitle(TITLE + " (Disconnected)");
            }
        };

        if(SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                boolean mac = false;

                try {
                    mac = System.getProperty("os.name", "").toLowerCase().indexOf("mac") != -1;
                } catch(Exception e) {

                }

                if(!mac) {
                    JFrame.setDefaultLookAndFeelDecorated(true);
                }

                try {
                    UIManager.setLookAndFeel(new org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel());
                } catch (Exception e) {
                  System.out.println("Substance Graphite failed to initialize");
                }

                new FMCGUWindow().setVisible(true);
            }
        });
    }


}
