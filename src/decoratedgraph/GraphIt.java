package decoratedgraph;

import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

/**
 *
 * @author smithk
 */
public class GraphIt implements Filter {
    private class Datum {
        final double ind;
        final double dep;
        
        Datum (double ind, double dep) {
            this.ind = ind;
            this.dep = dep;
        }
    }
    
    private List<Datum> data;
    
    public void addDatum (double ind, double dep) {
        data.add (new Datum (ind, dep));
    }
    
    PApplet graph;

    public GraphIt() {
        data = new ArrayList();
        
        graph = new PApplet() {
            public void settings() {
                size (800, 400);
            }  
            public void setup() {
            }
            public void draw() {
                background (255, 0, 50);
                Datum prev = null;
                strokeWeight(3);
                for (Datum d : data) {
                    if (prev != null) {
                        point ((float)prev.ind, (float)prev.dep);
                        line ((float)prev.ind, (float)prev.dep, (float)d.ind, (float)d.dep);
                    }
                    prev = d;
                }
                if (prev != null)
                    point ((float)prev.ind, (float)prev.dep);
                
                noLoop();
            }
        };

        PApplet.runSketch (new String[] {""}, graph);
    }
    
    @Override
    public void write(double ind, double dep) {
        addDatum (ind, dep);
        graph.loop();
    }
}
