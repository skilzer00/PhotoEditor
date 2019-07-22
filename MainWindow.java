import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;




public class MainWindow extends JFrame implements
			ActionListener, MouseListener, MouseMotionListener
{
	// the following avoids a "warning" with Java 1.5.0 complier (?)
	private static final long serialVersionUID = 1L;

	final JPanel MAIN_PANEL		 = new JPanel();
	final Color	 BACKGOUND_COLOR = new Color(212, 212, 212);//Color.BLACK;
	
	JPanel toolboxPanel;
	final JScrollPane PAINTING	= new JScrollPane(MAIN_PANEL);
	
	final int BRUSH = 0;
	final int LINE = 1;
	final int PENCIL = 2;
	final int ERASER = 3;

	final int MAX_POINTS = 1048576;
	private int pointsCount;
	private Point[] points = new Point[MAX_POINTS];

	Brush brush		= new Brush();
	Brush pencil	= new Brush();
	Brush eraser	= new Brush();
	int state;
	
	
	//	ImageIcon ic = new ImageIcon(getClass().getResource("tmp.jpg"));
	DrawingAreaPanel canvas;
	JScrollPane scrollPane;
	JPanel canvasPanel;

	JToolBar toolBar;
	JPanel colorPlate;

	JButton inkColor, canvasColor, bPencil, bBrush, bEraser;
	NewFileDialog dNewFile;
	CustomUI customUI;

	JColorChooser cc;
	JFileChooser fc;

	// The Menu Items:
	JMenuItem newFile;
	JMenuItem openFile; 
	JMenuItem close;
	JMenuItem saveFile;
	JMenuItem saveFileAs;
	JMenuItem exit;





	// The Colors:
	JButton black, white, dGray, gray, lGray, green, blue, cyan,
			magenta, orange, purple, red, yellow, brown;

	MainWindow()
	{
		initGUI();
	}


	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}
	
	protected void updateInkColor(Color c)
	{
		brush.setColor(c);
		pencil.setColor(c);
		inkColor.setBackground(c);
		customUI.setBrushColor(c);
	}
	
	protected void updateCanvasColor(Color c)
	{
		eraser.setColor(c);
		canvasColor.setBackground(c);
	}
	
	// Set a constant size for the component
	private JButton colorButton(final Color c) {
		JButton b = new JButton();		
		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				updateInkColor(c);
			}
		});
		Model.setComponentSize(b, 16, 16);
		b.setBackground(c);
		colorPlate.add(b);
		return b;
	}
	
	private JButton stateButton(final int state, String s) {
		JButton b = new JButton();
		ImageIcon ic = new ImageIcon(getClass().getResource(s));
		b.setIcon(ic);
		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				setState(state);
			}
		});
		toolboxPanel.add(b);
		return b;
	}


	private /*static*/ void initGUI()
	{
		this.setFocusable(true);
		
		Dimension d = new Dimension(444, 664);
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		
		this.setSize(1000, 500);
		this.setTitle("3461 Paint");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Point location = MouseInfo.getPointerInfo().getLocation();
		this.setLocation(location);

		Container mainPanel = this.getContentPane();

		canvas	 = new DrawingAreaPanel();
		cc		 = new JColorChooser();
		dNewFile = new NewFileDialog();

		canvas.addMouseMotionListener(this);
		canvas.addMouseListener(this);

		setState(BRUSH);
		inkColor	= new JButton();
		canvasColor = new JButton();
		
		customUI = new CustomUI(BACKGOUND_COLOR, brush.getColor());
		this.updateInkColor(Color.BLACK);
		brush.setSize(8.0f);
		
		this.updateCanvasColor(Color.WHITE);
		eraser.setSize(16.0f);
		

		colorPlate = new JPanel(new GridLayout(7, 2, 2, 2));
		colorPlate.setBackground(BACKGOUND_COLOR);
		colorPlate.setBounds(8, 86, 64, 212);

		
		inkColor.setBackground(brush.getColor());
		inkColor.setBounds(8, 18, 64, 64);
		inkColor.addActionListener(this);
		Model.setComponentSize(inkColor, 64, 64);
		
		inkColor.setUI(customUI);
		
		
		JPanel colorPanel = new JPanel(null);
		colorPanel.setBorder(new TitledBorder(new EtchedBorder(), "Colors"));
		colorPanel.setBackground(BACKGOUND_COLOR);
		colorPanel.add(inkColor);
		colorPanel.add(colorPlate);		
		Model.setComponentSize(colorPanel, 80, 304);
		
		canvas.setLayout(new BorderLayout());
		canvas.setBounds(0, 0, 1024, 768);


		canvasPanel = new JPanel(null);
		canvasPanel.setBackground(BACKGOUND_COLOR);
		canvasPanel.add(canvas);
		Model.setComponentSize(canvasPanel, 1024, 768);

		scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		scrollPane.setBackground(BACKGOUND_COLOR);


		scrollPane.setViewportView(canvasPanel);

		scrollPane.setBounds(0, 0, 1, 1);
		Model.setComponentSize(scrollPane, 1, 1);

		//AS_NEEDED is a default
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, new JButton()); //some "interesting" button can be put there

		scrollPane.revalidate(); 
		scrollPane.repaint();    // repaint as soon as possible
		scrollPane.setBorder(new TitledBorder(new EtchedBorder(),
				"Drawing Canvas"));


		// Create a JMenuBar and menu items:
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(BACKGOUND_COLOR);

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		newFile = new JMenuItem("New", new ImageIcon("new.gif"));
		newFile.setMnemonic(KeyEvent.VK_N);
		newFile.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newFile.addActionListener(this);

		openFile = new JMenuItem("Open...", new ImageIcon("open.gif"));
		openFile.setMnemonic(KeyEvent.VK_O);
		openFile.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openFile.addActionListener(this);

		exit = new JMenuItem("Exit", new ImageIcon("blank.gif"));
		exit.setMnemonic(KeyEvent.VK_X);
		exit.addActionListener(this);


		menuBar.add(fileMenu);
		fileMenu.add(newFile);
		fileMenu.add(openFile);
		fileMenu.addSeparator();
		fileMenu.add(exit);

		// Create a toolbar and set it to be horizontal:
		toolBar = new JToolBar(JToolBar.VERTICAL);
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.PAGE_AXIS));
		toolBar.setFloatable(false);

		
		// The Colors:
		black	= colorButton(Color.BLACK);
		brown	= colorButton(new Color(139,69,19));

		dGray	= colorButton(Color.DARK_GRAY);
		orange	= colorButton(Color.ORANGE);

		gray	= colorButton(Color.GRAY);
		yellow	= colorButton(Color.YELLOW);

		lGray	= colorButton(Color.LIGHT_GRAY);
		green	= colorButton(Color.GREEN);

		white	= colorButton(Color.WHITE);
		cyan	= colorButton(Color.CYAN);

		magenta	= colorButton(Color.MAGENTA);
		blue	= colorButton(Color.BLUE);

		red		= colorButton(Color.RED);
		purple	= colorButton(new Color(138,43,226));//Color.PINK);



		
		toolboxPanel = new JPanel(new GridLayout(0, 1, 0, 2));
		toolboxPanel.setBackground(BACKGOUND_COLOR);
		toolboxPanel.setBorder(new TitledBorder(new EtchedBorder(), "Toolbox"));
		Model.setComponentSize(toolboxPanel, 80, 280);

		stateButton(PENCIL, "pencil.png");
		stateButton(BRUSH, "brush.png");
		stateButton(LINE, "line.png");
		stateButton(ERASER, "eraser.png");

		toolBar.setBackground(BACKGOUND_COLOR);
		toolBar.add(toolboxPanel);
		toolBar.addSeparator(new Dimension(16,16));
		toolBar.add(colorPanel);


		this.setJMenuBar(menuBar);
		mainPanel.setBackground(BACKGOUND_COLOR);
		mainPanel.add(toolBar, BorderLayout.WEST);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if (source == newFile)
		{
			dNewFile.setVisible(scrollPane, canvasPanel, canvas);
		}

		else if (source == openFile)
		{
			// show the file chooser 'open' dialog box and get user response
			fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter("Image files",
					ImageIO.getReaderFileSuffixes()));
			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				File f = fc.getSelectedFile();
				try
				{
					BufferedImage image = ImageIO.read(f);
					Model.setNewCanvasSize(canvas, canvasPanel, scrollPane, image.getWidth(), image.getHeight());
					canvas.setImage(image);
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				scrollPane.revalidate();
				scrollPane.repaint();    // repaint as soon as possible
			}
		}

		else if (source == exit)
			System.exit(0);
		else if (source == inkColor)
		{
			cc.setColor(brush.getColor());
			ActionListener lOK = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e)
				{
					updateInkColor(cc.getColor());
				}
			};

			JColorChooser.createDialog(this, "Select the Ink Color", true, cc, lOK, null).setVisible(true);
		}
		else if (source == canvasColor)
		{
			Color c;
			c = JColorChooser.showDialog(inkColor, "Select the Background Color", brush.getColor());
			updateInkColor(c);
		}
	}

	//	-------------------------------------------------

	@Override
	public void mouseClicked(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent event)
	{
		int x = event.getX();
		int y = event.getY();

		// indicated which mouse button was pressed
		if (SwingUtilities.isLeftMouseButton(event))
		{
			// Do something
		} else if (SwingUtilities.isRightMouseButton(event))
		{
			// Do something
		}

		points[pointsCount] = new Point(x, y);
		if (pointsCount < MAX_POINTS - 1) pointsCount++;
	}		

	@Override
	public void mouseReleased(MouseEvent event)
	{
		pointsCount = 0;
		if (state == LINE) canvas.removeLine();
	}

	public void mouseDragged(MouseEvent event)
	{

		int x = event.getX();
		int y = event.getY();

		if (SwingUtilities.isLeftMouseButton(event))
		{
			if (state == BRUSH) 
			{
				points[pointsCount] = new Point(x, y);
				if (pointsCount < MAX_POINTS - 1) {
					canvas.drawPoint(points, pointsCount, brush);
					pointsCount++;
				}
			}
			else if (state == LINE)
			{
				points[pointsCount] = new Point(x, y);
				if (pointsCount < MAX_POINTS - 1) {
					canvas.drawLine(points, pointsCount, brush);
					pointsCount++;
				}
			}
			else if (state == PENCIL) 
			{
				points[pointsCount] = new Point(x, y);
				if (pointsCount < MAX_POINTS - 1) {
					canvas.drawPoint(points, pointsCount, pencil);
					pointsCount++;
				}
			}
			else if (state == ERASER) 
			{
				points[pointsCount] = new Point(x, y);
				if (pointsCount < MAX_POINTS - 1) {
					canvas.drawPoint(points, pointsCount, eraser);
					pointsCount++;
				}
			}
		}
	}




	@Override
	public void mouseMoved(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

}
