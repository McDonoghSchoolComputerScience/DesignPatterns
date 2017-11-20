
package robotSim;

import processing.core.PApplet;
import processing.core.PSurface;

/**
 * @author smithk
 */
public class Universe extends PApplet {
    public static Grid theGrid;
    private Robot george;
    
    public void settings() {
        size (600, 300, P3D);
    }
    
    public void setup() {
        theGrid = new Grid (12, 12);
        theGrid.getLocation(10, 2).addToHere (new Wall());
        theGrid.getLocation(9, 2).addToHere (new Wall());
        theGrid.getLocation(8, 3).addToHere (new Wall(true));
        theGrid.getLocation(8, 4).addToHere (new Wall(true));
        theGrid.getLocation(8, 2).addToHere (new Wall());
        theGrid.getLocation(8, 2).addToHere (new Wall(true));

        george = new Robot();
        theGrid.getLocation(9, 4).addToHere (george);
    }
    
    public void draw() {
        background (100);
        theGrid.render(this, 60);
    }
    
    public void keyPressed() {
        if (key == CODED) {
            int row = george.getLocation().getRow();
            int col = george.getLocation().getCol();

            switch (keyCode) {
                case UP: row--; break;
                case DOWN: row++; break;
                case RIGHT: col++; break;
                case LEFT: col--; break;
            }
        
            row = min (max (0, row), theGrid.getRows()-1);
            col = min (max (0, col), theGrid.getCols()-1);
            
            george.moveTo (row, col);
        
            george.getLocation().removeFromHere(george);
            theGrid.getLocation(row, col).addToHere (george);
        }
    }
    
    public static void main(String[] args) {
        PApplet.main (new String[] {"robotSim.Universe"});
    }
}
