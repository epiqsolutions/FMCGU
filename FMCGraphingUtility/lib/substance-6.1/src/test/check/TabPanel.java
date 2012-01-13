/*
 * Copyright (c) 2005-2008 Substance Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of Substance Kirill Grouchnikov nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package test.check;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.pushingpixels.lafwidget.LafWidget;

import test.Check;

/**
 * Test application panel for testing {@link JTabbedPane} component.
 * 
 * @author Kirill Grouchnikov
 */
public class TabPanel extends ControllablePanel {
	/**
	 * Tabbed pane.
	 */
	public JTabbedPane jtp;

	/**
	 * Creates a test panel with tabbed pane.
	 */
	public TabPanel() {
		this.setLayout(new BorderLayout());
		jtp = new JTabbedPane();
		NumberedPanel tnp1 = new NumberedPanel(1);
		jtp.addTab("tab0", null, new JPanel());
		jtp.addTab("tab1", Check.getIcon("flag_mexico"), tnp1);
		jtp.addTab("tab 2", Check.getIcon("flag_sweden"), new NumberedPanel(2));
		NumberedPanel tnp3 = new NumberedPanel(3);
		jtp.addTab("tab  3", Check.getIcon("flag_russia"), tnp3);
		final NumberedPanel np4 = new NumberedPanel(4);
		jtp.addTab("tab   4", np4);
		final NumberedPanel np5 = new NumberedPanel(5);
		jtp.addTab("tab    5", np5);

		jtp.setEnabledAt(2, false);
		jtp.setEnabledAt(3, false);
		this.add(jtp, BorderLayout.CENTER);

		try {
			MyTabPreviewPainter previewPainter = new MyTabPreviewPainter();
			jtp.putClientProperty(LafWidget.TABBED_PANE_PREVIEW_PAINTER,
					previewPainter);
			this.controlPanel = new TabControlPanel(jtp, previewPainter);
		} catch (NoClassDefFoundError ncdfe) {
			this.controlPanel = new TabControlPanel(jtp, null);
		} catch (Throwable e) {
		}
	}

}