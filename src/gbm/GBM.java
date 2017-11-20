package gbm;

/**
 * @author smithk
 */
public class GBM implements GBMControls {
    // the trick here is that, while the GBM implements the public controls, the
    // states have need for a private dispense method...
    //
    private interface DGBMControls extends GBMControls {
        public void dispense();
    }
    
    private int gumBalls;
    public int getGumBalls() {
        return gumBalls;
    }
    private final int REFILL;
    private DGBMControls currentState;

    // Really, guys? Sigh, well, yeah... if we are going to do this right. Of 
    // course, since inner classes have complete access to the enclosing class
    // fields, getters and setters are hardly necessary... But, we ought to do
    // this right. Think how proud your children will be!
    //
    private void setCurrentState(DGBMControls currentState) {
        this.currentState = currentState;
    }

    private DGBMControls getCurrentState() {
        return currentState;
    }

    // Use a private adadper class to cover all defaults.
    //
    private abstract class StateAdapter implements DGBMControls {
        public void insertQtr() { }
        public void ejectQtr() { }
        public void turnCrank() { }
        public void refill() { }
        public void dispense() { }
    }

    private class NoQtrState extends StateAdapter {
        public void insertQtr() {
            System.out.println("Quarter Accepted");
            setCurrentState(getHasQtrState());
        }
    }
    private DGBMControls noQtrState;
    private DGBMControls getNoQtrState() {
        return noQtrState;
    }

    private class HasQtrState extends StateAdapter {
        public void ejectQtr() {
            System.out.println("Quarter Returned");
            setCurrentState(getNoQtrState());
        }

        public void turnCrank() {
            System.out.println("Gumball Purchased");
            if (getGumBalls() > 2 && Math.random() < 0.25)
                setCurrentState (getWinnerState());
            else
                setCurrentState(getSoldState());
            getCurrentState().dispense();
        }
    }
    private DGBMControls hasQtrState;
    private DGBMControls getHasQtrState() {
        return hasQtrState;
    }
    
    private class SoldState extends StateAdapter implements DGBMControls {
        public void dispense() {
            GBM.this.dispense();
            if (gumBalls == 0) {
                setCurrentState(getSoldOutState());
            }
            else {
                setCurrentState(getNoQtrState());
            }
        }
    }
    private DGBMControls soldState;
    private DGBMControls getSoldState() {
        return soldState;
    }

    private class SoldOutState extends StateAdapter {
        public void refill() {
            gumBalls = REFILL;
            System.out.println("Refilled GBM with " + getGumBalls() + " Gumballs");
            setCurrentState (getNoQtrState());
        }
    }
    private DGBMControls soldOutState;
    private DGBMControls getSoldOutState() {
        return soldOutState;
    }
    
    private class WinnerState extends StateAdapter implements DGBMControls {
        public void dispense() {
            System.out.println("Congratulations!!");
            GBM.this.dispense();
            setCurrentState (getSoldState());
            getCurrentState().dispense();
        }
    }
    private DGBMControls winnerState;
    private DGBMControls getWinnerState() {
        return winnerState;
    }

    // implementation of the GBM public controls. Notice that we don't need to
    // realy "do" anything here: just defer to the current state (sort of like
    // the command pattern).
    //
    public void insertQtr() {
        getCurrentState().insertQtr();
    }

    public void ejectQtr() {
        getCurrentState().ejectQtr();
    }

    public void turnCrank() {
        getCurrentState().turnCrank();
    }

    public void refill() {
        getCurrentState().refill();
    }
    
    // we want to keep this one hidden and since it's not part of the public
    // GBMControls, we can!
    //
    private void dispense() {
        System.out.println("Gumball Issued");
        gumBalls--;
    }

    public GBM(int size) {
        REFILL = size;
        gumBalls = 0;

        // buid the machine states. These could be created on-demand (look at
        // the singleton pattern) but that seems a nuisance. Also, consider the
        // significance of creating states here, rather than just reconstructing
        // each time we return to a state!
        //
        noQtrState = new NoQtrState();
        hasQtrState = new HasQtrState();
        soldState = new SoldState();
        soldOutState = new SoldOutState();
        winnerState = new WinnerState();

        setCurrentState(getSoldOutState());
    }
}
