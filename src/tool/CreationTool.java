/**
 * Manages the tool for the creation of celestial objects
 * @author Karim
 */

package tool;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JButton;

public class CreationTool extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldMass;
	private JLabel labelMass;
	private JLabel labelName;
	private JTextField textFieldName;
	private JLabel labelTexture;
	private JButton buttonTexture;
	private TextureSelection selection = new TextureSelection();

	/**
	 * Initializes the tool
	 */
	public CreationTool() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setClosable(true);
		setResizable(false);
		setTitle("Creation Tool");
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textFieldMass = new JTextField();
		textFieldMass.setBounds(100, 29, 86, 21);
		textFieldMass.setText("1");
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
		textFieldName.setText("Object");
		textFieldName.setColumns(10);
		textFieldName.setBounds(100, 3, 86, 21);
		contentPane.add(textFieldName);
		
		labelTexture = new JLabel("Texture: ");
		labelTexture.setForeground(Color.WHITE);
		labelTexture.setBounds(203, 6, 111, 14);
		contentPane.add(labelTexture);
		
		Image image = null;
		try {
			URL fichDecor = getClass().getClassLoader().getResource("planet"+0+".png");
			image = ImageIO.read(fichDecor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon start = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(53, 53, Image.SCALE_DEFAULT));
		buttonTexture = new JButton(start);
		buttonTexture.setBounds(267, 6, 47, 47);
		contentPane.add(buttonTexture);
		
		selection.addSelectionListener(new SelectionListener() {
			@Override
			public void nouvelleTexture(Icon icon) {
				buttonTexture.setIcon(icon);
			}
			
		});
	}
	
	/**
	 * Returns the mass value in the Mass text field
	 * @return The mass
	 */
	public String getMass(){
			return textFieldMass.getText();
	}
	
	/**
	 * Returns the name in the Name text field
	 * @return The name
	 */
	public String getNameInTextField() {
		return textFieldName.getText() + "";
	}

	/**
	 * Returns the button that opens the Texture Selection tool
	 * @return The button
	 */
	public JButton getButton() {
		return buttonTexture;
	}

	/**
	 * Returns the Texture Selection instance of the Creation Tool 
	 * @return The Texture Selection instance
	 */
	public TextureSelection getSelection() {
		return selection;
	}
	
}
