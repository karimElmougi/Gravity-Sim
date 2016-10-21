/**
 * Custom listener for texture selection
 * @author Karim
 */

package tool;

import java.util.EventListener;

import javax.swing.Icon;

public interface SelectionListener extends EventListener{
	public void nouvelleTexture(Icon icon);
}
