# Introduction

The FMC Graphing Utility (FMCGU) is a simple cross platform compatible desktop spectrum analyzer intended to be used with Epiq Solutions' Bitshark FMC-1RX card (http://epiqsolutions.com/fmc-1rx.php).

# Application Screenshot

  ![screenshot](http://epiqsolutions.com/images/fmcgu_git_screenshot.png)

# Building and Running

To build and run this project, the only requirement is Java 6+.

For the path of least resistance in building and running the FMCGU from source, download and install the latest version of the NetBeans IDE (http://netbeans.org/downloads/). Once NetBeans is installed and open, go to <b>File > Open Project</b> and select the <b>FMCGraphingUtility</b> directory within the root of this repository. The primary benefit to using NetBeans is that it will ensure that all external libraries are correctly setup "out of the box" without the need to download or configure anything else.

Once the project is open within NetBeans, ensure you select the correct runtime configuration for your platform: either Windows, Linux 32bit, Linux 64bit, or MacOS. This is important to run the application because the FMCGU uses native libraries to perform the required serial port communication and the NetBeans runtime configuration forces the correct library path to be used.