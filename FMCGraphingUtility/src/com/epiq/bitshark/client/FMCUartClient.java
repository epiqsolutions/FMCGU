/*  +--------------------------+
 *  | FMC-1RX Graphing Utility |
 *  +--------------------------+
 *
 *  Copyright (C) 2011 Epiq Solutions
 *                     epiqsolutions.com
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
 *  01/09/2011 : Changed package name, renamed class to FMCUartClient
 *  01/19/2011 : Added check that inputStream EOS indicator is only valid when
 *               no longer connected
 *  12/12/2011 : Added startup commands for version >= 1.14 and updated comments
 */

package com.epiq.bitshark.client;

import com.epiq.bitshark.client.test.FMCTestClient;
import com.epiq.bitshark.Common;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.RXTXHack;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Class that can be used to interface with the FMC-1RX board over
 * the UART/Serial interface.
 * <br>
 * Interface specification for Bitshark FMC-1RX is based on BURX UART interface 
 * which can be found at:
 * http://www.epiqsolutions.com/twiki/bin/view/Main/BurxUartInterfaceSpecification
 *
 *
 * @author Epiq Solutions
 */
public class FMCUartClient {

    public static final boolean DEBUG = true; // controls debug prints

    public static final int BAUD_RATE = 115200;
    public static final int DATABITS = SerialPort.DATABITS_8;
    public static final int STOPBITS = SerialPort.STOPBITS_1;
    public static final int PARITY = SerialPort.PARITY_NONE;
    public static final int FLOWCONTROL = SerialPort.FLOWCONTROL_NONE;

    public static final int PORT_OPEN_TIMEOUT = 5000;      //  5 seconds
    public static final long CONNECT_TIMEOUT = 10000;      // 10 seconds
    public static final long DISCONNECT_TIMEOUT = 10000;   // 10 seconds
    public static final long COMMAND_TIMEOUT = 30000;      // 30 seconds
    public static final long COMMAND_LINE_TIMEOUT = 30000; // 30 seconds

    // 'F' indicates that the destination of the command to follow is the FPGA host board
    public static final String FPGA_CMD_CLASS_NAME = "F";
     // 'B' indicates that the destination of the command to follow is the Bitshark FMC-1RX board
    public static final String BITSHARK_CMD_CLASS_NAME = "B";

    public static final String CENTER_FREQ_VAR_NAME = "freq";
    public static final String SAMPLE_RATE_VAR_NAME = "adclk";
    public static final String RF_GAIN_VAR_NAME = "rfgain";
    public static final String BASEBAND_GAIN_VAR_NAME = "bbgain";
    public static final String CLOCK_VAR_NAME = "clock";
    public static final String BASEBAND_FILTER_BW_VAR_NAME = "chanbw";
    public static final String VERSION_VAR_NAME = "version";
    public static final String IQCAL_VAR_NAME = "iqcal";

    public static final String POS_ACK = "ACK";
    public static final String NEG_ACK = "NCK";

    public static final String GO_COMMAND
            = String.format("%s g", FPGA_CMD_CLASS_NAME);

    public static final String CLEAR_CAL_COMMAND = "F W 09 00000040";

    /**
     * 1K sample block size defined by spec
     */
    public static final int BLOCK_SIZE = 1024;

    /**
     * 16-bits I / 16-bits Q
     */
    public static final int BYTES_PER_COMPLEX_SAMPLE = 4;

    /**
     * Commands to send out at startup 
     * - for version 1.00 and 1.11 (board rev B)
     */
    public static final String [] STARTUP_COMMANDS_1_00 = new String[] {
        "F W 00 00000080", // reset ADC
        "F W 02 00000001", // setup ADC for two-lane mode, 14bit serialization
        "F W 01 0000002C", // 2s compliment, set LTC2174 ADC 3 & 4 in nap mode
        CLEAR_CAL_COMMAND, // clear out calibration
    };
    
    /**
     * Commands to send out at startup 
     * - for version >= 1.14 (board rev C)
     */    
    public static final String [] STARTUP_COMMANDS_1_14 = new String[] {
        "F W 00 00000080", // reset ADC
        "F W 02 00000001", // setup ADC for two-lane mode, 14bit serialization
        "F W 01 00000020", // 2s compliment (changed for rev c since LTC2267 has only 2 ADCs)
        CLEAR_CAL_COMMAND, // clear out calibration
    };
    

    /**
     * In order to ensure that all communication over the serial port
     * (i.e. Connecting to the port, disconnecting from the port, writing
     * data to the port, and reading data from the port) is performed
     * synchronously, this executor is used to ensure all operations can
     * take place on a single thread.  All the methods that interact with
     * the serial port queue their specific operations on the thread provided
     * by the ExecutorService so that no operations are performed in
     * parallel or out of order.
     */
    private static final ExecutorService executor
            = Executors.newSingleThreadExecutor();

    private SerialPort serialPort;
    private CommPortIdentifier portId = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    
    private byte [] buffer = null;
    private ByteBuffer byteBuffer = null;

    private boolean useIqcal = true;

    protected boolean connected = false;

    /**
     * This constructor should only be called by this class
     * or a child class
     */
    protected FMCUartClient() {
        buffer = new byte[BLOCK_SIZE * BYTES_PER_COMPLEX_SAMPLE];
        byteBuffer = ByteBuffer.wrap(buffer);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
    }

    /**
     * Creates a client and attempts a connection to the given portId
     * 
     * @param portId
     * 
     * @throws PortInUseException
     * @throws UnsupportedCommOperationException
     * @throws IOException
     * @throws TimeoutException
     */
    public FMCUartClient(CommPortIdentifier portId) throws PortInUseException,
                                                            UnsupportedCommOperationException,
                                                            IOException,
                                                            TimeoutException {
        this();
        
        this.portId = portId;

        if(portId != null) {
            log("Client created: " + this.portId.getName() + " (current owner: " + this.portId.getCurrentOwner() + ")");
        } else {
            log("Client created: TEST CLIENT"); // assume null identifier means test client
        }

        connect();
    }

    /**
     * Helper method for listing all possible serial ports
     *
     * @return list of possible serial ports
     */
    public static CommPortIdentifier [] getCommPortList() {

        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        CommPortIdentifier portId = null;

        List<CommPortIdentifier> portIdList = new ArrayList<CommPortIdentifier>();

        if(FMCTestClient.SHOW_TEST_SERVER) {
            portIdList.add(null);
        }

        while(portEnum.hasMoreElements()) {
            portId = (CommPortIdentifier)portEnum.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portIdList.add(portId);
            }
        }

        return portIdList.toArray(new CommPortIdentifier[0]);
    }

    /**
     * Connects to the port.  This is intended to be called by the constructor.
     *
     * @throws PortInUseException
     * @throws UnsupportedCommOperationException
     * @throws IOException
     * @throws TimeoutException
     */
    protected void connect() throws PortInUseException,
                                    UnsupportedCommOperationException,
                                    IOException,
                                    TimeoutException {

        Callable<Boolean> c = new Callable<Boolean>() {
            @Override
            public Boolean call() throws PortInUseException,
                                         UnsupportedCommOperationException,
                                         IOException {

                log("Attempting to connect to serial port...");

                // open the serial port
                serialPort = (SerialPort)portId.open(this.getClass().getName(),
                                                     PORT_OPEN_TIMEOUT);

                // setup serial port to specification
                serialPort.setSerialPortParams(BAUD_RATE,
                        DATABITS,
                        STOPBITS,
                        PARITY
                        );

                // configure flow control
                serialPort.setFlowControlMode(FLOWCONTROL);

                // 2x just for good measure?
                serialPort.setInputBufferSize(BLOCK_SIZE * BYTES_PER_COMPLEX_SAMPLE * 2); 

                // setup input stream
                inputStream = serialPort.getInputStream();

                // setup output stream
                outputStream = serialPort.getOutputStream();

                Common.sleep(1000); // probably not needed, but does not hurt

                // we should now be connected
                FMCUartClient.this.connected = true;

                log("Serial port connected OK (" + portId.getName() + ")");

                log("Sending startup commands...");
                
                // get the required startup commands for the host hardware
                String [] startupCommands = getStartupCommands();
                try {
                    for(String c : startupCommands) {
                        try {
                            // do not attempt to put these commands on the
                            // thread pool because we are already on it
                            sendCommand(c, false);
                        } catch(Exception e) {
                            error("Startup command failed (" + c + "): " + e.getMessage());
                        }
                    }                    
                } catch(Exception e) {
                    error("Failed to send startup commands");
                }

                log("Finished sending startup commands");

                return true;
            }
        };

        Future<Boolean> ret = executor.submit(c);

        try {
            // should always return true (unless an exception is thrown)
            Boolean success
                    = ret.get(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch(InterruptedException ie) {

            error("Unable to connect to serial port: " + ie.getMessage());

            throw new IOException("Unable to open serial port", ie);
        } catch(ExecutionException ee) {

            error("Unable to connect to serial port: " + ee.getCause() != null ? ee.getCause().getMessage() : ee.getMessage());

            if(ee.getCause() instanceof PortInUseException) {
                throw (PortInUseException)ee.getCause();
            } else if (ee.getCause() instanceof UnsupportedCommOperationException) {
                throw (UnsupportedCommOperationException)ee.getCause();
            } else if (ee.getCause() instanceof IOException) {
                throw (IOException)ee.getCause();
            } else {
                throw new IOException("Unable to open serial port", ee);
            }
        }

    }

    /**
     * Disconnect from the port
     */
    public void disconnect() throws IOException,
                                    TimeoutException {

        Callable<Boolean> c = new Callable<Boolean>() {
            @Override
            public Boolean call() throws IOException {

                try {

                    // close the input stream
                    try {
                        FMCUartClient.this.inputStream.close();
                    } catch(Exception e) {
                        e.printStackTrace();
                    } finally {

                    }

                    // close the output stream
                    try {
                        FMCUartClient.this.outputStream.close();
                    } catch(Exception e) {
                        e.printStackTrace();
                    } finally {

                    }

                    // make sure there are not active even listeners
                    try {
                        ((RXTXPort)serialPort).removeEventListener();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    // close serial port
                    try {
                        RXTXHack.closeRxtxPort((RXTXPort)serialPort);
                    } catch(Exception e) {
                        e.printStackTrace();
                    } finally {
                        
                    }

                } finally {
                    FMCUartClient.this.connected = false;
                }

                log("Client connection terminated");

                return true;
            }
        };

        Future<Boolean> ret = executor.submit(c);

        try {
            // should always return true (unless an exception is thrown)
            Boolean success = ret.get(DISCONNECT_TIMEOUT, TimeUnit.MILLISECONDS); 
        } catch(InterruptedException ie) {
            throw new IOException("Unable to close serial port", ie);
        } catch(ExecutionException ee) {
            throw new IOException("Unable to close serial port", ee);
        }

    }

    /**
     * Returns if this client is connected or not
     *
     * @return true if client connected, false if not
     */
    public boolean isConnected() {
        return this.connected;
    }

    /**
     * Issues the given set of commands
     * 
     * @param cmds Raw commands to send
     * 
     * @throws IOException
     */
    public void sendCommands(String ... cmds) throws IOException {
        for(String c : cmds) {
            try {
                sendCommand(c);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns if there is any input currently on the input stream.
     * 
     * @return true if bytes are available, false if not
     * 
     * @throws IOException
     */
    protected boolean isInputAvailable() throws IOException {
        return this.inputStream.available() > 0;
    }

    /**
     * Blocks until a carrot (">") is read from the input stream.  All other
     * characters on the input stream up until the carrot is found are
     * discarded.
     *
     * @throws IOException
     */
    protected void waitForCarrot() throws IOException {
        
        log("Waiting for carrot...");

        int b;

        boolean end = false;

        while(!end) {
            b = inputStream.read();
            end = b == '>' || (b == -1 && !connected);
        }
    }

    /**
     * Discards all data current on the input stream
     * <br>
     * This can be called before a command is sent out to make
     * sure any data is not left on the input stream from an
     * earlier command.
     * 
     * @throws IOException
     */
    protected void flushInputStream() throws IOException {
        while(inputStream.available() > 0) {
            inputStream.read();
        }
    }

    /**
     * Reads a line of text from the input stream and return it
     * without any EOL characters (i.e. \n and \r)
     *
     * @return Line from input stream
     * 
     * @throws IOException
     */
    protected String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();

        boolean eol = false;
        int b;

        while(!eol) {
            b = inputStream.read();
            eol = b == '\n' || (b == -1 && !connected);
            if(!eol && b != '\r' && b != -1) {
                sb.append((char)b);
            }
        }

        return sb.toString();

    }

    /**
     *
     * @param str
     * @return
     * @throws IOException
     */
    private String writeLine(final String str) throws IOException {
        
        log("writing line: " + str);

        outputStream.write((str + "\n").getBytes());

        String echoRsp = readLine();
        log("echo: " + echoRsp);

        return echoRsp;
    }

    /**
     * Fills the given array from the input stream, blocking if needed,
     * until the array is completely filled.
     * 
     * @param b array to fill up
     * 
     * @throws IOException
     */
    protected void readFully(final byte [] b) throws IOException {
        int bytesRead = 0;
        int justRead = inputStream.read(b, 0, b.length);
        if(justRead > -1) bytesRead += justRead;

        log("Just read " + justRead + " (total = " + bytesRead + ") of IQ");

        while(bytesRead < b.length && justRead > -1) {
            justRead = inputStream.read(b, bytesRead, b.length - bytesRead);
            if(justRead > -1) bytesRead += justRead;

            log("Just read " + justRead + " (total = " + bytesRead + ") of IQ");
        }

         log("Getting IQ - total bytes read from input stream: " + bytesRead + " (should be " + b.length + ") - just read bytes: " + justRead);
    }

    /**
     * Returns a block of IQ samples that should have been made available
     * on the input stream.
     *
     * @return Array of interleaved IQ samples
     *
     * @throws IOException
     */
    private short [] getIQReply() throws IOException {
        // read block of data
        byteBuffer.clear();
        readFully(buffer);
        // put block in to a short array
        short [] array = getIQBufferAsShortArray();

        return array;
    }

    /**
     * Grabs a block of IQ
     * 
     * @return Array of interleaved IQ samples
     *
     * @throws IOException
     */
    public short [] grabIQ() throws IOException, 
                                    NckException,
                                    TimeoutException {

        Callable<short[]> c = new Callable<short[]>() {
            @Override
            public short[] call() throws IOException {

                log("Getting IQ - sending 'g' command...");

                flushInputStream();

                writeLine(GO_COMMAND);

                log("Getting IQ - g send, assuming ECHO AND ACK...");

                String ackRsp = readLine();
                log("Got ACK: " + ackRsp);

                short [] array = getIQReply();

                waitForCarrot();

                // return IQ
                return array;
            }
        };

        Future<short[]> ret = executor.submit(c);
        try {
            return ret.get(COMMAND_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch(InterruptedException ie) {

            ie.printStackTrace();

            error("Error getting IQ: " + ie.getMessage());

            throw new NckException(ie.getMessage());
        } catch(ExecutionException ee) {

            ee.printStackTrace();

            error("Error getting IQ: " + ee.getCause() != null ? ee.getCause().getMessage() : ee.getMessage());

            if(ee.getCause() instanceof IOException) {
                throw (IOException)ee.getCause();
            } else {
                throw new NckException(ee.getMessage());
            }
        }
    }

    /**
     * Reads through the byteBuffer and returns the contained data
     * as an array of shorts
     *
     * @return array of shorts taken from the byteBuffer
     */
    private short [] getIQBufferAsShortArray() {
        short [] array = new short[buffer.length / 2];
        for(int i = 0; i < array.length; ++i) {
            array[i] = byteBuffer.getShort();
        }
        return array;
    }

    /**
     * Writes a value with the given name, directed toward
     * the Bitshark board.
     *
     * @param name Bitshark variable to set
     * @param value value to set the given variable
     * 
     * @throws NckException
     * @throws IOException
     */
    public void writeValue(final String name,
                           final String value) throws IOException,
                                                      NckException,
                                                      TimeoutException {

        Callable<Boolean> c = new Callable<Boolean>() {
            @Override
            public Boolean call() throws IOException, NckException {
                return writeValueNow(name, value);
            }
        };

        Future<Boolean> ret = executor.submit(c);

        try {
             // should always return true (unless an exception is thrown)
            Boolean success
                    = ret.get(COMMAND_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch(InterruptedException ie) {

            error("Error writing value: " + ie.getMessage());

            throw new NckException(ie.getMessage());
        } catch(ExecutionException ee) {
            
            error("Error writing value: " + 
                    ee.getCause() != null ? ee.getCause().getMessage() :
                                            ee.getMessage());

            if(ee.getCause() instanceof IOException) {
                throw (IOException)ee.getCause();
            } else if (ee.getCause() instanceof NckException) {
                throw (NckException)ee.getCause();
            } else {
                throw new NckException(ee.getMessage());
            }
        }
    }

    
    /**
     * Reads the value of a Bitshark variable.
     * 
     * @param name Bitshark variable to read
     * @return value of the given variable
     * 
     * @throws NckException
     * @throws IOException
     */
    public String readValue(final String name) throws IOException,
                                                      NckException,
                                                      TimeoutException {

        Callable<String> c = new Callable<String>() {
            @Override
            public String call() throws IOException,
                                        NckException {
                return readValueNow(name);
            }
        };

        Future<String> ret = executor.submit(c);
        try {
            return ret.get(COMMAND_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch(InterruptedException ie) {

            error("Error reading value: " + ie.getMessage());

            throw new NckException(ie.getMessage());
        } catch(ExecutionException ee) {

            error("Error reading value: " + 
                    ee.getCause() != null ? ee.getCause().getMessage() :
                                            ee.getMessage());

            if(ee.getCause() instanceof IOException) {
                throw (IOException)ee.getCause();
            } else if (ee.getCause() instanceof NckException) {
                throw (NckException)ee.getCause();
            } else {
                throw new NckException(ee.getMessage());
            }
        }
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return
     * @throws IOException
     * @throws NckException 
     */
    public boolean writeValueNow(final String name,
                                 final String value) throws IOException,
                                                            NckException {
        flushInputStream();

        // create string to write out
        String command = String.format("%s W %s %s", BITSHARK_CMD_CLASS_NAME, name, value);
        writeLine(command);

        log("Reading ack...");
        // read out ack
        String ack = readLine();
        log("Got ack type: " + ack);

        // check for a positive ack
        if(ack.equalsIgnoreCase("ack")) {

            waitForCarrot();

            // always return true because if there was a failure, we
            // would throw an exception and never get to this point
            return true;
        } else {
            // assumed to be a nck, so error message to follow
            String errorMessage = readLine();

            log("Got NCK, error message: " + errorMessage);

            waitForCarrot();

            throw new NckException(errorMessage);
        }
    }
    
    
    public String readValueNow(final String name) throws IOException, NckException {

        flushInputStream();

        // create string to write out
        String command = String.format("%s R %s", BITSHARK_CMD_CLASS_NAME, name);
        writeLine(command);

        // read out ack
        String ack = readLine();

        log("Got ACK type back: " + ack);

        if(ack.equalsIgnoreCase("ack")) {
            // read and return reply value
            String value = readLine();

            if(value != null) {
                value = value.trim();
            }

            log("read value returned: " + value);

            waitForCarrot();

            return value;
        } else {

            String nckMessage = readLine();
            log("GOT NCK Message Back: " + nckMessage);

            waitForCarrot();

            throw new NckException(String.format("Failed to read %s", name));
        }        
    }

    /**
     * Sends the given command
     * <br>
     * This command is by default issued on the thread pool
     * 
     * @param cmd Raw command to send
     * @return Response to the given command, if any
     *
     * @throws IOException
     * @throws NckException
     * @throws TimeoutException
     */
    public String sendCommand(final String cmd) throws IOException,
                                                       NckException,
                                                       TimeoutException {
        return sendCommand(cmd, true);
    }


    /**
     * Send the given command
     *
     * @param cmd Raw command to send
     * @param onThreadPool true to place on thread pool, false to not
     * @return Response to the given command, if any
     *
     * @throws IOException
     * @throws NckException
     * @throws TimeoutException
     */
     public String sendCommand(final String cmd,
                               final boolean onThreadPool) throws IOException,
                                                                  NckException,
                                                                  TimeoutException {

        Callable<String> c = new Callable<String>() {
            @Override
            public String call() throws IOException {

                log("Preparing to send command: " + cmd);

                flushInputStream();

                String command = cmd.trim();
                writeLine(command);

                StringBuilder builder = new StringBuilder();

                // special support for GO command since the response
                // to this command is binary, not ASCII
                if(command.equalsIgnoreCase(GO_COMMAND)) {
                    log("IQ command issued from command line...");

                    String ackRsp = readLine();
                    log("Got ACK: " + ackRsp);

                    short [] iq = getIQReply();

                    builder.append("(I,Q)");
                    builder.append('\n');
                    builder.append("---------------------\n");
                    for(int i = 0; i < iq.length / 2; ++i) {
                       builder.append(iq[i * 2 + 0]);
                       builder.append('\t');
                       builder.append(iq[i * 2 + 1]);
                       builder.append('\n');
                    }

                }

                int b;

                boolean end = false;

                while(!end) {
                    b = inputStream.read();

                    if(b != '>' && b != -1) {
                        if(b != '\r') {
                            builder.append((char)b);
                        }
                    } else {
                        end = true;
                    }
                }

                return builder.toString();

            }
        };

        try {
            if(onThreadPool) {
                Future<String> ret = executor.submit(c);
                return ret.get(COMMAND_LINE_TIMEOUT, TimeUnit.MILLISECONDS);
            } else {
                return c.call();
            }
        } catch(InterruptedException ie) {

            error("Error sending command: " + ie.getMessage());

            throw new NckException(ie.getMessage());
        } catch(ExecutionException ee) {

            error("Error sending command: " + 
                    ee.getCause() != null ? ee.getCause().getMessage() :
                                            ee.getMessage());

            if(ee.getCause() instanceof IOException) {
                throw (IOException)ee.getCause();
            } else if (ee.getCause() instanceof NckException) {
                throw (NckException)ee.getCause();
            } else {
                throw new NckException(ee.getMessage());
            }
        } catch(Exception e) {
            error("Error sending command: " + 
                    e.getCause() != null ? e.getCause().getMessage() :
                                           e.getMessage());

            if(e.getCause() instanceof IOException) {
                throw (IOException)e.getCause();
            } else if (e.getCause() instanceof NckException) {
                throw (NckException)e.getCause();
            } else {
                throw new NckException(e.getMessage());
            }
        }
    }

    /**
     * Returns if the IQ calibration is applied each time the
     * center frequency is changed.
     *
     * @return true if the use of IQ calibration is enabled, false otherwise
     */
    public boolean isIqCalEnabled() {
        return useIqcal;
    }

    /**
     * Enables or disables the use of the IQ calibration when the
     * center frequency is changed.
     *
     * @param enable true to enable the use of IQ calibration, false to disable
     */
    public void setIqCalEnabled(final boolean enable) {
        this.useIqcal = enable;

        if(!enable) {
            try {
                // when disabled, ensure calibration previously written
                // is cleared out
                sendCommand(CLEAR_CAL_COMMAND);
            } catch(Exception e) {
                error("Clearing out IQ cal failed");
            }
        } else {
            try {
                // apply the calibration for the current center frequency
                applyIqCal(getCenterFrequency());
            } catch(Exception e) {
                error("Failed to update IQ cal: " + e.getMessage());
            }
        }
    }

    /**
     * Get the current center frequency in hertz
     *
     * @return Center frequency in HZ
     * 
     * @throws NckException
     * @throws IOException
     * @throws TimeoutException
     */
    public long getCenterFrequency() throws NckException,
                                            IOException,
                                            TimeoutException {
        String str = this.readValue(CENTER_FREQ_VAR_NAME);
        return parseFrequency(str);
    }

    /**
     * Sets the center frequency
     * <br>
     * Valid frequencies should be 300 MHz to 4 GHz, with 1 KHz resolution
     * 
     * @param frequency new center frequency in hertz
     *
     * @throws NckException
     * @throws IOException
     */
    public void setCenterFrequency(long frequency) throws NckException,
                                                          IOException,
                                                          TimeoutException {

        if(useIqcal) {
            applyIqCal(frequency);
        }

        float freqMhz = (float)(frequency / Math.pow(10, 6));

        this.writeValue(CENTER_FREQ_VAR_NAME,
                        String.format("%.3f", freqMhz));
    }

    /**
     * Sets the sample rate.
     * <br>
     * Min is 5 Msamples/sec, max is 105 Msamples/sec, resolution is 1 Hz
     *
     * @param frequency
     * @throws NckException
     * @throws IOException
     * @throws TimeoutException
     */
    public void setSampleRate(int frequency) throws NckException,
                                                     IOException,
                                                     TimeoutException {
        this.writeValue(SAMPLE_RATE_VAR_NAME,
                        String.format("%d", frequency));
    }

    /**
     * Gets the current sample rate.
     *
     * @return Current sample rate in hertz
     * 
     * @throws NckException
     * @throws IOException
     * @throws TimeoutException
     */
    public int getSampleRate() throws NckException,
                                       IOException,
                                       TimeoutException {
        String str = this.readValue(SAMPLE_RATE_VAR_NAME);
        return (int)parseFrequency(str);
    }


    /**
     * Gets the current RF gain
     *
     * @return RF gain in dB
     * 
     * @throws NckException
     * @throws IOException
     * @throws TimeoutException
     */
    public float getRfGain() throws NckException,
                                    IOException,
                                    TimeoutException {

        String str = this.readValue(RF_GAIN_VAR_NAME);
        String [] tokens = str.split(" ");
        String gainStr = tokens[0];

        if(gainStr.indexOf(".") == -1) {
            gainStr = gainStr + ".0";
        }

        float gain = Float.parseFloat(gainStr);
        return gain;
    }

    /**
     * Sets the RF gain.
     * <br>
     * Valid values range between 0 to 31.5 dB, with 0.5 dB resolution
     *
     * @param gain RF gain in dB
     * 
     * @throws NckException
     * @throws IOException
     */
    public void setRfGain(float gain) throws NckException,
                                             IOException,
                                             TimeoutException {
        this.writeValue(RF_GAIN_VAR_NAME, 
                        String.format("%1.1f", gain));
    }

    /**
     * Gets the current baseband gain.
     *
     * @return Baseband gain in dB
     * 
     * @throws NckException
     * @throws IOException
     * @throws TimeoutException
     */
    public int getBasebandGain() throws NckException,
                                        IOException,
                                        TimeoutException {
        String bbGainStr = this.readValue(BASEBAND_GAIN_VAR_NAME);
        return Integer.parseInt(bbGainStr.split(" ")[0]);
    }

    /**
     * Sets the baseband gain.
     * <br>
     * Valid values range in 0 to 6 dB with 3 dB resolution (i.e 0, 3, 6)
     *
     * @param bbGain New baseband gain
     *
     * @throws NckException
     * @throws IOException
     */
    public void setBasebandGain(int bbGain) throws NckException,
                                                   IOException,
                                                   TimeoutException {
        this.writeValue(BASEBAND_GAIN_VAR_NAME, 
                        String.valueOf(bbGain));
    }

    /**
     * Gets the current baseband low-pass filter bandwidth
     *
     * @return baseband low-pass filter bandwidth
     *
     * @throws NckException
     * @throws IOException
     * @throws TimeoutException
     */
    public long getBasebandFilterBw() throws NckException,
                                             IOException,
                                             TimeoutException {
        String str = this.readValue(BASEBAND_FILTER_BW_VAR_NAME);
        return parseFrequency(str);
    }

    /**
     * Sets the baseband low-pass filter bandwidth
     * <br>
     * Valid values are 330 KHz, 660 KHz, 1 MHz to 28 MHz in 1 MHz increments
     *
     * @param filterBw New baseband low-pass filter bandwidth
     * @throws NckException
     * @throws IOException
     */
    public void setBasebandFilterBw(long filterBw) throws NckException,
                                                          IOException,
                                                          TimeoutException {
        float freqKhz = (float)(filterBw / Math.pow(10, 3));

        // TODO: Format to go to 1 decimal place
        this.writeValue(BASEBAND_FILTER_BW_VAR_NAME,
                        String.format("%d", (int)freqKhz));
    }

    /**
     * Gets the clock rate
     *
     * @param internalClock true for internal, false for external
     *
     * @return clock rate in hertz
     *
     * @throws NckException
     * @throws IOException
     * @throws TimeoutException
     */
    public int getClockRate(boolean internalClock) throws NckException,
                                                          IOException,
                                                          TimeoutException {
        String str = this.readValue(CLOCK_VAR_NAME);
        return (int)parseFrequency(str.split(" ")[0]);
    }

    /**
     * Returns if the internal clock is being used or not
     *
     * @return true if internal clock used, false if external
     * @throws NckException
     * @throws IOException
     * @throws TimeoutException
     */
    public boolean isInternalClock() throws NckException,
                                            IOException,
                                            TimeoutException {
        String str = this.readValue(CLOCK_VAR_NAME);
        return str.split(" ")[1].equalsIgnoreCase("1");
    }

    /**
     * Sets the clock rate
     * 
     * @param internalClock true for internal, false for external
     * @param frequency New clock rate, in hertz
     *
     * @throws NckException
     * @throws IOException
     * @throws TimeoutException
     */
    public void setClockRate(boolean internalClock,
                             int frequency) throws NckException,
                                                    IOException,
                                                    TimeoutException {
        float freqKhz = (float)(frequency / Math.pow(10, 3));

        // TODO: Format to go to 1 decimal place
        this.writeValue(CLOCK_VAR_NAME,
                        String.format("%d %f %s",
                                      internalClock ? 1 : 0,
                                      freqKhz,
                                      "kHz"));
    }

    /**
     * Applies the IQ calibration for the given frequency
     *
     * @param frequency in Hertz
     */
    public void applyIqCal(final long frequency) {
        float freqMhz = (float)(frequency / Math.pow(10, 6));
        try {
            String val = this.readValue(String.format("%s %.2f", IQCAL_VAR_NAME, freqMhz));
            val = val.replaceAll("0x", "");
            String [] bytes = val.split(" ");
            this.sendCommand(String.format("F W 09 %s%s", bytes[0], bytes[1]));
        } catch(Exception e) {
            error("Failed to apply IQ calibration");
        }
    }

    /**
     * Gets the version String
     *
     * @return Version string of the Bitshark FMC-1RX board
     *
     * @throws NckException
     * @throws IOException
     * @throws TimeoutException
     */
    public String getVersionString() throws NckException,
                                            IOException,
                                            TimeoutException {
        return this.readValue(VERSION_VAR_NAME);
    }

    /**
     * Gets the version String, staying on the current thread. This method
     * will not throw a timeout.
     *
     * @return Version string of the Bitshark FMC-1RX board
     *
     * @throws NckException
     * @throws IOException
     */
    public String getVersionStringNow() throws NckException,
                                               IOException {
        return this.readValueNow(VERSION_VAR_NAME);
    }    
    
    /**
     * Returns the version number contained within the version string
     * 
     * @return Numerical version of the Bitshark FMC-1RX. Zero will be returned
     *         in the event that the version string can not be correctly read
     *         or parsed.
     */
    public double getVersion() {
        String versionStr = null;
        
        try {
            versionStr = getVersionString();
        } catch(NckException ne) {
            error("Failed to read version: " + ne);
        } catch(IOException ioe) {
            error("Failed to read version: " + ioe);
        } catch(TimeoutException te) {
            error("No reply to read version");
        }
        
        double version = 0.0;
        
        try {            
            
            // convert everything to lower case
            versionStr = versionStr.toLowerCase();
            
            // find the " v" that indicates the start of the version number
            // and break out the number only portion out
            versionStr = versionStr.substring(versionStr.lastIndexOf(" v") + 2);
            
            // parse the version
            version = Double.parseDouble(versionStr);
            
        } catch(Exception e) {
            error("Failed to parse version: " + e);
        } 
        
        return version;
    }
    
    /**
     * Returns the version number contained within the version string, staying
     * on the current thread to do so. This method
     * will not throw a timeout.
     * 
     * @return Numerical version of the Bitshark FMC-1RX. Zero will be returned
     *         in the event that the version string can not be correctly read
     *         or parsed.
     */
    public double getVersionNow() {
        String versionStr = null;
        
        try {
            versionStr = getVersionStringNow();
        } catch(NckException ne) {
            error("Failed to read version: " + ne);
        } catch(IOException ioe) {
            error("Failed to read version: " + ioe);
        }
        
        double version = 0.0;
        
        try {            
            
            // convert everything to lower case
            versionStr = versionStr.toLowerCase();
            
            // find the " v" that indicates the start of the version number
            // and break out the number only portion out
            versionStr = versionStr.substring(versionStr.lastIndexOf(" v") + 2);
            
            // parse the version
            version = Double.parseDouble(versionStr);
            
        } catch(Exception e) {
            error("Failed to parse version: " + e);
        } 
        
        return version;
    }    
    
    /**
     * Returns the appropriate startup commands for the
     * connected Bitshark FMC-1RX.
     * 
     * @return Array of commands that should be sent down immediately after
     *         connecting to the Bitshark FMC-1RX
     */
    private String [] getStartupCommands() {
        // ensure we read the version on this thread and we don't try
        // and post it to our worker thread
        double version = getVersionNow(); 
        
        if(version >= 1.14) { // assuming any version >= 1.14 is rev C hardware
            return STARTUP_COMMANDS_1_14;
        } else {
            return STARTUP_COMMANDS_1_00;
        }
        
    }
    
    /**
     * Parses a frequency string provided by the server
     * and returns the value in hertz
     * 
     * @param str Frequency string to parse
     * 
     * @return
     */
    public static long parseFrequency(final String str) {
        String [] tokens = str.split(" ");
        String baseStr = tokens[0];
        String unitStr = "";

        if(tokens.length > 1) {
            unitStr = tokens[1];
        }

        float base = Float.parseFloat(baseStr);

        int unitExponent = 0;

        if(unitStr.equalsIgnoreCase("GHz")) {
            unitExponent = 9;
        } else if(unitStr.equalsIgnoreCase("MHz")) {
            unitExponent = 6;
        } else if(unitStr.equalsIgnoreCase("kHz")) {
            unitExponent = 3;
        } else {
            // if not one of the above listed units, assume
            // value is in hz
            unitExponent = 0;
        }

        long value = (long)(base * Math.pow(10, unitExponent));
        return value;
    }

    /**
     * Method used for basic debugging
     *
     * @param msg 'error' message to log
     */
    protected void error(final String msg) {
        if(!DEBUG) {
            return;
        }

        System.out.println(
                String.format("[%s] ERROR - %s",
                              Long.toString(System.currentTimeMillis()),
                              msg));
    }

    /**
     * Method used for basic debugging
     *
     * @param msg 'info' message to log
     */
    protected void log(final String msg) {

        if(!DEBUG) {
            return;
        }
        
        System.out.println(
                String.format("[%s] INFO -  %s",
                              Long.toString(System.currentTimeMillis()),
                              msg));
    }

}
