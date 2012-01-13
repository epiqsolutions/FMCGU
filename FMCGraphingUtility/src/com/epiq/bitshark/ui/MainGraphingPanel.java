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

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.jdesktop.swingx.JXPanel;

/**
 * Panel that contains a time domain, I versus Q, and frequency domain
 * graph.
 * 
 * @author Epiq Solutions
 */
public class MainGraphingPanel extends JPanel
                               implements IQHandler {

    private FrequencyDomainPanel frequencyDomainPanel;
    private JPanel bottomPanel;
    private JXPanel headerPanel;
    private IVQPanel ivqPanel;
    private JPanel mainPanel;
    private JSplitPane mainSplitPane;
    private TimeDomainPanel timeDomainPanel;
    private JPanel topLeftPanel;
    private JPanel topPanel;
    private JPanel topRightPanel;
    private JSplitPane topSplitPane;

    /** Creates new form MainGraphingPanel */
    public MainGraphingPanel() {
        initComponents();
    }

    /**
     * Clear all the graphs out
     */
    public void clear() {
        this.frequencyDomainPanel.clear();
        this.timeDomainPanel.clear();
        this.ivqPanel.clear();
    }

    /**
     * Sets the IQ sample to display. This goes out to all
     * child graphs.
     * 
     * @param iq
     */
    @Override
    public void setIq(short [] iq) {
        this.timeDomainPanel.setIq(iq);
        this.ivqPanel.setIq(iq);
        this.frequencyDomainPanel.setIq(iq);
    }

    /**
     *
     */
    @Override
    public void handleIqEnded() {
        this.clear();
    }

    /**
     *
     * @param frequency
     * @param sampleRate
     */
    @Override
    public void setConfiguration(long frequency, int sampleRate) {
        this.frequencyDomainPanel.setParameters(frequency, sampleRate);
    }

    /**
     * This was originally generated code
     */
    private void initComponents() {

        headerPanel = new JXPanel();
        mainPanel = new JPanel();
        mainSplitPane = new JSplitPane();
        topPanel = new JPanel();
        topSplitPane = new JSplitPane();
        topLeftPanel = new JPanel();
        timeDomainPanel = new TimeDomainPanel();
        topRightPanel = new JPanel();
        ivqPanel = new IVQPanel();
        bottomPanel = new JPanel();
        frequencyDomainPanel = new FrequencyDomainPanel();

        setLayout(new java.awt.BorderLayout());
        add(headerPanel, java.awt.BorderLayout.NORTH);

        mainPanel.setLayout(new java.awt.BorderLayout());

        mainSplitPane.setDividerLocation(200);
        mainSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setOneTouchExpandable(true);

        topPanel.setLayout(new java.awt.BorderLayout());

        topSplitPane.setResizeWeight(0.5);
        topSplitPane.setContinuousLayout(true);
        topSplitPane.setOneTouchExpandable(true);

        topLeftPanel.setLayout(new java.awt.BorderLayout());
        topLeftPanel.add(timeDomainPanel, java.awt.BorderLayout.CENTER);

        topSplitPane.setLeftComponent(topLeftPanel);

        topRightPanel.setLayout(new java.awt.BorderLayout());
        topRightPanel.add(ivqPanel, java.awt.BorderLayout.CENTER);

        topSplitPane.setRightComponent(topRightPanel);

        topPanel.add(topSplitPane, java.awt.BorderLayout.CENTER);

        mainSplitPane.setLeftComponent(topPanel);

        bottomPanel.setLayout(new java.awt.BorderLayout());
        bottomPanel.add(frequencyDomainPanel, java.awt.BorderLayout.CENTER);

        mainSplitPane.setRightComponent(bottomPanel);

        mainPanel.add(mainSplitPane, java.awt.BorderLayout.CENTER);

        add(mainPanel, java.awt.BorderLayout.CENTER);
    }
}
