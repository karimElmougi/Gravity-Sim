/**
 * Main interface of the simulator
 * @author Karim
 * 
 */

package aapplication;

import java.awt.EventQueue;

import javax.swing.JFrame;

import component.Simulator;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.WindowStateListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;


public class GravitySimulatorApp {

	private JFrame frame;
	private Simulator simulator;

	public static void main(String[] args) {
		try {
			com.jtattoo.plaf.hifi.HiFiLookAndFeel.setTheme("Default", "", "");
			UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GravitySimulatorApp window = new GravitySimulatorApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the main interface
	 */
	public GravitySimulatorApp() {
		frame = new JFrame();
		frame.addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent arg0) {
				simulator.repaint();
			}
		});
		frame.setBounds(100, 10, 1000, 800);
		frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("2D Gravity Simulator");

		simulator = new Simulator();
		frame.getContentPane().add(simulator, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem subMenuLoad = new JMenuItem("Load celestial object configuration");
		subMenuLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filePath = System.getProperty("user.home") + "\\AppData\\Local\\Gravity Simulator";
				File mainDirectory = new File(filePath);
			    if (!mainDirectory.exists()) {
			      mainDirectory.mkdir();
			    }
			    JFileChooser openFile = new JFileChooser(mainDirectory);
			    
			    FileFilter fileTypeFilter = new FileFilter()
			    {
			      public boolean accept(File file)
			      {
			        if (file.isDirectory()) {
			          return true;
			        }
			        String path = file.getAbsolutePath();
			        if (path.endsWith(".txt")) {
			          return true;
			        }
			        return false;
			      }
			      
			      public String getDescription()
			      {
			        return ".txt";
			      }
			    };
			    openFile.addChoosableFileFilter(fileTypeFilter);
			    openFile.setFileFilter(fileTypeFilter);
			    openFile.showOpenDialog(null);
			    File fileToLoad = openFile.getSelectedFile();
			    if ((fileToLoad == null) || (!fileToLoad.getName().endsWith(".txt")))
			    {
			      return;
			    }
			    simulator.read(fileToLoad);
			}
		});
		fileMenu.add(subMenuLoad);
		
		JMenuItem subMenuSave = new JMenuItem("Save celestial object configuration");
		subMenuSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filePath = System.getProperty("user.home") + "\\AppData\\Local\\Gravity Simulator";
				File mainDirectory = new File(filePath);
			    if (!mainDirectory.exists()) {
			      mainDirectory.mkdir();
			    }
			    JFileChooser saveFile = new JFileChooser(mainDirectory);
			    
			    FileFilter fileTypeFilter = new FileFilter()
			    {
			      public boolean accept(File file)
			      {
			        if (file.isDirectory()) {
			          return true;
			        }
			        String path = file.getAbsolutePath();
			        if (path.endsWith(".txt")) {
			          return true;
			        }
			        return false;
			      }
			      
			      public String getDescription()
			      {
			        return ".txt";
			      }
			    };
			    
			    saveFile.addChoosableFileFilter(fileTypeFilter);
			    saveFile.setFileFilter(fileTypeFilter);
			    saveFile.showSaveDialog(null);

			    mainDirectory = saveFile.getSelectedFile();
			    
			    simulator.write(mainDirectory);
			}
		});
		fileMenu.add(subMenuSave);

		JMenuItem subMenuExit = new JMenuItem("Exit");
		subMenuExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(subMenuExit);
	}
}
