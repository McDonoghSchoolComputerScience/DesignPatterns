
package beanybabies;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author smithk
 * 
 * instances of HiddenKey are essentially private vaults into which a referencer
 * can lock away a Key (a String, in this case) that is unattainable without 
 * having a direct reference to the instance.
 */
    public class HiddenKey {
        
    /**
     * KeyManager is a private interface--undetectable outside of the containing
     * class. The idea is that there will be one shared instance of KeyManager for
     * all instances of HiddenKey. All a HiddenKey can see of the implementing class
     * are the interface instances, further "hiding" the mechanisms.
     * 
     * The vetoableChange method is used to verify that no to instances of HiddenKey
     * wind up with the same key value.
     */
    private interface KeyManager extends VetoableChangeListener {
        void addHiddenKey (HiddenKey hk);
        void removeHiddenKey (HiddenKey hk);
        void vetoableChange (PropertyChangeEvent pce) throws PropertyVetoException;
    }
    
    /**
     * note the use of both static and private modifiers.
     */
    static private KeyManager theKeyManager;
    static {
        // a static constructor is used to initialize static class members at the
        // time the class is loaded by the JVM. 
        
        /**
         * We create an anonymous class to instantiate the KeyManager. Since it is
         * anonymous, there is no opportunity for a reference holder to even downcast
         * to obtain the implementation details. Even reflection will not be useful
         * because all of the important details are encapsulated (private).
         */
        theKeyManager = new KeyManager() {
            private List<HiddenKey> hiddenKeys = new ArrayList();
            
            public void addHiddenKey(HiddenKey hk) {
                hiddenKeys.add (hk);
            }

            public void removeHiddenKey(HiddenKey hk) {
                hiddenKeys.remove (hk);
            }

            public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {
                for (HiddenKey hk : hiddenKeys)
                    if (hk != pce.getSource() && 
                            pce.getPropertyName().equals("key") && 
                            hk.getKey() != null &&
                            hk.getKey().equals(pce.getNewValue()))
                        throw new PropertyVetoException(null, pce);
            }
        };
    }
    
    private String key;
    
    private String getKey() {
        return key;
    }
    /**
     * Note that this method cannot be overridden (final)
     * 
     * @param newKey
     * @throws PropertyVetoException 
     */
    final private void setKey(String newKey) throws PropertyVetoException {
        // passing only the newKey to the keyManager precludes the original--old--key
        // value from being leaked. Arguably, passing "this" is a potential hole that
        // ought to be filled--ideas?
        //
        theKeyManager.vetoableChange(new PropertyChangeEvent (this, "key", null, newKey));
        this.key = newKey;
    }
    
    public HiddenKey() {
        theKeyManager.addHiddenKey(this);
    }
    
    /** the remainder is test stub **/
    private String iAm;
    public HiddenKey(String iAm) {
        this();
        this.iAm = iAm;
    }
    private static void tryChange (HiddenKey hk, String change) {
        try {
            hk.setKey (change);
            System.out.println(hk.iAm + " key successfully changed to " + change);
        }
        catch (PropertyVetoException pve) {
            System.out.println(hk.iAm + " key change to " + change + " vetoed");
        }
    }
    
    public static void main(String[] args) {
        HiddenKey k1 = new HiddenKey("k1");
        HiddenKey k2 = new HiddenKey("k2");
        HiddenKey k3 = new HiddenKey("k3");
        
        tryChange (k1, "one");
        tryChange (k2, "one");
        tryChange (k2, "two");
        tryChange (k3, "three");
        tryChange (k3, "three");
        tryChange (k2, "three");
        tryChange (k3, "four");
        tryChange (k2, "three");
    }
}