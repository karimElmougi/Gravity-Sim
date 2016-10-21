/**
 * Manages the tool for managing various functionalities of the simulation
 * @author Karim
 */

package tool;


import java.awt.Color;


import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;


public class ManagerTool extends JInternalFrame {
	private static final long serialVersionUID = -3588317955825255063L;
	private JPanel contentPane;
	
	private JCheckBox chckbxTrajectoire;
	private JCheckBox chckbxVecteur;
	private JCheckBox chckbxNom;
	
	private JComboBox<String> lstDrlAstres;
	private JComboBox<String> lstDrlCalcul;
	
	private JButton btnRenitialiser;
	
	private JLabel lblSlectionnerUnAstre;
	private JLabel lblMthodeDeCalcul;
	private JLabel lblValeurDeG;
	
	private JSpinner spinnerG;
	private JSpinner spinnerOrdreG;
	private JLabel label_1;


	/**
	 * Initializes the tool
	 */
	public ManagerTool() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setClosable(true);
		setResizable(false);
		setTitle("Manager Tool");
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("");
		label.setBounds(10, 11, 46, 14);
		contentPane.add(label);

		chckbxTrajectoire = new JCheckBox("Display trajectories");
		chckbxTrajectoire.setBounds(6, 18, 380, 23);
		chckbxTrajectoire.setBackground(Color.BLACK);
		chckbxTrajectoire.setForeground(Color.WHITE);
		contentPane.add(chckbxTrajectoire);

		chckbxVecteur = new JCheckBox("Display speed vectors");
		chckbxVecteur.setBounds(6, 44, 226, 23);
		chckbxVecteur.setForeground(Color.WHITE);
		chckbxVecteur.setBackground(Color.BLACK);
		contentPane.add(chckbxVecteur);

		chckbxNom = new JCheckBox("Display names");
		chckbxNom.setBounds(6, 70, 160, 23);
		chckbxNom.setBackground(Color.BLACK);
		chckbxNom.setForeground(Color.WHITE);
		contentPane.add(chckbxNom);

		lstDrlAstres = new JComboBox<String>();
		lstDrlAstres.setBounds(160, 100, 150, 20);
		lstDrlAstres.addItem("None");
		contentPane.add(lstDrlAstres);

		lblSlectionnerUnAstre = new JLabel("Select an object: ");
		lblSlectionnerUnAstre.setBounds(10, 100, 140, 14);
		lblSlectionnerUnAstre.setForeground(Color.WHITE);
		contentPane.add(lblSlectionnerUnAstre);

		btnRenitialiser = new JButton("Reinitialize");
		btnRenitialiser.setBounds(10, 199, 300, 25);
		contentPane.add(btnRenitialiser);

		lstDrlCalcul = new JComboBox<String>();
		lstDrlCalcul.setBounds(160, 128, 150, 20);
		lstDrlCalcul.setModel(new DefaultComboBoxModel<String>(new String[] {"Euler", "Inverse Euler", "Runge-Kutta 4th order"}));
		contentPane.add(lstDrlCalcul);

		lblMthodeDeCalcul = new JLabel("Integration Method: ");
		lblMthodeDeCalcul.setBounds(10, 131, 120, 14);
		lblMthodeDeCalcul.setForeground(Color.WHITE);
		contentPane.add(lblMthodeDeCalcul);

		lblValeurDeG = new JLabel("G constant value:");
		lblValeurDeG.setBounds(10, 164, 140, 14);
		lblValeurDeG.setForeground(Color.WHITE);
		contentPane.add(lblValeurDeG);

		spinnerG = new JSpinner();
		spinnerG.setBounds(160, 159, 60, 20);
		spinnerG.setModel(new SpinnerNumberModel(new Double(6.67), null, null, new Double(0.01)));
		contentPane.add(spinnerG);

		spinnerOrdreG = new JSpinner();
		spinnerOrdreG.setBounds(269, 159, 39, 20);
		spinnerOrdreG.setModel(new SpinnerNumberModel(new Double(-11), null, null, new Double(1)));
		contentPane.add(spinnerOrdreG);
		
		label_1 = new JLabel("\u00B710^");
		label_1.setForeground(Color.WHITE);
		label_1.setBounds(230, 159, 29, 22);
		contentPane.add(label_1);
	}//end constructor

	/**
	 * Returns whether to draw the trajectory or not
	 * @return True or False
	 */
	public boolean trajectoryBool(){
		if(chckbxTrajectoire.isSelected()){
			return true;
		}
		return false;
	}

	/**
	 * Returns whether to draw the vectors or not
	 * @return True or False
	 */
	public boolean vecteur(){
		if(chckbxVecteur.isSelected()){
			return true;
		}
		return false;
	}
	
	/**
	 * Returns whether to draw the names or not
	 * @return True or False
	 */
	public boolean nom(){
		if(chckbxNom.isSelected()){
			return true;
		}
		return false;	
	}

	/**
	 * Returns the checkbox related to the drawing of the trajectory
	 * @return The checkbox
	 */
	public AbstractButton getChckbxTrajectoire() {
		return chckbxTrajectoire;
	}
	
	/**
	 * Returns the checkbox related to the drawing of the vectors
	 * @return The checkbox
	 */
	public AbstractButton getChckbxVecteur() {
		return chckbxVecteur;
	}
	
	/**
	 * Returns the checkbox related to the drawing of the names
	 * @return The checkbox
	 */
	public AbstractButton getChckbxNom() {
		return chckbxNom;
	}

	/**
	 * Returns the combobox of the integration methods
	 * @return The combobox
	 */
	public JComboBox<String> getCmbBoxCalcul(){
		return lstDrlCalcul;
	}

	/**
	 * Returns the spinner with the base of the value of G
	 * @return The spinner
	 */
	public JSpinner getSpinnerG(){
		return spinnerG;
	}

	/**
	 * Returns the spinner with the exponent of the value of G
	 * @return The spinner
	 */
	public JSpinner getSpinnerOrdreG(){
		return spinnerOrdreG;
	}

	/**
	 * Returns the reinitialization button
	 * @return The button
	 */
	public JButton getBtnRenitialiser() {
		return btnRenitialiser;
	}

	/**
	 * Returns the combobox of the list of celestial objects
	 * @return The combobox
	 */
	public JComboBox<String> getCmbBoxAstres() {
		return lstDrlAstres;
	}
}
