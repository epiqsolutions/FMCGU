/*
 * $Id: UIDependent.java 3692 2010-05-03 18:05:38Z kschaefe $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.decorator;

/**
 * Encapsulates state that depends on the UI and needs
 * to be updated on LookAndFeel change.
 * 
 * @author Jeanette Winzenburg
 * @deprecated (pre-1.6.1) moved; use {@link org.jdesktop.swingx.plaf.UIDependent} instead
 */
@Deprecated
public interface UIDependent {
    /**
     * method to call after the LookAndFeel changed. 
     *
     */
    void updateUI();
}
