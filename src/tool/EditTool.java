/**
 * Manages the tool for editing the properties of the selected celestial object
 * @author Karim
 */

package tool;

import geometry.CelestialObject;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class EditTool  extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldMass;
	private JLabel labelMass;
	private JLabel labelName;
	private JTextField textFieldName;
	private JLabel labelTexture;
	private JButton buttonTexture;
	private TextureSelection selection = new TextureSelection();
	private JLabel labelSpeedX;
	private JLabel labelSpeedY;
	private JTextField textFieldSpeedX;
	private JTextField textFieldSpeedY;
	private JButton buttonConfirm;
	private JButton buttonDelete;
	private JLabel labelPositionX;
	private JTextField textFieldPositionX;
	private JTextField textFieldPositionY;
	private JLabel labelPositionY;
	private CelestialObject celestialObject = new CelestialObject("", 0, 0, 0, 0, 0);
	private JComboBox<String> comboBox;	
	private ArrayList<CelestialObject> list;
	private int index;

	/**
	 * Initializes the tool
	 */
	public EditTool() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setClosable(false);
		setResizable(false);
		setTitle("Edit Tool");
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textFieldMass = new JTextField();
		textFieldMass.setBounds(127, 29, 75, 20);
		textFieldMass.setText(celestialObject.getMass()+"");
		contentPane.add(textFieldMass);
		textFieldMass.setColumns(10);
		
		labelMass = new JLabel("Mass (in M\u2609):");
		labelMass.setForeground(Color.WHITE);
		labelMass.setBounds(6, 32, 111, 14);
		contentPane.add(labelMass);
		
		labelName = new JLabel("Object name: ");
		labelName.setForeground(Color.WHITE);
		labelName.setBounds(6, 6, 97, 14);
		contentPane.add(labelName);
		
		textFieldName = new JTextField();
		textFieldName.setText(celestialObject.toString()+"");
		textFieldName.setColumns(10);
		textFieldName.setBounds(127, 3, 75, 20);
		contentPane.add(textFieldName);
		
		labelTexture = new JLabel("Texture: ");
		labelTexture.setForeground(Color.WHITE);
		labelTexture.setBounds(210, 6, 111, 14);
		contentPane.add(labelTexture);
		
		Image image = null;
		try {
			URL fichDecor = getClass().getClassLoader().getResource("planet"+selection.returnIndex()+".png");
			image = ImageIO.read(fichDecor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon start = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(53, 53, Image.SCALE_DEFAULT));
		buttonTexture = new JButton(start);
		buttonTexture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selection.setVisible(true);
			}
		});
		buttonTexture.setBounds(260, 6, 39, 39);
		contentPane.add(buttonTexture);
		
		selection.addSelectionListener(new SelectionListener() {
			@Override
			public void nouvelleTexture(Icon icon) {
				buttonTexture.setIcon(icon);
			}
			
		});
		
		textFieldSpeedX = new JTextField();
		textFieldSpeedX.setBounds(128, 58, 171, 20);
		textFieldSpeedX.setText(celestialObject.getSpeedX()+"");
		contentPane.add(textFieldSpeedX);
		textFieldSpeedX.setColumns(10);
		
		textFieldSpeedY = new JTextField();
		textFieldSpeedY.setBounds(128, 83, 171, 20);
		textFieldSpeedY.setText(celestialObject.getSpeedY()+"");
		contentPane.add(textFieldSpeedY);
		textFieldSpeedY.setColumns(10);
		
		labelSpeedX = new JLabel("Speed in X (Mm/s): ");
		labelSpeedX.setForeground(Color.WHITE);
		labelSpeedX.setBounds(6, 61, 128, 14);
		contentPane.add(labelSpeedX);
		
		labelSpeedY = new JLabel("Speed in Y (Mm/s): ");
		labelSpeedY.setForeground(Color.WHITE);
		labelSpeedY.setBounds(6, 86, 128, 14);
		contentPane.add(labelSpeedY);
		
		buttonConfirm = new JButton("Confirm the changes");
		buttonConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(isNumeric(textFieldMass.getText()) && isNumeric(textFieldSpeedX.getText()) && isNumeric(textFieldSpeedY.getText()) && isNumeric(textFieldPositionX.getText()) && isNumeric(textFieldPositionY.getText())){
					celestialObject.setMass(Double.parseDouble(textFieldMass.getText()));
					celestialObject.setName(textFieldName.getText());
					celestialObject.setSpeedX(Double.parseDouble(textFieldSpeedX.getText()));
					celestialObject.setSpeedY(Double.parseDouble(textFieldSpeedY.getText()));
					celestialObject.setPosition(Double.parseDouble(textFieldPositionX.getText()), Double.parseDouble(textFieldPositionY.getText()));
					celestialObject.setTexture(selection.returnIndex());
					
					comboBox.removeAllItems() ;
					comboBox.addItem("None");
					for (int k=0; k<list.size(); k++){
						comboBox.addItem( (k+1) + ". "+ list.get(k).toString());
					}
					comboBox.setSelectedIndex(index);
					
				}
				else{
					JOptionPane.showMessageDialog(null, "Invalid values in the Edit Tool");
				}
				
			}
		});
		buttonConfirm.setBounds(6, 193, 295, 25);
		contentPane.add(buttonConfirm);
		
		labelPositionX = new JLabel("Position in X (Mm): ");
		labelPositionX.setForeground(Color.WHITE);
		labelPositionX.setBounds(6, 114, 128, 14);
		contentPane.add(labelPositionX);
		
		textFieldPositionX = new JTextField();
		textFieldPositionX.setColumns(10);
		textFieldPositionX.setBounds(128, 110, 171, 20);
		textFieldPositionX.setText(celestialObject.getPositionX()+"");
		contentPane.add(textFieldPositionX);
		
		textFieldPositionY = new JTextField();
		textFieldPositionY.setColumns(10);
		textFieldPositionY.setBounds(128, 135, 171, 20);
		textFieldPositionY.setText(celestialObject.getPositionY()+"");
		contentPane.add(textFieldPositionY);
		
		labelPositionY = new JLabel("Position in Y (Mm): ");
		labelPositionY.setForeground(Color.WHITE);
		labelPositionY.setBounds(6, 135, 128, 14);
		contentPane.add(labelPositionY);
		
		buttonDelete = new JButton("Delete");
		buttonDelete.setBounds(6, 166, 295, 25);
		contentPane.add(buttonDelete);
	}
	
	private static boolean isNumeric(String str)  {  
		@SuppressWarnings("unused")
		double d;
		try {  
			d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe){  
			return false;  
		}
		return true;
	}
	
	/**
	 * Returns the value of the mass in the mass text field
	 * @return The value of the mass
	 */
	public double getMass(){
		return Double.parseDouble(textFieldMass.getText());
	}
	
	/**
	 * Returns the selected index in the list of textures
	 * @return The selected index
	 */
	public int returnIndex(){
		return selection.returnIndex();
	}

	/**
	 * Returns the string in the name text field
	 * @return The string in the name text field
	 */
	public String getNameInTextField() {
		return textFieldName.getText();
	}

	/**
	 * Changes the selected celestial object to the one in argument
	 * @param celestialObject2 New selected celestial object
	 */
	public void astre(CelestialObject celestialObject2) {
		celestialObject = celestialObject2;
		update();
	}

	/**
	 * Updates the values in the Edit Tool fields to the ones of the selected celestial object
	 */
	public void update() {
		textFieldMass.setText(celestialObject.getMass()+"");
		textFieldName.setText(celestialObject.toString()+"");
		textFieldSpeedX.setText(celestialObject.getSpeedX()+"");
		textFieldSpeedY.setText((celestialObject.getSpeedY())+"");
		textFieldPositionX.setText((celestialObject.getPositionX())+"");
		textFieldPositionY.setText((celestialObject.getPositionY())+"");
		selection.setIndex(celestialObject.getTextureIndex());
		buttonTexture.setIcon(selection.getIcon());
	}

	/**
	 * Returns the confirm button
	 * @return The button
	 */
	public JButton getConfirmButton() {
		return buttonConfirm;
	}
	
	/**
	 * Returns the delete button
	 * @return The delete button
	 */
	public JButton getDeleteButton() {
		return buttonDelete;
	}

	/**
	 * Returns the Texture Selection instance of the Edit Tool
	 * @return The Texture Selection instance of the Edit Tool
	 */
	public TextureSelection getSelection() {
		return selection;
	}
	
	/**
	 * Updates the internal list of celestial objects
	 * @param cmbBoxAstres ComboBox of celestial object names
	 * @param listeAstres List of celestial objects
	 * @param index Index of the selected celestial object
	 */
	public void updateList(JComboBox<String> cmbBoxAstres,	ArrayList<CelestialObject> listeAstres, int index) {
		comboBox = cmbBoxAstres;
		list = listeAstres;
		this.index=index;
	}
}
