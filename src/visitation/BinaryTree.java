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
public class BinaryTree implements AcceptsBinaryTreeVisitors {
    BinaryTree left;
    BinaryTree right;
    Object fruit;
    
    BinaryTree (Object fruit, BinaryTree left, BinaryTree right) {
        this.fruit = fruit;
        this.left = left;
        this.right = right;
    }

    @Override
    public void acceptVisitor(BinaryTreeVisitor visitor) {
        visitor.visit (this);
    }
}