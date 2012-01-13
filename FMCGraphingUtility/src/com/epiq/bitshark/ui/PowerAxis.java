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

import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.ui.TextAnchor;

/**
 * Y-Axis for the frequency domain graph
 * 
 * @author Epiq Solutions
 */
public class PowerAxis extends NumberAxis {

    /**
     * Default constructor
     */
    public PowerAxis() {
        super("");
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

        for(int i = -50; i < 180; i += 10) {
            tickList.add(new NumberTick(new Double(i), 
                         String.format("%d dB",
                                       (int)Math.round(valueToDb(i))),
                         TextAnchor.CENTER_RIGHT,
                         TextAnchor.CENTER, 0));
        }

        return tickList;
    }

    /**
     * Converts a value to a 0 based value in Db. This calculation is
     * pretty simplistic and is only used to improve the power axis
     * values.
     * 
     * @param val
     * @return
     */
    public static double valueToDb(double val) {
        return val + 30;
    }
}


