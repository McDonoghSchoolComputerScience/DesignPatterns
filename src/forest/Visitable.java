/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forest;

/**
 *
 * @author smithk
 */
public interface Visitable {
    void acceptVisitor (Visitor guest);
}
