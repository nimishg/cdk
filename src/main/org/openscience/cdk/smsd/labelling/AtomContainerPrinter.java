/* Copyright (C) 2010  Gilleain Torrance <gilleain.torrance@gmail.com>
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

package org.openscience.cdk.smsd.labelling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

/**
 * @cdk.module smsd
 * @cdk.githash
 */

public class AtomContainerPrinter {
    
    private class Edge implements Comparable<Edge> {
        public String firstString;
        public String lastString;
        public int first;
        public int last;
        public int order;
        public Edge(int first, int last, int order, String firstString, String lastString) {
            this.first = first;
            this.last = last;
            this.order = order;
            this.firstString = firstString;
            this.lastString = lastString;
        }
        
        @Override
        public int compareTo(Edge o) {
            if (first < o.first || (first == o.first && last < o.last)) {
                return -1;
            } else {
                return 1;
            }
        }
        
        public String toString() {
            return firstString + first + ":" + lastString + last + "(" + order + ")";
        }
    }
    
    public String toString(IAtomContainer atomContainer) {
        StringBuffer sb = new StringBuffer();
        for (IAtom atom : atomContainer.atoms()) {
            sb.append(atom.getSymbol());
        }
        sb.append(" ");
        List<Edge> edges = new ArrayList<Edge>();
        for (IBond bond : atomContainer.bonds()) {
            IAtom a0 = bond.getAtom(0);
            IAtom a1 = bond.getAtom(1);
            int a0N = atomContainer.getAtomNumber(a0);
            int a1N = atomContainer.getAtomNumber(a1);
            String a0S = a0.getSymbol();
            String a1S = a1.getSymbol();
            int o = bond.getOrder().ordinal();
            if (a0N < a1N) {
                edges.add(new Edge(a0N, a1N, o, a0S, a1S));
            } else {
                edges.add(new Edge(a1N, a0N, o, a1S, a0S));
            }
        }
        Collections.sort(edges);
        sb.append(edges.toString());
        return sb.toString();
    }

}
