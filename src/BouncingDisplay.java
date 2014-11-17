// BouncingDisplay.java - build the display panel
// ICS 45J, Fall 2014: Lab Assignment 3
//
// Completed by: Alfonso Aranzazu
// UCInetiD:     aaranzaz
// ID:           42584672


import java.awt.*;

//import javafx.scene.shape.Ellipse;

// Panel that displays the moving smiley
	class BouncingDisplay extends BasicDisplay
	{
		// necessary for classes that implement Serializable
		private static final long serialVersionUID = 1L;

		// Constants go here. You'll likely want them
		// for the four edges of the frame, the 
		// thickness of the walls, the background 
		// color of the screen, and perhaps others
		private static Dimension leftWallDimensions, rightWallDimensions, 
								 topWallDimensions, bottomWallDimensions;
		
		//upper left x and y coordinates
		private static Point upperLeftFaceCoordinates, 
									leftWallCoordinates, rightWallCoordinates,
										topWallCoordinates, bottomWallCoordinates;
		private static Point upperLeftX, upperLeftY; 
		private static int upperLeftX, upperLeftY; 
		
		private static Point leftWallCoordinates, rightWallCoordinates,
							 topWallCoordinates, bottomWallCoordinates;
				
		private int wallThickness;
		private Color BACKGROUND_COLOR;
		
		private static Wall leftWall, rightWall, topWall, bottomWall;
		
		// Needed fields go here. You'll likely want
		// fields for the moving smileys, the 
		// smileys as they were in the previous frame
		// (so they can be erased), the graphics 2D
		// environment, the x and y coordinates
		// of the upper left corner of the current face 
		// part, and for each of the four walls -- and 
		// perhaps others as well
		
		private static Graphics2D graphicManager;
		private AnimatedSmiley animSmiley1;
		private AnimatedSmiley animSmiley2;
		private AnimatedSmiley animSmiley3;
		
		// previous smileys
		/*private AnimatedSmiley prevSmiley1;
		private AnimatedSmiley prevSmiley2;
		private AnimatedSmiley prevSmiley3;*/
		private BouncingGroup bouncingGroup;
		
		private int width;
		private int height;
		
		// A wall of the display (off which the smiley bounces)
		// The outer class will make four objects of this class,
		// one for each wall. Not required (since private); still,
		// strongly recommended!
		private static class Wall
		{
			// A wall consists of a rectangle, color, name, and edge --
			// the position of the edge of the wall that the smiley touches.
			private Rectangle wallRect;
			private Color wallColor;
			private String name;
			private int wallEdge;

			// Build a wall, given its name and color -- we can figure 
			// out the wall's shape from the provided name and the geometry 
			// of the screen--and provide that info to Rectangle() 
			// to build the wall
			public Wall(WallName position, Color c)
			{
				// walls have a left position, top position, 
				// length in x dimension, length in y dimension,
				// color the same as c; which wall we're making
				// is indicated by position (WallName.LEFT, 
				// WallName.RIGHT, WallName.TOP, WallName.BOTTOM)
				wallColor = c;
				
				// figure out upper left, upper right, xLength and 
				// yLength for each rectangle representing a wall, 
				// and the edge the smiley will hit when it touches 
				// a wall, 

				// using information about the display screen 
				// and frame, and the wall's thickness
				
				//create left wall
				if (position == WallName.LEFT)
				{
					wallRect = new Rectangle(leftWallCoordinates, leftWallDimensions);
					wallEdge = (int) (leftWallCoordinates.getX() + leftWallDimensions.getWidth());

				}
				
				//create right wall
				else if (position == WallName.RIGHT)
				{
					wallRect = new Rectangle(rightWallCoordinates, rightWallDimensions);
					wallEdge = (int) (rightWallCoordinates.getX() + rightWallDimensions.getWidth());

				}
				
				//create top wall
				else if (position == WallName.TOP)
				{
					wallRect = new Rectangle(topWallCoordinates, topWallDimensions);
					wallEdge = (int) (topWallCoordinates.getY() + topWallDimensions.getHeight());

				}
				
				//create bottom wall
				else if (position == WallName.BOTTOM)
				{
					wallRect = new Rectangle(bottomWallCoordinates, bottomWallDimensions);
					wallEdge = (int) (bottomWallCoordinates.getY() + bottomWallDimensions.getHeight());

				}
			}
		}

	
	// Inherited method:
	// 	public void repaint() - forces Java to redraw this display
		
	// The bouncing display is a display with a black background,
	// four walls (left, right, top, bottom) and three moving
	// smileys
	public BouncingDisplay(BouncingGroup bouncingGroup)
	{
		//screen Info
		this.BACKGROUND_COLOR = Color.WHITE;
		wallThickness = 10;
		
		// these dimensions probably need to come from the BouncingFrame...not sure yet
		width = 495;
		height = 472; 
		
		//width = this.getPreferredSize().width;
		//height = this.getPreferredSize().height;
		
		//wall edge information (width, height) 
		leftWallDimensions = new Dimension(wallThickness, height);
		rightWallDimensions = new Dimension(wallThickness, height);
		topWallDimensions = new Dimension(width, wallThickness);
		bottomWallDimensions = new Dimension(width, wallThickness);
		
		// making the smileys
		bouncingGroup = new BouncingGroup();
		animSmiley1 = bouncingGroup.getSmiley1();
//		animSmiley2 = bouncingGroup.getSmiley2();
//		animSmiley3 = bouncingGroup.getSmiley3();
		
		// make the previous smileys, calling AnimatedSmiley copy constructor
		prevSmiley1 = new AnimatedSmiley(animSmiley1);
//		prevSmiley2 = new AnimatedSmiley(animSmiley2);
//		prevSmiley3 = new AnimatedSmiley(animSmiley3);
		this.bouncingGroup = bouncingGroup;
		this.animSmiley1 = bouncingGroup.getSmiley1();
		this.animSmiley2 = bouncingGroup.getSmiley2();
		this.animSmiley3 = bouncingGroup.getSmiley3();
		
		// make the previous smileys, calling AnimatedSmiley copy constructor
		/*this.prevSmiley1 = new AnimatedSmiley(animSmiley1);
		this.prevSmiley2 = new AnimatedSmiley(animSmiley2);
		this.prevSmiley3 = new AnimatedSmiley(animSmiley3);*/
		
		//Wall coordinates
		//upperLeftFaceCoordinates = new Point(200, 200);
		leftWallCoordinates = new Point(0,0);
		rightWallCoordinates = new Point(width - wallThickness, 0);
		topWallCoordinates = new Point(0, 0);
		bottomWallCoordinates = new Point(0, height - wallThickness);
		
		//build walls
		leftWall = new Wall(WallName.LEFT, Color.blue);
		rightWall = new Wall(WallName.RIGHT, Color.yellow);
		topWall = new Wall(WallName.TOP, Color.red);
		bottomWall = new Wall(WallName.BOTTOM, Color.orange);
		
		repaint();
	}
	
	// paintComponent: called by the runtime environment 
	// whenever it thinks the displayed screen has changed, 
	// or as soon as possible after the program makes a
	// call to repaint()
	public void paintComponent(Graphics g)
	{
		// Done as a matter of course as the first two
		// lines of a paintComponent routine
		super.paintComponent(g);
		graphicManager = (Graphics2D)g;
		
		// Erase the currently-displayed smileys
		// Draw each smiley onto its place on the screen
		// The moving smileys are now the previous smileys...
		erase(prevSmiley1);
		drawSmiley(animSmiley1);
		prevSmiley1 = new AnimatedSmiley(animSmiley1);
		graphicManager.setColor(leftWall.wallColor);
		
		//g2.draw(leftWall.wallRect);
		drawWall(leftWall);
		drawWall(rightWall);
		drawWall(bottomWall);
		drawWall(topWall);
	
		animSmiley1 = bouncingGroup.getSmiley1();
		animSmiley2 = bouncingGroup.getSmiley2();
		animSmiley3 = bouncingGroup.getSmiley3();		
		
		drawSmiley(animSmiley1);
		drawSmiley(animSmiley2);
		drawSmiley(animSmiley3);
		
	}
	
	private void drawWall(Wall wall)
	{
		graphicManager.setColor(wall.wallColor);
		graphicManager.fillRect(wall.wallRect.x, wall.wallRect.y, wall.wallRect.width, wall.wallRect.height);
		graphicManager.draw(wall.wallRect);
	}
		
	// Return which wall's edge was hit
	public int getWallEdge(WallName wallName)
	{
		// complete
		if (wallName == WallName.LEFT) {return leftWall.wallEdge;}
		else if (wallName == WallName.RIGHT) {return rightWall.wallEdge; }
		else if (wallName == WallName.TOP) {return topWall.wallEdge; }
		else if (wallName == WallName.BOTTOM) {return bottomWall.wallEdge; }
		else {
			return 0;
		}
		return getWall(wallName).wallEdge;
	}
	
	// Return the color of the wallName wall
	public Color getWallColor(WallName wallName)
	{
		if (wallName == WallName.LEFT) { return leftWall.wallColor; }
		else if (wallName == WallName.RIGHT) { return rightWall.wallColor; }
		else if (wallName == WallName.TOP) { return topWall.wallColor; }
		else if (wallName == WallName.BOTTOM) { return bottomWall.wallColor; }
		else {
			return null;
		}
		return getWall(wallName).wallColor;
	}
	
	private Wall getWall(WallName wallName)
	{
		Wall wall = null;
		
		if(wallName == WallName.BOTTOM)
		{
			wall = bottomWall;
		}
		else if(wallName == WallName.LEFT)
		{
			wall = leftWall;
		}
		else if(wallName == WallName.RIGHT)
		{
			wall = rightWall;
		}
		else if(wallName == WallName.TOP)
		{
			wall = topWall;
		}
		
		return wall;
	}
	
	// Set the specified wall to the provided color
	public void setWallColor(WallName wallName, Color c)
	{	
		if (wallName == WallName.LEFT){
			leftWall.wallColor = c;
		}
		else if (wallName == WallName.RIGHT){
			rightWall.wallColor = c;
		}
		else if (wallName == WallName.TOP){
			topWall.wallColor = c;
		}
		else if (wallName == WallName.BOTTOM){
			bottomWall.wallColor = c;
		}
		// if wallName param isn't one of these types, nothing gets changed
		if(wallName == WallName.BOTTOM)
		{
			bottomWall = new Wall(wallName, c);
		}
		else if(wallName == WallName.LEFT)
		{
			leftWall = new Wall(wallName, c);
		}
		else if(wallName == WallName.RIGHT)
		{
			rightWall = new Wall(wallName, c);
		}
		else if(wallName == WallName.TOP)
		{
			topWall = new Wall(wallName, c);
		}	
	}
	
	// The methods described below are private, and so
	// only suggested; however, the functionality they 
	// provide will almost certainly be needed, regardless of 
	// whether you choose to implement them.


	// computeUpperLeft() determines the x- and y-coordinate of the
	// upper-left of a SmileyFacePart.  This should be called whenever
	// an attributes change would cause the upper-left position to
	// change. It's recommended because the graphic drawing routines
	// need to be given the upper left corner of a figure (in addition
	// to x and y lengths) to draw it.
	private void computeUpperLeft(SmileyFacePart part)
	{
		double newX = part.getCenterX() - (part.getXLength()/2);
		double newY = part.getCenterY() - (part.getYLength()/2);
		upperLeftFaceCoordinates.setLocation(newX, newY);
		// complete
		
		upperLeftX = part.getCenterX() - (int)part.getXLength()/2;
		upperLeftY = part.getCenterY() - (int)part.getYLength()/2;
	}
	
	// drawSmiley: draw a smiley by drawing each of its parts
	private void drawSmiley(SmileyFace cntSmiley)
	{
		drawPart(cntSmiley.getFace());
		drawPart(cntSmiley.getLeftEye());
		drawPart(cntSmiley.getRightEye());
		drawPart(cntSmiley.getSmile());
	}
	
	
	// drawPart: make an ellipse corresponding to the shape 
	// of the given smiley face part, and draw it, filled
	// in with the part's color
	private void drawPart(SmileyFacePart part)
	{
		computeUpperLeft(part);

		graphicManager.setColor(part.getColor());
		graphicManager.fillOval(upperLeftX, upperLeftY, (int)part.getXLength(), (int)part.getYLength());
	}
	
}
	
	
