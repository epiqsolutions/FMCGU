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
 *  01/09/2011 : Changed package name, changed class name to FMCTestClient
 */

package com.epiq.bitshark.client.test;

import com.epiq.bitshark.client.FMCUartClient;
import com.epiq.bitshark.client.NckException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * This class is intended to be used as a "test client"
 * when validating the user interface apart from any
 * hardware.
 *
 * @author Epiq Solutions
 */
public class FMCTestClient extends FMCUartClient {

    public static final boolean SHOW_TEST_SERVER = false;

    public static final String TEST_DATA_FILENAME = "data/fmc_test_data.bin";

    protected HashMap<String, String> valueMap
            = new HashMap<String, String>();

    protected List<short[]> samples = null;
    protected int index = 0;

    protected float scaleFactor = 1;

    public FMCTestClient() throws PortInUseException,
                                UnsupportedCommOperationException,
                                IOException,
                                TimeoutException {
        super(null);

        valueMap.put(CENTER_FREQ_VAR_NAME, "800 MHz");
        valueMap.put(SAMPLE_RATE_VAR_NAME, "5 MHz");
        valueMap.put(RF_GAIN_VAR_NAME, "0 dB");
        valueMap.put(BASEBAND_GAIN_VAR_NAME, "0 dB");
        valueMap.put(CLOCK_VAR_NAME, null);
        valueMap.put(BASEBAND_FILTER_BW_VAR_NAME, "4 MHz");
        valueMap.put(VERSION_VAR_NAME, "Test Server");
        valueMap.put(IQCAL_VAR_NAME, "0x0024 0x0001");

        loadTestData();

        System.out.println("Test client created");
    }

    @Override
    public short[] grabIQ() throws IOException, 
                                   NckException,
                                   TimeoutException {

        if(samples == null) {
            System.err.println("Test Sample List is null");
            return new short[FMCUartClient.BLOCK_SIZE];
        }

        if(index >= samples.size()) {
            index = 0;
        }

        short [] sample = samples.get(index++);

        if(scaleFactor == 1) {
            return sample;
        } else {
            
            short [] scaledSample = new short[sample.length];
            for(int i = 0; i < sample.length; ++i) {
                scaledSample[i] = (short)(sample[i] * scaleFactor);
            }

            return scaledSample;
        }

    }

    protected void loadTestData() {
        try {
            FileInputStream fis = new FileInputStream(new File(TEST_DATA_FILENAME));
            InputStream inputStream = fis;

            byte [] buffer = null;
            ByteBuffer byteBuffer = null;

            buffer = new byte[BLOCK_SIZE * BYTES_PER_COMPLEX_SAMPLE];
            byteBuffer = ByteBuffer.wrap(buffer);
            byteBuffer.order(ByteOrder.BIG_ENDIAN);

            samples = new ArrayList<short[]>();

            for(int sample = 0; sample < 10; ++sample) {
                byteBuffer.clear();
                int totalRead = inputStream.read(buffer, 0, buffer.length);

                if(totalRead > -1) {
                    // put block in to a short array
                    short [] array = new short[buffer.length / 2];
                    for(int i = 0; i < array.length; ++i) {
                        array[i] = byteBuffer.getShort();
                    }

                    samples.add(array);

                }
            }

            fis.close();
        } catch(Exception e) {
            System.err.println("Error Loading Test Data: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    protected void connect() throws PortInUseException,
                                    UnsupportedCommOperationException,
                                    IOException,
                                    TimeoutException {
        this.connected = true;
    }

    @Override
    public void disconnect() throws IOException,
                                    TimeoutException {
        this.connected = false;
    }

    @Override
    public String readValue(final String name) throws IOException, 
                                                      NckException,
                                                      TimeoutException {

        System.out.println("Read Value: " + name);

        if(name.contains(IQCAL_VAR_NAME)) {
            return valueMap.get(IQCAL_VAR_NAME); // special case
        }

        return valueMap.get(name);
    }

    @Override
    public void writeValue(final String name, final String value) throws IOException, NckException, TimeoutException {
        System.out.println("Write Value: " + name + " = " + value);
        valueMap.put(name, value);
    }

    @Override
    public String sendCommand(String cmd) throws IOException, NckException, TimeoutException {

        System.out.println("sendCommand(" + cmd + ")");

        StringBuilder builder = new StringBuilder();
        builder.append("You sent \"" + cmd + "\"");
        builder.append('\n');
        builder.append("ACK");
        
        return builder.toString();
    }


}




















