/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beanybabies;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.List;

/**
 *
 * @author smithk
 */
public class Constrained<T> {
    private List<VetoableChangeListener> vcls;
    public void addVetoableChangeListener (VetoableChangeListener vcl) {
        vcls.add (vcl);
    }
    public void removeVetoableChangeListener (VetoableChangeListener vcl) {
        vcls.remove (vcl);
    }
    
    private List<PropertyChangeListener> pcls;
    public void addPropertyChangeListener (PropertyChangeListener pcl) {
        pcls.add (pcl);
    }
    public void removePropertyChangeListener (PropertyChangeListener pcl) {
        pcls.remove (pcl);
    }
    
    private T cBean;
    public T getCBean() {
        return cBean;
    }
    public void setCBean (T cBean) {
        PropertyChangeEvent pce = new PropertyChangeEvent (this, "cBean", getCBean(), cBean);
        try {
            for (VetoableChangeListener vcl : vcls)
                vcl.vetoableChange (pce);
            this.cBean = cBean;
            for (PropertyChangeListener pcl : pcls)
                pcl.propertyChange(pce);
        }
        catch (PropertyVetoException pve) {
        }
    }
}
