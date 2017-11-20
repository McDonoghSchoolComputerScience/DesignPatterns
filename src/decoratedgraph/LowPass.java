
package decoratedgraph;

/**
 * @author smithk
 */
public class LowPass implements Filter {
    private Filter downStream;
    private double limit;
    private boolean passThru;
    
    LowPass (Filter downStream, double limit, boolean passThru) {
        this.downStream = downStream;
        this.limit = limit;
        this.passThru = passThru;
    }

    @Override
    public void write(double ind, double dep) {
        if (dep <= limit)
            downStream.write (ind, dep);
        else
            if (passThru)
                downStream.write (ind, limit);
    }
}
