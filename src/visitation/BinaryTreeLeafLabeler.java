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
public class BinaryTreeLeafLabeler implements BinaryTreeVisitor {

    @Override
    public void visit(BinaryTree tree) {
        if (tree.left != null)
            tree.left.acceptVisitor (this);
        if (tree.right != null)
            tree.right.acceptVisitor (this);
    }

    @Override
    public void visit(Leaf tree) {
        tree.fruit = "Leafy " + tree.fruit.toString();
    }
    
}
