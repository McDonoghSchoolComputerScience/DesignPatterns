
package robotSim;

import processing.core.PApplet;

/**
 * @author smithk
 * 
 * The Grid is a two-dimensional space that represents the set of discrete
 * locations that can be occupied by a Robot. Each location in the Grid is
 * represented by an instance of Location. When the Grid is rendered, each
 * Location is iteratively rendered within a determined space.
 */
public class Grid {
    private Location[][] theGrid;

    public int getRows() {
        return theGrid.length;
    }
    
    public int getCols() {
        return theGrid[0].length;
    }
    
    public Location getLocation (int row, int col) {
        return theGrid[row][col];
    }
        
    public Grid (int rows, int cols) {
        theGrid = new Location[rows][cols];
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                theGrid[row][col] = new Location(row, col);
    }
    
    /**
     * A Grid is rendered on the z/x plane as rows/columns with the zero row
     * placed in the distant z and columns left to right (increasing x). The
     * "last" row will be oriented such that the lower edge of the grid will
     * be at z = 0.
     * 
     * @param pixels specifies
     */
    public void render (PApplet surface, int pixels) {
        int z = - pixels * theGrid.length;
        for (Location[] row : theGrid) {
            int x = 0;
            for (Location location : row) {
                surface.pushMatrix ();
                surface.translate (x, surface.height, z);
                location.render (surface, pixels);
                surface.popMatrix ();
                x += pixels;
            }
            z += pixels;
        }
    }
}
