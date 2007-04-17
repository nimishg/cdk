/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 * 
 * Copyright (C) 2004-2007  The Chemistry Development Kit (CDK) project
 *
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
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
package org.openscience.cdk.isomorphism.matchers.smarts;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.CDKConstants;

/**
 * This matcher any aromatic atom. This assumes that aromaticity in the molecule
 * has been perceived.
 *
 * @cdk.module extra
 */
public class AromaticAtom extends SMARTSAtom {
    
    private static final long serialVersionUID = -3345204886992669829L;

    public AromaticAtom() {
    }
   
    public int getOperator(){
        if(ID!=null)
            return 1;
        return 2;
    }
    public boolean matches(IAtom atom) {
     switch(getOperator()){
                case 1: { if(!atom.getFlag(CDKConstants.ISAROMATIC)) return true;}
                case 2: { if(atom.getFlag(CDKConstants.ISAROMATIC)) return true;}
                default: return false;
            }   
    };

    public String toString() {
		return "AromaticAtom()";
    }
}

