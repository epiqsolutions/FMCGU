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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
 * Panel containing a graph display for an I versus Q graph.
 * 
 * @author Epiq Solutions
 */
public class IVQPanel extends JPanel {

    private javax.swing.JLabel chartLabel;
    private org.jdesktop.swingx.JXPanel headerPanel;
    private javax.swing.JPanel labelHolderPanel;
    private javax.swing.JPanel leftSpacer;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel rightSpacer;
    private javax.swing.JPanel titleLabelLeftSpacer;
    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel topRightMainPanel;
    private javax.swing.JPanel topRightPanel;

    private JFreeChart graph = null;
    private ChartPanel chartPanel = null;
    private XYSeriesCollection dataset = null;
    private XYPlot plot = null;
    private BasicSeries ivqSeries = null;
    protected boolean firstSample = true;

    public IVQPanel() {

        initComponents();

        initGraph();

        JPanel holderPanel = new JPanel() {

            @Override
            public void paint(Graphics g) {

                Range newDomain = null; // x
                Range newRange = null;  // y

                Range currentDomainRange = plot.getDomainAxis().getRange();
                Range currentRangeRange = plot.getRangeAxis().getRange();

                if(plot.getDomainAxis().isAutoRange()) {
                    plot.getDomainAxis().setAutoRange(false);
                }

                if(plot.getRangeAxis().isAutoRange()) {
                    plot.getRangeAxis().setAutoRange(false);
                }

                Rectangle2D dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();
                double width = dataArea.getWidth();
                double height = dataArea.getHeight();

                double domainScale = 1;
                double rangeScale = 1;

                // scale the domain values up to match the pixels
                if(width > height) {
                    domainScale = width / height;
                    double extent = currentRangeRange.getUpperBound() - currentRangeRange.getLowerBound();
                    newDomain = new Range( -(extent * domainScale) / 2,
                                            (extent * domainScale) / 2);

                } else if(height > width) {
                    rangeScale = height / width;
                    double extent = currentDomainRange.getUpperBound() - currentDomainRange.getLowerBound();
                    newRange = new Range( -(extent * rangeScale) / 2,
                                           (extent * rangeScale) / 2);
                }

                if(newDomain == null) {
                    double extent = currentDomainRange.getUpperBound() - currentDomainRange.getLowerBound();
                    newDomain = new Range( -(extent) / 2,
                                            (extent) / 2);
                }

                if(newRange == null) {
                    double extent = currentRangeRange.getUpperBound() - currentRangeRange.getLowerBound();
                    newRange = new Range( -(extent) / 2,
                                           (extent) / 2);
                }

                if(newDomain != null) {
                    plot.getDomainAxis().setRange(newDomain, true, false);
                }

                if(newRange != null) {
                    plot.getRangeAxis().setRange(newRange, true, false);
                }

                Graphics2D g2 = (Graphics2D)g.create();
                super.paint(g2);

                g2.dispose();
            }
        };

        holderPanel.setLayout(new BorderLayout());
        holderPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(holderPanel, BorderLayout.CENTER);

        headerPanel.setBackgroundPainter(Common.getHeaderPainter());

        chartLabel.setUI(new BasicLabelUI());
    }

    /**
     * Clears the graph
     */
    public void clear() {
        this.ivqSeries.clear();
    }

    /**
     * Initialized the graph
     */
    private void initGraph() {
        dataset = new XYSeriesCollection();
        ivqSeries = new BasicSeries("I Versus Q");

        dataset.addSeries(ivqSeries);

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
        graph.setPadding(new RectangleInsets(-5,0,0,-0));
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
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)r;
            renderer.setBaseShapesVisible(false);
            renderer.setBaseShapesFilled(true);
            renderer.setSeriesPaint(0, Common.EPIQ_GREEN);
        }

        // X-axis setup
        plot.getDomainAxis().setAutoRange(false);
        plot.getDomainAxis().setVisible(true);
        plot.getDomainAxis().setUpperBound(Math.pow(2, 13));
        plot.getDomainAxis().setLowerBound(-Math.pow(2, 13));

        // Y-axis setup
        plot.getRangeAxis().setAutoRange(false);
        plot.getRangeAxis().setVisible(true);
        plot.getRangeAxis().setUpperBound(Math.pow(2, 13));
        plot.getRangeAxis().setLowerBound(-Math.pow(2, 13));

        chartPanel = new ChartPanel(graph,
                                         true, // properties
                                         true, // save
                                         false,
                                         true, // zoom
                                         false);

        chartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
        chartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
        chartPanel.setMinimumDrawWidth(0);
        chartPanel.setMinimumDrawHeight(0);
        chartPanel.setOpaque(false);

        // enable zoom control via mouse wheel
        chartPanel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int clicks = e.getWheelRotation();
                plot.getRangeAxis()
                    .setUpperBound(plot.getRangeAxis().getUpperBound() - (clicks * 100));
                plot.getRangeAxis()
                    .setLowerBound(plot.getRangeAxis().getLowerBound() + (clicks * 100));
            }
        });
    }

    /**
     * Sets the IQ sample to display as I versus Q
     * 
     * @param iq interleaved array
     */
    public void setIq(short [] iq) {
        this.ivqSeries.setXYData(iq);

        // allow first sample to cause graph to
        // auto-zoom
        if(firstSample) {
            plot.getDomainAxis().setAutoRange(true);
            plot.getRangeAxis().setAutoRange(true);
            firstSample = false;
        }
    }

    /**
     * This was originally generated code,
     * so it needs a little cleanup.
     */
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        headerPanel = new org.jdesktop.swingx.JXPanel();
        labelHolderPanel = new javax.swing.JPanel();
        chartLabel = new javax.swing.JLabel();
        titleLabelLeftSpacer = new javax.swing.JPanel();
        topRightPanel = new javax.swing.JPanel();
        topRightMainPanel = new javax.swing.JPanel();
        rightSpacer = new javax.swing.JPanel();
        leftSpacer = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        topPanel.setPreferredSize(new java.awt.Dimension(0, 30));
        topPanel.setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(0, 0, 0));
        headerPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(121, 123, 125)));
        headerPanel.setPreferredSize(new java.awt.Dimension(0, 30));
        headerPanel.setLayout(new java.awt.BorderLayout());

        labelHolderPanel.setOpaque(false);
        labelHolderPanel.setLayout(new java.awt.BorderLayout());

        chartLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        chartLabel.setForeground(new java.awt.Color(255, 255, 255));
        chartLabel.setText("I versus Q");
        labelHolderPanel.add(chartLabel, java.awt.BorderLayout.CENTER);

        titleLabelLeftSpacer.setOpaque(false);
        titleLabelLeftSpacer.setPreferredSize(new java.awt.Dimension(10, 0));
        labelHolderPanel.add(titleLabelLeftSpacer, java.awt.BorderLayout.WEST);

        headerPanel.add(labelHolderPanel, java.awt.BorderLayout.WEST);

        topRightPanel.setOpaque(false);
        topRightPanel.setLayout(new java.awt.BorderLayout());

        topRightMainPanel.setOpaque(false);
        topRightMainPanel.setPreferredSize(new java.awt.Dimension(40, 0));
        topRightMainPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 2));
        topRightPanel.add(topRightMainPanel, java.awt.BorderLayout.EAST);

        headerPanel.add(topRightPanel, java.awt.BorderLayout.EAST);

        topPanel.add(headerPanel, java.awt.BorderLayout.CENTER);

        rightSpacer.setPreferredSize(new java.awt.Dimension(7, 0));
        topPanel.add(rightSpacer, java.awt.BorderLayout.EAST);

        leftSpacer.setOpaque(false);
        leftSpacer.setPreferredSize(new java.awt.Dimension(44, 0));
        topPanel.add(leftSpacer, java.awt.BorderLayout.WEST);

        add(topPanel, java.awt.BorderLayout.NORTH);

        mainPanel.setLayout(new java.awt.BorderLayout());
        add(mainPanel, java.awt.BorderLayout.CENTER);
    }
}
