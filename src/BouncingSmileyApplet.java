// BouncingSmileyApplet.java 


import java.awt.*;

import java.applet.*;
import java.awt.geom.*;
import java.util.Random;
//import BouncingDisplay.Wall;


// An applet that bounces a group of smileys around a wall-lined screen
public class BouncingSmileyApplet extends Applet
{
	
	// Constants go here. You'll likely want them
	// for the four edges of the display, the 
	// thickness of the walls, the time the
	// animation should run (in milliseconds)
	// and perhaps others
	Color BACKGROUND_COLOR;
	long TIME_TO_RUN = 5000;
	int REVERSE_DIRECTION = -1;
	
	// Suggested fields:
	//	the smiley group to be displayed
	//  the three animated smileys
	//	random number generator (used in the animation)
	//  the graphics environment
	//	the four walls of the display
	//  the x and y coordinates of the upper left corner 
	//  of the current face part	
	AnimatedSmiley animSmiley1;
	AnimatedSmiley animSmiley2;
	AnimatedSmiley animSmiley3;
	
	
	BouncingGroup bouncingGroup;
	BouncingDisplay bouncingDisplay;
	Random random;

	//upper left x and y coordinates
	private static Point upperLeftFaceCoordinates, leftWallCoordinates, rightWallCoordinates,
							topWallCoordinates, bottomWallCoordinates;
	
	// wall dimensions
	private static Dimension leftWallDimensions, rightWallDimensions, 
									topWallDimensions, bottomWallDimensions;
	
	// wall names
	private Wall leftWall, rightWall, topWall, bottomWall;

	
	// A wall of the display (off which the smiley bounces)
	// The outer class will make four objects of this class,
	// one for each wall. Not required (since private); still,
	// strongly recommended!
	private static class Wall
	{
		// A wall consists of a rectangle, color, name, and edge --
		// the position of the edge of the wall that the smiley touches.

		// Build a wall, given its name and color -- we can figure 
		// out the wall's shape from the provided name and the geometry 
		// of the screen--and provide that info to Rectangle() 
		// to build the wall
		
		private Rectangle wallRect;
		private Color wallColor;
		private String name;
		private int wallEdge;
		
		public Wall(WallName position, Color c)                            // WHY AM I REUSING CODE ???
		{
			// walls have a left position, top position, 
			// length in x dimension, length in y dimension,
			// color the same as c; which wall we're making
			// is indicated by position (WallName.LEFT, 
			// WallName.RIGHT, WallName.TOP, WallName.BOTTOM)
			
			// figure out upper left, upper right, xLength and 
			// yLength for each rectangle representing a wall, 
			// and the edge the smiley will hit when it touches 
			// a wall, using information about the display screen 
			// and frame, and the wall's thickness
			
			// Use that info to make a new rectangle that represents the wall
			wallColor = c;
			
			//create left wall
			if (position == WallName.LEFT)
			{
				wallRect = new Rectangle(leftWallCoordinates, leftWallDimensions);
				wallEdge = (int) (leftWallCoordinates.getX() + leftWallDimensions.getWidth());
//				graphicManager.drawString(name, 0,250);
//				graphicManager.drawRect((int) leftWallCoordinates.getX(), (int) leftWallCoordinates.getY(),
//										(int) leftWallDimensions.getWidth(), (int) leftWallDimensions.getHeight());
			}
			
			//create right wall
			else if (position == WallName.RIGHT)
			{
				wallRect = new Rectangle(rightWallCoordinates, rightWallDimensions);
				wallEdge = (int) (rightWallCoordinates.getX() + rightWallDimensions.getWidth());
//				graphicManager.drawString(name, 500, 250);
//				graphicManager.drawRect((int) rightWallCoordinates.getX(), (int) rightWallCoordinates.getY(),
//										(int) rightWallDimensions.getWidth(), (int) rightWallDimensions.getHeight());
			}
			
			//create top wall
			else if (position == WallName.TOP)
			{
				wallRect = new Rectangle(topWallCoordinates, topWallDimensions);
				wallEdge = (int) (topWallCoordinates.getY() + topWallDimensions.getHeight());
//				graphicManager.drawString(name, 0, 250);
//				graphicManager.drawRect((int) topWallCoordinates.getX(), (int) topWallCoordinates.getY(),
//						(int) topWallDimensions.getWidth(), (int) topWallDimensions.getHeight());
			}
			
			//create bottom wall
			else if (position == WallName.BOTTOM)
			{
				wallRect = new Rectangle(bottomWallCoordinates, bottomWallDimensions);
				wallEdge = (int) (bottomWallCoordinates.getY() + bottomWallDimensions.getHeight());
//				graphicManager.drawString(name, 500, 250);
//				graphicManager.drawRect((int) bottomWallCoordinates.getX(), (int) bottomWallCoordinates.getY(),
//						(int) bottomWallDimensions.getWidth(), (int) bottomWallDimensions.getHeight());
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
		// initialize bouncing display and bouncing group		
		bouncingGroup = new BouncingGroup();
		bouncingDisplay = new BouncingDisplay(bouncingGroup);
		
		BACKGROUND_COLOR = Color.black;
		
		//wall edge information (width, height) 
		leftWallDimensions = new Dimension(50,500);
		rightWallDimensions = new Dimension(-50, 500);
		topWallDimensions = new Dimension(500, 50);
		bottomWallDimensions = new Dimension(500, -50);
		
		upperLeftFaceCoordinates = new Point(200, 200);
		leftWallCoordinates = new Point(0,0);
		rightWallCoordinates = new Point(500, 0);
		topWallCoordinates = new Point(0, 0);
		bottomWallCoordinates = new Point(0, 500);

		//build walls
		leftWall = new Wall(WallName.LEFT, Color.blue);
		rightWall = new Wall(WallName.RIGHT, Color.yellow);
		topWall = new Wall(WallName.TOP, Color.red);
		bottomWall = new Wall(WallName.BOTTOM, Color.orange);
		
		random = new Random();
		
		repaint();
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

		resize(700, 500);
		setBackground(BACKGROUND_COLOR);
		animSmiley1 = bouncingGroup.getSmiley1();
		animSmiley2 = bouncingGroup.getSmiley2();
		animSmiley3 = bouncingGroup.getSmiley3();		
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
					//paint( takes Grahics g )
					moveCntSmiley(animSmiley1);
					bouncingDisplay.repaint();
					pause(200);
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
		// Call parent
		super.paint(g);
		// Make a graphics2D reference
		Graphics2D graphicManager;	
		
		// Draw the walls
		graphicManager.draw(leftWall.wallRect);
		// Erase the currently-displayed smileys
		
		// Move each smiley one "frame" of animation
		
		// Draw each smiley onto its place on the screen
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
		cntSmiley.moveIt(); //moves the smiley
		WallName wall = whichWallWasHit(cntSmiley); // checks to see if wall was hit
		switchColor(cntSmiley, wall); //switches color of face with wall that's hit
		adjustDirection(cntSmiley, wall); // adjusts direction randomly
	}
	
	// Swap the colors of the wall just touched and the smiley
	private void switchColor(AnimatedSmiley cntSmiley, WallName wallTouched)
	{
		///check case where wall is null value (no color)
		//  if its not, go ahead and swap face color with wall color
		if (getWallColor(wallTouched) != null)
		{
			Color temp = cntSmiley.getFace().getColor(); //save the color of the smiley face to a temp variable
			cntSmiley.getFace().setColor(getWallColor(wallTouched)); //change the smiley face color to the wall color it hit
			setWallColor(wallTouched, temp); //set the wall color equal to the temp variable
		}
	}
	
	// Change the smiley's direction so it is away from
	// the wall just touched.
	private void adjustDirection(AnimatedSmiley cntSmiley, WallName wallTouched)
	{	
		// If hit top or bottom wall, y direction is reversed,
		// x direction can be to the left, to the right, or
		// no movement at all; it is randomly chosen
		
		if (whichWallWasHit(cntSmiley) == WallName.TOP || (whichWallWasHit(cntSmiley) == WallName.BOTTOM) )
		{
			//right off the bat, reverse the Y movement
			cntSmiley.setCurrentYMovement(cntSmiley.getCurrentYMovement() * REVERSE_DIRECTION);
			int r = random.nextInt(3);
			if (r == 0) //we want to make x negative
			{
				if (cntSmiley.getCurrentXMovement() == 0) //if x is 0, manually change it to -40
				{
					cntSmiley.setCurrentXMovement(-40);
				}
				else if (cntSmiley.getCurrentXMovement() > 0) //if x is greater than 0, make it negative
				{
					cntSmiley.setCurrentXMovement(-(cntSmiley.getCurrentXMovement()));
				}
				return;
			}
			else if (r == 1) //we Want to make X 0
			{
				cntSmiley.setCurrentXMovement(0);
			}
			else if (r == 2) // we want to make X Positive
			{
				if (cntSmiley.getCurrentXMovement() == 0) // if x is 0, make it positive 40
				{
					cntSmiley.setCurrentXMovement(40);
				}
				else if (cntSmiley.getCurrentXMovement() < 0) // if x is less than 0, make it positive
				{
					cntSmiley.setCurrentXMovement(-(cntSmiley.getCurrentXMovement()));
				}
				return;
			}
			
		}

		// If hit left or right wall, x direction is reversed,
		// y direction can be up, down, or no movement; it is 
		// randomly chosen
		
		else if (whichWallWasHit(cntSmiley) == WallName.LEFT || (whichWallWasHit(cntSmiley) == WallName.RIGHT) )
		{
			//right off the bat, reverse the X movement
			cntSmiley.setCurrentXMovement(cntSmiley.getCurrentXMovement() * REVERSE_DIRECTION);
			int r = random.nextInt(3);
			if (r == 0) //We want to make y negative
			{
				
				if (cntSmiley.getCurrentYMovement() == 0) //if y is make it negative
				{
					cntSmiley.setCurrentYMovement(-40);
				}
				else if (cntSmiley.getCurrentYMovement() > 0) //if y is Pos. make it negative
				{
					cntSmiley.setCurrentYMovement(-(cntSmiley.getCurrentYMovement()));
				}
				return;
			}
			else if (r == 1) //we want to make y 0
			{
				cntSmiley.setCurrentYMovement(0);
			}
			else if (r == 2) //we want to make y Positive
			{
				if (cntSmiley.getCurrentYMovement() < 0) //if y is less than 0 (negative) make it positive
				{
					cntSmiley.setCurrentYMovement(-(cntSmiley.getCurrentYMovement()));
				}
				else if (cntSmiley.getCurrentYMovement() == 0) //if y is 0, manually make it positive 40
				{
					cntSmiley.setCurrentYMovement(40);
				}
				return;
			}
		
		}
	}


	// whichWallWasHit: return a label (LEFT, RIGHT, TOP, BOTTOM) to tell us which wall 
	// was hit or NONE if none was hit
	private WallName whichWallWasHit(AnimatedSmiley cntSmiley)
	{
		if (hitLeftWall(cntSmiley)) { return WallName.LEFT; }
		else if (hitRightWall(cntSmiley)) { return WallName.RIGHT; }
		else if (hitTopWall(cntSmiley)) { return WallName.TOP; }
		else if (hitBottomWall(cntSmiley)) { return WallName.BOTTOM; }
		return WallName.NONE;
	}
	
	// Return true if hit left wall, false otherwise
	private boolean hitLeftWall(AnimatedSmiley cntSmiley)
	{
		// Wall was hit if x coordinate of leftmost point of smiley is
		// same or less than edge of the left wall and is (still)
		// heading into the wall
		
		if (cntSmiley.getLeftEdge() <= getWallEdge(WallName.LEFT))
		{
			//checks to see if its still going
			if (cntSmiley.getCurrentXMovement() < 0)
			{
				return true;
			}
		}
		return false;
	}

	
	// Return true if hit right wall, false otherwise
	private boolean hitRightWall(AnimatedSmiley cntSmiley)
	{		
		// Wall was hit if x coordinate of rightmost point of smiley is
		// same or greater than edge of the right wall and is (still)
		// heading into the wall
		
		if (cntSmiley.getRightEdge() >= getWallEdge(WallName.RIGHT))
		{
			//checks to see if its still going
			if (cntSmiley.getCurrentXMovement() > 0)
			{
				return true;
			}
		}
		return false;
	}

	
	// Return true if hit top wall, false otherwise
	private boolean hitTopWall(AnimatedSmiley cntSmiley)
	{
		// Wall was hit if y coordinate of top-most point of smiley is
		// same or less than edge of the top wall and is (still)
		// heading into the wall
		
		if (cntSmiley.getTopEdge() <= getWallEdge(WallName.TOP))
		{ 
			//checks to see if its still going
			if (cntSmiley.getCurrentYMovement() < 0)
			{
				return true; 
			}
		}
		return false;
	}

	
	// Return true if hit bottom wall, false otherwise
	private boolean hitBottomWall(AnimatedSmiley cntSmiley)
	{
		// Wall was hit if y coordinate of bottom-most point of smiley is
		// same or greater than edge of the bottom wall and is (still)
		// heading into the wall
		
		if (cntSmiley.getBottomEdge() >= getWallEdge(WallName.BOTTOM)) //check if it hits edge
		{
			//checks to see if its still going
			if (cntSmiley.getCurrentYMovement() > 0) // check if its still going towards the wall
			{
				return true;
			}
		}
		return false;
	}	

	
	// Return which wall's edge was hit
	public int getWallEdge(WallName wallName)
	{
		// complete
		return 3;
	}
	
	// Return the color of the wallName wall
	public Color getWallColor(WallName wallName)
	{
		if (wallName == WallName.LEFT) { return leftWall.wallColor; }
		if (wallName == WallName.RIGHT) { return rightWall.wallColor; }
		if (wallName == WallName.TOP) { return topWall.wallColor; }
		if (wallName == WallName.BOTTOM) { return bottomWall.wallColor; }
		else{
			return null;
		}
	}
	
	
	// Set the specified wall to the provided color
	public void setWallColor(WallName wallName, Color c)
	{
		if (wallName == WallName.LEFT) {leftWall.wallColor = c; }
		if (wallName == WallName.RIGHT) {  rightWall.wallColor = c; }
		if (wallName == WallName.TOP) {  topWall.wallColor = c; }
		if (wallName == WallName.BOTTOM) {  bottomWall.wallColor = c; }
		
	}
	
	// drawSmiley: draw a smiley by drawing each of its parts
	private void drawSmiley(SmileyFace cntSmiley)
	{
		// complete
	}
	
	// drawPart: make an ellipse corresponding to the shape 
	// of the given smiley face part; the ellipses are what 
	// are actually drawn
	private void drawPart(SmileyFacePart part)
	{
		// complete
	}
	
	// erase: erase (by making the same as a background color) the
	// smiley face currently shown on the display
	private void erase(SmileyFace smiley)
	{
		// complete
	}
	
	// computeUpperLeft: determine the x- and y-coordinate of the
	// upper-left of a SmileyFacePart.  This should be called whenever
	// an attributes change would cause the upper-left position to
	// change.
	private void computeUpperLeft(SmileyFacePart part)	{
		// complete
	}
}


