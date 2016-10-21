/**
 * Manages the animation of the simulation and user interactions
 * @author Karim
 */

package component;

import geometry.CelestialObject;
import geometry.Vector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import simulation.Calculation;
import simulation.IntegrationMethod;
import tool.CreationTool;
import tool.EditTool;
import tool.ManagerTool;
import tool.TextureSelection;
import tool.TimeTool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


public class Simulator extends JDesktopPane implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private static final double CORRECTION_FACTOR = 20000;
	private static final long ANIMATION_CONSTANT = 50;
	private double timeFactor = 1800.0;
	private double deltaT;//One second of animation is equal to this value in real time in seconds
	private AffineTransform matMC = null;
	private double worldLength = 200000; //megameters
	private double worldHeightUndistorted;
	private double pixelsPerUnitX, pixelsPerUnitY;
	private ArrayList<CelestialObject> celestialObjectsList = new ArrayList<CelestialObject>(); //tableau dynamique des instances Astre
	private Calculation calculation = new Calculation();
	private Vector creationVector;
	private boolean animationIsRunning = false;
	private boolean origin;
	private boolean initialRun = true;
	private double xClic, yClic, xCurrent, yCurrent;
	private double speedX;
	private double speedY;
	private double mass;
	private String name;
	private TextureSelection text = new TextureSelection();
	private CreationTool creationTool = new CreationTool();
	private ManagerTool managerTool = new ManagerTool();
	private TimeTool timeTool = new TimeTool();
	private EditTool editTool = new EditTool();
	private JButton creationToolButton, managerToolButton, timeToolButton;
	private CelestialObject celestialObject = new CelestialObject("",0,0,0,0,0);
	private Image image = null;
	private Graphics2D gImage;
	private boolean initImage;
	private int selectedIndex = 0;


	/**
	 * Manages user inputs and some related interface elements
	 */
	public Simulator() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(isPositiveNumeric(creationTool.getMass())){
					xClic = e.getX()/pixelsPerUnitX;
					yClic = e.getY()/pixelsPerUnitY;
					xCurrent = e.getX()/pixelsPerUnitX;
					yCurrent = e.getY()/pixelsPerUnitY;
				}
				else{
					JOptionPane.showMessageDialog(null, "Invalid mass value");
				}
				

			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
					
					xCurrent = e.getX()/pixelsPerUnitX;
					yCurrent = e.getY()/pixelsPerUnitY;
					
					speedX = (xCurrent - xClic)/CORRECTION_FACTOR ; 
					speedY = (yCurrent - yClic)/CORRECTION_FACTOR ; 
					
					creationVector = new Vector(xClic, yClic, xCurrent-xClic, yCurrent-yClic);
					origin = true;
				
					repaint();
					
				}
			
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(isPositiveNumeric(creationTool.getMass())){
					name = creationTool.getNameInTextField();
					mass = Double.parseDouble(creationTool.getMass());
					CelestialObject astreAjouter = new CelestialObject(name, mass, xClic, yClic, speedX, speedY);
					celestialObjectsList.add(astreAjouter);
					managerTool.getCmbBoxAstres().addItem(( (celestialObjectsList.size()) )+". "+ name );
					celestialObjectsList.get(celestialObjectsList.size()-1).setTexture(creationTool.getSelection().returnIndex());
					origin = false;
					speedX = 0;
					speedY = 0;
					repaint();
				}
				else{
					JOptionPane.showMessageDialog(null, "Invalid time value");
				}
				
			}
		});
		
		setPreferredSize(new Dimension(100, 100));
		setBackground(Color.BLACK);
	}//end constructor

	/**
	 * Draws celectial objects, their speed vectors and their trajectories
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawString("", 0, 0);
		
		if(initialRun){
			image(g);
			generateMatrixWorldPixel(worldLength);
			text.setBounds(0,0,100,200);
			add(text);
			outils();
			initialRun = false;
			
			//generates 500 random celestial objects in a circle
//			for (int k=0; k<500; k++){
//				double angle = Math.random() * Math.PI * 2;
//				//double radius = Math.random() * 20000;
//				double radius = Math.sqrt(Math.random()) * 20000;
//				double x = 100000 + radius * Math.cos(angle);
//				double y = worldHeightUndistorted/2 + radius * Math.sin(angle);
//				celestialObjectsList.add(new CelestialObject("", 1, x, y, Math.random()*5-2.5, Math.random()*5-2.5));
//				celestialObjectsList.get(k).draw(g2d, matMC);
//				celestialObjectsList.get(k).drawTrajectory(gImage, matMC);
//			}
//				celestialObjectsList.add(new CelestialObject("", 10, 100000, worldHeightUndistorted/2, 0, 0));
			
			//generates 500 random celestial objects with no speed
//			for (int k=0; k<500; k++){
//				celestialObjectsList.add(new CelestialObject("", 1, Math.random()*200000, Math.random()*worldHeightUndistorted, 0, 0));
//				celestialObjectsList.get(k).draw(g2d, matMC);
//				celestialObjectsList.get(k).drawTrajectotory(gImage, matMC);
//			}
			
			//generates 1000 random celestial objects with random speeds
//			for (int k=0; k<1000; k++){
//			celestialObjectsList.add(new CelestialObject("", 0.1, Math.random()*200000, Math.random()*worldHeightUndistorted, (Math.random()*10)-5, (Math.random()*10)-5));
//			celestialObjectsList.get(k).draw(g2d, matMC);
//			celestialObjectsList.get(k).drawTrajectory(gImage, matMC);
//		}
			
			//generates a central star and 1000 celestial objects in a line with random speeds in the y axis
//			celestialObjectsList.add(new CelestialObject("Center", 1000, 100000, 46000, 0, 0));
//			for (int k=0; k<1000; k++){
//				celestialObjectsList.add(new CelestialObject("", 0.1, (Math.random()+0.5)*200000, 46000, 0, (Math.random()*10)-5));
//				celestialObjectsList.get(k).draw(g2d, matMC);
//				celestialObjectsList.get(k).drawTrajectory(gImage, matMC);
//			}	
			
			//generates a central star with 25 celestial objects in a line with appropriate speeds for a circular orbit
//			celestialObjectsList.add(new CelestialObject("Center", 1000, 100000, 46000, 0, 0));
//			for (int k=0; k<25; k++){
//				double x = 104000+k*2000;
//				double sign = Math.random()-0.5;
//				if(sign<0){sign=-1;}
//				else{sign=1;}
//				celestialObjectsList.add(new CelestialObject(""+k, 0.01, x, 46000, 0, sign*(Math.sqrt(1.32636285*Math.pow(10, 23)/((x-100000)*Math.pow(10, 6)))/(Math.pow(10, 6))) ));
//				celestialObjectsList.get(k).draw(g2d, matMC);
//				celestialObjectsList.get(k).drawTrajectory(gImage, matMC);
//			}	
			
//			celestialObjectsList.add(new CelestialObject("Center", 1000, 100000, 46000, 0, 0));
//			for (int k=0; k<25; k++){
//				double x = 104000+k*2000;
//				double sign = Math.random()-0.5;
//				if(sign<0){sign=-1;}
//				else{sign=1;}
//				celestialObjectsList.add(new CelestialObject(""+k, 0.01, x, 46000, 0, (Math.sqrt(1.32636285*Math.pow(10, 23)/((x-100000)*Math.pow(10, 6)))/(Math.pow(10, 6))) ));
//				celestialObjectsList.get(k).draw(g2d, matMC);
//				celestialObjectsList.get(k).drawTrajectory(gImage, matMC);
//			}
		
		}

		else{
			repositionTools();
			if(initImage){
				image(g);
				initImage=false;
			}
			g2d.drawImage(image, 0, 0, null);
			for (int k=0; k<celestialObjectsList.size(); k++){
				if(k>=celestialObjectsList.size()){
					k=celestialObjectsList.size()-1;
				}
				celestialObjectsList.get(k).draw(g2d, matMC);
				celestialObjectsList.get(k).drawTrajectory(gImage, matMC);
			}
			if (origin){
				g2d.setColor(Color.BLUE);
				creationVector.draw(g2d, matMC);
			}
			g2d.setColor(Color.WHITE);
			g2d.draw(new Line2D.Double(getWidth()-200, 10, getWidth()-200-getWidth()/10, 10));
			g2d.drawString((int)worldLength/10+" Mm", getWidth()-200-getWidth()/10, 30);
		}

		
		
	}// end paintComponent
	
	/**
	 * Executes the animation with appropriate calculation method
	 */
	@Override
	public void run() {
		while (animationIsRunning) {
			deltaT = timeFactor*(ANIMATION_CONSTANT/1000.0);
			int tailleInit = celestialObjectsList.size();
			celestialObjectsList = calculation.methodeDeCalcul(celestialObjectsList, deltaT);
			int tailleFinale = celestialObjectsList.size();
			
			if(tailleFinale-tailleInit!=0){
				managerTool.getCmbBoxAstres().removeAllItems() ;
				managerTool.getCmbBoxAstres().addItem("None");
				for (int k=0; k<celestialObjectsList.size(); k++){
					managerTool.getCmbBoxAstres().addItem( (k+1) + ". "+ celestialObjectsList.get(k).toString());
				}
			}
			editTool.update();
			repaint();
			try {
				Thread.sleep(ANIMATION_CONSTANT);
			} 
			catch (InterruptedException e) {
				System.out.println("Error during the animation");
			}
		}
	}// end run
	
	/**
	 * Starts the animation thread
	 */
	private void demarrer() {
		if (!animationIsRunning){
			Thread proc = new Thread(Simulator.this);
			proc.start();
			animationIsRunning = true;
		}
	}
	
	/**
	 * Adds listeners to tool objects
	 */
	private void outils(){
		//creation
		creationToolButton = new JButton("Creation Tool");
		creationToolButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creationTool.show();
				creationToolButton.setVisible(false);
				positionCreation();
			}
		});
		positionCreation();
		add(creationToolButton);
		creationTool.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent arg0) {
				creationToolButton.setVisible(true);
				positionCreation();
			}
		});
		creationTool.getButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				creationTool.getSelection().setBounds(5, getHeight()-90-152, 620, 150);
				creationTool.getSelection().show();
			}
		});
		
		add(creationTool.getSelection());
		add(creationTool);
		
		//edit
		initEdit();
		editTool.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent arg0) {
				initEdit();
			}
		});
		editTool.getConfirmButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				editTool.updateList(managerTool.getCmbBoxAstres(), celestialObjectsList, selectedIndex);
				repaint();
			}
		});
		editTool.getDeleteButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				celestialObjectsList.remove(selectedIndex-1);
				managerTool.getCmbBoxAstres().removeAllItems() ;
				managerTool.getCmbBoxAstres().addItem("None");
				for (int k=0; k<celestialObjectsList.size(); k++){
					managerTool.getCmbBoxAstres().addItem( (k+1) + ". "+ celestialObjectsList.get(k).toString());
				}
				selectedIndex=0;
				editTool.hide();
				repaint();
			}
		});
		
		add(editTool.getSelection());
		add(editTool);
		
		//manager
		managerToolButton = new JButton("Manager Tool");
		managerToolButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				managerTool.show();
				managerToolButton.setVisible(false);
				initManager();
			}
		});
		initManager();
		add(managerToolButton);
		managerTool.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent arg0) {
				managerToolButton.setVisible(true);
				initManager();
			}
		});
		
		managerTool.getChckbxTrajectoire().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				celestialObject.toggleTrajectory();
				initImage = true;
				repaint();
			}
		});
		
		managerTool.getChckbxVecteur().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				celestialObject.toggleVector();
				repaint();
			}
		});
		
		managerTool.getChckbxNom().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				celestialObject.toggleName();
				repaint();
			}
		});
		
		managerTool.getCmbBoxCalcul().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (managerTool.getCmbBoxCalcul().getSelectedIndex()) {
				case 0:
					calculation.setMethodeDeCalcul(IntegrationMethod.EULER);
					break;
				case 1:
					calculation.setMethodeDeCalcul(IntegrationMethod.INVERSE_EULER);
				default:
					calculation.setMethodeDeCalcul(IntegrationMethod.RUNGE_KUTTA_4);
					break;
				}
			}
		});
		
		managerTool.getCmbBoxAstres().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(managerTool.getCmbBoxAstres().getItemCount()>1){
					if(selectedIndex>0){
						celestialObjectsList.get(selectedIndex-1).setSelected(false);
						if(managerTool.getCmbBoxAstres().getSelectedIndex()==0){
							selectedIndex=managerTool.getCmbBoxAstres().getSelectedIndex();
							editTool.hide();
						}
						else{
							selectedIndex=managerTool.getCmbBoxAstres().getSelectedIndex();
							celestialObjectsList.get(selectedIndex-1).setSelected(true);
							editTool.astre(celestialObjectsList.get(selectedIndex-1));
						}
					}
				
					else{
						selectedIndex=managerTool.getCmbBoxAstres().getSelectedIndex();
						editTool.hide();
						if(selectedIndex>0){
							celestialObjectsList.get(selectedIndex-1).setSelected(true);
							initEdit();
							editTool.show();
							editTool.astre(celestialObjectsList.get(selectedIndex-1));
						}
					}
				}
				repaint();
			}
		});

		managerTool.getSpinnerG().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				calculation.setGConstanteGravitationnelle((double)managerTool.getSpinnerG().getValue());
			}
		});		
		managerTool.getSpinnerOrdreG().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				calculation.setOrdreConstanteGravitationnelle((double)managerTool.getSpinnerOrdreG().getValue());
			}
		});
	
		managerTool.getBtnRenitialiser().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedIndex = 0;
				setAnimationState(false);
				resetCelectialObjectsList();
				editTool.hide();
				initImage = true;
				timeTool.getBtnDemarrer().setVisible(true);
				repaint();
			}
		
		});
		
		add(managerTool);
		
		//time
		timeToolButton = new JButton("Time Tool");
		timeToolButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeTool.show();
				timeToolButton.setVisible(false);
				initTime();
			}
		});
		initTime();
		add(timeToolButton);
		timeTool.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent arg0) {
				timeToolButton.setVisible(true);
				initTime();
			}
		});
		
		timeTool.getBtnDemarrer().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				demarrer();
				setAnimationState(true);
				timeTool.getBtnDemarrer().setVisible(false);;
			}
		});
		
		timeTool.getBtnArret().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setAnimationState(false);
				timeTool.getBtnDemarrer().setVisible(true);
			}
		});
		
		timeTool.getBtnConfirmer().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateTimeFactor();
			}
		});
		add(timeTool);
		
		
		
	}
	
	/**
	 * Positions and sizes the Creation Tool
	 */
	private void positionCreation(){
		creationToolButton.setBounds(5, getHeight()-30, 155, 25);
		creationTool.setBounds(5, getHeight()-90, 340, 85);
		creationTool.getSelection().setBounds(5, getHeight()-90-152, 620, 150);
	}

	/**
	 * Positions and sizes the Manager Tool
	 */
	private void initManager(){
		managerToolButton.setBounds(5, 5, 155, 25);
		managerTool.setBounds(5, 5, 320, 260);
	}
	
	/**
	 * Positions and sizes the Time Tool
	 */
	private void initTime(){
		timeToolButton.setBounds(getWidth()-160, 5, 155, 25);
		timeTool.setBounds(getWidth()-290, 5, 285, 130);
	}
	
	/**
	 * Positions and sizes the Edit Tool
	 */
	private void initEdit(){
		editTool.setBounds(getWidth()-320, getHeight()-255, 315, 250);
		editTool.getSelection().setBounds(getWidth()-620, getHeight()-255-150, 620, 150);
	}
	
	/**
	 * Repositions the Creation Tool and the Time Tool
	 */
	private void repositionTools(){
		creationToolButton.setBounds(5, getHeight()-30, 155, 25);
		timeToolButton.setBounds(getWidth()-160, 5, 155, 25);
	}
	
	/**
	 * Generates the matrix to go from pixels to real world units (and vice versa)
	 * @param worldLength The length of the world
	 * @return The proportional height 
	 */
	private double generateMatrixWorldPixel( double worldLength ) {
		matMC = new AffineTransform(); 
		
		pixelsPerUnitX = getWidth() / worldLength;
		double ratio = getHeight()/(double)getWidth();
		worldHeightUndistorted = worldLength*ratio;
		pixelsPerUnitY = getHeight()/worldHeightUndistorted ;
		
		matMC.scale(pixelsPerUnitX, pixelsPerUnitY); 
		
		return worldHeightUndistorted;
	}
	
	/**
	 * Reads the background image
	 * @param g Graphics object that draws the background
	 */
	private void image(Graphics g){
		try {
			URL background = getClass().getClassLoader().getResource("black.png");
			image = ImageIO.read(background);
		} 
		catch (IOException e) {
			System.out.println("Error fetching the background image");
		}
		gImage = (Graphics2D) image.getGraphics();
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	/**
	 * Changes the state of the animation (Play/Pause)
	 * @param animationIsRunning Animation state
	 */
	public void setAnimationState(boolean animationIsRunning) {
		this.animationIsRunning = animationIsRunning;
	}
	
	/**
	 * Changes the rate of the animation based on user input
	 */
	private void updateTimeFactor(){
		if(isPositiveNumeric(timeTool.getTxtFTemps().getText())){
			double temps = Double.parseDouble(timeTool.getTxtFTemps().getText());
			
			switch (timeTool.getCmbBxTemps().getSelectedIndex()) {
			
			//second
			case 0:
				setTimeFactor(temps);
				break;
			//minute
			case 1:
				setTimeFactor(temps*60);
				break;
			//hour
			case 2:
				setTimeFactor(temps*60*60);
				break;
			//day
			case 3:
				setTimeFactor(temps*60*60*24);
				break;
			}	
		}
		else{
			JOptionPane.showMessageDialog(null, "Invalid time value");
		}
	}
	
	/**
	 * Resets the simulation
	 */
	private void resetCelectialObjectsList(){
		for(int k=0; k<celestialObjectsList.size(); k++){
			celestialObjectsList.get(k).getTrajectory().clear();
			repaint();
		}
		celestialObjectsList.clear();
		managerTool.getCmbBoxAstres().removeAllItems();
		managerTool.getCmbBoxAstres().addItem("Aucun");
		repaint();
	}
	
	/**
	 * Returns true if the string in the argument is a positive numeric, false if not
	 * @param str String to check
	 * @return True or False
	 */
	private static boolean isPositiveNumeric(String str)  {  
		double d;
		try {  
			d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe){  
			return false;  
		}
		if(d>0){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Reads text file in argument and loads the celestial objects configuration within
	 * @param file The text file
	 */
	public void read(File file){
		BufferedReader inputStream = null;
		double mass = 0, posX = 0, posY = 0, vitX = 0, vitY = 0;
		String name;
		String line;
		String tabItems[];
		
		try {
			inputStream = new BufferedReader(new FileReader(file));
			
			celestialObjectsList.clear();
			managerTool.getCmbBoxAstres().removeAllItems() ;
			managerTool.getCmbBoxAstres().addItem("None");
			initImage = true;
			
			do { 
				line = inputStream.readLine();
				if (line != null) {
					tabItems = line.split(",");
					name = tabItems[0];
					mass = Double.parseDouble( tabItems[1] );
					posX = Double.parseDouble( tabItems[2] );
					posY = Double.parseDouble( tabItems[3] );
					vitX = Double.parseDouble( tabItems[4] );
					vitY = Double.parseDouble( tabItems[5] );
					celestialObjectsList.add(new CelestialObject(name, mass, posX, posY, vitX, vitY));
					managerTool.getCmbBoxAstres().addItem(( (celestialObjectsList.size()) )+". "+ name );
				}
			} while (line != null);	
		}		
		
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Can't find  " + file.getAbsolutePath());
			System.exit(0);
		}
		
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Read error");
			e.printStackTrace();
			System.exit(0);
		}
		
		finally {
		  	try { 
		  		inputStream.close();  
		  	}
		    catch (IOException e) { 
		    	JOptionPane.showMessageDialog(null,"Read closure error"); 
		    }
		}
		selectedIndex=0;
		editTool.hide();
		repaint();
	}

	/**
	 * Writes current celestial objects to a text file
	 * @param directory The directory where the file will be written
	 */
	public void write(File directory) {
		File file = new File( directory+".txt");
		PrintWriter outputStream=null;
		try {
			outputStream = new PrintWriter(new BufferedWriter (new FileWriter( file )));
			for (int k=0; k<celestialObjectsList.size(); k++){
				outputStream.println(celestialObjectsList.get(k).write());
			}
		} 
		catch (IOException e) {
			System.out.println("Erreur d'écriture!");
			e.printStackTrace();

		}
		finally {
			if (outputStream != null)
				outputStream.close();
		}
	}
	
	public void setTimeFactor(double timeFactor) {
		this.timeFactor = timeFactor;
	}



}//end class 