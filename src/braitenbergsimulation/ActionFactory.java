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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class supplies the various actions for the simulation.
 * 
 * @author Douglas B. Caulkins
 */
class ActionFactory 
{
	private final JFrame parentFrame;
	private final DarkPlainFramePanel pnlDarkPlainFrame;
	private final ConfigurationPanel pnlConfiguration;
	private final PresetPanel pnlPreset;

	private final Action actRestart;
	private final Action actExit;
	private final Action actConfiguration;
	private final Action actPreset;
	private final Action actExplanation;
	private final Action actLicense;
	private final Action actAbout;
	
	private JDialog dlgConfiguration;
	private JDialog dlgPreset;
	private JDialog dlgExplanation;
	private JDialog dlgLicense;
	private JDialog dlgAbout;

	/**
	 * Constructor
	 * 
	 * @param prntFrame the parent frame
	 * @param pnlDrkFrm the panel containing the dark plain
	 * @param pnlConfig the panel containing various configured settings 
	 */
	ActionFactory(JFrame prntFrm, DarkPlainFramePanel pnlDrkFrm, 
			ConfigurationPanel pnlConfig, PresetPanel pnlPrst)
	{
		parentFrame = prntFrm;
		pnlDarkPlainFrame = pnlDrkFrm;
		pnlConfiguration = pnlConfig;
		pnlPreset = pnlPrst;
		
		actRestart = new RestartAction();
		actExit = new ExitAction();
		actConfiguration = new ConfigurationAction();
		actPreset = new PresetAction();
		actExplanation = new ExplanationAction();
		actLicense = new LicenseAction();
		actAbout = new AboutAction();
	}

	/**
	 * Create the menu bar
	 * 
	 * @return the menu bar
	 */
	JMenuBar createMenuBar()
	{
		/* Set up the menu bar*/
		JMenuBar menuBar = new JMenuBar();

		/* Set up the File menu */
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		JMenuItem miRestart = new JMenuItem("Restart");
		menuFile.add(miRestart);
		miRestart.addActionListener(actRestart);
		menuFile.addSeparator();
		JMenuItem miExit = new JMenuItem("Exit");
		menuFile.add(miExit);
		miExit.addActionListener(actExit);
		
		/* Set up the Configuration menu */
		JMenu menuConfiguration = new JMenu("Configuration");
		menuBar.add(menuConfiguration);
		JMenuItem miSettings = new JMenuItem("Settings...");
		menuConfiguration.add(miSettings);
		miSettings.addActionListener(actConfiguration);
		JMenuItem miPresets = new JMenuItem("Presets...");
		menuConfiguration.add(miPresets);
		miPresets.addActionListener(actPreset);
		
		/* Set up the Help menu */
		JMenu menuHelp = new JMenu("Help");
		menuBar.add(menuHelp);
		JMenuItem miExplanation = new JMenuItem("Explanation");
		menuHelp.add(miExplanation);
		miExplanation.addActionListener(actExplanation);
		JMenuItem miLicense = new JMenuItem("License");
		menuHelp.add(miLicense);
		miLicense.addActionListener(actLicense);
		menuHelp.addSeparator();
		JMenuItem miAbout = new JMenuItem("About");
		menuHelp.add(miAbout);
		miAbout.addActionListener(actAbout);

		return menuBar;
	}
	
	/**
	 * Release any resources before exiting
	 */
	void dispose()
	{
		if (dlgExplanation != null)
		{
			dlgExplanation.dispose();
		}
		if (dlgLicense != null)
		{
			dlgLicense.dispose();
		}
		if (dlgAbout != null)
		{
			dlgAbout.dispose();
		}
	}
	
	/**
	 * Show the introductory dialog
	 * 
	 * @param owner the dialog parent
	 */
	void showIntroductoryDialog(Frame owner)
	{
		JDialog dlgIntro = new IntroductoryDialog(owner);
		
		dlgIntro.setVisible(true);
		dlgIntro.dispose();
	}
	
	/**
	 * A utility class for creating textareas
	 * 
	 * @param contents the text in the textarea
	 * @return the textarea
	 */
	private JTextArea createTextArea(String contents)
	{
		JTextArea taContents = new JTextArea();
		taContents.setEditable(false);
		taContents.setLineWrap(true);
		taContents.setWrapStyleWord(true);
		taContents.setOpaque(false);
		taContents.setPreferredSize(new Dimension(550, 270));
		taContents.setText(contents);
		
		return taContents;
	}

	private class RestartAction extends AbstractAction 
	{
		private static final long serialVersionUID = 6263991061940942144L;

		/**
		 * Restart the simulation with the current configuration
		 * 
		 * @param aevt the action event
		 */
		@Override
		public void actionPerformed(ActionEvent aevt) 
		{
			pnlDarkPlainFrame.restart();
		}
	}

	private class ExitAction extends AbstractAction
	{
		private static final long serialVersionUID = 6210516111269036081L;

		/**
		 * Dispose of the parent frame. This closes the whole megillah.
		 * 
		 * @param aevt the action event
		 */
		@Override
		public void actionPerformed(ActionEvent aevt) 
		{
			parentFrame.dispose();
		}
	}

	private class ConfigurationAction extends AbstractAction 
	{
		private static final long serialVersionUID = -6901285543251226921L;

		/**
		 * Display the configuration dialog, creating it if necessary.
		 * 
		 * @param aevt the action event
		 */
		@Override
		public void actionPerformed(ActionEvent aevt) 
		{
			if (dlgConfiguration == null)
			{
				dlgConfiguration = new ConfigurationDialog(parentFrame);
			}
			dlgConfiguration.setVisible(true);
		}
	}

	private class PresetAction extends AbstractAction 
	{
		private static final long serialVersionUID = 5688137425802159555L;

		/**
		 * Display the preset dialog, creating it if necessary.
		 * 
		 * @param aevt the action event
		 */
		@Override
		public void actionPerformed(ActionEvent aevt) 
		{
			if (dlgPreset == null)
			{
				dlgPreset = new PresetDialog(parentFrame);
			}
			dlgPreset.setVisible(true);
		}
	}

	private class ExplanationAction extends AbstractAction 
	{
		private static final long serialVersionUID = -6901285543251226921L;

		/**
		 * Display the explanation dialog, creating it if necessary.
		 * 
		 * @param aevt the action event
		 */
		@Override
		public void actionPerformed(ActionEvent aevt) 
		{
			if (dlgExplanation == null)
			{
				dlgExplanation = new ExplanationDialog(parentFrame);
			}
			dlgExplanation.setVisible(true);
		}
	}

	private class LicenseAction extends AbstractAction 
	{
		private static final long serialVersionUID = 2658933595066096208L;

		/**
		 * Display the license dialog, creating it if necessary.
		 * 
		 * @param aevt the action event
		 */
		@Override
		public void actionPerformed(ActionEvent aevt) 
		{
			if (dlgLicense == null)
			{
				dlgLicense = new LicenseDialog(parentFrame);
			}
			dlgLicense.setVisible(true);
		}
	}

	private class AboutAction extends AbstractAction 
	{
		private static final long serialVersionUID = -6885768794110126634L;

		/**
		 * Display the about dialog, creating it if necessary.
		 * 
		 * @param aevt the action event
		 */
		@Override
		public void actionPerformed(ActionEvent aevt) 
		{
			if (dlgAbout == null)
			{
				dlgAbout = new AboutDialog(parentFrame);
			}
			dlgAbout.setVisible(true);
		}
	}

	/*
	 * Base class for the other dialogs
	 */
	protected class OkayDialog extends JDialog
	{
		private static final long serialVersionUID = -5709284803478323343L;

		/**
		 * Constructor
		 * 
		 * @param owner the dialog parent
		 * @param title the dialog title
		 */
		private OkayDialog(Frame owner, String title)
		{
			super(owner, title, true);

			JButton btnOkay = new JButton("OK");
			btnOkay.addActionListener(new AbstractAction() 
			{
				private static final long serialVersionUID = -1708033505563488636L;

				@Override
				public void actionPerformed(ActionEvent aevt) 
				{
					setVisible(false);
				}
			});

			JPanel pnlButton = new JPanel();
			pnlButton.add(btnOkay);			

			Container contentPane = getContentPane();
			contentPane.add(pnlButton, BorderLayout.SOUTH);
		}
		
		/**
		 * Pack the dialog and then center the dialog
		 */
		public void pack()
		{
			super.pack();
			centerDialog();
		}
		
		/**
		 * Center the dialog in the frame
		 */
		protected void centerDialog()
		{
			Dimension dimOwner = getOwner().getSize();
			Dimension dimDialog = getSize();
			int x = (int) ((dimOwner.getWidth() - dimDialog.getWidth())/2);
			x = ((x < 0) ? 0 : x);
			int y = (int) ((dimOwner.getHeight() - dimDialog.getHeight())/2);
			y = ((y < 0) ? 0 : y);
			setLocation(x, y);
		}
	}

	private class IntroductoryDialog extends OkayDialog
	{
		private static final long serialVersionUID = -268861781756890879L;

		/**
		 * Constructor
		 * 
		 * @param owner the parent
		 */
		private IntroductoryDialog(Frame owner)
		{
			super(owner, "Introduction");
			String strLicense = "Braitenberg Vehicle Simulation by Douglas B. Caulkins\n\n" +
			"Imagine a dark plain with roaming vehicles that respond to light\n" +
			"sources or other vehicles. These vehicles are simple in design,\n" +
			"yet display surprisingly complex behaviors. This is a thought\n" +
			"experiment proposed by Valentino Braitenberg and that I have\n" +
			"simulated in this application. For a more detailed explanation,\n" +
			"see the Help menu.\n\n" +
			"Press the start button to begin the simulation. This application\n" +
			"allows the attributes of these vehicles to be configured - see the\n" +
			"Settings dialog under the Configuration menu. As well, there are\n" +
			"interesting configurations available in the Presets dialog under\n" +
			"the Configuration menu.\n";
			JTextArea taLicense = createTextArea(strLicense);
			taLicense.setPreferredSize(new Dimension(550, 270));

			Container contentPane = getContentPane();
			contentPane.add(taLicense, BorderLayout.CENTER);
			pack();
		}
	}
	
	private class ConfigurationDialog extends OkayDialog
	{
		private static final long serialVersionUID = 7116607269423572549L;

		/**
		 * Constructor
		 * 
		 * @param owner the parent
		 */
		private ConfigurationDialog(Frame owner)
		{
			super(owner, "Configuration");
			add(pnlConfiguration);
			pack();
		}
	}
	
	private class PresetDialog extends OkayDialog
	{
		private static final long serialVersionUID = -2199539491930063285L;

		/**
		 * Constructor
		 * 
		 * @param owner the parent
		 */
		private PresetDialog(Frame owner)
		{
			super(owner, "Preset Configurations");
			add(pnlPreset);
			pack();
		}
	}
	
	private class ExplanationDialog extends OkayDialog
	{
		private static final long serialVersionUID = -4027835811852496699L;

		/**
		 * Constructor
		 * 
		 * @param owner the parent
		 */
		private ExplanationDialog(Frame owner)
		{
			super(owner, "Explanation");
			
			String strIntroduction = "Valentino Braitenberg, in his book 'Vehicles: Experiments in " +
					"Synthetic Psychology', proposed a series of simple vehicle designs " +
					"that would result in complex and interesting behaviors. His intent was " +
					"to demonstrate that simple designs can result in complex behaviors. " +
					"These vehicles are now known as Braitenberg vehicles, and I simulate " +
					"six of them with this software.\n\n" +
					"These vehicles are composed of two sensors on front, which can be " +
					"imagined as sensing light sources. A sensor produces pulses which " +
					"are sent along wires connected to the two wheels on the back of the " +
					"vehicle and cause them to spin forward. The various ways that a sensor " +
					"can be connected to a wheel is what results in the different vehicle " +
					"behaviors.\n\n" +
					"Imagine the vehicles on a dark plain populated with occasional light sources.\n\n";
			JTextArea taIntroduction = ActionFactory.this.createTextArea(strIntroduction);
			taIntroduction.setMinimumSize(new Dimension(600, 300));
			taIntroduction.setPreferredSize(new Dimension(600, 300));

			String strTypeFearful =
					"Vehicle Type 2a, Fearful: The sensor wires connect directly to the wheels, " +
					"uncrossed. If the right sensor receives light, the right wheel will spin more, " +
					"turning the vehicle away from the light source, and into the dark.\n\n";
			JTextArea taTypeFearful = ActionFactory.this.createTextArea(strTypeFearful);
			taTypeFearful.setMinimumSize(new Dimension(300, 250));
			taTypeFearful.setPreferredSize(new Dimension(300, 250));
			JPanel pnlTypeFearful = VehicleRenderer.getUncrossedExcitatoryPanel();
			
			String strTypeAggressive =
					"Vehicle Type 2b, Aggressive: The sensor wires cross before connecting the " +
					"wheels. If the right sensor receives light, the left wheel will spin more, " +
					"turning the vehicle towards the light source. This vehicle speeds up as it " +
					"approaches the light, until it crashes into it.\n\n";
			JTextArea taTypeAggressive = ActionFactory.this.createTextArea(strTypeAggressive);
			taTypeAggressive.setMinimumSize(new Dimension(300, 250));
			taTypeAggressive.setPreferredSize(new Dimension(300, 250));
			JPanel pnlTypeAggressive = VehicleRenderer.getCrossedExcitatoryPanel();

			String strTypeAdoring =
					"Vehicle Type 3a, Quietly Adoring: The sensor wires connect directly to the " +
					"wheels, uncrossed, but the sensor sends fewer pulses as it receives more " +
					"light. If the right sensor receives light, the right wheel will spin less, " +
					"turning the vehicle toward the light source which it slowly approaches " +
					"until it stops.\n\n";
			JTextArea taTypeAdoring = ActionFactory.this.createTextArea(strTypeAdoring);
			taTypeAdoring.setMinimumSize(new Dimension(300, 250));
			taTypeAdoring.setPreferredSize(new Dimension(300, 250));
			JPanel pnlTypeAdoring = VehicleRenderer.getUncrossedInhibitoryPanel();

			String strTypeExploring =
					"Vehicle Type 3b, Exploring: The sensor wires cross before connecting the " +
					"wheels and the sensor sends fewer pulses as it receives more light. If the " +
					"right sensor receives light, the left wheel will spin less, turning the " +
					"vehicle away from the light source. This vehicle speeds through the dark, " +
					"avoiding light sources.\n\n";
			JTextArea taTypeExploring = ActionFactory.this.createTextArea(strTypeExploring);
			taTypeExploring.setMinimumSize(new Dimension(300, 250));
			taTypeExploring.setPreferredSize(new Dimension(300, 250));
			JPanel pnlTypeExploring = VehicleRenderer.getCrossedInhibitoryPanel();

			String strTypeDeciding1 =
					"Vehicle Type 4b, Deciding: The sensor wires connect directly to the " +
					"wheels, uncrossed. The sensor sends more pulses as it receives more " +
					"light until a threshold is reached, then it sends fewer pulses. If the right " +
					"sensor receives low light, the right wheel will spin more, but light that " +
					"exceeds the threshold will cause the right wheel to spin less. The vehicle " +
					"turns away from distant light sources but may turn towards nearby light " +
					"sources.\n\n";
			JTextArea taTypeDeciding1 = ActionFactory.this.createTextArea(strTypeDeciding1);
			taTypeDeciding1.setMinimumSize(new Dimension(300, 250));
			taTypeDeciding1.setPreferredSize(new Dimension(300, 250));
			JPanel pnlTypeDeciding1 = VehicleRenderer.getUncrossedThresholdPanel();
			
			String strTypeDeciding2 =
					"Vehicle Type 4b, Deciding: The sensor wires cross before connecting the " +
					"wheels. The sensor sends more pulses as it receives more light until a " +
					"threshold is reached, then it sends fewer pulses. If the right " +
					"sensor receives low light, the left wheel will spin more, but light that" +
					"exceeds the threshold will cause the left wheel to spin less. The vehicle " +
					"turns toward distant light sources but may turn away from a nearby light " +
					"sources.\n\n";
			JTextArea taTypeDeciding2 = ActionFactory.this.createTextArea(strTypeDeciding2);
			taTypeDeciding2.setMinimumSize(new Dimension(300, 250));
			taTypeDeciding2.setPreferredSize(new Dimension(300, 250));
			JPanel pnlTypeDeciding2 = VehicleRenderer.getCrossedThresholdPanel();

			/* Add the panels and text areas to a gridbag panel */
			GridbagPanel pnlExplanation = new GridbagPanel();			
			pnlExplanation.add(taIntroduction, 0, 0, 2, 1, GridBagConstraints.WEST);
			pnlExplanation.add(taTypeFearful, 0, 1, 1, 1, GridBagConstraints.WEST);
			pnlExplanation.add(pnlTypeFearful, 1, 1, 1, 1, GridBagConstraints.EAST);
			pnlExplanation.add(taTypeAggressive, 0, 2, 1, 1, GridBagConstraints.WEST);
			pnlExplanation.add(pnlTypeAggressive, 1, 2, 1, 1, GridBagConstraints.EAST);
			pnlExplanation.add(taTypeAdoring, 0, 3, 1, 1, GridBagConstraints.WEST);
			pnlExplanation.add(pnlTypeAdoring, 1, 3, 1, 1, GridBagConstraints.EAST);
			pnlExplanation.add(taTypeExploring, 0, 4, 1, 1, GridBagConstraints.WEST);
			pnlExplanation.add(pnlTypeExploring, 1, 4, 1, 1, GridBagConstraints.EAST);
			pnlExplanation.add(taTypeDeciding1, 0, 5, 1, 1, GridBagConstraints.WEST);
			pnlExplanation.add(pnlTypeDeciding1, 1, 5, 1, 1, GridBagConstraints.EAST);
			pnlExplanation.add(taTypeDeciding2, 0, 6, 1, 1, GridBagConstraints.WEST);
			pnlExplanation.add(pnlTypeDeciding2, 1, 6, 1, 1, GridBagConstraints.EAST);
			
			/* Set up a scroll pane to contain the explanation */
			JScrollPane spExplanation = new JScrollPane(pnlExplanation);
			Container contentPane = getContentPane();
			contentPane.add(spExplanation, BorderLayout.CENTER);
			setMinimumSize(new Dimension(650, 700));
			setPreferredSize(new Dimension(650, 700));
			centerDialog();
			/* Display the first line of the explanation in the scroll pane */
			taIntroduction.setCaretPosition(0);
		}
	}
	
	private class LicenseDialog extends OkayDialog
	{
		private static final long serialVersionUID = 132387749608983614L;

		/**
		 * Constructor
		 * 
		 * @param owner the parent
		 */
		private LicenseDialog(Frame owner)
		{
			super(owner, "License");

			String strLicense = "Copyright 2010 Douglas B. Caulkins\n\n" +
					"This file is part of the Braitenberg Simulation Java package.\n\n" +
					"The Braitenberg Simulation Java package is free software: you can\n" +
					"redistribute it and/or modify it under the terms of the GNU General\n" +
					"Public License as published by the Free Software Foundation, either\n" +
					"version 3 of the License, or (at your option) any later version.\n\n" +
					"The Braitenberg Simulation Java package is distributed in the hope\n" +
					"that it will be useful, but WITHOUT ANY WARRANTY; without even the\n" +
					"implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR\n" +
					"PURPOSE. See the GNU General Public License for more details at\n" +
					"<http://www.gnu.org/licenses/>.";
			JTextArea taLicense = createTextArea(strLicense);
			taLicense.setPreferredSize(new Dimension(550, 270));

			Container contentPane = getContentPane();
			contentPane.add(taLicense, BorderLayout.CENTER);
			pack();
		}
	}
	
	private class AboutDialog extends OkayDialog
	{
		private static final long serialVersionUID = -3517925288437632558L;

		/**
		 * Constructor
		 * 
		 * @param owner the parent
		 */
		private AboutDialog(Frame owner)
		{
			super(owner, "About This Braitenberg Vehicle Simulation");

			String strAbout = "This Braitenberg Vehicle Simulation was designed and programmed " +
				"by Douglas B. Caulkins, May 2010.";
			JTextArea taAbout = createTextArea(strAbout);
			taAbout.setPreferredSize(new Dimension(300, 70));
			Container contentPane = getContentPane();
			contentPane.add(taAbout, BorderLayout.CENTER);
			pack();
		}
	}
}