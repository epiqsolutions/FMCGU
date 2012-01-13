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
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicLabelUI;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author Epiq Solutions
 */
public class FrequencyDomainPanel extends JPanel {

    private javax.swing.JLabel alphaLabel;
    private javax.swing.JSlider alphaSlider;
    private javax.swing.JPanel chartLabelSpacerPanel;
    private javax.swing.JLabel graphLabel;
    private org.jdesktop.swingx.JXPanel headerPanel;
    private javax.swing.JPanel leftSpacerPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel rightSpacerPanel;
    private javax.swing.JPanel topLeftHeaderPanel;
    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel topRightControlPanel;
    private javax.swing.JPanel topRightHeaderPanel;
    private javax.swing.JComboBox windowComboBox;
    private javax.swing.JLabel windowLabel;

    private ChartPanel chartPanel = null;
    private BasicSeries series;
    private FrequencyAxis frequencyAxis = null;
    private PowerAxis powerAxis = null;

    private XYPlot plot = null;
    private FrequencyDomainMouseMarker markerAnnotation
            = new FrequencyDomainMouseMarker();

    private TextTitle markerTitle
            = new TextTitle("Test Annotation");
    private XYTitleAnnotation markerTextAnnotation
            = new XYTitleAnnotation(.01, .95, markerTitle,
                                              RectangleAnchor.TOP_LEFT);

    private long centerFrequency = 1;
    private int sampleRate = 1;

    private WindowFunction currentWindow = WindowFunction.RECTANGULAR;

    private double previousMarkerX = 0;
    private float [] averageFft = null;
    private float currentAlpha = 1.0f;
    private int fftSize = 1;

    private boolean markerLocked = false;

    /** Creates new form FrequencyDomainPanel */
    public FrequencyDomainPanel() {
        initComponents();

        initGraph();

        headerPanel.setBackgroundPainter(Common.getHeaderPainter());

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
                    Range newRange = new Range(-50, 70);
                    plot.getRangeAxis().setRange(newRange, true, false);
                }

                super.paint(g);
            }
        };
        holderPanel.setLayout(new BorderLayout());
        holderPanel.add(chartPanel, BorderLayout.CENTER);
        this.mainPanel.add(holderPanel, BorderLayout.CENTER);

        windowComboBox.setModel(new DefaultComboBoxModel(WindowFunction.values()));
        windowComboBox.setSelectedItem(WindowFunction.BLACKMAN_HARRIS);
        windowComboBox.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list,
                                                          Object value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus) {
                
                Component c = super.getListCellRendererComponent(list,
                                                                 value,
                                                                 index,
                                                                 isSelected,
                                                                 cellHasFocus);
                if(c instanceof JLabel && value instanceof WindowFunction) {
                    JLabel l = (JLabel)c;
                    WindowFunction cw = (WindowFunction)value;
                    l.setText(cw.getName());
                }

                return c;
            }

        });

        graphLabel.setUI(new BasicLabelUI());
        alphaLabel.setUI(new BasicLabelUI());
        windowLabel.setUI(new BasicLabelUI());
    }

    /**
     * Clears the graph
     */
    public void clear() {
        this.series.clear();
    }

    /**
     * Updates the center frequency and sample rate of the data
     * @param centerFrequency
     * @param sampleRate
     */
    public void setParameters(final long centerFrequency,
                              final int sampleRate) {
        frequencyAxis.setParameters(centerFrequency, sampleRate);

        this.centerFrequency = centerFrequency;
        this.sampleRate = sampleRate;
    }

    /**
     *
     * @param iq
     */
    public void setIq(short [] iq) {

        float [] powers = null;

        if(currentWindow != null) {
            float [] windowed = currentWindow.apply(iq);
            powers = Common.calculateFftPowers(windowed);
        } else {
            powers = Common.calculateFftPowers(WindowFunction.RECTANGULAR.apply(iq));

        }

        setFFT(powers);
    }

    /**
     *
     * @param fft
     */
    public void setFFT(float [] fft) {

        if(averageFft != null) {
            Common.calculateFftAveragePowers(fft, averageFft, currentAlpha);
        } else {
            averageFft = fft;
        }

        this.series.setData(averageFft);
        this.fftSize = fft.length;

        updateMouseMarker(); // update marker for new powers
    }

    /**
     * Initialize the graph
     */
    private void initGraph() {

        this.series = new BasicSeries("FFT");
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart graph = ChartFactory.createXYLineChart(
            null,                       // title
            "",                         // no x-axis label
            "",                         // no y-axis label
            dataset,                    // data
            PlotOrientation.VERTICAL,
            false,                      // no legend
            false,                      // no tooltips
            false                       // no URLs
        );

        graph.setBorderVisible(false);
        graph.setPadding(new RectangleInsets(-5,0,0,0));
        graph.setBackgroundPaint(null);
        graph.setBackgroundImageAlpha(0.0f);
        graph.setAntiAlias(true);
        
        plot = (XYPlot)graph.getPlot();

        powerAxis = new PowerAxis();
        plot.setRangeAxis(powerAxis);

        frequencyAxis = new FrequencyAxis();
        frequencyAxis.setTickLabelInsets(new RectangleInsets(10, 0, 0, 0));
        plot.setDomainAxis(frequencyAxis);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(false);
            renderer.setBaseShapesFilled(true);
            renderer.setSeriesPaint(0, Common.EPIQ_GREEN);
            renderer.setSeriesStroke(0, new BasicStroke(1.0f));
        }

        plot.setBackgroundAlpha(0.0f);
        plot.setBackgroundPaint(null);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setAxisOffset(new RectangleInsets(0, 0, 0, 0));

        // X-axis setup
        plot.getDomainAxis().setAutoRange(false);
        plot.getDomainAxis().setVisible(true);
        plot.getDomainAxis().setRange(0, FMCUartClient.BLOCK_SIZE - 1);
        plot.setDomainCrosshairVisible(false);
        plot.setDomainCrosshairPaint(new Color(46,46,46));

        // Y-axis setup
        plot.getRangeAxis().setAutoRange(false);
        plot.getRangeAxis().setVisible(true);
        plot.getRangeAxis().setRange(-50, 70);
        plot.setRangeCrosshairVisible(false);

        markerAnnotation.setVisible(false);
        plot.addAnnotation(markerAnnotation);
        plot.addAnnotation(markerTextAnnotation);

        this.markerTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
        this.markerTitle.setFrame(new BlockBorder(new Color(46,46,46)));
        this.markerTitle.setBackgroundPaint(new Color(200, 200, 255, 100));
        this.markerTitle.setText("");
        this.markerTitle.setPadding(5, 15, 5, 15);
        this.markerTitle.setExpandToFitSpace(false);
        this.markerTitle.setBounds(new Rectangle2D.Double(0, 0, 100, 30));

        chartPanel = new ChartPanel(graph, false);
        chartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
        chartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
        chartPanel.setMinimumDrawWidth(0);
        chartPanel.setMinimumDrawHeight(0);
        chartPanel.setMouseZoomable(true);
        chartPanel.setOpaque(false);

        chartPanel.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                markerLocked = !markerLocked;
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                updateMouseMarker(event);
            }

        });

        chartPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) {
                if(!markerLocked) {
                    markerAnnotation.setVisible(false);
                    plot.setDomainCrosshairVisible(false);
                    markerTitle.setVisible(false);
                    markerTitle.setText("");
                }
            }

        });

        chartPanel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int clicks = e.getWheelRotation();
                plot.getRangeAxis().setUpperBound(plot.getRangeAxis().getUpperBound() + clicks);
                plot.getRangeAxis().setLowerBound(plot.getRangeAxis().getLowerBound() + clicks);
            }
        });

    }

    /**
     *
     */
    protected void updateMouseMarker() {
        if(markerAnnotation.isVisible()) {
            Point screenPoint = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(screenPoint,
                                                  chartPanel);
            updateMouseMarker(screenPoint);
        }
    }

    /**
     *
     * @param event
     */
    protected void updateMouseMarker(ChartMouseEvent event) {
        updateMouseMarker(event.getTrigger().getPoint());
    }

    /**
     * 
     * @param point
     */
    protected void updateMouseMarker(Point point) {

        double xValue = 0;

        if(!markerLocked) {
            double pointerX = point.getX();

            PlotRenderingInfo renderingInfo = chartPanel.getChartRenderingInfo().getPlotInfo();
            Rectangle2D dataArea = renderingInfo.getDataArea();
            Rectangle2D plotArea = renderingInfo.getPlotArea();
            RectangleEdge domainEdge = plot.getDomainAxisEdge();

            xValue = frequencyAxis.java2DToValue(pointerX, dataArea, domainEdge);

            previousMarkerX = xValue;

        } else {
            xValue = previousMarkerX;
        }

        int bin = (int)Math.round(xValue);

        try {
            XYDataItem item = series.getDataItem(bin);
            if(item != null) {
                double yValue = item.getYValue();
                markerAnnotation.setXY(xValue, yValue);
                markerAnnotation.setVisible(true);

                plot.setDomainCrosshairValue(xValue);
                plot.setDomainCrosshairVisible(true);


                markerTitle.setText(
                        "Frequency: " + binToFrequencyStr(bin) +
                     " \nPower: " + String.format("%5.1f", PowerAxis.valueToDb(yValue)) + " dB"
                        );

                markerTitle.setVisible(true);
            } else {
                markerAnnotation.setVisible(false);
                plot.setDomainCrosshairVisible(false);
            }
        } catch(Exception e) {
            markerAnnotation.setVisible(false);
            plot.setDomainCrosshairVisible(false);
        }
    }

    /**
     *
     * @param bin
     * @return
     */
    public long binToFrequency(int bin) {
        double binWidth = (double)sampleRate / (double)fftSize;
        long freq = (long)(centerFrequency - (sampleRate / 2.0) + (binWidth * bin));
        return freq;
    }

    /**
     *
     * @param bin
     * @return
     */
    public String binToFrequencyStr(int bin) {
        double freqMhz = (double)(binToFrequency(bin) / Math.pow(10, 6));
        return String.format("%.6f MHz", freqMhz);
    }

    /**
     * This was originally generated code so it still
     * needs some cleanup.
     */
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        headerPanel = new org.jdesktop.swingx.JXPanel();
        topLeftHeaderPanel = new javax.swing.JPanel();
        graphLabel = new javax.swing.JLabel();
        chartLabelSpacerPanel = new javax.swing.JPanel();
        topRightHeaderPanel = new javax.swing.JPanel();
        topRightControlPanel = new javax.swing.JPanel();
        alphaLabel = new javax.swing.JLabel();
        alphaSlider = new javax.swing.JSlider();
        windowLabel = new javax.swing.JLabel();
        windowComboBox = new javax.swing.JComboBox();
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

        topLeftHeaderPanel.setOpaque(false);
        topLeftHeaderPanel.setLayout(new java.awt.BorderLayout());

        graphLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        graphLabel.setText("Frequency Domain");
        graphLabel.setForeground(new java.awt.Color(255, 255, 255));
        topLeftHeaderPanel.add(graphLabel, java.awt.BorderLayout.CENTER);

        chartLabelSpacerPanel.setOpaque(false);
        chartLabelSpacerPanel.setPreferredSize(new java.awt.Dimension(10, 0));
        topLeftHeaderPanel.add(chartLabelSpacerPanel, java.awt.BorderLayout.WEST);

        headerPanel.add(topLeftHeaderPanel, java.awt.BorderLayout.WEST);

        topRightHeaderPanel.setOpaque(false);
        topRightHeaderPanel.setLayout(new java.awt.BorderLayout());

        topRightControlPanel.setOpaque(false);
        topRightControlPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 2));

        alphaLabel.setText("Alpha:");
        alphaLabel.setForeground(new java.awt.Color(255, 255, 255));
        topRightControlPanel.add(alphaLabel);

        alphaSlider.setMaximum(10000);
        alphaSlider.setOpaque(false);
        alphaSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                alphaSliderStateChanged(evt);
            }
        });
        topRightControlPanel.add(alphaSlider);

        windowLabel.setText("Window:");
        windowLabel.setForeground(new java.awt.Color(255, 255, 255));
        topRightControlPanel.add(windowLabel);

        windowComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        windowComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                windowComboBoxActionPerformed(evt);
            }
        });
        topRightControlPanel.add(windowComboBox);

        topRightHeaderPanel.add(topRightControlPanel, java.awt.BorderLayout.EAST);

        headerPanel.add(topRightHeaderPanel, java.awt.BorderLayout.EAST);

        topPanel.add(headerPanel, java.awt.BorderLayout.CENTER);

        rightSpacerPanel.setPreferredSize(new java.awt.Dimension(7, 0));
        topPanel.add(rightSpacerPanel, java.awt.BorderLayout.EAST);

        leftSpacerPanel.setOpaque(false);
        leftSpacerPanel.setPreferredSize(new java.awt.Dimension(44, 0));
        topPanel.add(leftSpacerPanel, java.awt.BorderLayout.WEST);

        add(topPanel, java.awt.BorderLayout.NORTH);

        mainPanel.setLayout(new java.awt.BorderLayout());
        add(mainPanel, java.awt.BorderLayout.CENTER);
    }

    private void windowComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        currentWindow = (WindowFunction)windowComboBox.getSelectedItem();
    }

    private void alphaSliderStateChanged(javax.swing.event.ChangeEvent evt) {
        currentAlpha = 1.0f - (float)((float)alphaSlider.getValue() / (float)alphaSlider.getMaximum());
    }
}
