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
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.ui.TextAnchor;

/**
 * Frequency / X-Axis of a frequency domain graph
 * 
 * @author Epiq Solutions
 */
public class FrequencyAxis extends NumberAxis {

    protected long center = (long)800e6;
    protected int rate = (int)1e6;

    /**
     * Default constructor
     */
    public FrequencyAxis() {
        super("");
    }

    /**
     * Updates the center frequency and sample rate for the axis
     * 
     * @param centerFrequency
     * @param sampleRate
     */
    public void setParameters(final long centerFrequency,
                              final int sampleRate) {
        this.center = centerFrequency;
        this.rate = sampleRate;

        this.fireChangeEvent();
    }

    /**
     * 
     * @param g2
     * @param state
     * @param dataArea
     * @param edge
     * @return
     */
    @Override
    public List refreshTicks(java.awt.Graphics2D g2,
                             AxisState state,
                             java.awt.geom.Rectangle2D dataArea,
                             org.jfree.ui.RectangleEdge edge) {

        List<NumberTick> tickList = new ArrayList<NumberTick>();

        // start
        tickList.add(new NumberTick(0,
                                    toMhzString(center - (rate / 2.0)),
                                    TextAnchor.CENTER_LEFT, TextAnchor.CENTER, 0));

        // half down
        tickList.add(new NumberTick((FMCUartClient.BLOCK_SIZE - 1) * .25,
                                    toMhzString(center - (rate / 4.0)),
                                    TextAnchor.CENTER, TextAnchor.CENTER, 0));

        // center
        tickList.add(new NumberTick((FMCUartClient.BLOCK_SIZE - 1) * .5,
                                    toMhzString(center),
                                    TextAnchor.CENTER, TextAnchor.CENTER, 0));

        // half up
        tickList.add(new NumberTick((FMCUartClient.BLOCK_SIZE - 1) * .75,
                                    toMhzString(center + (rate / 4.0)),
                                    TextAnchor.CENTER, TextAnchor.CENTER, 0));

        // end
        tickList.add(new NumberTick(FMCUartClient.BLOCK_SIZE - 1,
                                    toMhzString(center + (rate / 2.0)),
                                    TextAnchor.CENTER_RIGHT, TextAnchor.CENTER, 0));

        return tickList;
    }

    /**
     * For the axis labels, use this method to ensure we display
     * an appropriate resolution
     * 
     * @param frequency
     * @return
     */
    public static String toMhzString(final double frequency) {
        double freqMhz = (double)(frequency / Math.pow(10, 6));
        return String.format("%.3f MHz", freqMhz);
    }

}
