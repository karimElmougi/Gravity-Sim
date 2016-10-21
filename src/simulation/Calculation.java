/**
 * Manages the calculations of the gravitational attraction between objects according to the chosen integration method
 * @author Karim
 */

package simulation;


import java.util.ArrayList;

import geometry.CelestialObject;

public class Calculation {
	private final static double MASSE_SOLAIRE=(1.98855*java.lang.Math.pow(10,30));
	
	private double ordreConstanteGravitationnelle = -11;
	private double gConstanteGravitationnelle = 6.67;
	private double constanteGravitationnelle = gConstanteGravitationnelle*(java.lang.Math.pow(10,ordreConstanteGravitationnelle));
	
	double diffX;	//difference between the positions of two celestial objects on the X axis
	double diffY;	//difference between the positions of two celestial objects on the Y axis
	double angle;	//angle between the horizontal and the vector between the two celestial objects
	
	private double x, y, accX, accY, vitX, vitY, distance;
	private double deltaT;
	double accGX = 0; 
	double accGY = 0;

	private ArrayList<CelestialObject> listeDeTravail = new ArrayList<CelestialObject>();	//working list of celestial objects
	private simulation.IntegrationMethod methodeDeCalcul = IntegrationMethod.EULER;
	
	/**
	 * Calculates the effect of gravitational attraction on each celestial object
	 * @param listeAstres The list of celestial objects
	 * @param deltaT Time interval
	 * @return The list of celestial objects after having been affected by gravitational attraction over the time interval
	 */
	public ArrayList<CelestialObject> methodeDeCalcul(ArrayList<CelestialObject> listeAstres, double deltaT) {
		listeDeTravail=listeAstres;
		CelestialObject a1;
		this.deltaT = deltaT;
		
		// single object case
		if(listeDeTravail.size()==1){	
			CelestialObject a = listeDeTravail.get(0);
			a.setPosition(a.getPositionX()+a.getSpeedX()*deltaT, a.getPositionY()+a.getSpeedY()*deltaT);
		}
		else{
			for(int i=0; i<listeDeTravail.size(); i++){
				for(int l=0; l<i; l++){
					a1 = null;
					diffX = (listeDeTravail.get(i).getPositionX()-listeDeTravail.get(l).getPositionX());
					diffY = (listeDeTravail.get(i).getPositionY()-listeDeTravail.get(l).getPositionY());
					angle = Math.abs(Math.atan(diffY/diffX));
					distance = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
					a1 = collision(listeDeTravail.get(i), listeDeTravail.get(l));
					if(!(a1==null)){				
						listeDeTravail.set(i, a1);
						listeDeTravail.remove(l);
						if(i>=listeDeTravail.size()){
							i--;
						}
					}
					else{
						calculAccelerationGlobale(listeDeTravail.get(i), listeDeTravail.get(l));
					}
				}
				for (int k=i+1; k<=listeDeTravail.size()-1; k++){
					diffX = (listeDeTravail.get(i).getPositionX()-listeDeTravail.get(k).getPositionX());
					diffY = (listeDeTravail.get(i).getPositionY()-listeDeTravail.get(k).getPositionY());
					angle = Math.abs(Math.atan(diffY/diffX));
					distance = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
					a1 =collision(listeDeTravail.get(i), listeDeTravail.get(k));
					if(!(a1==null)){				
						listeDeTravail.set(i, a1);
						listeDeTravail.remove(k);
					}
					else{
						calculAccelerationGlobale(listeDeTravail.get(i), listeDeTravail.get(k));
					}
				}
				calcul(i);
			}
		}
		return listeDeTravail;
	}
	
	/**
	 * Compares a celestial object to all others in the list to obtain its global acceleration
	 * @param list List of celestial objects
	 * @param a1 Celestial object for which global acceleration is calculated
	 */
	private void comparaisonRK4(ArrayList<CelestialObject> list, CelestialObject a1){
		CelestialObject astre;
		for (int k=0; k<=list.size()-1; k++){
			diffX = (a1.getPositionX()-list.get(k).getPositionX());
			diffY = (a1.getPositionY()-list.get(k).getPositionY());
			angle = Math.abs(Math.atan(diffY/diffX));
			distance = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
			astre = collision(a1, list.get(k));
			if(!(astre == null)){				
				list.remove(k);
			}
			else{
				calculAccelerationGlobale(a1, list.get(k));
			}
		}
	}
	
	/**
	 * Chooses which integration method to use based on the input of the user
	 * @param index Index of the celestial object for which the calculation is being done
	 */
	private void calcul(int index) {
			switch (methodeDeCalcul) {
			case EULER:
				calculEuler(index, deltaT);
				break;
			case INVERSE_EULER:
				calculEulerInverse(index, deltaT);
				break;
			case RUNGE_KUTTA_4:
				calculRungeKutta4(index, deltaT);
				break;
			}
	}
	
	/**
	 * Checks if two objects have collided
	 * @param a1 First object
	 * @param a2 Second object
	 * @return Celestial object resulting from the collision (null if no collision happened)
	 */
	private CelestialObject collision(CelestialObject a1, CelestialObject a2){
		if(distance<=a1.getRadius()/2.0+a2.getRadius()/2.0){
			return combine(a1, a2);
		}
		else{
			return null;
		}	
	}
	
	/**
	 * Combines two celestial objects, adding their mass and preserving their velocities
	 * @param a1 First object
	 * @param a2 Second object
	 * @return The resulting combined celestial object
	 */
	private CelestialObject combine(CelestialObject a1, CelestialObject a2){
		double masseCombine = a1.getMass()+a2.getMass();
		
		x = (a1.getPositionX()*a1.getMass() + 
				a2.getPositionX()*a2.getMass())/masseCombine;
		y = (a1.getPositionY()*a1.getMass() + 
				a2.getPositionY()*a2.getMass())/masseCombine;
		vitX = (a1.getSpeedX()*a1.getMass() + 
				a2.getSpeedX()*a2.getMass())/masseCombine;
		vitY = (a1.getSpeedY()*a1.getMass() + 
				a2.getSpeedY()*a2.getMass())/masseCombine;
		
		CelestialObject nouvAstre = new CelestialObject("", masseCombine, x, y, vitX, vitY);
		nouvAstre.setTexture(a1.getTextureIndex());
		nouvAstre.getTrajectory().addAll(a1.getTrajectory());
		nouvAstre.getTrajectory().addAll(a2.getTrajectory());
		
		if(a1.getMass()>=a2.getMass()){
			nouvAstre.setName(a1.toString());
		}
		else{
			nouvAstre.setName(a2.toString());
		}
		return nouvAstre;
	}
	
	/**
	 * Changes the orientation of the acceleration values based on position difference
	 */
	private void orientation(){
		if(diffX>0){
			accX=-1.0*accX;
		}
		
		if(diffY>0){
			accY=-1.0*accY;
		}
	}
	
	/**
	 * Calculates the force exerted by the second object on the first and adds the resulting acceleration to the global acceleration
	 * @param a1 First object
	 * @param a2 Second object
	 */
	private void calculAccelerationGlobale(CelestialObject a1, CelestialObject a2){
		double fg = (constanteGravitationnelle*a1.getMassInKilos()*a2.getMassInKilos()/Math.pow(distance*Math.pow(10, 6), 2));
		double fgMod = fg/(MASSE_SOLAIRE*Math.pow(10, 6)); //mise à l'échelle en fonction de largeurMonde et masse solaire
		
		double fgX = fgMod*Math.cos(angle);
		double fgY = fgMod*Math.sin(angle);

		accX = fgX/a1.getMass();
		accY = fgY/a1.getMass();
		orientation();
		
		accGX = (accGX+accX);
		accGY = (accGY+accY);
	}
	
	/**
	 * Calculates the new position and speed of the celestial object whose index is passed in argument according to the Euler method
	 * @param index The index of the celestial object
	 * @param deltaT The time interval
	 */
	private void calculEuler(int index, double deltaT){	
		CelestialObject a = listeDeTravail.get(index);
		
		x = a.getPositionX() + a.getSpeedX()*deltaT;
		y = a.getPositionY() + a.getSpeedY()*deltaT;

		vitX = a.getSpeedX() + accGX*deltaT;
		vitY = a.getSpeedY() + accGY*deltaT;	
		
		a.setSpeedX(vitX);
		a.setSpeedY(vitY);
		a.setPosition(x, y);
		
		accGX = 0;
		accGY = 0;
	}
	
	/**
	 * Calculates the new position and speed of the celestial object whose index is passed in argument according to the Inverse Euler method
	 * @param index The index of the celestial object
	 * @param deltaT The time interval
	 */
	private void calculEulerInverse(int index, double deltaT){
		CelestialObject a = listeDeTravail.get(index);
		
		vitX = a.getSpeedX() + accGX*deltaT;
		vitY = a.getSpeedY() + accGY*deltaT;
		
		x = a.getPositionX() + vitX*deltaT;
		y = a.getPositionY() + vitY*deltaT;
		
		a.setSpeedX(vitX);
		a.setSpeedY(vitY);
		a.setPosition(x, y);
		
		accGX = 0;
		accGY = 0;
	}
	
	/**
	 * Calculates the new position and speed of the celestial object whose index is passed in argument according to the 4th order Runge-Kutta method
	 * @param index The index of the celestial object
	 * @param deltaT The time interval
	 */
	private void calculRungeKutta4(int index, double deltaT){
		CelestialObject a1 = new CelestialObject (listeDeTravail.get(index));
		ArrayList<CelestialObject> listeRK4 = new ArrayList<CelestialObject>(listeDeTravail);
		double x0 = a1.getPositionX();
		double vX0 = a1.getSpeedX();
		double y0 = a1.getPositionY();
		double vY0 = a1.getSpeedY();
		double accX0 = accGX+0;
		double accY0 = accGY+0;
		double xf1 = x0 + deltaT*vX0;
		double vxf1 = vX0 + accX0*deltaT;
		double yf1 = y0 + deltaT*vY0;
		double vyf1 = vY0 + accY0*deltaT;
		a1.setPosition(xf1, yf1);
		a1.setSpeedX(vxf1);
		a1.setSpeedY(vyf1);
		comparaisonRK4(listeRK4, a1); //axf(1)=accGX1
		double accXF1 = accGX+0;
		double accYF1 = accGY+0;
		accGX=0;
		accGY=0;
		
		double xMid = x0 + vX0*deltaT/2 + 0.5*accX0*Math.pow((deltaT/2), 2);
		double vxMid = vX0 + (0.75*accX0 + 0.25*accXF1)*(deltaT/2);
		double yMid = y0 + vY0*deltaT/2 + 0.5*accY0*Math.pow((deltaT/2), 2);
		double vyMid = vY0 + (0.75*accY0 + 0.25*accYF1)*(deltaT/2);
		a1.setPosition(xMid, yMid);
		a1.setSpeedX(vxMid);
		a1.setSpeedY(vyMid);
		comparaisonRK4(listeRK4, a1);
		double accXMid = accGX+0;
		double accYMid = accGY+0;
		accGX=0;
		accGY=0;
		
		double xf2 = x0 + deltaT*vX0 + 0.5*accX0*deltaT*deltaT;
		double vxf2 = vX0 + (0.5*accX0 + 0.50*accXF1)*(deltaT);
		double yf2 = y0 + deltaT*vY0 + 0.5*accY0*deltaT*deltaT;
		double vyf2 = vY0 + (0.5*accY0 + 0.50*accYF1)*(deltaT);
		a1.setPosition(xf2, yf2);
		a1.setSpeedX(vxf2);
		a1.setSpeedY(vyf2);
		comparaisonRK4(listeRK4, a1);
		double accXF2 = accGX+0;
		double accYF2 = accGY+0;
		accGX=0;
		accGY=0;
		
		x = x0 + vX0*deltaT +0.5*((100*accX0/3)/100 + (200*accXMid/3)/100)*deltaT*deltaT;
		vitX = (vX0+((100*accXF2/6)/100 + (400*accXMid/6)/100 + (100*accX0/6)/100)*deltaT);
		y = y0 + vY0*deltaT +0.5*((100*accY0/3)/100 + (200*accYMid/3)/100)*deltaT*deltaT;
		vitY = (vY0+((100*accYF2/6)/100 + (400*accYMid/6)/100 + (100*accY0/6)/100)*deltaT);
		
		listeDeTravail.get(index).setSpeedX(vitX);
		listeDeTravail.get(index).setSpeedY(vitY);
		listeDeTravail.get(index).setPosition(x, y);
		
		accGX=0;
		accGY=0;
	}
	
	/**
	 * Returns the value of the G constant used in the calculation
	 * @return The value of the G constant
	 */
	public double getConstanteGravitationnelle() {
		return constanteGravitationnelle;
	}
	
	/**
	 * Sets which integration method to use
	 * @param methodeChoisie The chosen integration method (Euler by default) 
	 */
	public void setMethodeDeCalcul(simulation.IntegrationMethod methodeChoisie){
		methodeDeCalcul = methodeChoisie;
	}

	/**
	 * Sets the exponent of the G constant (-11 by default)
	 * @param nOrdre New exponent
	 */
	public void setOrdreConstanteGravitationnelle(double nOrdre) {
		ordreConstanteGravitationnelle = nOrdre;
		constanteGravitationnelle = gConstanteGravitationnelle *Math.pow(10, ordreConstanteGravitationnelle);
		
	}

	/**
	 * Sets the base of the G constant
	 * @param nG New base 
	 */
	public void setGConstanteGravitationnelle(double nG) {
		gConstanteGravitationnelle = nG;
		constanteGravitationnelle = gConstanteGravitationnelle *Math.pow(10, ordreConstanteGravitationnelle);
	}

	

}
