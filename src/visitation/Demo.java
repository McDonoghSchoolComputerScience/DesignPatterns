package visitation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author smithk
 */
public class Demo {
        
    public static void main(String[] args) {
        BinaryTree bt = new BinaryTree ("first", 
                                        new Leaf ("second"), 
                                        new BinaryTree ("Third", 
                                                        new Leaf ("fourth"),
                                                        null));
        
        bt.acceptVisitor (new BinaryTreeNodePrinter());
        bt.acceptVisitor (new BinaryTreeLeafLabeler());
        bt.acceptVisitor (new BinaryTreeNodePrinter());
    }
}
