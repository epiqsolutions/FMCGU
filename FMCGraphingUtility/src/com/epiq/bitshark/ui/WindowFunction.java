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

/**
 * All functions found here were implemented based on the
 * definitions found at http://en.wikipedia.org/wiki/Window_functions
 *
 * @author Epiq Solutions
 */
public enum WindowFunction {
    RECTANGULAR("Rectangular") {
        @Override
        public float [] generate(final int n) {
            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                window[i] = 1;
            }
            return window;
        }
    },
    HANN("Hann") {
        @Override
        public float [] generate(final int n) {
            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                window[i] = (float)(0.5f * (1 - Math.cos( (2 * Math.PI * i) / (n - 1) )));
            }
            return window;
        }
    },
    HAMMING("Hamming") {
        @Override
        public float [] generate(final int n) {
            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                window[i] = (float)(0.54f - 0.46f * Math.cos( (2 * Math.PI * i) / (n - 1) ));
            }
            return window;
        }
    },
    COSINE("Cosine") {
        @Override
        public float [] generate(final int n) {
            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                window[i] = (float)Math.sin( (Math.PI * i) / (n - 1) );
            }
            return window;
        }
    },
    LANCZOS("Lanczos") { //
        @Override
        public float [] generate(final int n) {

            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                float x = ((2.0f * i) / (n - 1.0f)) - 1.0f;
                float sinc = (float)(Math.sin(Math.PI * x) / (Math.PI * x)); // normalized sinc function
                window[i] = sinc;
            }
            return window;
        }
    },
    TRIANGULAR("Triangular") { //
        @Override
        public float [] generate(final int n) {
            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                window[i] = (2.0f / (n - 1)) * ( ((n - 1) / 2.0f) - Math.abs(i - ((n - 1) / 2.0f)) ); // zero-valued end-points
            }
            return window;
        }
    },
    GAUSSIAN("Gaussian") {
        @Override
        public float [] generate(final int n) {
            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                window[i] = (float)(Math.exp(-0.5 * Math.pow(( (i-(n-1)/2) / (.4 * (n - 1) / 2) ), 2)));
            }
            return window;
        }
    },
    BARTLETT_HANN("Bartlett-Hann") {

        final float a0 = 0.62f;
        final float a1 = 0.48f;
        final float a2 = 0.38f;

        @Override
        public float [] generate(final int n) {
            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                window[i] = (float)(a0 - a1 * Math.abs(i/(n-1) - .5) - a2 * Math.cos(2 * Math.PI * i / (n - 1)));
            }
            return window;
        }
    },
    BLACKMAN("Blackman") {

        final float alpha = 0.16f;
        final float a0 = (1 - alpha) / 2.0f;
        final float a1 = .5f;
        final float a2 = alpha / 2.0f;

        @Override
        public float [] generate(final int n) {
            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                window[i] = (float)(a0 - a1 * Math.cos(2 * Math.PI * i / (n - 1)) + a2 * Math.cos(4 * Math.PI * i / (n - 1)));
            }
            return window;
        }
    },
    NUTTAL("Nuttall") {

        final float a0 = 0.355768f;
        final float a1 = 0.487396f;
        final float a2 = 0.144232f;
        final float a3 = 0.012604f;

        @Override
        public float [] generate(final int n) {
            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                window[i] = (float)(
                              a0
                            - a1 * Math.cos(2 * Math.PI * i / (n - 1))
                            + a2 * Math.cos(4 * Math.PI * i / (n - 1))
                            - a3 * Math.cos(6 * Math.PI * i / (n - 1)));
            }
            return window;
        }
    },
    BLACKMAN_HARRIS("Blackman-Harris") {

        final float a0 = 0.35875f;
        final float a1 = 0.48829f;
        final float a2 = 0.14128f;
        final float a3 = 0.01168f;

        @Override
        public float [] generate(final int n) {
            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                window[i] = (float)(
                              a0
                            - a1 * Math.cos(2 * Math.PI * i / (n - 1))
                            + a2 * Math.cos(4 * Math.PI * i / (n - 1))
                            - a3 * Math.cos(6 * Math.PI * i / (n - 1)));
            }
            return window;
        }
    },
    BLACKMAN_NUTTALL("Blackman-Nuttall") {
        
        final float a0 = 0.3635819f;
        final float a1 = 0.4891775f;
        final float a2 = 0.1365995f;
        final float a3 = 0.0106411f;

        @Override
        public float [] generate(final int n) {
            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                window[i] = (float)(
                              a0
                            - a1 * Math.cos(2 * Math.PI * i / (n - 1))
                            + a2 * Math.cos(4 * Math.PI * i / (n - 1))
                            - a3 * Math.cos(6 * Math.PI * i / (n - 1)));
            }
            return window;
        }
    },
    FLATTOP("Flat Top") {

        final float a0 = 1.0f;
        final float a1 = 1.93f;
        final float a2 = 1.29f;
        final float a3 = 0.388f;
        final float a4 = 0.032f;

        @Override
        public float [] generate(final int n) {
            float [] window = new float[n];
            for(int i = 0; i < n; ++i) {
                window[i] = (float)(
                              a0
                            - a1 * Math.cos(2 * Math.PI * i / (n - 1))
                            + a2 * Math.cos(4 * Math.PI * i / (n - 1))
                            - a3 * Math.cos(6 * Math.PI * i / (n - 1))
                            + a4 * Math.cos(8 * Math.PI * i / (n - 1)));
            }
            return window;
        }
    },

    ;

    private final String name; // window function name
    private final float [] window; // pre-generated window - assumes fixed size window lengths

    /**
     * Constructs a window with the given name, calling generate to pre-compute
     * and cache the function.
     * @param name User friendly name of the given window function
     */
    private WindowFunction(final String name) {
        this.name = name;
        this.window = this.generate(FMCUartClient.BLOCK_SIZE);
    }

    /**
     * Returns the name of the window function
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the window of the given size
     * 
     * @param n
     * @return
     */
    public abstract float [] generate(final int n);

    /**
     * Applies the window to the given array of interleaved IQ samples
     * @param iq
     * @return
     */
    public float [] apply(short [] iq) {

        float [] output = new float[iq.length];

        for(int i = 0; i < window.length; ++i) {
            output[i * 2 + 0] = iq[i * 2 + 0] * window[i];
            output[i * 2 + 1] = iq[i * 2 + 1] * window[i];
        }

        return output;
    }
}
