/* Copyright (C) 2007-2011  Egon Willighagen <egonw@users.sf.net>
 *               2007       Rajarshi Guha
 *               2011       Nimish Gopal <gopal.nimish@gmail.com>
 *               2011       Syed Asad Rahman <asad@ebi.ac.uk>
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
package org.openscience.cdk.atomtype;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.nonotify.NNChemFile;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

/**
 * This class tests the matching of atom types defined in the
 * CDK atom type list. All tests in this class <b>must</b> use
 * explicit {@link IAtomContainer}s; test using data files
 * must be placed in {@link CDKAtomTypeMatcherFilesTest}.
 *
 * @cdk.module test-core
 */
public class CDKAtomTypeMatcherEBITest extends AbstractCDKAtomTypeTest {

    private static Map<String, Integer> testedAtomTypes = new HashMap<String, Integer>();

    private IAtomContainer getMolFromFile(String molName) throws CDKException {
        String filename = "data" + File.separator + "mdl" + File.separator + molName + ".mol";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        MDLV2000Reader reader = new MDLV2000Reader(ins);
        IChemFile chemFile = (IChemFile) reader.read(new NNChemFile());

        // test the resulting ChemFile content
        Assert.assertNotNull(chemFile);
        return ChemFileManipulator.getAllAtomContainers(chemFile).get(0);
    }

    @Test
    public void testGetInstance_IChemObjectBuilder() throws Exception {
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(
                DefaultChemObjectBuilder.getInstance());
        Assert.assertNotNull(matcher);
    }

    @Test
    public void testGetInstance_IChemObjectBuilder_int() throws Exception {
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(
                DefaultChemObjectBuilder.getInstance(),
                CDKAtomTypeMatcher.REQUIRE_EXPLICIT_HYDROGENS);
        Assert.assertNotNull(matcher);
    }

    @Test
    public void testFindMatchingAtomType_IAtomContainer_IAtom() throws Exception {
        IMolecule mol = new Molecule();
        IAtom atom = new Atom("C");
        final IAtomType.Hybridization thisHybridization = IAtomType.Hybridization.SP3;
        atom.setHybridization(thisHybridization);
        mol.addAtom(atom);

        String[] expectedTypes = {"C.sp3"};
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_Fe() throws Exception {

        String molName = "Fe_metallic";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Fe.metallic"}; // Fix1
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Fe_plus";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"C.sp3", "C.sp3", "Fe.plus"}; // Fix6
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "Fe_4";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"C.sp3", "C.sp3", "Fe.4", "C.sp3", "C.sp3"}; // Fix5
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

        String molName3 = "Fe_3minus";
        IAtomContainer mol3 = getMolFromFile(molName3);
        String[] expectedTypes3 = {"Fe.3minus", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix21
        assertAtomTypes(testedAtomTypes, expectedTypes3, mol3);

        String molName4 = "Fe_2plus";
        IAtomContainer mol4 = getMolFromFile(molName4);
        String[] expectedTypes4 = {"Fe.2plus"}; // Fix25
        assertAtomTypes(testedAtomTypes, expectedTypes4, mol4);

        String molName5 = "Fe_4minus";
        IAtomContainer mol5 = getMolFromFile(molName5);
        String[] expectedTypes5 = {"Fe.4minus", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix26
        assertAtomTypes(testedAtomTypes, expectedTypes5, mol5);

        String molName7 = "Fe_6";
        IAtomContainer mol7 = getMolFromFile(molName7);
        String[] expectedTypes7 = {"Fe.6", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix28
        assertAtomTypes(testedAtomTypes, expectedTypes7, mol7);

        String molName8 = "Fe_2minus";
        IAtomContainer mol8 = getMolFromFile(molName8);
        String[] expectedTypes8 = {"Fe.2minus", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix29
        assertAtomTypes(testedAtomTypes, expectedTypes8, mol8);

        String molName9 = "Fe_3plus";
        IAtomContainer mol9 = getMolFromFile(molName9);
        String[] expectedTypes9 = {"Fe.3plus"}; // Fix30
        assertAtomTypes(testedAtomTypes, expectedTypes9, mol9);

        String molNameA = "Fe_2";
        IAtomContainer molA = getMolFromFile(molNameA);
        String[] expectedTypesA = {"C.sp3", "Fe.2", "C.sp3"}; // Fix32
        assertAtomTypes(testedAtomTypes, expectedTypesA, molA);

    }

    @Test
    public void fix_Co() throws Exception {

        String molName10 = "Co_3plus";
        IAtomContainer mol10 = getMolFromFile(molName10);
        String[] expectedTypes10 = {"Co.3plus"}; // Fix134
        assertAtomTypes(testedAtomTypes, expectedTypes10, mol10);

        String molName11 = "Co_metallic";
        IAtomContainer mol11 = getMolFromFile(molName11);
        String[] expectedTypes11 = {"Co.metallic"}; // Fix135
        assertAtomTypes(testedAtomTypes, expectedTypes11, mol11);

        String molName0 = "Co_plus_6";
        IAtomContainer mol0 = getMolFromFile(molName0);
        String[] expectedTypes0 = {"Co.plus.6", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; //Fix9
        assertAtomTypes(testedAtomTypes, expectedTypes0, mol0);

        String molName1 = "Co_2plus";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Co.2plus", "C.sp3"}; // Fix10
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "Co_plus_2";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"Co.plus.2", "C.sp3", "C.sp3"}; // Fix11
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

        String molName3 = "Co_2";
        IAtomContainer mol3 = getMolFromFile(molName3);
        String[] expectedTypes3 = {"C.sp3", "C.sp3", "Co.2"}; //Fix12
        assertAtomTypes(testedAtomTypes, expectedTypes3, mol3);

        String molName4 = "Co_6";
        IAtomContainer mol4 = getMolFromFile(molName4);
        String[] expectedTypes4 = {"Co.6", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; //Fix14
        assertAtomTypes(testedAtomTypes, expectedTypes4, mol4);

        String molName5 = "Co_plus_4";
        IAtomContainer mol5 = getMolFromFile(molName5);
        String[] expectedTypes5 = {"Co.plus.4", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix15
        assertAtomTypes(testedAtomTypes, expectedTypes5, mol5);

        String molName6 = "Co_4";
        IAtomContainer mol6 = getMolFromFile(molName6);
        String[] expectedTypes6 = {"Co.4", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix16
        assertAtomTypes(testedAtomTypes, expectedTypes6, mol6);

        String molName7 = "Co_plus_5";
        IAtomContainer mol7 = getMolFromFile(molName7);
        String[] expectedTypes7 = {"Co.plus.5", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix17
        assertAtomTypes(testedAtomTypes, expectedTypes7, mol7);

        String molName8 = "Co_plus_1";
        IAtomContainer mol8 = getMolFromFile(molName8);
        String[] expectedTypes8 = {"C.sp3", "Co.plus.1"}; // Fix18
        assertAtomTypes(testedAtomTypes, expectedTypes8, mol8);

        String molName9 = "Co_1";
        IAtomContainer mol9 = getMolFromFile(molName9);
        String[] expectedTypes9 = {"Co.1", "C.sp3"}; // Fix19
        assertAtomTypes(testedAtomTypes, expectedTypes9, mol9);

    }

    @Test
    public void fix_Pb() throws Exception {

        String molName = "Pb_1";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Pb.1", "C.sp2"}; // Fix116
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Pb_2plus";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Pb.2plus"}; // Fix54
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "Pb_neutral";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"Pb.neutral"}; // Fix53
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

    }

    @Test
    public void fix_Se() throws Exception {

        String molName1 = "Se_sp3d1_4";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Se.sp3d1.4", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix133
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName = "Se_sp3_4";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Se.sp3.4", "C.sp3", "C.sp3", "C.sp2", "C.sp2"}; // Fix132
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName2 = "Se_sp2_2";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"Se.sp2.2", "C.sp2", "C.sp2"}; // Fix131
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

        String molName3 = "Se_1";
        IAtomContainer mol3 = getMolFromFile(molName3);
        String[] expectedTypes3 = {"C.sp2", "Se.1"}; // Fix58
        assertAtomTypes(testedAtomTypes, expectedTypes3, mol3);

        String molName4 = "Se_2";
        IAtomContainer mol4 = getMolFromFile(molName4);
        String[] expectedTypes4 = {"Se.2", "C.sp3", "C.sp3"}; // Fix58
        assertAtomTypes(testedAtomTypes, expectedTypes4, mol4);

        String molName5 = "Se_3";
        IAtomContainer mol5 = getMolFromFile(molName5);
        String[] expectedTypes5 = {"C.sp3", "Se.3", "C.sp3", "C.sp2"}; // Fix59
        assertAtomTypes(testedAtomTypes, expectedTypes5, mol5);

        String molName6 = "Se_4plus";
        IAtomContainer mol6 = getMolFromFile(molName6);
        String[] expectedTypes6 = {"Se.4plus"}; // Fix108
        assertAtomTypes(testedAtomTypes, expectedTypes6, mol6);

        String molName7 = "Se_plus_3";
        IAtomContainer mol7 = getMolFromFile(molName7);
        String[] expectedTypes7 = {"C.sp3", "Se.plus.3", "C.sp3", "C.sp3"}; // Fix61
        assertAtomTypes(testedAtomTypes, expectedTypes7, mol7);

        String molName8 = "Se_5";
        IAtomContainer mol8 = getMolFromFile(molName8);
        String[] expectedTypes8 = {"Se.5", "C.sp2", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix130
        assertAtomTypes(testedAtomTypes, expectedTypes8, mol8);

    }

    @Test
    public void fix_P() throws Exception {

        String molName1 = "P_ine";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"P.ine", "C.sp3", "C.sp3", "C.sp3"}; // earlier
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "P_irane";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"P.irane", "C.sp2", "C.sp3"}; // earlier
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

        String molName3 = "P_1";
        IAtomContainer mol3 = getMolFromFile(molName3);
        String[] expectedTypes3 = {"P.1", "C.sp"}; // earlier
        assertAtomTypes(testedAtomTypes, expectedTypes3, mol3);

    }

    @Test
    public void fix_Pt() throws Exception {

        String molName1 = "Pt_2";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Pt.2", "C.sp3", "C.sp3"}; // earlier
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "Pt_2plus_4";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"Pt.2plus.4", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix56
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

    }

    @Test
    public void fix_Mg() throws Exception {

        String molName = "Mg_neutral_2";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"C.sp3", "Mg.neutral.2", "C.sp3"}; // Fix46
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Mg_neutral_4";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"C.sp3", "C.sp3", "Mg.neutral.4", "C.sp3", "C.sp3"}; // Fix45
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "Mg_neutral_1";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"Mg.neutral.1", "C.sp2"}; // Fix47
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

    }

    @Test
    public void fix_Gd() throws Exception {

        String molName = "Gd_3plus";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Gd.3plus"}; // Fix78
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

    }

    @Test
    public void fix_Mn() throws Exception {

        String molName = "Mn_3plus";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Mn.3plus"}; // earlier
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Mn_2";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Mn.2", "C.sp3", "C.sp3"}; // Fix4
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "Mn_metallic";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"Mn.metallic"}; // Fix2
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

    }

    @Test
    public void fix_Cd() throws Exception {

        String molName = "Cd_2plus";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Cd.2plus"}; // Fix34
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Cd_2";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Cd.2", "C.sp3", "C.sp3"}; // Fix115
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "Cd_metallic";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"Cd.metallic"}; // Fix33
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);
    }

    @Test
    public void fix_Si() throws Exception {

        String molName = "Si_2minus_6";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Si.2minus.6", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix64
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Si_3";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Si.3", "C.sp2", "C.sp3", "C.sp3"}; // Fix64
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "Si_2";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"Si.2", "C.sp2", "C.sp2"}; // Fix60
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

    }

    @Test
    public void fix_As() throws Exception {

        String molName = "As_minus";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"As.minus", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix42
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "As_3plus";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"As.3plus"}; // Fix41
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "As_2";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"C.sp3", "As.2", "C.sp2"}; // Fix40
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

        String molName3 = "As_5";
        IAtomContainer mol3 = getMolFromFile(molName3);
        String[] expectedTypes3 = {"As.5", "C.sp3", "C.sp3", "C.sp3", "C.sp2"}; // Fix39
        assertAtomTypes(testedAtomTypes, expectedTypes3, mol3);
    }

    @Test
    public void fix_Tl() throws Exception {

        String molName = "Tl_neutral";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Tl"}; // Fix106
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Tl_1";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"C.sp3", "Tl.1"}; // Fix114
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "Tl_plus";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"Tl.plus"}; // Fix74
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

    }

    @Test
    public void fix_Th() throws Exception {

        String molName = "Th";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Th"}; // Fix74
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

    }

    @Test
    public void fix_Pu() throws Exception {

        String molName = "Pu";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Pu"}; // Fix106
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_Cr() throws Exception {

        String molName = "Cr_neutral";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Cr.neutral"}; // Fix94
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Cr_4";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Cr.4", "C.sp2", "C.sp2", "C.sp3", "C.sp3"}; // Fix95
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "Cr_3plus";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"Cr.3plus"}; // Fix111
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

        String molName3 = "Cr_6plus";
        IAtomContainer mol3 = getMolFromFile(molName3);
        String[] expectedTypes3 = {"Cr.6plus"}; // Fix83
        assertAtomTypes(testedAtomTypes, expectedTypes3, mol3);

    }

    @Test
    public void fix_S() throws Exception {

        String molName = "S_sp3d1";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"S.sp3d1", "C.sp3", "C.sp3", "C.sp2", "C.sp3", "C.sp3"}; // changed
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "S_inyl_2";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"S.inyl.2", "C.sp2", "C.sp2"}; // changed
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "S_2minus";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"S.2minus"}; // changed
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

        String molName3 = "S_sp3";
        IAtomContainer mol3 = getMolFromFile(molName3);
        String[] expectedTypes3 = {"S.sp3", "C.sp3", "C.sp3"}; // changed
        assertAtomTypes(testedAtomTypes, expectedTypes3, mol3);
    }

    @Test
    public void fix_N() throws Exception {

        String molName = "N_2minus_sp2";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"C.sp3", "N.2minus.sp2"}; // Fix113
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "N_minus";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"C.sp3", "C.sp", "N.minus"}; // Fix49
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

    }

    @Test
    public void fix_Rb() throws Exception {

        String molName = "Rb_neutral";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Rb.neutral"}; // Fix67
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Rb_plus";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Rb.plus"}; // Fix60
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

    }

    @Test
    public void fix_Mo() throws Exception {

        String molName = "Mo_4";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Mo.4", "C.sp2", "C.sp2", "C.sp3", "C.sp3"}; // Fix8
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Mo_metallic";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Mo.metallic"}; // Fix7
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);
    }

    @Test
    public void fix_Ra() throws Exception {

        String molName = "Ra";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Ra.neutral"}; // Fix70
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

    }

    @Test
    public void fix_Na() throws Exception {

        String molName = "Na_neutral";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Na.neutral"}; // Fix63
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Na_minus";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Na.minus"}; // Fix101
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);


    }

    @Test
    public void fix_Hg() throws Exception {

        String molName = "Hg_2plus";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Hg.2plus"}; // Fix35
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Hg_plus";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Hg.plus", "C.sp3"}; // Fix36
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "Hg_metallic";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"Hg.metallic"}; // Fix37
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

        String molName3 = "Hg_1";
        IAtomContainer mol3 = getMolFromFile(molName3);
        String[] expectedTypes3 = {"Hg.1", "C.sp2"}; // Fix92
        assertAtomTypes(testedAtomTypes, expectedTypes3, mol3);

        String molName4 = "Hg_2";
        IAtomContainer mol4 = getMolFromFile(molName4);
        String[] expectedTypes4 = {"Hg.2", "C.sp3", "C.sp3"}; // Fix38
        assertAtomTypes(testedAtomTypes, expectedTypes4, mol4);
    }

    @Test
    public void fix_Ca() throws Exception {

        String molName = "Ca_2";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Ca.2", "C.sp3", "C.sp3"}; // Fix89
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Ca_1";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Ca.1", "C.sp2"}; // Fix104
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "Ca_2plus_2";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"C.sp3", "C.sp3", "Ca.2plus.2"}; // Fix88
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);
    }

    @Test
    public void fix_Ge() throws Exception {

        String molName = "Ge_3";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"C.sp3", "Ge.3", "C.sp2", "C.sp3"}; // Fix79
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_Bi() throws Exception {

        String molName = "Bi_neutral";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Bi.neutral"}; // Fix72
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Bi_3";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"C.sp3", "Bi.3", "C.sp3", "C.sp3"}; // Fix77
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);
    }

    @Test
    public void fix_Cu() throws Exception {

        String molName = "Cu_metallic";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Cu.metallic"}; // Fix3
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Cu_plus";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Cu.plus"}; // Fix23
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName3 = "Cu_1";
        IAtomContainer mol3 = getMolFromFile(molName3);
        String[] expectedTypes3 = {"Cu.1", "C.sp3",}; // Fix105
        assertAtomTypes(testedAtomTypes, expectedTypes3, mol3);
    }

    @Test
    public void fix_Ba() throws Exception {

        String molName = "Ba_2plus";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Ba.2plus"}; // Fix76
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_In() throws Exception {

        String molName = "In_3plus";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"In.3plus"}; // Fix96
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "In_3";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"In.3", "C.sp3", "C.sp3", "C.sp3"}; // Fix96
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);

        String molName2 = "In_1";
        IAtomContainer mol2 = getMolFromFile(molName2);
        String[] expectedTypes2 = {"In.1", "C.sp"}; // Fix96
        assertAtomTypes(testedAtomTypes, expectedTypes2, mol2);

        String molName3 = "In";
        IAtomContainer mol3 = getMolFromFile(molName3);
        String[] expectedTypes3 = {"In"}; // Fix96
        assertAtomTypes(testedAtomTypes, expectedTypes3, mol3);

    }

    @Test
    public void fix_Ru() throws Exception {

        String molName = "Ru_10plus_6";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Ru.10plus.6", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix102
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);

        String molName1 = "Ru_6";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Ru.6", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix82
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);
    }

    @Test
    public void fix_Zn() throws Exception {


        String molName = "Zn_metallic";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Zn.metallic"}; // Fix44
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);


        String molName1 = "Zn_1";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Zn.1", "C.sp2"}; // Fix22
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);
    }

    @Test
    public void fix_I() throws Exception {

        String molName = "I_sp3d2_3";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"I.sp3d2.3", "C.sp3", "C.sp3", "C.sp3"}; // changed
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);


        String molName1 = "I_minus_2";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"I.minus.2", "C.sp3", "C.sp3"}; // Fix earlier
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);


    }

    @Test
    public void fix_Al() throws Exception {


        String molName = "Al_3minus";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Al.3minus", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix62
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_Ni() throws Exception {


        String molName = "Ni_metallic";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Ni.metallic"}; // Fix earlier
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);


        String molName1 = "Ni_plus";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"C.sp3", "Ni.plus"}; // Fix87
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);
    }

    @Test
    public void fix_F() throws Exception {


        String molName = "F_minus_1";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"C.sp3", "F.minus.1"}; // Fix81
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_Au() throws Exception {


        String molName = "Au_1";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"C.sp3", "Au.1"}; // Fix85
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_Te() throws Exception {


        String molName = "Te_4plus";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Te.4plus"}; // Fix earlier
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_Cl() throws Exception {

        String molName = "Cl_2";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Cl.2", "C.sp3", "C.sp2"}; // Fix50
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_K() throws Exception {

        String molName = "K_neutral";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"K.neutral", "C.sp3"}; // Fix57
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_V() throws Exception {

        String molName = "V_3minus_4";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"V.3minus.4", "C.sp2", "C.sp2", "C.sp2", "C.sp2"}; // Fix84
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_Sb() throws Exception {

        String molName = "Sb_4";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"C.sp3", "Sb.4", "C.sp3", "C.sp3", "C.sp2"}; // Fix99
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);


        String molName1 = "Sb_3";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Sb.3", "C.sp3", "C.sp3", "C.sp3"}; // Fix73
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);
    }

    @Test
    public void fix_Sr() throws Exception {

        String molName = "Sr_2plus";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Sr.2plus"}; // Fix75
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_Li() throws Exception {


        String molName = "Li_neutral";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Li.neutral"}; // Fix71
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);


        String molName1 = "Li_plus";
        IAtomContainer mol1 = getMolFromFile(molName1);
        String[] expectedTypes1 = {"Li.plus"}; // Fix86
        assertAtomTypes(testedAtomTypes, expectedTypes1, mol1);
    }

    @Test
    public void fix_Br() throws Exception {


        String molName = "Br_3";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Br.3", "C.sp2", "C.sp2", "C.sp3"}; // Fix107
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);


    }

    @Test
    public void fix_B() throws Exception {


        String molName = "B_3plus";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"B.3plus", "C.sp3", "C.sp3", "C.sp3", "C.sp3"}; // Fix80
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_Ag() throws Exception {


        String molName = "Ag_neutral";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Ag.neutral"}; // Fix55
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_Ti() throws Exception {

        String molName = "Ti_2";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Ti.2", "C.sp2", "C.sp2"}; // Fixed earlier
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }

    @Test
    public void fix_Be() throws Exception {

        String molName = "Be_neutral";
        IAtomContainer mol = getMolFromFile(molName);
        String[] expectedTypes = {"Be.neutral"}; // Fix68
        assertAtomTypes(testedAtomTypes, expectedTypes, mol);
    }
}