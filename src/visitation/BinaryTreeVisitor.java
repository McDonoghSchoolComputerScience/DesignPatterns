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
public interface BinaryTreeVisitor {
    void visit (BinaryTree tree);
    void visit (Leaf tree);
}
