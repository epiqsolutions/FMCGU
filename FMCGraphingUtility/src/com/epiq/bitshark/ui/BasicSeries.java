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

import javax.swing.SwingUtilities;
import org.jfree.data.xy.XYSeries;

/**
 * An XYSeries that is targeted for maintaining data for the FMC-1RX graphing
 * utility.  This data series is ideal for either time domain, I versus Q, or
 * frequency domain data sets.
 * 
 * @author Epiq Solutions
 */
public class BasicSeries extends XYSeries {

    /**
     * Constructs a new series with the given name
     *
     * @param name Name of the series
     */
    public BasicSeries(final String name) {
        super(name,
              false,  // avoid sorted insert
              true);  // avoid search
    }

    /**
     * Sets the data for this series to be array.length of items where
     * each item is the (x,y) pair (i, array[i]).
     * <br>
     * This is primarily intended to be used for time domain series
     *
     * @param array Array of y-values
     */
    public void setData(final short [] array) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                BasicSeries.this.data.clear();

                for(int i = 0; i < array.length; ++i) {
                    BasicSeries.this.add(i, array[i], false);
                }

                BasicSeries.this.fireSeriesChanged();
            }
        };

        if(SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    /**
     * Sets the data for this series to be array.length of items where
     * each item is the (x,y) pair (i, array[i]).
     * <br>
     * This is primarily intended to be used for frequency domain series
     *
     * @param array Array of y-values
     */
    public void setData(final float [] array) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                BasicSeries.this.data.clear();

                for(int i = 0; i < array.length; ++i) {
                    BasicSeries.this.add(i, array[i], false);
                }

                BasicSeries.this.fireSeriesChanged();
            }
        };

        if(SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

    /**
     * Sets the data for this series to be array.length/2 of items where
     * each item is the (x,y) pair (array[i], array[i+1]).
     * <br>
     * This is primarily intended to be used for I versus Q series
     *
     * @param array Array of interleaved (x,y) values
     */
    public void setXYData(final short [] array) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                BasicSeries.this.data.clear();

                for(int i = 0; i < array.length / 2; ++i) {
                    BasicSeries.this.add(array[i * 2], array[i * 2 + 1], false);
                }

                BasicSeries.this.fireSeriesChanged();
            }
        };

        if(SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }

}
