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

package com.epiq.bitshark;

import java.util.Scanner;

/**
 * Class to represent a frequency that generally is input from a user.
 *
 * @author Epiq Solutions
 */
public class Frequency implements Comparable<Frequency> {

    /**
     * Enumeration used to represent the SI units
     * most typically used for frequency
     */
    public enum Unit {
        HZ("Hz", 0),
        KHZ("kHz", 3),
        MHZ("MHz", 6),
        GHZ("GHz", 9),
        ;

        private final String name;
        private final int expo;

        private Unit(String name, int expo) {
            this.name = name;
            this.expo = expo;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public int getExponent() {
            return this.expo;
        }

        public long getMagnitude() {
            return (long)Math.pow(10, expo);
        }

        public String toString(long freq) {
            float base = (float)freq / getMagnitude();
            return (base == (int)base ? Integer.toString((int)base) :
                                        Float.toString(base)) + " " + toString();
        }

        public static Unit getBestFitUnit(final long freq) {

            Unit [] values = Unit.values();
            for(int i = values.length - 1; i >= 0; --i) {
                if( (float)freq / (float)values[i].getMagnitude() >= 1 ) {
                    return values[i];
                }
            }

            return HZ;
        }

        public static Unit parse(String str) {
            String oldVal = str;

            str = str.trim();
            str = str.toLowerCase();
            str = str.replaceAll("h", "");
            str = str.replaceAll("z", "");

            if(str.equalsIgnoreCase("")) {
                return HZ;
            } else if(str.equalsIgnoreCase("k")) {
                return KHZ;
            } else if(str.equalsIgnoreCase("m")) {
                return MHZ;
            } else if(str.equalsIgnoreCase("g")) {
                return GHZ;
            } else {
               throw new IllegalArgumentException("Unrecognized format: " + oldVal);
            }
        }
    };

    protected long value; // frequency in hertz
    protected Unit unit = null; // display unit
    protected String originalString = null; // original string, generally user originated

    /**
     * Default constructor
     */
    public Frequency() {

    }

    /**
     *
     * @param value Frequency in hertz
     */
    public Frequency(long value) {
        this.value = value;
        this.unit = null;
    }

    /**
     *
     * @param value Frequency
     * @param u Unit to generally be used for display
     */
    public Frequency(long value, Unit u) {
        this.value = value;
        this.unit = u;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getValue() {
        return this.value;
    }

    public Unit getUnit() {
        return this.unit;
    }

    @Override
    public int compareTo(Frequency that) {
        if(this.getValue() == that.getValue()) return 0;
        else if(this.getValue() < that.getValue()) return -1;
        else return 1;
    }

    @Override
    public String toString() {
        if(unit == null) {
            return Unit.getBestFitUnit(value).toString(value);
        } else {
            return unit.toString(value);
        }
    }

    /**
     * Parses the given line in to a frequency.  This method is intended
     * to be pretty forgiving.
     * 
     * @param line frequency input text
     * @return
     */
    public static Frequency parse(String line) {

        double value = -1;
        Unit unit = null;
        Frequency freq = null;

        try {
            Scanner s = new Scanner(line);
            s.useDelimiter("[\\p{Lower}\\p{Upper}\\p{javaWhitespace}]");

            value = s.nextDouble();

            s.reset();
            s.useDelimiter("[\\p{Digit}\\p{Punct}\\p{javaWhitespace}]");

            long multiplier = Unit.HZ.getMagnitude();
            String unitString = "";

            if(s.hasNext()) {
                unitString = s.next();
                unit = Unit.parse(unitString);
                multiplier = unit.getMagnitude();
            }

            if(s.hasNext()) {
                throw new IllegalArgumentException(
                        String.format("Invalid Frequency Format: %s", line));
            }

            freq = new Frequency((long)(value * multiplier), unit);
            freq.originalString = line;
        }
        catch(Exception e) {
            throw new IllegalArgumentException("Error parsing: " + line);
        }

        return freq;
    }

}