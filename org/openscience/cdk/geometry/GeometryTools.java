/* GeometryTools.java
 * 
 * $RCSfile$    $Author$    $Date$    $Revision$
 * 
 * Copyright (C) 1997-2000  The CompChem project
 * 
 * Contact: steinbeck@ice.mpg.de, gezelter@maul.chem.nd.edu, egonw@sci.kun.nl
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All I ask is that proper credit is given for my work, which includes
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *  */
package org.openscience.cdk.geometry;


import javax.vecmath.*;
import org.openscience.cdk.*;
import java.awt.Dimension;
import java.util.Vector;

public class GeometryTools
{

	/**
	 * Adds an automatically calculated offset to the coordinates of all atoms
	 * such that all coordinates are positive and the smallest x or y coordinate 
	 * is exactly zero.
	 *
	 * @param   molecule for which all the atoms are translated to positive coordinates
	 */
	public static void translateAllPositive(Molecule molecule)
	{
		double transX = 0,transY = 0;
		for (int i = 0; i < molecule.getAtomCount(); i++)
		{
			if (molecule.getAtomAt(i).getPoint2D() != null)
			{
				if (molecule.getAtomAt(i).getPoint2D().x < transX)
				{
					transX = molecule.getAtomAt(i).getPoint2D().x;
				}
				if (molecule.getAtomAt(i).getPoint2D().y < transY)
				{
					transY = molecule.getAtomAt(i).getPoint2D().y;
				}
			}
		}
		translate2D(molecule,transX * -1,transY * -1);		
	}
	

	/**
	 * Translates the given molecule by the given Vector.
	 *
	 * @param   molecule  The molecule to be translated
	 * @param   transX  translation in x direction
	 * @param   transY  translation in y direction
	 */
	public static void translate2D(Molecule molecule, double transX, double transY)
	{
		translate2D(molecule, new Vector2d(transX, transY));
	}
	

	/**
	 * Multiplies all the coordinates of the atoms of the given molecule with the scalefactor.
	 *
	 * @param   molecule  The molecule to be scaled
	 */
	public static void scaleMolecule(Molecule molecule, double scaleFactor)
	{
		for (int i = 0; i < molecule.getAtomCount(); i++)
		{
			if (molecule.getAtomAt(i).getPoint2D() != null)
			{
			
				molecule.getAtomAt(i).getPoint2D().x *= scaleFactor;
				molecule.getAtomAt(i).getPoint2D().y *= scaleFactor;
			}
		}
	}
	
    /**
	 * Scales a molecule such that it fills a given percentage of a given dimension
	 *
	 * @param   molecule  The molecule to be scaled
	 * @param   dim       The dimension to be filled
	 * @param   percentage  The percentage of the dimension to be filled
	 */
	public static void scaleMolecule(Molecule molecule, Dimension areaDim, double fillFactor)
	{
		Dimension molDim = get2DDimension(molecule);
		double widthFactor = (double)areaDim.width / (double)molDim.width;
		double heightFactor = (double)areaDim.height / (double)molDim.height;
		double scaleFactor = Math.min(widthFactor, heightFactor) * fillFactor;
		scaleMolecule(molecule, scaleFactor);
	}

	/**
	 * Returns the java.awt.Dimension of a molecule
	 *
	 * @param   molecule of which the dimension should be returned
	 * @return The java.awt.Dimension of this molecule
	 */
	public static Dimension get2DDimension(Molecule molecule)
	{
		double xOffset = 0, yOffset = 0;
		double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
		for (int i = 0; i < molecule.getAtomCount(); i++)
		{
			if (molecule.getAtomAt(i).getPoint2D() != null)
			{
		
				if (molecule.getAtomAt(i).getX2D() > maxX)
					maxX = molecule.getAtomAt(i).getX2D();
				if (molecule.getAtomAt(i).getY2D() > maxY)
					maxY = molecule.getAtomAt(i).getY2D();
				if (molecule.getAtomAt(i).getX2D() < minX)
					minX = molecule.getAtomAt(i).getX2D();
				if (molecule.getAtomAt(i).getY2D() < minY)
					minY = molecule.getAtomAt(i).getY2D();
			}	
		}
		return new Dimension((int)(maxX - minX + 1),
			(int)(maxY - minY + 1));
	}


	/**
	 * Translates a molecule (i.e. its atom with the smallest coordinates)
	 *  to the orgin (0,0) and the translates it by the vector given by the dimension
	 *
	 * @param   molecule to be translated	
	 * @param   dimension that represents the translation vector 
	 */
	public static void translate2D(Molecule molecule, Vector2d vector)
	{
		for (int i = 0; i < molecule.getAtomCount(); i++)
		{
			if (molecule.getAtomAt(i).getPoint2D() != null)
			{
				molecule.getAtomAt(i).getPoint2D().add(vector);
			}
		}
	}
	
	/**
	 * Centers the molecule in the given area
	 *
	 * @param   molecule to be centered
	 * @param   areaDim in which the molecule is to be centered
	 */
	public static void center(Molecule molecule, Dimension areaDim)
	{
		Dimension molDim = get2DDimension(molecule);		
		int transX = (int)((areaDim.width - molDim.width) / 2);
		int transY = (int)((areaDim.height - molDim.height) / 2);
		translate2D(molecule, new Vector2d(transX, transY));
	}
	

	/**
	 * Calculates the center of the given atoms and returns it as a Point2d
	 *
	 * @param   atoms  The vector of the given atoms
	 * @return     The center of the given atoms as Point2d
	 */
	public static Point2d get2DCenter(Vector atoms)
	{
		Atom atom; 
		double x = 0, y = 0;
		for (int f = 0; f < atoms.size(); f++)
		{
			atom = (Atom)atoms.elementAt(f);
			if (atom.getPoint2D() != null)
			{
				x += atom.getX2D();
				y += atom.getY2D();						
			}
		}
		return new Point2d(x/(double)atoms.size(), y/(double)atoms.size());
	}


	public static double getAngle(double xDiff, double yDiff)
	{
		double angle = 0; 
//		System.out.println("getAngle->xDiff: " + xDiff);
//		System.out.println("getAngle->yDiff: " + yDiff);		
		if (xDiff >= 0 && yDiff >= 0)
		{
		    angle = Math.atan(yDiff / xDiff);
		}
		else if (xDiff < 0 && yDiff >= 0)
		{
			angle = Math.PI + Math.atan(yDiff / xDiff);
		}
		else if (xDiff < 0 && yDiff < 0)
		{
		    angle = Math.PI + Math.atan(yDiff / xDiff);
		}
		else if (xDiff >= 0 && yDiff < 0)
		{
		    angle = 2 * Math.PI + Math.atan(yDiff / xDiff);
		}
		return angle;
	}
	/**
	 * Gets the coordinates of two points (that represent a bond) and
	 * calculates for each the coordinates of two new points that have the given
	 * distance vertical to the bond.
	 *
	 * @param   coords  The coordinates of the two given points of the bond
	 * @param   dist  The vertical distance between the given points and those to be calculated
	 * @return     The coordinates of the calculated four points
	 */
	public static int[] distanceCalculator(int[] coords,double dist)
	{
		double angle;
		if ((coords[2] - coords[0]) == 0) angle = Math.PI/2;
		else
		{
			angle = Math.atan(((double)coords[3] - (double)coords[1]) / ((double)coords[2] - (double)coords[0]));
		}
		int begin1X = (int)(Math.cos(angle + Math.PI/2) * dist + coords[0]);
		int begin1Y = (int)(Math.sin(angle + Math.PI/2) * dist + coords[1]);
		int begin2X = (int)(Math.cos(angle - Math.PI/2) * dist + coords[0]);
		int begin2Y = (int)(Math.sin(angle - Math.PI/2) * dist + coords[1]);
		int end1X = (int)(Math.cos(angle - Math.PI/2) * dist + coords[2]);
		int end1Y = (int)(Math.sin(angle - Math.PI/2) * dist + coords[3]);
		int end2X = (int)(Math.cos(angle + Math.PI/2) * dist + coords[2]);
		int end2Y = (int)(Math.sin(angle + Math.PI/2) * dist + coords[3]);
		
		int[] newCoords = {begin1X,begin1Y,begin2X,begin2Y,end1X,end1Y,end2X,end2Y};
		return newCoords; 
	}


	/**
	 * Writes the coordinates of the atoms participating the given bond into an array.
	 *
	 * @param   bond   The given bond 
	 * @return     The array with the coordinates
	 */
	public static int[] getBondCoordinates(Bond bond)
	{
		int beginX = (int)bond.getAtomAt(0).getPoint2D().x;
		int endX = (int)bond.getAtomAt(1).getPoint2D().x;
		int beginY = (int)bond.getAtomAt(0).getPoint2D().y;
		int endY = (int)bond.getAtomAt(1).getPoint2D().y;
		int[] coords = {beginX,beginY,endX,endY};
		return coords;
	}
	

	/**
	 * Returns the atom of the given molecule that is closest to the given 
	 * coordinates.
	 *
	 * @param   xPosition  The x coordinate
	 * @param   yPosition  The y coordinate	
	 * @param   molecule  The molecule that is searched for the closest atom
	 * @return   The atom that is closest to the given coordinates  
	 */
	public static Atom getClosestAtom(int xPosition, int yPosition, SetOfMolecules setOfMolecules)
	{
		Molecule molecule;
		Atom closestAtom = null, currentAtom;
		double smallestMouseDistance = -1, mouseDistance, atomX, atomY;
		for (int f = 0; f < setOfMolecules.getMoleculeCount(); f++)
		{
			molecule = setOfMolecules.getMolecule(f);
			for (int i = 0; i < molecule.getAtomCount(); i++)
			{
				currentAtom = molecule.getAtomAt(i);
				atomX = currentAtom.getX2D();
				atomY = currentAtom.getY2D();
				mouseDistance = Math.sqrt(Math.pow(atomX - xPosition, 2) + Math.pow(atomY - yPosition, 2));
				if (mouseDistance < smallestMouseDistance || smallestMouseDistance == -1)
				{
					smallestMouseDistance = mouseDistance;
					closestAtom = currentAtom;
				}
			}
		}
		return closestAtom;
	}
	

	/**
	 * Returns the bond of the given molecule that is closest to the given 
	 * coordinates.
	 *
	 * @param   xPosition  The x coordinate
	 * @param   yPosition  The y coordinate	
	 * @param   molecule  The molecule that is searched for the closest bond
	 * @return   The bond that is closest to the given coordinates  
	 */
	public static Bond getClosestBond(int xPosition, int yPosition, SetOfMolecules setOfMolecules)
	{
		Molecule molecule;
		Point2d bondCenter;
		Bond closestBond = null, currentBond;
		double smallestMouseDistance = -1, mouseDistance, bondCenterX, bondCenterY;
		for (int f = 0; f < setOfMolecules.getMoleculeCount(); f++)
		{
			molecule = setOfMolecules.getMolecule(f);
			for (int i = 0; i < molecule.getBondCount(); i++)
			{
				currentBond = molecule.getBondAt(i);
				bondCenter = get2DCenter(currentBond.getAtomsVector());
				mouseDistance = Math.sqrt(Math.pow(bondCenter.x - xPosition, 2) + Math.pow(bondCenter.y - yPosition, 2));
				if (mouseDistance < smallestMouseDistance || smallestMouseDistance == -1)
				{
					smallestMouseDistance = mouseDistance;
					closestBond = currentBond;
				}
			}
		}
		return closestBond;
	}

	

	/**
	 * Sorts a Vector of atoms such that the 2D distances of the 
	 * atom locations from a given point are smallest for the first
	 * atoms in the vector 
	 *
	 * @param   point  The point from which the distances to the atoms are measured
	 * @param   atoms  The atoms for which the distances to point are measured
	 */
	public static void sortBy2DDistance(Atom[] atoms, Point2d point)
	{
		double distance1, distance2;
		Atom atom1 = null, atom2 = null;
		boolean doneSomething = false;
		do
		{
			doneSomething = false;
			for (int f = 0; f < atoms.length - 1; f++)
			{
				atom1 = atoms[f];
				atom2 = atoms[f + 1];
				distance1 = point.distance(atom1.getPoint2D());				
				distance2 = point.distance(atom2.getPoint2D());
				if (distance2 < distance1)
				{
					atoms[f] = atom2;
					atoms[f + 1] = atom1;					
					doneSomething = true;
				}								
			}
		}while(doneSomething);
	}
}



