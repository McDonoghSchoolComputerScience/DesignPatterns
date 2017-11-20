
package robotSim;

import processing.core.PApplet;

/**
 * @author smithk
 */
public class Wall extends Locatable implements Renderable {
    private final boolean horizontal;
    
    public Wall (boolean isHorizontal) {
        horizontal = isHorizontal;
    }
    public Wall () {
        this (false);
    }
    
    @Override
    public void render(PApplet surface, int pixels) {
        surface.translate (0, -pixels/4, 0);
        if (horizontal)
            surface.box (pixels, pixels/2, pixels/4);
        else
            surface.box (pixels/4, pixels/2, pixels);
        surface.translate (0, pixels/4, 0);
    }

}
