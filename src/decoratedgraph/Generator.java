
package decoratedgraph;

import java.util.Scanner;

/**
 * @author smithk
 */
public class Generator implements Runnable {
    final private Filter receiver;
    
    public Generator (Filter f) {
        this.receiver = f;
    }
    
    @Override
    public void run() {
        receiver.write(0, 0);
        receiver.write(50, 50);
        receiver.write(100, 70);
        receiver.write(140, 20);
        
        Scanner input = new Scanner (System.in);
        while (input.hasNextInt()) {
            receiver.write (input.nextInt(), input.nextInt());
        }
        System.out.println("Done");
    }
}
