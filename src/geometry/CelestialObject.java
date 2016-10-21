/**
 * Manages celectial objects and their methods and properties
 * 
 * @author Karim
 */

package geometry;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class CelestialObject {
	private final static double SOLAR_MASS=(1.98855*java.lang.Math.pow(10,30));
	private final static double SOLAR_RADIUS=696.0; //in megameters
	private final static double CORRECTION_FACTOR=244.84; //puts 1 Solar Mass objects to scale
	private final static double VECTOR_MULTIPLIER = 7000;
	
	private static boolean drawTrajectory = false;
	private static boolean drawVector = false; 
	private static boolean drawName = false;
	private boolean texture = false;
	
	
	private String name;
	private double mass, speedX, speedY, radius;
	private Vector vector; 
	private Point2D.Double position;
	private Shape objectShape;
	
	private ArrayList<Line2D.Double> trajectory = new ArrayList<Line2D.Double>(); 
	private Color color = new Color(255, 255, 255); 
	private Image image = null;
	private boolean selected = false;
	private int textureIndex = 0;
	
	/**
	 * Creates a celestial object
	 * @param name Name of the object
	 * @param mass Mass of the object
	 * @param x Current position on the X axis
	 * @param y Current position on the Y axis
	 * @param sX Current speed on the X axis
	 * @param sY Current speed on the Y axis
	 */
	public CelestialObject(String name, double mass, double x, double y, double sX, double sY){
		this.name = name;
		this.mass = mass;
		position = new Point2D.Double(x,y);
		setSpeedX(sX);
		setSpeedY(sY);
		radiusCalc();
		vector = new Vector(x, y, speedX*VECTOR_MULTIPLIER, speedY*VECTOR_MULTIPLIER);
		colorCalc();
	}
	
	/**
	 * Creates a copy of a CelestialObject
	 * @param objectToCopy 
	 */
	public CelestialObject(CelestialObject objectToCopy){
		this.name = objectToCopy.getName();
		this.mass = objectToCopy.getMass();
		position = new Point2D.Double(objectToCopy.getPositionX(), objectToCopy.getPositionY());
		setSpeedX(objectToCopy.getSpeedX());
		setSpeedY(objectToCopy.getSpeedY());
		radiusCalc();
		vector = new Vector(objectToCopy.getPositionX(), objectToCopy.getPositionY(), speedX*VECTOR_MULTIPLIER, speedY*VECTOR_MULTIPLIER);
		colorCalc();
	}

	/**
	 * Draws a celestial object
	 * @param g2d Graphics2D object used in the drawing
	 * @param matTransfo Transformation matrix for the conversion between pixels and real world units
	 */
	public void draw(Graphics2D g2d, AffineTransform matTransfo) {
		AffineTransform matBackup = new AffineTransform(g2d.getTransform());
		objectShape = new Ellipse2D.Double(position.getX()-radius/2, position.getY()-radius/2, radius, radius);
		objectShape =  matTransfo.createTransformedShape(objectShape);
		
		if(selected){
			g2d.setColor(Color.YELLOW);
			Shape rec = new Rectangle2D.Double(position.getX()-radius*1.2/2, position.getY()-radius*1.2/2, radius*1.2, radius*1.2);
			rec = matTransfo.createTransformedShape(rec);
			g2d.draw(rec);
		}
		
		if(drawVector){
			g2d.setColor(Color.GREEN);
			vector.draw(g2d, matTransfo);
		}
		
		if(drawName){
			Point2D.Double pDst = new Point2D.Double();
			Point2D.Double pSrc = new Point2D.Double(position.getX()+Math.sin(22.5*Math.PI/180)*45*Math.PI/180*radius, position.getY()+1000+radius/2);
			matTransfo.transform(pSrc, pDst);
			g2d.setColor(Color.RED);
			g2d.drawString(name, (int)pDst.getX(), (int)pDst.getY());	
		}
		
		g2d.setColor(color);
		if(textureIndex!=0){
			if(texture){
				try {
				URL fichDecor = getClass().getClassLoader().getResource("planet"+textureIndex+".png");
				image = ImageIO.read(fichDecor);
				} 
				catch (IOException e) {
					System.out.println("Image fetch error");
				}
				texture = false;
			}
			g2d.setTransform(matTransfo);
			g2d.drawImage(image, (int)(position.getX()-radius*1.6/2), (int)(position.getY()-radius*1.6/2), (int)(radius*1.6), (int)(radius*1.6), null);
		}
		else{
			g2d.fill(objectShape);
		}
		g2d.setTransform(matBackup);
	}
	
	/**
	 * Draws the past trajectory on the background image
	 * @param gImage Graphics2D object used in the drawing
	 * @param matTransfo Transformation matrix for the conversion between pixels and real world units
	 */
	public void drawTrajectory(Graphics2D gImage, AffineTransform matTransfo){
		if(drawTrajectory){
			gImage.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
			Stroke s = gImage.getStroke();
			gImage.setStroke(new BasicStroke((float) 1));
			gImage.setColor(color);
			for(int i=0; i<trajectory.size(); i++){
				gImage.draw(matTransfo.createTransformedShape(trajectory.get(i)));
			}
			trajectory.clear();
			gImage.setStroke(s);
		}
	}	
	
	/**
	 * Calculates the radius of the celestial object based on its mass
	 */
	private void radiusCalc(){
		if(mass>0.01){
			radius = CORRECTION_FACTOR*Math.log10(mass*SOLAR_RADIUS);
		}
		else{
			radius = mass*30000;
		}
	}
	
	/**
	 * Calculates the celestial object's color based on its mass (from white to red)
	 */
	private void colorCalc(){
		//random colors!
//		int r = (int) (Math.random()*255);
//		int g = (int) (Math.random()*255);
//		int b = (int) (Math.random()*255);
//		couleur = new Color(r, g, b);
		
		if(color.getBlue()>0 && mass<25){
			int r = 255; 
			int g = 255; 
			int b = (int) ((255-10*mass));
			color = new Color( r, g, b);
		}
		else if(color.getGreen()>0 && mass<255){
			int r = 255; 
			int g = (int) (255-1*mass); 
			int b = 0;
			color = new Color( r, g, b);
		}
		else{
			color = new Color(255, 0 ,0);
		}
	}

	/**
	 * Returns the mass of the object in kilogramms
	 * @return The mass of the object in kilogramms
	 */
	public double getMassInKilos() {
		return mass*SOLAR_MASS;
	}
	
	/**
	 * Returns the name of the CelestialObject
	 * @return Name of the CelestialObject
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the mass of the object in Solar Masses
	 * @return The mass of the object in Solar Masses
	 */
	public double getMass() {
		return mass;
	}
	
	/**
	 * Returns the speed of the object on the X axis in Megameters per second
	 * @return The speed of the object on the X axis in Megameters per second
	 */
	public double getSpeedX() {
		return speedX;
	}
	
	/**
	 * Returns the speed of the object on the Y axis in Megameters per second
	 * @return The speed of the object on the Y axis in Megameters per second
	 */
	public double getSpeedY() {
		return speedY;
	}

	/**
	 * Returns the position of the object on the X axis in Megameters
	 * @return The position of the object on the X axis in Megameters
	 */
	public double getPositionX() {
		return position.getX();
	}
	
	/**
	 * Returns the position of the object on the Y axis in Megameters
	 * @return The position of the object on the Y axis in Megameters
	 */
	public double getPositionY(){
		return position.getY();
	}

	/**
	 * Returns the radius of the object in Megameters
	 * @return The radius of the object in Megameters
	 */
	public double getRadius() {
		return radius;
	}
	
	/**
	 * Changes the position of the celestial object
	 * @param nX The new position in X
	 * @param nY The new position in Y
	 */
	public void setPosition(double nX, double nY){
		if(drawTrajectory){
			trajectory.add(new Line2D.Double(position.getX(), position.getY(), nX, nY));
		}
		position = new Point2D.Double(nX, nY);
		vector = new Vector(nX, nY, speedX*VECTOR_MULTIPLIER, speedY*VECTOR_MULTIPLIER);
	}
	
	/**
	 * Changes the speed of the celestial object on the X axis
	 * @param v The new speed on the X axis
	 */
	public void setSpeedX(double v){
		speedX = v;
	}
	
	/**
	 * Changes the speed of the celectial object on the Y axis
	 * @param v The new speed on the Y axis
	 */
	public void setSpeedY(double v){
			speedY = v;
	}

	/**
	 * Changes the mass of the celestial object
	 * @param m The new mass
	 */
	public void setMass(double m) {
		mass=m;
		radiusCalc();
		colorCalc();
	}

	/**
	 * Returns the list of Line2D objects that make up the past trajectory
	 * @return The list of Line2D objects that make up the past trajectory
	 */
	public ArrayList<java.awt.geom.Line2D.Double> getTrajectory() {
		return trajectory;
	}
	
	/**
	 * Toggles the drawing of the trajectory for all objects, based on the current state
	 */
	public void toggleTrajectory() {
		if(drawTrajectory){
			drawTrajectory = false;
		}
		else{
			drawTrajectory = true;
		}
		
	}
	
	/**
	 * Toggles the drawing of the names of celestial objects, based on the current state
	 */
	public void toggleName() {
		if(drawName){
			drawName = false;
		}
		else{
			drawName = true;
		}
	}
	
	/**
	 * Toggles the drawing of the speed vector of celestial objects, based on the current state
	 */
	public void toggleVector(){
		if(drawVector){
			drawVector = false;
		}else{
			drawVector = true;
		}
	}
	
	/**
	 * Returns the celestial object's name
	 * @return The celestial object's name
	 */
	public String toString(){
		return name;
	}
	
	/**
	 * Changes the name of the celestial object
	 * @param nom2 The new name of the celestial object
	 */
	public void setName(String nom2) {
		name = nom2;
	}
	
	/**
	 * Returns all the properties of the celestial object in a string
	 * @return The string containing all the properties of the celestial object
	 */
	public String write() {
		return (name+", "+mass+","+position.getX()+","+position.getY()+","+speedX+","+speedY);
	}

	/**
	 * Sets the seleced value of a celestial object 
	 * @param b Whether the object is selected
	 */
	public void setSelected(boolean b) {
		selected = b;
	}

	/**
	 * Sets which texture is to be used for the drawing of the celestial body
	 * @param i Index of the chosen texture in the list of textures
	 */
	public void setTexture(int i) {
		textureIndex = i;
		texture = true;
	}

	/**
	 * Returns the index of the current texture of the celestial object
	 * @return The index of the texture
	 */
	public int getTextureIndex() {
		return textureIndex;
	}
	
	/**
	 * Defines the equals method for celestial objects. Two objects are equal if they share the same name
	 * @param a2 Celestial object to compare to
	 * @return True or False
	 */
	public boolean equals(CelestialObject a2){
		if(name.equals(a2.toString())){
			return true;
		}
		else{
			return false;
		}
	}

}
