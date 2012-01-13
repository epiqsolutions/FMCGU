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

import com.epiq.bitshark.Common;
import com.epiq.bitshark.client.FMCUartClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicLabelUI;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 * Panel containing a time domain graph.
 * 
 * @author Epiq Solutions
 */
public class TimeDomainPanel extends JPanel {
    
    public static final Color I_COLOR = Common.EPIQ_GREEN;
    public static final Color Q_COLOR = new Color(221, 102, 51, 255);

    private javax.swing.JPanel chartLabelHolderPanel;
    private org.jdesktop.swingx.JXPanel headerPanel;
    private javax.swing.JPanel leftSpacerPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel rightSpacerPanel;
    private javax.swing.JCheckBox showICheckBox;
    private javax.swing.JCheckBox showQCheckBox;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel titleLabelLeftSpacerPanel;
    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel topRightMainPanel;
    private javax.swing.JPanel topRightPanel;

    private JFreeChart graph = null;
    private ChartPanel chartPanel = null;
    private XYSeriesCollection dataset = null;
    private XYPlot plot = null;

    private BasicSeries realSeries;
    private BasicSeries imaginarySeries;

    public TimeDomainPanel() {
        initComponents();

        initGraph();

        JPanel holderPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {

                if(plot.getDomainAxis().isAutoRange()) {
                    plot.getDomainAxis().setAutoRange(false);
                    Range newDomain = new Range(0, FMCUartClient.BLOCK_SIZE - 1);
                    plot.getDomainAxis().setRange(newDomain, true, false);
                }

                if(plot.getRangeAxis().isAutoRange()) {
                    plot.getRangeAxis().setAutoRange(false);
                }

                // ensureAxisMargin( ) ?

                Range newRange = null;
                Range currentRangeRange = plot.getRangeAxis().getRange();
                double extent = currentRangeRange.getUpperBound() - currentRangeRange.getLowerBound();
                newRange = new Range( -(extent) / 2,
                                       (extent) / 2);
                plot.getRangeAxis().setRange(newRange, true, false);

                super.paint(g);
            }
        };
        holderPanel.setLayout(new BorderLayout());
        holderPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(holderPanel, BorderLayout.CENTER);

        headerPanel.setBackgroundPainter(Common.getHeaderPainter());

        titleLabel.setUI(new BasicLabelUI());

        showICheckBox.setFocusPainted(false);
        showQCheckBox.setFocusPainted(false);
    }

    /**
     * Clears the graph
     */
    public void clear() {
        this.realSeries.clear();
        this.imaginarySeries.clear();
    }

    /**
     * Initializes the graph
     */
    private void initGraph() {
        dataset = new XYSeriesCollection();
      
        realSeries = new BasicSeries("Real");
        imaginarySeries = new BasicSeries("Imaginary");

        dataset.addSeries(realSeries);
        dataset.addSeries(imaginarySeries);

        graph = ChartFactory.createXYLineChart(
            null,                       // no title
            "",                         // no x-axis label
            "",                         // no y-axis label
            dataset,                    // data
            PlotOrientation.VERTICAL,
            false,                      // no legend
            false,                      // no tooltips
            false                       // no URLs
        );

        graph.setBorderVisible(false);
        graph.setPadding(new RectangleInsets(-5,-5,0,-7));
        graph.setBackgroundPaint(null);
        graph.setAntiAlias(true);

        plot = (XYPlot)graph.getPlot();
        plot.setBackgroundAlpha(0.0f);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(false);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(false);
            renderer.setBaseShapesFilled(true);
            renderer.setSeriesPaint(0, I_COLOR);
            renderer.setSeriesPaint(1, Q_COLOR);
        }

        // X-axis setup
        plot.getDomainAxis().setAutoRange(false);
        plot.getDomainAxis().setVisible(true);
        plot.getDomainAxis().setRange(0, FMCUartClient.BLOCK_SIZE - 1);
        
        // Y-axis setup
        plot.getRangeAxis().setAutoRange(false);
        plot.getRangeAxis().setVisible(true);
        plot.getRangeAxis().setUpperBound(Math.pow(2, 13));
        plot.getRangeAxis().setLowerBound(-Math.pow(2, 13));
       
        // setup chart panel
        chartPanel = new ChartPanel(graph, false);
        chartPanel.setMouseZoomable(false);
        chartPanel.setOpaque(false);
        chartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
        chartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
        chartPanel.setMinimumDrawWidth(0);
        chartPanel.setMinimumDrawHeight(0);
        chartPanel.setDomainZoomable(true);
        // mouse wheel zooming
        chartPanel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int clicks = e.getWheelRotation();
                plot.getRangeAxis().setUpperBound(plot.getRangeAxis().getUpperBound() - (clicks * 100));
                plot.getRangeAxis().setLowerBound(plot.getRangeAxis().getLowerBound() + (clicks * 100));
            }
        });
    }

    /**
     *
     * @param iq
     */
    public void setIq(short [] iq) {
        short [] real = new short[iq.length / 2];
        short [] imag = new short[iq.length / 2];

        for(int i = 0; i < iq.length / 2; ++i) {
            real[i] = iq[i * 2 + 0];
            imag[i] = iq[i * 2 + 1];
        }

        this.realSeries.setData(real);
        this.imaginarySeries.setData(imag);
    }

    /**
     * This was originally generated so it needs to be cleaned up.
     */
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        headerPanel = new org.jdesktop.swingx.JXPanel();
        chartLabelHolderPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        titleLabelLeftSpacerPanel = new javax.swing.JPanel();
        topRightPanel = new javax.swing.JPanel();
        topRightMainPanel = new javax.swing.JPanel();
        showICheckBox = new javax.swing.JCheckBox();
        showQCheckBox = new javax.swing.JCheckBox();
        rightSpacerPanel = new javax.swing.JPanel();
        leftSpacerPanel = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        topPanel.setPreferredSize(new java.awt.Dimension(0, 30));
        topPanel.setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(0, 0, 0));
        headerPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(121, 123, 125)));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 30));
        headerPanel.setLayout(new java.awt.BorderLayout());

        chartLabelHolderPanel.setOpaque(false);
        chartLabelHolderPanel.setLayout(new java.awt.BorderLayout());

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        titleLabel.setText("Time Domain");
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        chartLabelHolderPanel.add(titleLabel, java.awt.BorderLayout.CENTER);

        titleLabelLeftSpacerPanel.setOpaque(false);
        titleLabelLeftSpacerPanel.setPreferredSize(new java.awt.Dimension(10, 0));
        chartLabelHolderPanel.add(titleLabelLeftSpacerPanel, java.awt.BorderLayout.WEST);

        headerPanel.add(chartLabelHolderPanel, java.awt.BorderLayout.WEST);

        topRightPanel.setOpaque(false);
        topRightPanel.setLayout(new java.awt.BorderLayout());

        topRightMainPanel.setOpaque(false);
        topRightMainPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 2));

        showICheckBox.setFont(new java.awt.Font("Tahoma", 1, 11));
        showICheckBox.setForeground(new java.awt.Color(255, 255, 255));
        showICheckBox.setSelected(true);
        showICheckBox.setText("I");
        showICheckBox.setOpaque(false);
        showICheckBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showICheckBoxActionPerformed(evt);
            }
        });
        topRightMainPanel.add(showICheckBox);

        showQCheckBox.setFont(new java.awt.Font("Tahoma", 1, 11));
        showQCheckBox.setForeground(new java.awt.Color(255, 255, 255));
        showQCheckBox.setSelected(true);
        showQCheckBox.setText("Q");
        showQCheckBox.setOpaque(false);
        showQCheckBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showQCheckBoxActionPerformed(evt);
            }
        });
        topRightMainPanel.add(showQCheckBox);

        topRightPanel.add(topRightMainPanel, java.awt.BorderLayout.EAST);

        headerPanel.add(topRightPanel, java.awt.BorderLayout.EAST);

        topPanel.add(headerPanel, java.awt.BorderLayout.CENTER);

        rightSpacerPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        topPanel.add(rightSpacerPanel, java.awt.BorderLayout.EAST);

        leftSpacerPanel.setOpaque(false);
        leftSpacerPanel.setPreferredSize(new java.awt.Dimension(44, 0));
        topPanel.add(leftSpacerPanel, java.awt.BorderLayout.WEST);

        add(topPanel, java.awt.BorderLayout.NORTH);

        mainPanel.setLayout(new java.awt.BorderLayout());
        add(mainPanel, java.awt.BorderLayout.CENTER);
    }

    private void showICheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        plot.getRenderer().setSeriesVisible(0, showICheckBox.isSelected());
    }

    private void showQCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        plot.getRenderer().setSeriesVisible(1, showQCheckBox.isSelected());
    }
}
