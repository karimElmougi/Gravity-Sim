/**
 * Manages texture selection for drawing celestial objects
 *@author Karim
 */

package tool;

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
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;

public class TextureSelection extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private int indexSelec = 0;
	private ArrayList<JButton> listeBoutons = new ArrayList<JButton>();
	private final EventListenerList OBJETS_ENREGISTRES = new EventListenerList();
	
	/**
	 * Initializes the tool
	 */
	public TextureSelection() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setClosable(true);
		setBounds(0, 0, 620, 150);
		setTitle("Texture Selection");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
			
		Image image = null;
		ImageIcon icone = null;
		try {
			image = ImageIO.read( getClass().getClassLoader().getResource("planet"+0+".png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		icone = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(53, 53, Image.SCALE_DEFAULT));
		listeBoutons.add(new JButton(icone));
		listeBoutons.get(0).setBounds(0, 5, 53, 53);
		listeBoutons.get(0).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				indexSelec = 0;
				hide();
			}
		});
		contentPane.add(listeBoutons.get(0));
		
		for(int i=1; i<19; i++){
			try {		
				String url = ("planet"+i+".png");
				URL fichDecor = getClass().getClassLoader().getResource(url);
				image = ImageIO.read(fichDecor);
			} catch (IOException e) {
				e.printStackTrace();
			}
			icone = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(53, 53, Image.SCALE_DEFAULT));
			listeBoutons.add(new JButton(icone));
			if(i<=10){listeBoutons.get(i).setBounds(54*i, 5, 53, 53);}
			else{listeBoutons.get(i).setBounds((-594+(54*i)), 58, 53, 53);}
			listeBoutons.get(i).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					indexSelec = listeBoutons.indexOf(e.getSource());
					hide();
					for(SelectionListener ecout: OBJETS_ENREGISTRES.getListeners(SelectionListener.class)){
						ecout.nouvelleTexture(listeBoutons.get(indexSelec).getIcon());
					}
				}
			});
			contentPane.add(listeBoutons.get(i));
		}
	}
	
	/**
	 * Custom listener
	 */
	@SuppressWarnings("unused")
	private void lanceEventNouvelleTexture(){
		for(SelectionListener objEcout: OBJETS_ENREGISTRES.getListeners(SelectionListener.class)){
			objEcout.nouvelleTexture(listeBoutons.get(indexSelec).getIcon());
		}
	}
	
	/**
	 * Returns the index of the selected texture
	 * @return The index
	 */
	public int returnIndex(){
		return indexSelec;
	}
	
	/**
	 * Returns the custom buttons with images of the textures
	 * @return The list of buttons
	 */
	public JButton returnButton(){
		return listeBoutons.get(indexSelec);
	}
	
	public void addSelectionListener(SelectionListener objEcout){
		OBJETS_ENREGISTRES.add(SelectionListener.class, objEcout);
	}
	
	/**
	 * Sets the selected index to the one passed as argument
	 * @param indexTexture New selected index
	 */
	public void setIndex(int indexTexture) {
		indexSelec = indexTexture;
	}
	
	/**
	 * Returns the icon of the selected texture 
	 * @return The icon of the selected texture
	 */
	public Icon getIcon(){
		return listeBoutons.get(indexSelec).getIcon();
	}
}
