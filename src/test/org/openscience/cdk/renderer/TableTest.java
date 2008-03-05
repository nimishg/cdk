/* $RCSfile$
 * $Author$   
 * $Date$   
 * $Revision$
 * 
 * Copyright (C) 2001-2007  The Chemistry Development Kit (CDK) project
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

package org.openscience.cdk.renderer;

import java.io.FileInputStream;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.applications.swing.MoleculesTable;
import org.openscience.cdk.io.CMLReader;
import org.openscience.cdk.io.IChemObjectReader;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.io.XYZReader;
import org.openscience.cdk.io.IChemObjectReader.Mode;

/**
 * @cdk.module test-extra
 */
public class TableTest {

    public TableTest(String inFile) {
      try {        
        IChemObjectReader reader;
        System.out.println("Loading: " + inFile);
        if (inFile.endsWith(".xyz")) {
  	      reader = new XYZReader(new FileInputStream(inFile));
          System.out.println("Expecting XYZ format...");
        } else if (inFile.endsWith(".cml")) {
  	      reader = new CMLReader(new FileInputStream(inFile));
          System.out.println("Expecting CML format...");
        } else {
          reader = new MDLReader(new FileInputStream(inFile), Mode.STRICT);
          System.out.println("Expecting MDL MolFile format...");
        }
        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());

        org.openscience.cdk.interfaces.IChemSequence chemSequence;
        org.openscience.cdk.interfaces.IChemModel chemModel;
        org.openscience.cdk.interfaces.IMoleculeSet setOfMolecules;
        for (int sequence = 0; sequence < chemFile.getChemSequenceCount(); sequence++) {
          chemSequence = chemFile.getChemSequence(sequence);
          for (int model = 0; model < chemSequence.getChemModelCount(); model++) {
            chemModel = chemSequence.getChemModel(model);
            setOfMolecules = chemModel.getMoleculeSet();
            MoleculesTable mt = new MoleculesTable(setOfMolecules);
            mt.display();
          }
	      }
      } catch(Exception exc) {
	      exc.printStackTrace();
      }
    }
    
}
