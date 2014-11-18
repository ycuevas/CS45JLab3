// BouncingSmileyApplet.java 


import java.awt.*;
import java.applet.*;
import java.awt.geom.*;
import java.util.Random;

// An applet that bounces a group of smileys around a wall-lined screen
public class BouncingSmileyApplet extends Applet
{

	// Constants go here. You'll likely want them
	// for the four edges of the display, the
	// thickness of the walls, the time the
	// animation should run (in milliseconds)
	// and perhaps others

	private Color BACKGROUND_COLOR;
	long TIME_TO_RUN = 500000;
	int REVERSE_DIRECTION = -1;
	private static final int DELAY = 5;
	private int xMove;
	private int yMove;


	// necessary for classes that implement Serializable
	private static final long serialVersionUID = 1L;

	// Constants go here. You'll likely want them
	// for the four edges of the frame, the 
	// thickness of the walls, the background 
	// color of the screen, and perhaps others
	private static Dimension leftWallDimensions, rightWallDimensions, 
							 topWallDimensions, bottomWallDimensions;
	
	//upper left x and y coordinates
	private static int upperLeftX, upperLeftY; 
	
	private static Point leftWallCoordinates, rightWallCoordinates,
						 topWallCoordinates, bottomWallCoordinates;
			
	private int wallThickness;
	
	private Wall leftWall, rightWall, topWall, bottomWall;
	
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

	Random random = new Random();
	private BouncingGroup bouncingGroup;
	private BouncingDisplay bouncingDisplay;
	
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


	// The parts of the applet that need construction:
	//   build the four walls
	//   make the smiley group to be animated
	//   make a random number generator (used in animation)
	public BouncingSmileyApplet()
	{
		// complete		
		bouncingGroup = new BouncingGroup();
		bouncingDisplay = new BouncingDisplay(bouncingGroup);
		
		BACKGROUND_COLOR = Color.black;
		random = new Random();
	}


	// init: when applet is invoked:
	//  resize the screen to match the boundaries
	//    given above
	//	set the background color to the
	//    BACKGROUND_COLOR
	//	break the smileys from the
	//	  group and store separately, since they
	//	  are animated separately
	//  begin the animation
	public void init()
	{
		// complete
		setBackground(BACKGROUND_COLOR);
		wallThickness = 10;
		
		// these dimensions probably need to come from the BouncingFrame...not sure yet
		width = 500;
		height = 500; 
		
		//width = this.getPreferredSize().width;
		//height = this.getPreferredSize().height;
		
		//wall edge information (width, height) 
		leftWallDimensions = new Dimension(wallThickness, height);
		rightWallDimensions = new Dimension(wallThickness, height);
		topWallDimensions = new Dimension(width, wallThickness);
		bottomWallDimensions = new Dimension(width, wallThickness);
		
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
		
		animSmiley1 = bouncingGroup.getSmiley1();
		animSmiley2 = bouncingGroup.getSmiley2();
		animSmiley3 = bouncingGroup.getSmiley3();
		
		xMove = animSmiley1.getCurrentXMovement();
		yMove = animSmiley1.getCurrentYMovement();
		
		//SmileyAnimation smileyAnimation = new SmileyAnimation(bouncingGroup, bouncingDisplay);
		//smileyAnimation.animate();
		animate();
	}


	// Animate the smiley in it own thread,
	// so that it is separate from the rest
	// of the programs; operations: in particular,
	// when we repaint() to draw the next frame of
	// the animation, when this thread suspends,
	// the other implied program thread will
	// repaint the screen. (If a separate thread
	// is not used, repaint() is only acted upon
	// once the animation is complete!)
	// All details to actually animate are in the
	// paint() method; this just loops through
	// repainting and pausing as paint() generates
	// and displays each frame of the animation

	public void animate()
	{
		class AnimationRunnable implements Runnable
		{	
			public void run()
			{
				// Set the current time
			
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis() + TIME_TO_RUN;
				// For each frame, for as long as we are animating...
					// repaint the current frame and pause
				while (startTime < endTime)
				{
					repaint();
					// move each smiley in the bouncing group
					moveCntSmiley(animSmiley1);
					moveCntSmiley(animSmiley2);
					moveCntSmiley(animSmiley3);
					startTime = System.currentTimeMillis();
				}
			}
		}
		Thread t = new Thread(new AnimationRunnable());
		t.start();
	}


	// pause(): pause the animation for the given
	// number of milliseconds
	// DO NOT MODIFY
	private void pause(int millisecs)
	{
		try
		{
			Thread.sleep(millisecs);
		}
		catch (InterruptedException e)
		{
		}
	}
	
	// paint: draw a frame of the animation
	public void paint(Graphics g)
	{
		super.paint(g);
		graphicManager = (Graphics2D)g;
		
		//g2.draw(leftWall.wallRect);
		drawWall(leftWall);
		drawWall(rightWall);
		drawWall(bottomWall);
		drawWall(topWall);		
		
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

	// The methods described below are private, and so
	// only suggested; however, the functionality they
	// provide will almost certainly be needed, regardless of
	// whether you choose to implement them.

	// moveCntSmiley: Continue to move smiley until
	// it hits a wall; when it does, swap color of
	// smiley and wall, and change direction
	private void moveCntSmiley(AnimatedSmiley cntSmiley)
	{
		WallName wall = null;
		
		// if a wall was not hit, just change the coordinates
		// of the smiley
		if( (wall = whichWallWasHit(cntSmiley)) == WallName.NONE)
		{
			cntSmiley.moveIt();
		}
		else // otherwise, handle the wall collision
		{
			// swap colors of animated smiley with the color of the wall
			switchColor(cntSmiley, wall);
			// change direction to the opposite direction
			adjustDirection(cntSmiley, wall);
			// adjust animated smiley's coordinates
			cntSmiley.moveIt();
		}
		
		// delay to control animation speed
		pause(DELAY);

	}
	




	// Swap the colors of the wall just touched and the smiley
	private void switchColor(AnimatedSmiley cntSmiley, WallName wallTouched)
	{
		Color faceColor = cntSmiley.getFace().getColor();
		cntSmiley.getFace().setColor(bouncingDisplay.getWallColor(wallTouched));
		bouncingDisplay.setWallColor(wallTouched, faceColor);
	}

	




	// Change the smiley's direction so it is away from
	// the wall just touched.
	private void adjustDirection(AnimatedSmiley cntSmiley, WallName wallTouched)
	{
		// If hit top or bottom wall, y direction is reversed,
		// x direction can be to the left, to the right, or
		// no movement at all; it is randomly chosen

		// If hit left or right wall, x direction is reversed,
		// y direction can be up, down, or no movement; it is
		// randomly chosen
		
		// gives me a random number in the range of -1 and 1 inclusive
		int direction = random.nextInt(3)-1;

		// if top or bottom wall was hit, switch directions accordingly
		if(wallTouched == WallName.TOP || wallTouched == WallName.BOTTOM)
		{ 
			cntSmiley.setCurrentXMovement(xMove*direction);
			cntSmiley.setCurrentYMovement(cntSmiley.getCurrentYMovement()*REVERSE_DIRECTION);
		}
		// if right or left wall was hit, switch directions accordingly
		else if(wallTouched == WallName.RIGHT || wallTouched == WallName.LEFT)
		{ 
			cntSmiley.setCurrentXMovement(cntSmiley.getCurrentXMovement()*REVERSE_DIRECTION);
			cntSmiley.setCurrentYMovement(yMove*direction);
		}
	}


	// whichWallWasHit: return a label (LEFT, RIGHT, TOP, BOTTOM) to tell us which wall
	// was hit or NONE if none was hit
	private WallName whichWallWasHit(AnimatedSmiley cntSmiley)
	{
		if(hitLeftWall(cntSmiley))
		{
			return WallName.LEFT;
		}
		else if (hitRightWall(cntSmiley))
		{
			return WallName.RIGHT;
		}
		else if(hitTopWall(cntSmiley))
		{
			return WallName.TOP;
		}
		else if(hitBottomWall(cntSmiley))
		{
			return WallName.BOTTOM;
		}
		
		return WallName.NONE;
	}
	

	// Return true if hit left wall, false otherwise
	private boolean hitLeftWall(AnimatedSmiley cntSmiley)
	{
		// Wall was hit if x coordinate of leftmost point of smiley is
		// same or less than edge of the left wall and is (still)
		// heading into the wall
		
		// get the left most point of the smiley
		double leftPos = cntSmiley.getFace().getCenterX() - cntSmiley.getFace().getXLength()/2;

		// return true or false depending on if the wall was hit or not
		return ((leftPos - bouncingDisplay.getWallEdge(WallName.LEFT)) <= 0);
	}


	// Return true if hit right wall, false otherwise
	private boolean hitRightWall(AnimatedSmiley cntSmiley)
	{
		// Wall was hit if x coordinate of rightmost point of smiley is
		// same or greater than edge of the right wall and is (still)
		// heading into the wall

		// get the right most point of the smiley
		double rightPos = cntSmiley.getFace().getCenterX() + cntSmiley.getFace().getXLength()/2;	

		// return true or false depending on if the wall was hit or not
		return ((rightPos - bouncingDisplay.getWallEdge(WallName.RIGHT)) >= 0);

	}


	// Return true if hit top wall, false otherwise
	private boolean hitTopWall(AnimatedSmiley cntSmiley)
	{
		// Wall was hit if y coordinate of top-most point of smiley is
		// same or less than edge of the top wall and is (still)
		// heading into the wall
		// get the top most point of the smiley
		double topPos = cntSmiley.getFace().getCenterY() - cntSmiley.getFace().getYLength()/2 ;

		// return true or false depending on if the wall was hit or not
		return ((topPos - bouncingDisplay.getWallEdge(WallName.TOP)) <= 0);

	}


	// Return true if hit bottom wall, false otherwise
	private boolean hitBottomWall(AnimatedSmiley cntSmiley)
	{
		// get the bottom most point of the smiley
		double bottomPos = cntSmiley.getFace().getCenterY() + cntSmiley.getFace().getYLength()/2;

		// return true or false depending on if the wall was hit or not
		return ((bottomPos - bouncingDisplay.getWallEdge(WallName.BOTTOM)) >= 0); 

	}


	// Return which wall's edge was hit
	public int getWallEdge(WallName wallName)
	{
		// complete
		return getWall(wallName).wallEdge;
	}
	
	// Return the color of the wallName wall
	public Color getWallColor(WallName wallName)
	{
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
	

	// drawSmiley: draw a smiley by drawing each of its parts
	private void drawSmiley(SmileyFace cntSmiley)
	{
		drawPart(cntSmiley.getFace());
		drawPart(cntSmiley.getLeftEye());
		drawPart(cntSmiley.getRightEye());
		drawPart(cntSmiley.getSmile());
	}
	
	// drawPart: make an ellipse corresponding to the shape 
	// of the given smiley face part; the ellipses are what 


	// drawPart: make an ellipse corresponding to the shape
	// of the given smiley face part; the ellipses are what
	// are actually drawn
	private void drawPart(SmileyFacePart part)
	{
		computeUpperLeft(part);

		graphicManager.setColor(part.getColor());
		graphicManager.fillOval(upperLeftX, upperLeftY, (int)part.getXLength(), (int)part.getYLength());
	}

	// computeUpperLeft: determine the x- and y-coordinate of the
	// upper-left of a SmileyFacePart.  This should be called whenever
	// an attributes change would cause the upper-left position to
	// change.
	private void computeUpperLeft(SmileyFacePart part)	{
		upperLeftX = part.getCenterX() - (int)part.getXLength()/2;
		upperLeftY = part.getCenterY() - (int)part.getYLength()/2;
	}
}


