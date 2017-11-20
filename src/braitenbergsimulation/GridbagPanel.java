package braitenbergsimulation;
/*
 * Copyright 2010 Douglas B. Caulkins
 * 
 * This file is part of the Braitenberg Simulation Java package.
 *     
 * The Braitenberg Simulation Java package is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * The Braitenberg Simulation Java package is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with the Braitenberg Simulation Java package.  If not, 
 * see <http://www.gnu.org/licenses/>.
 */

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

/**
 * This class is simply a JPanel that uses a GridBagLayout and has a helper method
 * for adding GridBagConstraints
 * 
 * @author Douglas B. Caulkins
 */
class GridbagPanel extends JPanel
{
	private static final long serialVersionUID = -5179867089047548460L;

	/**
	 * Constructor
	 */
	protected GridbagPanel()
	{
		setLayout(new GridBagLayout());
	}
	
	/**
	 * Helper class for adding gridbag layout components to this panel
	 * 
	 * @param comp the component to add to this panel
	 * @param gridx the x position
	 * @param gridy the y position
	 * @param gridwidth the width
	 * @param gridheight the height
	 * @param anchor the relative position with in the cell
	 */
	protected void add(Component comp, int gridx, int gridy, int gridwidth, int gridheight,
			int anchor)
	{
        double weightx = 1; 
        double weighty = 1;
        int fill = GridBagConstraints.NONE;
        Insets insets = new Insets(3, 3, 3, 3); 
        int ipadx = 0;
        int ipady = 0; 		

        add(comp, new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx,
        		weighty, anchor, fill, insets, ipadx, ipady));
	}
}
