import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.swing.*;


public class DrawingAreaPanel extends JPanel
{
	static final long serialVersionUID = 1L;
	
	private Vector<InkSegment> v;
	InkSegment prevLine;
	private BufferedImage image;

	private class InkSegment extends Line2D.Double
	{	
	  // the following avoids a "warning" with Java 1.5.0 complier (?)
		private static final long serialVersionUID = 1L;
		private Color	color;
		private Stroke	stroke;
		
		@SuppressWarnings("unused")
		public InkSegment()
		{
			super();
			this.stroke	= new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		}
		public InkSegment(double x1, double y1, double x2, double y2, Color c, Stroke s)
		{
			super(x1, y1, x2, y2);
			this.color = c;
			this.stroke = s;
		}
		@SuppressWarnings("unused")
		public InkSegment(Line2D.Double l, Color c, Stroke s)
		{
			super(l.getX1(), l.getY1(), l.getX2(), l.getY2());
			this.color = c;
			this.stroke = s;
		}
		
		public Stroke getStroke()
		{
			return this.stroke;
		}
		@SuppressWarnings("unused")
		public void setStroke(Stroke s)
		{
			this.stroke = s;
		}
		public Color getColor()
		{
			return this.color;
		}
		@SuppressWarnings("unused")
		public void setColor(Color c)
		{
			this.color = c;
		}
	}
	

	
	DrawingAreaPanel()
	{
		v = new Vector<InkSegment>();
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

  // Overrides the original paintComponent
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (image != null)
		{
			g.drawImage(image, 0, 0, null, null);
		}
				

	  // Paint all the line segments stored in the vector
		for (int i = 0; i < v.size(); ++i) {
			InkSegment s = (InkSegment) v.elementAt(i);

			g.setColor(s.getColor());		// set the inking color
			((Graphics2D) g).setStroke(s.getStroke());	// set desired stroke
			
			((Graphics2D) g).draw((Line2D.Double) s);
		}
	}  


	public BufferedImage getImage()
	{
		return image;
	}

	public void setImage(BufferedImage bI)
	{
		this.image = bI;
	}

	/** Draw one line segment, then add it to the vector.<p>
	 */
	public void drawPoint(Point[] p, int lastPoint, Brush b)
	{
		// get graphics context
		Graphics2D g = (Graphics2D) this.getGraphics();

		// create the line
		int oldX = (int) p[lastPoint - 1].getX();
		int newX = (int) p[lastPoint].getX();
		int oldY = (int) p[lastPoint - 1].getY();
		int newY = (int) p[lastPoint].getY();

		InkSegment segment = new InkSegment(oldX, oldY, newX, newY, b.color, b.stroke);
		
		g.setColor(b.color);		// set the inking color
		Stroke s = g.getStroke();	// save current stroke
		g.setStroke(b.stroke);		// set desired stroke 
		g.draw((Line2D.Double) segment);			// draw it!   
		g.setStroke(s);			// restore stroke 
		v.add(segment);				// add to vector
	}

	public void clear()
	{
		v.clear();
		this.setImage(null);
		this.repaint();
	}
	
	public void drawLine(Point[] p, int lastPoint, Brush b)
	{
		// get graphics context
		Graphics2D g = (Graphics2D) this.getGraphics();

		InkSegment segment;

		// create the line
		int oldX = (int) p[0].getX();
		int newX = (int) p[lastPoint].getX();
		int oldY = (int) p[0].getY();
		int newY = (int) p[lastPoint].getY();

//		InkSegment inkSegment = new InkSegment(segment, b.color, b.stroke);
		segment = new InkSegment(oldX, oldY, newX, newY, b.color, b.stroke);
		
		this.repaint();
		
		g.setColor(b.color);		// set the inking color
		Stroke s = g.getStroke();	// save current stroke
		g.setStroke(s);				// set desired stroke 
		
		
		g.draw((Line2D.Double) segment);			// draw it!   
 
		v.remove(prevLine);
		prevLine = segment;			// save as the previous line
		v.add(prevLine);
	}
	
	public void removeLine()
	{
		if (prevLine != null) prevLine = null;
	}


}
