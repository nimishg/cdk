/* Copyright (C) 2006-2010  Syed Asad Rahman <asad@ebi.ac.uk>
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.cdk.smsd.interfaces;

/**
 * @cdk.module smsd
 * @cdk.githash
 * @author Syed Asad Rahman <asad@ebi.ac.uk>
 */
public interface ITimeOut {

    /**
     * get timeout in mins for bond insensitive searches
     * @return the bondInSensitive TimeOut
     */
    double getBondInSensitiveCDKMCSTimeOut();

    /**
     * get timeout in mins for bond insensitive searches
     * @return the bondInSensitive TimeOut
     */
    double getBondInSensitiveMCSPlusTimeOut();

    /**
     * get timeout in mins for bond insensitive searches
     * @return the bondInSensitive TimeOut
     */
    double getBondInSensitiveVFTimeOut();

    /**
     * get timeout in mins for bond sensitive searches
     * @return the bondSensitive TimeOut
     */
    double getBondSensitiveCDKMCSTimeOut();

    /**
     * get timeout in mins for bond sensitive searches
     * @return the bondSensitive TimeOut
     */
    double getBondSensitiveMCSPlusTimeOut();

    /**
     * get timeout in mins for bond sensitive searches
     * @return the bondSensitive TimeOut
     */
    double getBondSensitiveVFTimeOut();

    /**
     * set timeout in mins (default 1.00 min) for bond insensitive searches
     * @param bondInSensitiveTimeOut the bond insensitive
     */
    void setBondInSensitiveCDKMCSTimeOut(double bondInSensitiveTimeOut);

    /**
     * set timeout in mins (default 1.00 min) for bond insensitive searches
     * @param bondInSensitiveTimeOut the bond insensitive
     */
    void setBondInSensitiveMCSPlusTimeOut(double bondInSensitiveTimeOut);

    /**
     * set timeout in mins (default 1.00 min) for bond insensitive searches
     * @param bondInSensitiveTimeOut the bond insensitive
     */
    void setBondInSensitiveVFTimeOut(double bondInSensitiveTimeOut);

    /**
     * set timeout in mins (default 0.10 min) for bond sensitive searches
     * @param bondSensitiveTimeOut the bond Sensitive Timeout in mins (default 0.30 min)
     */
    void setBondSensitiveCDKMCSTimeOut(double bondSensitiveTimeOut);

    /**
     * set timeout in mins (default 0.10 min) for bond sensitive searches
     * @param bondSensitiveTimeOut the bond Sensitive Timeout in mins (default 0.30 min)
     */
    void setBondSensitiveMCSPlusTimeOut(double bondSensitiveTimeOut);

    /**
     * set timeout in mins (default 0.10 min) for bond sensitive searches
     * @param bondSensitiveTimeOut the bond Sensitive Timeout in mins (default 0.30 min)
     */
    void setBondSensitiveVFTimeOut(double bondSensitiveTimeOut);

}
