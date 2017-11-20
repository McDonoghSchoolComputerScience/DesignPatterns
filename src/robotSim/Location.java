
package robotSim;

import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

/**
 * @author smithk
 */
public class Location {
    private final int row;
    private final int col;
    
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    private List<Object> foundHere;
  
    public void addToHere (Object isHere) {
        if (foundHere == null)
            foundHere = new ArrayList();
        foundHere.add (isHere);
        
        if (isHere instanceof Locatable)
            ((Locatable)isHere).setLocation (this);
    }
    
    public void removeFromHere (Object wasHere) {
        foundHere.remove (wasHere);
        
        if (wasHere instanceof Locatable)
            ((Locatable)wasHere).setLocation (null);
    }  
    
    public Location (int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public void render(PApplet surface, int pixels) {
        surface.box (pixels, 0, pixels);
        if (foundHere != null)
            for (Object isHere : foundHere) {
                if (isHere instanceof Renderable) {
                    ((Renderable)isHere).render (surface, pixels);
                }
            }
    }
}
