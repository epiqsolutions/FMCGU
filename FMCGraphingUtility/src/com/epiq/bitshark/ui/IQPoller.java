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

/**
 *
 * @author Epiq Solutions
 */
public class IQPoller implements Runnable {

    protected boolean running = false;
    protected IQHandler handler = null;
    protected FMCUartClient client = null;

    public IQPoller(IQHandler handler) {
        this.handler = handler;
    }

    public void start(FMCUartClient client) {
        this.client = client;
        start();
    }

    private void start() {
        if(!running) {
            this.running = true;
            new Thread(this).start();
        }
    }

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {

        Common.sleep(1000);

        while(this.running) {
            try {
                handler.setIq(client.grabIQ());
            } catch(Exception e) {
                System.err.println("Error getting IQ: " + e.getMessage());
            }

            if(client == null) {
                this.running = false;
            } else {
                Common.sleep(100);
            }
        }
    }
}