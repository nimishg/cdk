/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openscience.cdk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
//import org.openscience.reactionblast.tools.chemaxonhandler.ChemaxonHydrogenHandler;

/**
 *
 * @author Asad
 */
public class MolAtomType {

    public static void main(String[] args) throws FileNotFoundException, IOException, CloneNotSupportedException {
        String molDir = "/Users/Asad/Software/Kegg/molforHTest/mol/";
        File files = new File(molDir);
        File outFile = new File("/Users/Asad/Software/Kegg/molforHTest/CDKChemAxonHAdder.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFile));
        bufferedWriter.write("\"Mol ID\"\t" + "\"Molecular Formula\"\t" + "\"Atom Count w/o H\"\t" + "\"CDKHCount\"\t" + "\"ChemaxonHCount\"\t" + "\"Difference in H addition\"\t");
        bufferedWriter.newLine();
        for (String fileName : files.list()) {

            if (fileName.equalsIgnoreCase("C19421.mol")) {

                File f = new File(molDir + File.separator + fileName);
                MDLV2000Reader reader = new MDLV2000Reader(new FileReader(f));
                Molecule read = null;
                try {
                    read = reader.read(new Molecule());
                } catch (CDKException ex) {
                    Logger.getLogger(MolAtomType.class.getName()).log(Level.SEVERE, null, ex);
                }
                IAtomContainer removeH = AtomContainerManipulator.removeHydrogens(read);

                IAtomContainer readMolForCDK = (IAtomContainer) removeH.clone();
                IAtomContainer readMolForChemaxon = (IAtomContainer) removeH.clone();
                readMolForCDK.setID(f.getName());
                readMolForChemaxon.setID(f.getName());

                CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(readMolForCDK.getBuilder());
                for (IAtom atom : readMolForCDK.atoms()) {
                    if (!(atom instanceof IPseudoAtom)) {
                        IAtomType matched = null;
                        try {
                            matched = matcher.findMatchingAtomType(readMolForCDK, atom);
                        } catch (CDKException ex) {
                            Logger.getLogger(MolAtomType.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (matched != null) {
                            AtomTypeManipulator.configure(atom, matched);
                            System.out.println("Matched Name " + matched.getAtomTypeName());
                        } else {
                            System.out.println("Molecule " + f.getName() + ", Atom not supported " + atom.getSymbol());
                        }
                    } else {
                        //System.out.println("P Atom " + atom.getSymbol());
                    }
                }

                reader.close();
//                IMolecularFormula molecularFormula = MolecularFormulaManipulator.getMolecularFormula(removeH);
//                CDKHydrogenAdder cha = CDKHydrogenAdder.getInstance(readMolForCDK.getBuilder());
//                try {
//                    cha.addImplicitHydrogens(readMolForCDK);
//                } catch (CDKException ex) {
//                    Logger.getLogger(MolAtomType.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                AtomContainerManipulator.convertImplicitToExplicitHydrogens(readMolForCDK);
//                IMolecule addExplicitHByChemaxon = null;
//                try {
//                    //addExplicitHByChemaxon = ChemaxonHydrogenHandler.addExplicitH(AtomContainerManipulator.removeHydrogens(readMolForChemaxon));
//                } catch (Exception ex) {
//                    Logger.getLogger(MolAtomType.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                int hCountFromCDK = readMolForCDK.getAtomCount() - removeH.getAtomCount();
//                int hCountFromChemaxon = addExplicitHByChemaxon.getAtomCount() - removeH.getAtomCount();
//                bufferedWriter.write(readMolForCDK.getID() + "\t"
//                        + MolecularFormulaManipulator.getString(molecularFormula) + "\t"
//                        + removeH.getAtomCount() + "\t"
//                        + hCountFromCDK + "\t"
//                        + hCountFromChemaxon + "\t"
//                        + (Math.abs(hCountFromCDK - hCountFromChemaxon)));
//                bufferedWriter.newLine();
            }
        }
        bufferedWriter.close();
    }
}
