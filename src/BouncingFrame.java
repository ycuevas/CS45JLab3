import javax.swing.JFrame;

// Make the frame consisting of a panel in which smiley faces are drawn,
// building on the attributes of the BasicFrame
public class BouncingFrame extends BasicFrame
{
	// The frame contains an animation
	// of a bouncing smiley group
	JFrame frame;
	// public inherited constants: 

	// add needed fields here

	// Construct a bouncing frame from a basic frame -
	// their characteristics are the same:
	//	height of HEIGHT, width of WIDTH,
	//  not resizeable, titled with the given title,
	//  program exits when the close box hit
	
	public BouncingFrame(String title)
	{
		super(title);
		frame = new JFrame();
		frame.setSize(HEIGHT, WIDTH);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	// activateAnimation: from the given bouncing group and display, construct a 
	//  smiley animation, make the bouncing frame visible, and fire up the animation
	public void activateAnimation(BouncingGroup bouncers, BouncingDisplay bounceDisplay)
	{
		SmileyAnimation smileyAnimation = new SmileyAnimation(bouncers, bounceDisplay);
		frame.add(bounceDisplay);
		frame.setVisible(true);
		smileyAnimation.animate();
	}
}