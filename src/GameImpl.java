import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.event.*;

import java.io.FileNotFoundException;
import java.util.*;

public class GameImpl extends Pane implements Game {

	/**
	 * Defines different states of the game.
	 */
	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}

	// Constants
	/**
	 * The width of the game board.
	 */
	public static final int WIDTH = 400;
	/**
	 * The height of the game board.
	 */
	public static final int HEIGHT = 600;
	/**
	 * The error of the mouse loop
	 */
	public static final int MOUSE_ERROR = 1;
	
	// Instance variables
	private Ball ball;
	private Paddle paddle;
	private List<Entity> entities;

	// gameLosses counter
	public static int gameLosses = 0;

	/**
	 * Constructs a new GameImpl.
	 */
	public GameImpl() {
		setStyle("-fx-background-color: white;");

		restartGame(GameState.NEW);
	}

	public String getName() {
		return "Zutopia";
	}

	public Pane getPane() {
		return this;
	}

	public Paddle getPaddle() {
		return paddle;
	}

	public void addImageToScreen(String filename, double x, double y) throws FileNotFoundException {
		Entity e = new Entity(filename, x, y);
		e.addToPane(this);
		entities.add(e);
	}

	/**
	 * This checks if the border of the a is within the b rectangle in the x direction
	 * 
	 * @return true if a and b are intersecting in the x direction; false otherwise
	 */

	public boolean isCollidingX(Collidable a, Collidable b) {
		double left = a.getX() - (a.getWidth() / 2);
		double right = a.getX() + (a.getWidth() / 2);
		double top = a.getY() - (a.getHeight() / 2);
		double bottom = a.getY() + (a.getHeight() / 2);
		if (b.getX() + b.getWidth() >= left && b.getX() - b.getWidth() <= right && b.getY() + b.getHeight() >= top
				&& b.getY() - b.getHeight() <= bottom) {
			if ((b.getX() < left || b.getX() > right)
					&& (b.getY() + b.getHeight() >= top && b.getY() - b.getHeight() <= bottom)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * This checks if the border of the a is within the b rectangle in the y direciton
	 * 
	 * @return true if a and b are intersecting in the y direction; false otherwise
	 */

	public boolean isCollidingY(Collidable a, Collidable b) {
		double left = a.getX() - (a.getWidth() / 2);
		double right = a.getX() + (a.getWidth() / 2);
		double top = a.getY() - (a.getHeight() / 2);
		double bottom = a.getY() + (a.getHeight() / 2);
		if (b.getX() + b.getWidth() >= left && b.getX() - b.getWidth() <= right && b.getY() + b.getHeight() >= top
				&& b.getY() - b.getHeight() <= bottom) {

			if ((b.getY() < bottom || b.getY() > top)
					&& (b.getX() + b.getWidth() >= left && b.getX() - b.getWidth() <= right)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 *  This function iterates through all of the know entities and implements features
	 *  associated with ball collisions
	 *  ie
	 *  	- removing the image
	 *  	- speeding the ball
	 */
	
	public void handleEntityCollisions() {
		List<Entity> temp = new ArrayList<Entity>();

		for (Entity e : entities) {
			if (isCollidingX(e, ball) == true) {
				handleBallCollisionX(e);
				e.removeImage(this);
				temp.add(e);
				ball.speedUp();
			} else if (isCollidingY(e, ball) == true) {
				handleBallCollisionY(e);
				e.removeImage(this);
				temp.add(e);
				ball.speedUp();
			}
		}
		entities.removeAll(temp);
	}

	/**
	 * Determines whether the ball was above or below the paddle during a collision
	 * Ensures no overlap in the ball and the paddle Reverses the direction of the
	 * ball
	 * @param a the object that we are checking the collision with the ball for 
	 */
	public void handleBallCollisionY(Collidable a) {
		if (ball.getY() > a.getY()) {
			ball.setY(a.getY() + (a.getHeight() / 2) + Ball.BALL_RADIUS);
		} else {
			ball.setY(a.getY() - (a.getHeight() / 2) - Ball.BALL_RADIUS);
		}
		ball.reverseDirectionY();
	}
	
	/**
	 * Determines whether the ball was above or below the paddle during a collision
	 * Ensures no overlap in the ball and the paddle Reverses the direction of the
	 * ball
	 * @param a the object that we are checking the collision with the ball for 
	 */
	public void handleBallCollisionX(Collidable a) {
		if (ball.getX() > a.getX()) {
			ball.setX(a.getX() + (a.getWidth() / 2) + Ball.BALL_RADIUS);
		} else {
			ball.setX(a.getX() - (a.getWidth() / 2) - Ball.BALL_RADIUS);
		}
		ball.reverseDirectionX();
	}
	
	private void restartGame(GameState state) {
		getChildren().clear(); // remove all components from the game
		
		
		entities = new ArrayList<Entity>();
		
		// Resets loss counter
		gameLosses = 0;

		// Create and add ball
		ball = new Ball();
		getChildren().add(ball.getCircle()); // Add the ball to the game board
		
		// Resets speed
		ball.resetSpeed();
		// Create and add animals ...

		// Create and add paddle
		paddle = new Paddle();
		getChildren().add(paddle.getRectangle()); // Add the paddle to the game board

		try {
			for (int i = 0; i < 4; i++) {
				addImageToScreen("horse.jpg",  .5*WIDTH / 4, (i+.5)*HEIGHT / 8);
				addImageToScreen("duck.jpg",  1.5*WIDTH / 4, (i+.5)*HEIGHT / 8);
				addImageToScreen("duck.jpg",  2.5*WIDTH / 4, (i+.5)*HEIGHT / 8);
				addImageToScreen("goat.jpg",  3.5*WIDTH / 4, (i+.5)*HEIGHT / 8);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		// Add start message
		final String message;
		if (state == GameState.LOST) {
			message = "Game Over\n";
		} else if (state == GameState.WON) {
			message = "You won!\n";
		} else {
			message = "";
		}
		final Label startLabel = new Label(message + "Click mouse to start");
		startLabel.setLayoutX(WIDTH / 2 - 50);
		startLabel.setLayoutY(HEIGHT / 2 + 100);
		getChildren().add(startLabel);

		// Add event handler to start the game
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				GameImpl.this.setOnMouseClicked(null);
				// As soon as the mouse is clicked, remove the startLabel from the game board
				getChildren().remove(startLabel);
				run();

			}
		});
		setOnMouseMoved(new EventHandler<MouseEvent>() {

			/**
			 * This method handles a mouse move event by incrementing by the PADDLE_VELOCITY
			 * until the paddle reaches the destination. This method was chosen over using
			 * solely the moveTo because of the ability to check collisions along a path and
			 * for the advantage of smoothness of the paddle movement.
			 * 
			 * @param e The current mouse event
			 */
			@Override
			public void handle(MouseEvent e) {
				double mouseX = e.getSceneX();
				double mouseY = e.getSceneY();
				// This area here checks if the mouse is off the screen, or in an unreachable
				// location.
				if (mouseX < Paddle.PADDLE_WIDTH / 2) {
					mouseX = paddle.getX();
				} else if (mouseX > GameImpl.WIDTH - Paddle.PADDLE_WIDTH / 2) {
					mouseX = paddle.getX();
				}

				if (mouseY < Paddle.MIN_Y_LOCATION_FRAC * GameImpl.HEIGHT) {
					mouseY = paddle.getY();
				} else if (mouseY > Paddle.MAX_Y_LOCATION_FRAC * GameImpl.HEIGHT) {
					mouseY = paddle.getY();
				}

				// This area targets the current mouse and moves the paddle toward it
				while (!(paddle.getX() >= mouseX - MOUSE_ERROR && paddle.getX() <= mouseX + MOUSE_ERROR
						&& paddle.getY() >= mouseY - MOUSE_ERROR && paddle.getY() <= mouseY + MOUSE_ERROR)) {
					if (paddle.getX() < mouseX) {
						paddle.moveTo(paddle.getX() + Paddle.PADDLE_VELOCITY, paddle.getY());
					} else if (paddle.getX() > mouseX) {
						paddle.moveTo(paddle.getX() - Paddle.PADDLE_VELOCITY, paddle.getY());
					}

					if (paddle.getY() < mouseY) {
						paddle.moveTo(paddle.getX(), paddle.getY() + Paddle.PADDLE_VELOCITY);
					} else if (paddle.getY() > mouseY) {
						paddle.moveTo(paddle.getX(), paddle.getY() - Paddle.PADDLE_VELOCITY);
					}

					// collisions are also tracked here so that the paddle does not skip over the
					// ball
					// this happened in testing when we moved the mouse very fast
					if (isCollidingX(paddle, ball) == true) {
						handleBallCollisionX(paddle);
					} else if (isCollidingY(paddle, ball) == true) {
						handleBallCollisionY(paddle);
					}
				}
			}
		});

		// Add another event handler to steer paddle...
	}

	/**
	 * Begins the game-play by creating and starting an AnimationTimer.
	 */
	public void run() {
		// Instantiate and start an AnimationTimer to update the component of the game.
		new AnimationTimer() {
			private long lastNanoTime = -1;

			public void handle(long currentNanoTime) {
				if (lastNanoTime >= 0) { // Necessary for first clock-tick.
					GameState state;
					
					if (entities.isEmpty()) {
						state = GameState.WON;
						stop();
						restartGame(state);
					}
					
					if (gameLosses == 5) {
						state = GameState.LOST;
						stop();
						restartGame(state);
					}
					
					
					// collisions between ball and paddle are also done here so that
					// they are not solely relying on mouse movement
					if (isCollidingX(paddle, ball) == true) {
						handleBallCollisionX(paddle);
					} else if (isCollidingY(paddle, ball) == true) {
						handleBallCollisionY(paddle);
					}
					
					handleEntityCollisions();

					if ((state = runOneTimestep(currentNanoTime - lastNanoTime)) != GameState.ACTIVE) {
						// Once the game is no longer ACTIVE, stop the AnimationTimer.
						stop();
						// Restart the game, with a message that depends on whether
						// the user won or lost the game.
						restartGame(state);
					}
				}
				// Keep track of how much time actually transpired since the last clock-tick.
				lastNanoTime = currentNanoTime;
			}
		}.start();
	}

	/**
	 * Updates the state of the game at each timestep. In particular, this method
	 * should move the ball, check if the ball collided with any of the animals,
	 * walls, or the paddle, etc.
	 * 
	 * @param deltaNanoTime how much time (in nanoseconds) has transpired since the
	 *                      last update
	 * @return the current game state
	 */
	public GameState runOneTimestep(long deltaNanoTime) {
		ball.updatePosition(deltaNanoTime);
		return GameState.ACTIVE;
	}
}
