
package robotSim;

import processing.core.PApplet;

/**
 * @author smithk
 */
public class Robot extends Locatable implements Renderable {

    public void render(PApplet surface, int pixels) {
        surface.pushStyle();
        surface.noStroke();
        surface.fill(255, 0, 0);
        surface.sphere (pixels/4);
        surface.popStyle();
    }
    
    public void moveTo (int row, int col) {
        getLocation().removeFromHere (this);
        Universe.theGrid.getLocation (row, col).addToHere (this);
    }
    
    public void makeAMemory() {
        getLocation().addToHere (new Memory(this));
    }
}
