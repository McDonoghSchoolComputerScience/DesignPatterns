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
public class Leaf extends BinaryTree {
    
    public Leaf(Object fruit) {
        super(fruit, null, null);
    }
    
    @Override
    public void acceptVisitor (BinaryTreeVisitor visitor) {
        visitor.visit (this);
    }
}
