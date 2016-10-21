/**
 * Manages the geometry and representation of visual vectors during the animation
 * @author Karim
 */

package geometry;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class Vector {

	private double origX;
	private double origY;
	private double y;
	private double x;
	private double orientation;

	private final double POINT_LENGTH = 1000.0; //lengths of the lines forming the head of the vector
	private final double POINT_ANGLE = 0.5; //radian angle between the body and a head line of a vector

	private Line2D.Double body, headLine; 
		
	/**
	 * Creates a graphical vector
	 * @param origX Origin on the X axis
	 * @param origY Origin on the Y axis
	 * @param x Final coordinate on the X axis
	 * @param y Final coordinate on the Y axis
	 */
	public Vector(double origX, double origY, double x, double y) {
		this.origX = origX;
		this.origY = origY;
		this.x=x;
		this.y=y;

		createSubShapes();
		
	}//end constructor
	
	/**
	 * Creates the sub-shapes involved in drawing the vector
	 */
	private void createSubShapes() {
		body = new Line2D.Double(origX, origY, (origX+x), (origY+y));
		headLine = new Line2D.Double(origX+x, origY+y, origX+x+POINT_LENGTH, origY+y-POINT_LENGTH);
	}
	
	/**
	 * Draws the vector
	 * @param g2d Graphics2D object used in the drawing
	 * @param matTransfo Transformation matrix
	 */
	public void draw(Graphics2D g2d, AffineTransform matTransfo) {
		//saves the current transformation matrix
		AffineTransform matBackup = new AffineTransform(g2d.getTransform());
		AffineTransform matWork = new AffineTransform(matTransfo);
		
		g2d.draw(matWork.createTransformedShape(body));  //body of the vector
		
		//angle calculation for the head lines
		double angle1 = Math.atan2(y, x);
		double angle2 = Math.atan2(0, 1000);
		orientation = angle1-angle2-Math.PI;
		
		
		matWork.rotate(orientation, origX+x, origY+y);
		matWork.rotate(POINT_ANGLE, origX+x, origY+y);
		g2d.draw(matWork.createTransformedShape(headLine));  //first head line
		
		matWork.rotate(POINT_ANGLE, origX+x, origY+y);
		g2d.draw(matWork.createTransformedShape(headLine)); //second head line
		
		matWork = matTransfo;
		g2d.setTransform(matBackup);  //restores initial transformation matrix
	}


}//end class

