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

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * This class contains the widgets that allow the user to configure miscellaneous settings.
 * 
 * @author Douglas B. Caulkins
 */
class MiscSettingsPanel extends GridbagPanel
{
	private static final long serialVersionUID = 3690371388344756056L;

	private static final int DEFAULTWIDTH = 900;
	private static final int MINWIDTH = 100;
	private static final int MAXWIDTH = 9999;
	private static final int INCWIDTH = 10;
	private static final int DEFAULTHEIGHT = 900;
	private static final int MINHEIGHT = 100;
	private static final int MAXHEIGHT = 9999;
	private static final int INCHEIGHT = 10;
	private static final int DEFAULTINTENSITY = 5000;
	private static final int MININTENSITY = 100;
	private static final int MAXINTENSITY = 99999;
	private static final int INCINTENSITY = 100;
	private static final int DEFAULTSLEEP = 50;
	private static final int MINSLEEP = 1;
	private static final int MAXSLEEP = 99;
	private static final int INCSLEEP = 1;

	private final JSpinner spnrDarkPlainWidth;
	private final JSpinner spnrDarkPlainHeight;
	private final JSpinner spnrLightIntensity;
	private final JSpinner spnrTimerPause;

	private final JButton btnDefault;

	/**
	 * Constructor
	 */
	MiscSettingsPanel()
	{
		spnrDarkPlainWidth = new JSpinner(new SpinnerNumberModel(DEFAULTWIDTH,
				MINWIDTH, MAXWIDTH, INCWIDTH));
		spnrDarkPlainHeight = new JSpinner(new SpinnerNumberModel(DEFAULTHEIGHT,
				MINHEIGHT, MAXHEIGHT, INCHEIGHT));

		spnrLightIntensity = new JSpinner(new SpinnerNumberModel(DEFAULTINTENSITY,
				MININTENSITY, MAXINTENSITY, INCINTENSITY));
		spnrTimerPause = new JSpinner(new SpinnerNumberModel(DEFAULTSLEEP,
				MINSLEEP, MAXSLEEP, INCSLEEP));

		btnDefault = new JButton("Default Values");
		
		/* Layout all the components */
		layoutComponents();
		addListeners();
	}
	
	/**
	 * Layout the components on the screen in an understandable and aesthetically pleasing manner
	 */
	private void layoutComponents()
	{
		GridbagPanel pnlMisc = new GridbagPanel();

		JLabel lblDarkPlainWidth = new JLabel("Dark Plain Width :");
		JLabel lblDarkPlainHeight = new JLabel("Dark Plain Height :");
		JLabel lblLightIntensity = new JLabel("Light Intensity :");
		JLabel lblTimerPause = new JLabel("Timer Pause (ms) :");
		pnlMisc.add(lblDarkPlainWidth, 0, 0, 1, 1, GridBagConstraints.WEST);
		pnlMisc.add(lblDarkPlainHeight, 0, 1, 1, 1, GridBagConstraints.WEST);
		pnlMisc.add(lblLightIntensity, 0, 2, 1, 1, GridBagConstraints.WEST);
		pnlMisc.add(lblTimerPause, 0, 3, 1, 1, GridBagConstraints.WEST);

		pnlMisc.add(spnrDarkPlainWidth, 1, 0, 1, 1, GridBagConstraints.EAST);
		pnlMisc.add(spnrDarkPlainHeight, 1, 1, 1, 1, GridBagConstraints.EAST);
		pnlMisc.add(spnrLightIntensity, 1, 2, 1, 1, GridBagConstraints.EAST);
		pnlMisc.add(spnrTimerPause, 1, 3, 1, 1, GridBagConstraints.EAST);
		
		JPanel pnlButton = new JPanel();
		pnlButton.add(btnDefault);
		
		pnlMisc.add(pnlButton, 1, 4, 1, 1, GridBagConstraints.EAST);

		add(pnlMisc, 0, 0, 1, 1, GridBagConstraints.NORTHWEST);
	}

	/**
	 * Add listeners to various components
	 */
	private void addListeners()
	{
		btnDefault.addActionListener(new AbstractAction() 
		{
			private static final long serialVersionUID = 2610860964134047183L;

			@Override
			public void actionPerformed(ActionEvent aevt) 
			{
				setDefaults();
			}
		});
	}

	/* Mutators */
	
	/**
	 * Set all the components to default values
	 */
	void setDefaults()
	{
		spnrDarkPlainWidth.setValue(Integer.valueOf(DEFAULTWIDTH));
		spnrDarkPlainHeight.setValue(Integer.valueOf(DEFAULTHEIGHT));
		spnrLightIntensity.setValue(Integer.valueOf(DEFAULTINTENSITY));
		spnrTimerPause.setValue(Integer.valueOf(DEFAULTSLEEP));
	}
	
	/**
	 * Set the width of the dark plain
	 * 
	 * @param width the width of the dark plain, in pixels
	 */
	void setDarkPlainWidth(int width)
	{
		spnrDarkPlainWidth.setValue(Integer.valueOf(width));
	}
	
	/**
	 * Set the height of the dark plain
	 * 
	 * @param height the height of the dark plain, in pixels
	 */
	void setDarkPlainHeight(int height)
	{
		spnrDarkPlainHeight.setValue(Integer.valueOf(height));
	}
	
	/**
	 * Set the light intensity
	 * 
	 * @param intensity the light intensity
	 */
	void setLightIntensity(int intensity)
	{
		spnrLightIntensity.setValue(Integer.valueOf(intensity));
	}
	
	/**
	 * Set the number of microseconds the timer sleeps
	 * 
	 * @param pause the number of microseconds the timer sleeps
	 */
	void setTimerPause(int pause)
	{
		spnrTimerPause.setValue(Integer.valueOf(pause));
	}

	/* Accessors */
	
	/**
	 * Get the width of the dark plain
	 * 
	 * @return the width of the dark plain, in pixels
	 */
	int getDarkPlainWidth()
	{
		return ((Number) spnrDarkPlainWidth.getValue()).intValue();
	}
	
	/**
	 * Get the height of the dark plain
	 * 
	 * @return the height of the dark plain, in pixels
	 */
	int getDarkPlainHeight()
	{
		return ((Number) spnrDarkPlainHeight.getValue()).intValue();
	}
	
	/**
	 * Get the light intensity
	 * 
	 * @return the light intensity
	 */
	int getLightIntensity()
	{
		return ((Number) spnrLightIntensity.getValue()).intValue();
	}
	
	/**
	 * Get the number of microseconds the timer sleeps
	 * 
	 * @return the number of microseconds the timer sleeps
	 */
	int getTimerPause()
	{
		return ((Number) spnrTimerPause.getValue()).intValue();
	}
}