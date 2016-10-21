/**
 * Manages the tool for controlling the temporal aspect of the simulation
 * @author Karim
 */

package tool;


import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class TimeTool extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblTemps;
	private JTextField txtFTemps;
	private JComboBox<String> cmbBxTemps;
	private JButton btnArret;
	private JButton btnDemarrer;
	private double temps = 30.0;
	private JButton btnConfirmer;
	
	/**
	 * Initializes the tool
	 */
	public TimeTool() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setClosable(true);
		setResizable(false);
		setTitle("Time Tool");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblTemps = new JLabel("1 second =");
		lblTemps.setForeground(Color.WHITE);
		lblTemps.setBounds(10, 10, 72, 30);
		contentPane.add(lblTemps);
		
		txtFTemps = new JTextField();
		txtFTemps.setText(Math.floor(temps)+"");
		txtFTemps.setBounds(92, 15, 54, 20);
		contentPane.add(txtFTemps);
		txtFTemps.setColumns(10);
		
		cmbBxTemps = new JComboBox<String>();
		cmbBxTemps.setModel(new DefaultComboBoxModel<String>(new String[] {"seconds", "minutes", "hours"}));
		cmbBxTemps.setSelectedIndex(1);
		cmbBxTemps.setBounds(156, 15, 115, 20);
		contentPane.add(cmbBxTemps);
		
		Image image = null;
		try {
			URL fichDecor = getClass().getClassLoader().getResource("play.png");
			image = ImageIO.read(fichDecor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon start = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(53, 53, Image.SCALE_DEFAULT));
		btnDemarrer= new JButton(start);
		btnDemarrer.setBounds(222, 46, 49, 49);
		contentPane.add(btnDemarrer);
		
		try {
			URL fichDecor = getClass().getClassLoader().getResource("pause.png");
			image = ImageIO.read(fichDecor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		start = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(53, 53, Image.SCALE_DEFAULT));
		btnArret= new JButton(start);
		btnArret.setBounds(222, 46, 49, 49);
		contentPane.add(btnArret);
		
		btnConfirmer = new JButton("Confirm");
		btnConfirmer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnConfirmer.setBounds(10, 46, 202, 49);
		contentPane.add(btnConfirmer);
		
	}
	
	/**
	 * Returns the textfield with the time value
	 * @return The textfield
	 */
	public JTextField getTxtFTemps() {
		return txtFTemps;
	}
	
	/**
	 * Returns the combobox with the time units
	 * @return The combobox
	 */
	public JComboBox<String> getCmbBxTemps() {
		return cmbBxTemps;
	}
	
	/**
	 * Returns the Pause button
	 * @return The Pause button
	 */
	public JButton getBtnArret() {
		return btnArret;
	}
	
	/**
	 * Returns the Play button
	 * @return The Play button
	 */
	public JButton getBtnDemarrer() {
		return btnDemarrer;
	}
	
	/**
	 * Returns the time value
	 * @return The time value
	 */
	public double getTemps(){
		return temps;
	}

	/**
	 * Sets the time value to the one passed in argument
	 * @param t New time value
	 */
	public void setTemps(double t) {
		temps = t;
	}

	/**
	 * Returns the Confirm button
	 * @return The Confirm button
	 */
	public JButton getBtnConfirmer() {
		return btnConfirmer;
	}
}
