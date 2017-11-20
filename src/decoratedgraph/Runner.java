
package decoratedgraph;

/**
 * @author smithk
 */
public class Runner {
    public static void main(String[] args) {
        Filter g = new GraphIt();
        g = new LowPass (g, 50, true);
        
        new Thread (new Generator (g)).start();
        
        System.out.println("Doing other stuff");
    }
}
