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
public class BinaryTreeNodePrinter implements BinaryTreeVisitor {
    public void visit(BinaryTree tree) {
        if (tree.left != null)
            tree.left.acceptVisitor (this);
        if (tree.right != null)
            tree.right.acceptVisitor (this);
        System.out.println(tree.fruit);
    }
    
    @Override
    public void visit(Leaf tree) {
        visit((BinaryTree)tree);
    }

}

