import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;


public final class Model
{
	private Model() {}
	
	public static void setNewCanvasSize(DrawingAreaPanel c, JPanel p, JScrollPane s, int width, int height)
	{
		c.clear();
		c.setBounds(0, 0, width, height);
		setComponentSize(p, width, height);
		s.revalidate();
	}
	
	public static void setComponentSize(Component c, int width, int height)
	{
		Dimension d = new Dimension(width, height);
		c.setMaximumSize(d);
		c.setMinimumSize(d);
		c.setPreferredSize(d);
	}

}
