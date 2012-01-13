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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;

/**
 *
 * @author Epiq Solutions
 */
public class Common {

    public static final Color EPIQ_GREEN = new Color(0, 153, 0);

    /**
     * This class is used as a factory class so it should
     * not be instantiated.
     */
    private Common() {

    }

    /**
     * Sleep without throwing an exception
     * @param ms
     */
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch(Exception e) { }
    }

    /**
     * Returns the default painter to be used for headers
     * 
     * @return
     */
    public static Painter getHeaderPainter() {
        GradientPaint gradient
                = new GradientPaint(new Point2D.Float(0, 0), new Color(77,77,77),
                                    new Point2D.Float(0, 1), new Color(46,46,46));
        MattePainter painter = new MattePainter(gradient, true);
        return painter;
    }

    /**
     * Performs a Fourier transform (a fast one at that) on the given
     * interleaved IQ data
     * 
     * @param iq interleaved IQ (this data is left as is)
     *
     * @return transformation
     */
    public static float [] calculateFft(final float [] iq) {

        final float [] transform = new float[iq.length];

        int id;
        int localN;
        int ti, tj;
        float wtemp, Wjk_r, Wjk_i, Wj_r, Wj_i;
        float theta, tempr, tempi;
        int nby2;
        int bitRn;
        int i, j, k, m;

        final int nm = (int)(Math.log(transform.length / 2) / Math.log(2.0));
        final int length = 1 << nm;
        final int n = length;

        for(i = 0; i < iq.length; ++i) {
            transform[i] = iq[i];
        }

        bitRn = transform.length / 2;
        j = 1;

        for (i = 1; i < bitRn; ++i) {
            if (i < j) {
                ti = i - 1;
                tj = j - 1;
                tempr = transform[tj*2];
                transform[tj*2] = transform[ti*2];
                transform[ti*2] = tempr;
                tempr = transform[tj*2 + 1];
                transform[tj*2 + 1] = transform[ti*2 + 1];
                transform[ti*2 + 1] = tempr;
            }

            k = bitRn / 2;

            while (k >= 1 && k < j) {
                j = j - k;
                k = k / 2;
            }

            j = j + k;
        }

        for(m = 1; m <= nm; ++m) {
           localN = 1 << m;

           nby2 = localN / 2;
           Wjk_r = 1;
           Wjk_i = 0;

           theta = (float)(Math.PI/nby2);

           Wj_r =  (float)Math.cos(theta);
           Wj_i = (float)Math.sin(theta);

            for (j = 0; j < nby2; ++j) {
                for (k = j; k < n; k += localN) {
                    id = k + nby2;
                    tempr = Wjk_r * transform[id * 2] - Wjk_i * transform[id * 2 + 1];
                    tempi = Wjk_r * transform[id * 2 + 1] + Wjk_i * transform[id * 2];

                    transform[id * 2] = transform[k * 2] - tempr;
                    transform[id * 2 + 1] = transform[k * 2 + 1] - tempi;
                    transform[k * 2] += tempr;
                    transform[k * 2 + 1] += tempi;
                }

                wtemp = Wjk_r;

                Wjk_r = Wj_r * Wjk_r - Wj_i * Wjk_i;
                Wjk_i = Wj_r * Wjk_i + Wj_i * wtemp;
            }
        }

        // normalize
        for (j = 0; j < transform.length; ++j) {
            transform[j] /= (float)n;
        }

        return transform;
    }

    /**
     * Takes the given IQ data, performs a FFT, and then returns
     * an array of powers of each bin for the resulting FFT.
     *
     * @param iq interleaved IQ
     * @return Array of computed powers
     */
    public static float [] calculateFftPowers(final float [] iq) {

        final float [] fft = calculateFft(iq);

        final float [] bins = new float[iq.length / 2];

        int binIndex = 0;
        int i;
        float iSquared;
        float qSquared;

        // first half
        for(i = (bins.length / 2) - 1; i >= 0; --i, ++binIndex) {
            iSquared = (fft[2 * i + 0] * fft[2 * i + 0]);
            qSquared = (fft[2 * i + 1] * fft[2 * i + 1]);
            bins[binIndex] = (float)(10 * Math.log10(iSquared + qSquared));
        }

        // second half
        for(i = bins.length - 1; i >= bins.length / 2; --i, ++binIndex) {
            iSquared = (fft[2 * i + 0] * fft[2 * i + 0]);
            qSquared = (fft[2 * i + 1] * fft[2 * i + 1]);
            bins[binIndex] = (float)(10 * Math.log10(iSquared + qSquared));
        }

        return bins;
    }

    /**
     * Applies a leaky integrator to the existing average, using the current
     * samples and given alpha value.
     * 
     * @param instant
     * @param average
     * @param alpha
     */
    public static void calculateFftAveragePowers(final float [] instant,
                                                 final float [] average,
                                                 final float alpha) {
        for(int i = 0; i < average.length; ++i) {
            average[i] = (instant[i] * alpha) + (average[i] * (1-alpha));
        }
    }

}
