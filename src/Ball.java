import java.awt.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Class that implements a ball with a position and velocity.
 */
//this is a test
// this is another test
public class Ball implements Collidable {
	// Constants
	/**
	 * The radius of the ball.
	 */
	public static final int BALL_RADIUS = 8;
	/**
	 * The initial velocity of the ball in the x direction.
	 */
	public static final double INITIAL_VX = 1e-7;
	/**
	 * The initial velocity of the ball in the y direction.
	 */
	public static final double INITIAL_VY = 1e-7;

	// Instance variables
	// (x,y) is the position of the center of the ball.
	private double x, y;
	private double vx, vy;
	private Circle circle;

	/**
	 * @return the Circle object that represents the ball on the game board.
	 */
	public Circle getCircle () {
		return circle;
	}
	
	
	/**
	 * multiplies the x velocity by negative 1
	 */
	public void reverseDirectionX() {
		vx = vx * -1;
	}
	/**
	 * multiplies the y velocity by negative 1
	 */
	public void reverseDirectionY() {
		vy = vy * -1;
	}
	
	/**
	 * sets the y coordinate of the ball
	 * @param newY new y coordinate
	 */
	public void setY(double newY) {
		y = newY;
	}
	
	/**
	 * sets the x coordinate of the ball
	 * @param newX new x coordinate
	 */
	public void setX(double newX) {
		x = newX;
	}
	
	/**
	 * getter for the x coordinate of the ball
	 * @return the x coordinate of the ball
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * getter for the y coordinate of the ball
	 * @return the y coordinate of the ball
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Constructs a new Ball object at the centroid of the game board
	 * with a default velocity that points down and right.
	 */
	public Ball () {
		x = GameImpl.WIDTH/2;
		y = GameImpl.HEIGHT/2;
		vx = INITIAL_VX;
		vy = INITIAL_VY;

		circle = new Circle(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
		circle.setLayoutX(x - BALL_RADIUS);
		circle.setLayoutY(y - BALL_RADIUS);
		circle.setFill(Color.BLACK);
	}

	/**
	 * Updates the position of the ball, given its current position and velocity,
	 * based on the specified elapsed time since the last update.
	 * Additionally ensures that where the ball is moving is in bounds.
	 * If the ball hits the bottom wall, the gameLosses counter is increased.
	 * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
	 */
	public void updatePosition (long deltaNanoTime) {
		double dx = vx * deltaNanoTime;
		double dy = vy * deltaNanoTime;
		x += dx;
		y += dy;
		
		if(x + BALL_RADIUS > GameImpl.WIDTH) {
			setX(GameImpl.WIDTH - BALL_RADIUS);
			reverseDirectionX();
		} else if (x - BALL_RADIUS < 0) {
			setX(BALL_RADIUS);
			reverseDirectionX();
		} else {
			circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
		}
		if(y - BALL_RADIUS < 0) {
			reverseDirectionY();
			setY(BALL_RADIUS);
		} else if(y + BALL_RADIUS > GameImpl.HEIGHT) {
			setY(GameImpl.HEIGHT - BALL_RADIUS);
			reverseDirectionY();
			GameImpl.gameLosses += 1;
			System.out.println(GameImpl.gameLosses);
			
		} else {
			circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));
		}
	}


	@Override
	public double getWidth() {
		return BALL_RADIUS;
	}


	@Override
	public double getHeight() {
		return BALL_RADIUS;
	}
	
	
}
