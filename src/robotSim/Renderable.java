/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robotSim;

import processing.core.PApplet;

/**
 *
 * @author smithk
 */
public interface Renderable {
    void render(PApplet surface, int pixels);
}
