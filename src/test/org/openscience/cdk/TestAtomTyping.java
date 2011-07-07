/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openscience.cdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

/**
 *
 * @author Asad
 */
public class TestAtomTyping {

    /**
     * @param args the command line arguments
     * @throws FileNotFoundException
     * @throws Exception  
     */
    public static void main(String[] args) throws FileNotFoundException, Exception {
        //To read a file and count Atoms
        int totalMolCount = 0;
        int failCount = 0;
        File molDir = new File("/Users/Asad/Software/DataKegg/ligand/mol");
        System.out.println("\"KEGG ID\"\t" + "\"Atom Count\"\t" + "\"Formula\"\t" + "\"Failed\"");
        for (String fileName : molDir.list()) {
            StringBuilder st = new StringBuilder();
            File file = new File(molDir + File.separator + fileName);
            MDLV2000Reader reader = new MDLV2000Reader(new FileReader(file));
            IMolecule molecule = null;
            try {
                molecule = reader.read(new Molecule());
                MDLV2000Writer writer = new MDLV2000Writer(new FileWriter(new File(fileName + "cdk.mol")));
                writer.writeMolecule(molecule);
                writer.close();
//                IMolecule addExplicitH = ChemaxonHydrogenHandler.addExplicitH(molecule);
//                MDLV2000Writer writer1 = new MDLV2000Writer(new FileWriter(new File(fileName + "chemaxon.mol")));
//                writer1.writeMolecule(addExplicitH);
//                writer1.close();
            } catch (CDKException ex) {
                continue;
            }
//            if (totalMolCount == 10) {
//                break;
//            }
            totalMolCount++;
            /*FileName*/
            st.append(fileName).append("\t");
            /*Number of Atoms*/
            st.append(molecule.getAtomCount()).append("\t");

            // To get Formula string

            IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(molecule);
            String string1 = MolecularFormulaManipulator.getString(formula);

            /*\tMolecular Formula:\t"*/
            st.append(string1).append("\t");

//            // To get symbols
//
//            for (IAtom atom : molecule.atoms()) {
//                st.append(atom.getSymbol());
//            }

            /*"\tFailes:\t"*/
            //Atomtype Matching
            CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(DefaultChemObjectBuilder.getInstance());
            int i = 1;
            boolean flag = false;
            for (IAtom atom : molecule.atoms()) {
                IAtomType atomtype = null;
                try {
                    atomtype = matcher.findMatchingAtomType(molecule, atom);
                } catch (CDKException ex) {
                    ex.printStackTrace();
                }

                if (atomtype == null) {
                    flag = true;
                    st.append(atom.getSymbol()).append(",");
                }
                i++;
            }
            if (flag) {
                failCount++;
                System.out.println(st.toString());
            }
            // TODO code application logic here
        }
        float f = 100 * ((float) failCount / (float) totalMolCount);
        System.out.println("\n Failed percentage " + f);
    }
}
