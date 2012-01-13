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
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;

/**
 * Custom marker for the frequency domain graph
 * 
 * @author Epiq Solutions
 */
public class FrequencyDomainMouseMarker extends AbstractXYAnnotation {

    /** The scaling factor. */
    private double drawScaleFactor = 1;

    /** The x-coordinate. */
    private double x = -1;

    /** The y-coordinate. */
    private double y = -1;

    /** The width. */
    private double displayWidth = 8;

    /** The height. */
    private double displayHeight = 8;

    protected boolean visible = true;

    protected Color outlineColor = Common.EPIQ_GREEN;

    /**
     * Default constructor
     */
    public FrequencyDomainMouseMarker() {

    }

    /**
     * Sets the visible state of the marker
     * @param visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Returns if the marker is visible
     * @return
     */
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Sets the position of the marker in chart space
     * @param x
     * @param y
     */
    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Draws the annotation.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param dataArea  the data area.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param rendererIndex  the renderer index.
     * @param info  if supplied, this info object will be populated with
     *              entity information.
     */
    @Override
    public void draw(Graphics2D g2, XYPlot plot, Rectangle2D dataArea,
                     ValueAxis domainAxis, ValueAxis rangeAxis,
                     int rendererIndex,
                     PlotRenderingInfo info) {

        if(!visible) {
            return;
        }

        PlotOrientation orientation = plot.getOrientation();
        RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(
                plot.getDomainAxisLocation(), orientation);
        RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(
                plot.getRangeAxisLocation(), orientation);
        float j2DX = (float) domainAxis.valueToJava2D(this.x, dataArea,
                domainEdge);
        float j2DY = (float) rangeAxis.valueToJava2D(this.y, dataArea,
                rangeEdge);
        Rectangle2D displayArea = new Rectangle2D.Double(
                j2DX - this.displayWidth / 2.0,
                j2DY - this.displayHeight / 2.0, this.displayWidth,
                this.displayHeight);

        // here we change the AffineTransform so we can draw the annotation
        // to a larger area and scale it down into the display area
        // afterwards, the original transform is restored
        AffineTransform savedTransform = g2.getTransform();
        Rectangle2D drawArea = new Rectangle2D.Double(0.0, 0.0,
                this.displayWidth * this.drawScaleFactor,
                this.displayHeight * this.drawScaleFactor);

        g2.scale(1/this.drawScaleFactor, 1/this.drawScaleFactor);
        g2.translate((j2DX - this.displayWidth / 2.0) * this.drawScaleFactor,
                (j2DY - this.displayHeight / 2.0) * this.drawScaleFactor);
        draw(g2, drawArea);
        g2.setTransform(savedTransform);
        String toolTip = getToolTipText();
        String url = getURL();
        if (toolTip != null || url != null) {
            addEntity(info, displayArea, rendererIndex, toolTip, url);
        }

    }

    /**
     * Draws the marker
     * @param g2
     * @param rect
     */
    public void draw(Graphics2D g2, Rectangle2D rect) {

        g2.setColor(outlineColor);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g2.fillOval((int)Math.round(rect.getX()),
                    (int)Math.round(rect.getY()),
                    (int)Math.round(rect.getWidth()),
                    (int)Math.round(rect.getHeight()));
    }

}