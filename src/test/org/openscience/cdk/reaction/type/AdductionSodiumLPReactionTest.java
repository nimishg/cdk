/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 *  Copyright (C) 2004-2007  Miguel Rojas <miguel.rojas@uni-koeln.de>
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
package org.openscience.cdk.reaction.type;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.matchers.IQueryAtomContainer;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainerCreator;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.reaction.IReactionProcess;
import org.openscience.cdk.reaction.ReactionProcessTest;
import org.openscience.cdk.reaction.type.parameters.IParameterReact;
import org.openscience.cdk.reaction.type.parameters.SetReactionCenter;
import org.openscience.cdk.tools.LonePairElectronChecker;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ReactionManipulator;

/**
 * TestSuite that runs a test for the AdductionProtonLPReactionTest.
 * Generalized Reaction: [X-] + [Na+] => X -Na.
 *
 * @cdk.module test-reaction
 */
public class AdductionSodiumLPReactionTest extends ReactionProcessTest {

	private final LonePairElectronChecker lpcheck = new LonePairElectronChecker();
	private IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
	/**
	 *  The JUnit setup method
	 */
	public  AdductionSodiumLPReactionTest()  throws Exception {
			setReaction(AdductionSodiumLPReaction.class);
	 }
	 
	 /**
	  *  The JUnit setup method
	  */
	 @Test public void testAdductionSodiumLPReaction() throws Exception {
			IReactionProcess type = new AdductionSodiumLPReaction();
			Assert.assertNotNull(type);
	 }
	 
	/**
	 * A unit test suite for JUnit for acetaldehyde. 
	 * Reaction: O=C-C-H => O(H)-C=C. 
	 * Automatically looks for active centre.
	 *
	 * @cdk.inchi InChI=1/C2H4O/c1-2-3/h2H,1H3
	 * 
	 * @return    The test suite
	 */
	@Test public void testInitiate_IMoleculeSet_IMoleculeSet() throws Exception {

		IReactionProcess type = new AdductionSodiumLPReaction();

		IMoleculeSet setOfReactants = getExampleReactants();
        
		/* initiate */
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
	    IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.FALSE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        Assert.assertEquals(1, setOfReactions.getReaction(0).getProductCount());

        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);
        
        IMolecule molecule2 = getExpectedProducts().getMolecule(0);
        
        IQueryAtomContainer queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2,queryAtom));
      
	}
	/**
	 * A unit test suite for JUnit for acetaldehyde. 
	 * Reaction: O=C-C-H => O(H)-C=C. 
	 * Manually tests for active centre..
	 *
	 * @cdk.inchi InChI=1/C2H4O/c1-2-3/h2H,1H3
	 * 
	 * @return    The test suite
	 */
	@Test public void testManuallyCentreActive() throws Exception {
		IReactionProcess type = new AdductionSodiumLPReaction();

		IMoleculeSet setOfReactants = getExampleReactants();
        IMolecule molecule = setOfReactants.getMolecule(0);

		/*manually putting the active center*/
		molecule.getAtom(0).setFlag(CDKConstants.REACTIVE_CENTER,true);
		
		/* initiate */
		List<IParameterReact> paramList = new ArrayList<IParameterReact>();
	    IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.TRUE);
        paramList.add(param);
        type.setParameterList(paramList);
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        
        Assert.assertEquals(1, setOfReactions.getReactionCount());
        Assert.assertEquals(1, setOfReactions.getReaction(0).getProductCount());

        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);

        IMolecule molecule2 = getExpectedProducts().getMolecule(0);
        
        IQueryAtomContainer queryAtom = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(product);
        Assert.assertTrue(UniversalIsomorphismTester.isIsomorph(molecule2,queryAtom));
        
	}

	/**
	 * A unit test suite for JUnit.
	 * 
	 * @cdk.inchi InChI=1/C2H4O/c1-2-3/h2H,1H3
	 * 
	 * @return    The test suite
	 */
	@Test public void testCDKConstants_REACTIVE_CENTER() throws Exception {
		IReactionProcess type  = new AdductionSodiumLPReaction();

		IMoleculeSet setOfReactants = getExampleReactants();
        IMolecule molecule = setOfReactants.getMolecule(0);

		/*manually putting the active center*/
		molecule.getAtom(0).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getAtom(1).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getAtom(2).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getAtom(4).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getBond(0).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getBond(1).setFlag(CDKConstants.REACTIVE_CENTER,true);
		molecule.getBond(3).setFlag(CDKConstants.REACTIVE_CENTER,true);
		
		List<IParameterReact> paramList = new ArrayList<IParameterReact>();
	    IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.TRUE);
        paramList.add(param);
        type.setParameterList(paramList);
        
        /* initiate */
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);

        IMolecule reactant = setOfReactions.getReaction(0).getReactants().getMolecule(0);
		Assert.assertTrue(molecule.getAtom(0).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(reactant.getAtom(0).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(molecule.getAtom(1).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(reactant.getAtom(1).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(molecule.getAtom(2).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(reactant.getAtom(2).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(molecule.getAtom(4).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(reactant.getAtom(4).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(molecule.getBond(0).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(reactant.getBond(0).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(molecule.getBond(1).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(reactant.getBond(1).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(molecule.getBond(3).getFlag(CDKConstants.REACTIVE_CENTER));
		Assert.assertTrue(reactant.getBond(3).getFlag(CDKConstants.REACTIVE_CENTER));
	}

	/**
	 * A unit test suite for JUnit.
	 *  
	 * @cdk.inchi InChI=1/C2H4O/c1-2-3/h2H,1H3
	 * 
	 * @return    The test suite
	 */
	@Test public void testMapping() throws Exception {
		IReactionProcess type = new AdductionSodiumLPReaction();
		
		IMoleculeSet setOfReactants = getExampleReactants();
        IMolecule molecule = setOfReactants.getMolecule(0);

		/*automatic looking for active center*/
        List<IParameterReact> paramList = new ArrayList<IParameterReact>();
	    IParameterReact param = new SetReactionCenter();
        param.setParameter(Boolean.FALSE);
        paramList.add(param);
        type.setParameterList(paramList);
		/* initiate */
		
        IReactionSet setOfReactions = type.initiate(setOfReactants, null);
        
        IMolecule product = setOfReactions.getReaction(0).getProducts().getMolecule(0);

        Assert.assertEquals(8,setOfReactions.getReaction(0).getMappingCount());
        
        IAtom mappedProductA0 = (IAtom)ReactionManipulator.getMappedChemObject(setOfReactions.getReaction(0), molecule.getAtom(0));
        Assert.assertEquals(mappedProductA0, product.getAtom(0));
   	
	}
	/**
	 * Get the Acetaldehyde structure.
	 * 
	 * @cdk.inchi InChI=1/C2H4O/c1-2-3/h2H,1H3
	 * 
	 * @return The IMoleculeSet
	 */
	private IMoleculeSet getExampleReactants() {
		IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newInstance(IMoleculeSet.class);
		IMolecule molecule = builder.newInstance(IMolecule.class);
        molecule.addAtom(builder.newInstance(IAtom.class,"O"));
        molecule.addAtom(builder.newInstance(IAtom.class,"C"));
        molecule.addBond(0, 1, IBond.Order.DOUBLE);
        molecule.addAtom(builder.newInstance(IAtom.class,"C"));
        molecule.addBond(1, 2, IBond.Order.SINGLE);
        molecule.addAtom(builder.newInstance(IAtom.class,"H"));
        molecule.addAtom(builder.newInstance(IAtom.class,"H"));
        molecule.addAtom(builder.newInstance(IAtom.class,"H"));
        molecule.addAtom(builder.newInstance(IAtom.class,"H"));
        molecule.addBond(1, 3, IBond.Order.SINGLE);
        molecule.addBond(2, 4, IBond.Order.SINGLE);
        molecule.addBond(2, 5, IBond.Order.SINGLE);
        molecule.addBond(2, 6, IBond.Order.SINGLE);
        try {
			AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);

	    
	        lpcheck.saturate(molecule);
		} catch (CDKException e) {
			e.printStackTrace();
		}
        setOfReactants.addMolecule(molecule);
		return setOfReactants;
	}

	/**
	 * Get the expected set of molecules.
	 * 
	 * @return The IMoleculeSet
	 */
	private IMoleculeSet getExpectedProducts() {
		IMoleculeSet setOfProducts = builder.newInstance(IMoleculeSet.class);
		IMolecule molecule = builder.newInstance(IMolecule.class);
        molecule.addAtom(builder.newInstance(IAtom.class,"O"));
        molecule.getAtom(0).setFormalCharge(1);
        molecule.addAtom(builder.newInstance(IAtom.class,"C"));
        molecule.addBond(0, 1, IBond.Order.DOUBLE);
        molecule.addAtom(builder.newInstance(IAtom.class,"C"));
        molecule.addBond(1, 2, IBond.Order.SINGLE);
        molecule.addAtom(builder.newInstance(IAtom.class,"H"));
        molecule.addAtom(builder.newInstance(IAtom.class,"H"));
        molecule.addAtom(builder.newInstance(IAtom.class,"H"));
        molecule.addAtom(builder.newInstance(IAtom.class,"H"));
        molecule.addAtom(builder.newInstance(IAtom.class,"Na"));
        molecule.addBond(1, 3, IBond.Order.SINGLE);
        molecule.addBond(2, 4, IBond.Order.SINGLE);
        molecule.addBond(2, 5, IBond.Order.SINGLE);
        molecule.addBond(2, 6, IBond.Order.SINGLE);
        molecule.addBond(0, 7, IBond.Order.SINGLE);
        try {
			AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);

	    
	        lpcheck.saturate(molecule);
		} catch (CDKException e) {
			e.printStackTrace();
		}
        
        setOfProducts.addMolecule(molecule);
		return setOfProducts;
	}
}
