/* Generated By:JJTree: Do not edit this line. ASTChirality.java */

package org.openscience.cdk.smiles.smarts.parser;

public class ASTChirality extends SimpleNode {
  public ASTChirality(int id) {
    super(id);
  }

  public ASTChirality(SMARTSParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SMARTSParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}