/*
 * Copyright (c) 2005-2010 Substance Kirill Grouchnikov. All Rights Reserved.
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
package tools.docrobot.watermarks;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceConstants.ImageWatermarkKind;
import org.pushingpixels.substance.api.colorscheme.CharcoalColorScheme;
import org.pushingpixels.substance.api.watermark.SubstanceImageWatermark;

import tools.docrobot.ImageWatermarkRobot;
import tools.docrobot.RobotDefaultDarkSkin;

/**
 * Screenshot robot for {@link SubstanceImageWatermark}.
 * 
 * @author Kirill Grouchnikov
 */
public class ImageWatermarkDominic extends ImageWatermarkRobot {
	/**
	 * Creates the screenshot robot.
	 */
	public ImageWatermarkDominic() {
		super(
				"/Users/kirillg/JProjects/substance/www/images/screenshots/watermarks/image-dominic.png");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see docrobot.ImageWatermarkRobot#apply()
	 */
	@Override
	protected void apply() {
		SubstanceImageWatermark watermark = new SubstanceImageWatermark(
				"tools/docrobot/watermarks/PrisonBreak3.jpg");
		watermark.setKind(ImageWatermarkKind.APP_ANCHOR);
		SubstanceLookAndFeel.setSkin(new RobotDefaultDarkSkin(
				new CharcoalColorScheme(), watermark));
	}
}
