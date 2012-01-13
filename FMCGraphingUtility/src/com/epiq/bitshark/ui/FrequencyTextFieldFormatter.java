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
 *  01/05/2012 : Added methods to set the lower and upper bounds
 */

package com.epiq.bitshark.ui;

import com.epiq.bitshark.Frequency;
import java.text.ParseException;
import javax.swing.JFormattedTextField;

/**
 * Formatter used for frequency input that has a lower bound, upper bound, and
 * has a specified resolution.
 * 
 * @author Epiq Solutions
 */
public class FrequencyTextFieldFormatter extends JFormattedTextField.AbstractFormatter {

    protected long lowerBound;
    protected long upperBound;
    protected long resolution;

    /**
     * Default constructor.  Creates a formatter that allows
     * 0 to Long.MAX_VALUE with resolution of 1 Hz
     */
    public FrequencyTextFieldFormatter() {
        this(0, Long.MAX_VALUE);
    }

    /**
     * Creates a formatter with the given bounds, allowing for 1 Hz resolution.
     *
     * @param lowerBound lower bound in Hz
     * @param upperBound upper bound in Hz
     */
    public FrequencyTextFieldFormatter(long lowerBound, long upperBound) {
        this(lowerBound, upperBound, 1);
    }

    /**
     * Create a formatter with the given bounds and resolution.
     * 
     * @param lowerBound lower bound in Hz
     * @param upperBound upper bound in Hz
     * @param resolution resolution (must be a power of 10)
     */
    public FrequencyTextFieldFormatter(final long lowerBound,
                                       final long upperBound,
                                       final long resolution) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.resolution = resolution;

        double l = Math.log10(resolution);

        if(l != (int)l) {
            throw new IllegalArgumentException("Resolution must be a power of 10");
        }
    }
    
    /**
     * Set the frequency lower bound
     * @param lowerBound 
     */
    public void setLowerBound(final long lowerBound) {
        this.lowerBound = lowerBound;
    }
    
    /**
     * Get the frequency lower bound
     * @return 
     */
    public long getLowerBound() {
        return this.lowerBound;
    }

    /**
     * Set the frequency upper bound
     * @param upperBound 
     */
    public void setUpperBound(final long upperBound) {
        this.upperBound = upperBound;
    }
    
    /**
     * Get the frequency upper bound
     * @return 
     */
    public long getUpperBound() {
        return this.upperBound;
    }
    
    /**
     * Takes the given string, parses the frequency, and truncates it
     * based on the allowed resolution;
     * 
     * @param text
     * @return
     * @throws ParseException
     */
    @Override
    public Object stringToValue(String text) throws ParseException {

        Frequency freq = null;

        try {
            
            freq = Frequency.parse(text);

            long truncatedValue = (freq.getValue() / resolution) * resolution;

            freq.setValue(truncatedValue);

        } catch(IllegalArgumentException iae) {
            throw new ParseException(iae.getMessage(), 0);
        }

        if(freq.getValue() < this.lowerBound || freq.getValue() > this.upperBound) {
            throw new ParseException("Invalid range", 0);
        }

        return freq;
    }

    /**
     * Returns a frequency string from the given Frequency.  If the value type
     * is not of type Frequency, then an empty string will be returned.
     * 
     * @param value
     * @return
     * @throws ParseException
     */
    @Override
    public String valueToString(Object value) throws ParseException {

        if(value == null) return "";
        
        if(value instanceof Frequency) {
            return ((Frequency)value).toString();
        } else {
            return value.toString();
        }
    }

    /**
     *
     * @param valid
     */
    @Override
    protected void setEditValid(boolean valid) {
        super.setEditValid(valid);
    }

    /**
     * 
     */
    @Override
    protected void invalidEdit() {
        super.invalidEdit();
    }


}
