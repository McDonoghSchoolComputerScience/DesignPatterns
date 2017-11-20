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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This class simulates a dark plain containing various Braitenberg vehicles.
 *  
 *  Ideas for future enhancements
 *  - Fix the iconify frame threading issue
 *  - Implement pausing the vehicle movement
 *  - Implement as an applet for embedding in a web page.
 *  - Put all text into a resource bundle. Implement dynamic Google translations.
 *  - Improve error handling. Replace printlns with logged messages.
 *  - Add the ability to leave vehicle trails.
 *  - Investigate the left to right movement bias or tendency.
 * 
 * @author Douglas B. Caulkins
 */
public class MainFrame 
{
	private final JFrame parentFrame;
	private final ActionFactory ftryAction;
	private final ConfigurationPanel pnlConfiguration;
	private final PresetPanel pnlPreset;
	private final DarkPlainFramePanel pnlDarkPlainFrame;

	/**
	 * Constructor
	 * 
	 * @param frm the parent frame
	 */
	MainFrame(JFrame frm)
	{
		parentFrame = frm;
		
		pnlConfiguration = new ConfigurationPanel();
		pnlDarkPlainFrame = new DarkPlainFramePanel(pnlConfiguration);
		pnlPreset = new PresetPanel(pnlConfiguration);
		ftryAction = new ActionFactory(parentFrame, pnlDarkPlainFrame, pnlConfiguration, pnlPreset);
	}

	/**
	 * Create the menu bar
	 * 
	 * @return the menu bar
	 */
	JMenuBar createMenuBar()
	{
		return ftryAction.createMenuBar();
	}
	
	/**
	 * Get the panel containing the dark plain
	 * 
	 * @return the dark plain panel
	 */
	JPanel getDarkPlain()
	{
		return pnlDarkPlainFrame;
	}
	
	/**
	 * Show the introductory dialog
	 * 
	 * @param owner the dialog parent
	 */
	void showIntroductoryDialog()
	{
		ftryAction.showIntroductoryDialog(parentFrame);
	}
	
	/**
	 * Release any resources before exiting
	 */
	void dispose()
	{
		ftryAction.dispose();
	}
	
	/**
	 * Display the tabbed pane with the various panels
	 */
	static void createAndShowGUI() 
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Braitenberg Vehicle Simulation");

		final MainFrame simulation = new MainFrame(frame);
		frame.setJMenuBar(simulation.createMenuBar());
		frame.addWindowListener(new WindowAdapter()
		{	
			@Override
			public void windowClosed(WindowEvent e) 
			{
				simulation.dispose();
			}
		});

		/* Now display the dark plain */
		frame.add(simulation.getDarkPlain());
		frame.pack();
		frame.setVisible(true);
		simulation.showIntroductoryDialog();
	}
	
	/**
	 * Application entry point
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) 
	{
		/*
		 * I don't find the default Metal look-and-feel very attractive - this
		 * sets it to the System look-and-feel. The user should be comfortable with
		 * how that looks.
		 */
		try 
		{
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException ulafexc) 
	    {
	    	ulafexc.printStackTrace();
	    }
	    catch (ClassNotFoundException cnfexc) 
	    {
	    	cnfexc.printStackTrace();
	    }
	    catch (InstantiationException iexc) 
	    {
	    	iexc.printStackTrace();
	    }
	    catch (IllegalAccessException iaexc) 
	    {
	    	iaexc.printStackTrace();
	    }
		SwingUtilities.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{        
				createAndShowGUI();
			}
		});
	}
}