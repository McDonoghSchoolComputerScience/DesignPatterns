
package gbm;

/**
 * @author smithk
 */
public class LittleKid {
    public static void main(String[] args) throws Exception {
        GBM gbm = new GBM(5);
        int command;
        do {
            switch ((command = System.in.read())) {
                case 'I':
                    gbm.insertQtr();
                    break;
                case 'E':
                    gbm.ejectQtr();
                    break;
                case 'T':
                    gbm.turnCrank();
                    System.out.println("There are " + gbm.getGumBalls()
                                        + " gumballs remaining");                    
                    break;
                case 'R':
                    gbm.refill();              
                    break;
                case 'D':
                    System.out.println("Decomissioned");
                    System.exit(0);
                    break;
                case '\n':
                    continue;
            }

        } while (command > 0);
    }
}
