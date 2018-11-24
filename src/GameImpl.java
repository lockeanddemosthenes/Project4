import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.event.*;
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

	// Instance variables
	private Ball ball;
	private Paddle paddle;
	
	// gameLosses counter
	public static int gameLosses = 0;

	/**
	 * Constructs a new GameImpl.
	 */
	public GameImpl () {
		setStyle("-fx-background-color: white;");

		restartGame(GameState.NEW);
	}

	public String getName () {
		return "Zutopia";
	}

	public Pane getPane () {
		return this;
	}
	
	public Paddle getPaddle() {
		return paddle;
	}
	
	/**
	 * This checks if the radius of the ball is within the paddle rectangle
	 * @return true if the paddle and the ball are intersecting; false otherwise
	 */
	
	public boolean isBallPaddleColliding() {
		double paddleLeft = paddle.getX() - (Paddle.PADDLE_WIDTH/2);
		double paddleRight = paddle.getX() + (Paddle.PADDLE_WIDTH/2);
		double paddleTop = paddle.getY() - (Paddle.PADDLE_HEIGHT/2);
		double paddleBottom = paddle.getY() + (Paddle.PADDLE_HEIGHT/2);
		if(ball.getX() + Ball.BALL_RADIUS >= paddleLeft && 
				ball.getX() - Ball.BALL_RADIUS <= paddleRight && 
				ball.getY() + Ball.BALL_RADIUS  >= paddleTop && 
				ball.getY() - Ball.BALL_RADIUS  <= paddleBottom) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * determines whether the ball was above or below the paddle during a collision 
	 * ensures no overlap in the ball and the paddle
	 * reverses the direction of the ball
	 */
	public void handleBallPaddleCollision() {
		if (ball.getY() > paddle.getY()) {
			ball.setY(paddle.getY() + (Paddle.PADDLE_HEIGHT/2) + Ball.BALL_RADIUS);
		}
		else {
			ball.setY(paddle.getY() - (Paddle.PADDLE_HEIGHT/2) - Ball.BALL_RADIUS);
		}
		ball.reverseDirectionY();
	}

	private void restartGame (GameState state) {
		getChildren().clear();  // remove all components from the game
		
		// Resets loss counter
		gameLosses = 0;

		// Create and add ball
		ball = new Ball();
		getChildren().add(ball.getCircle());  // Add the ball to the game board

		// Create and add animals ...

		// Create and add paddle
		paddle = new Paddle();
		getChildren().add(paddle.getRectangle());  // Add the paddle to the game board

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
		setOnMouseClicked(new EventHandler<MouseEvent> () {
			@Override
			public void handle (MouseEvent e) {
				GameImpl.this.setOnMouseClicked(null);
				
				setOnMouseMoved(new EventHandler<MouseEvent> () {
					
					/** This method handles a mouse move event by incrementing by the PADDLE_VELOCITY
					 *  until the paddle reaches the destination.
					 *  This method was chosen over using solely the moveTo because of the ability to check collisions along 
					 *  a path and for the advantage of smoothness of the paddle movement.
					 *  @param e The current mouse event
					 */
					@Override
					public void handle(MouseEvent e) {
						double mouseX = e.getSceneX();
						double mouseY = e.getSceneY();
						//This area here checks if the mouse is off the screen, or in an unreachable location.
						if (mouseX < Paddle.PADDLE_WIDTH/2) {
							mouseX = paddle.getX();
						} else if (mouseX > GameImpl.WIDTH - Paddle.PADDLE_WIDTH/2) {
							mouseX = paddle.getX();
						}

						if (mouseY < Paddle.MIN_Y_LOCATION_FRAC * GameImpl.HEIGHT) {
							mouseY = paddle.getY();
						} else if (mouseY > Paddle.MAX_Y_LOCATION_FRAC * GameImpl.HEIGHT) {
							mouseY = paddle.getY();
						}
						
						//This area targets the current mouse and moves the paddle toward it
						while (paddle.getX() != mouseX || paddle.getY() != mouseY) {
							if (paddle.getX() < mouseX) {
								paddle.moveTo(paddle.getX() + Paddle.PADDLE_VELOCITY,paddle.getY());
							}
							else if(paddle.getX() > mouseX) {
								paddle.moveTo(paddle.getX() - Paddle.PADDLE_VELOCITY, paddle.getY());
							}
							
							if (paddle.getY() < mouseY) {
								paddle.moveTo(paddle.getX(), paddle.getY() + Paddle.PADDLE_VELOCITY);
							}
							else if(paddle.getY() > mouseY) {
								paddle.moveTo(paddle.getX(), paddle.getY() - Paddle.PADDLE_VELOCITY);
							}
						}
					}
				});
				// As soon as the mouse is clicked, remove the startLabel from the game board
				getChildren().remove(startLabel);
				run();
			}
		});
		
		
		// Add another event handler to steer paddle...
	}

	/**
	 * Begins the game-play by creating and starting an AnimationTimer.
	 */
	public void run () {
		// Instantiate and start an AnimationTimer to update the component of the game.
		new AnimationTimer () {
			private long lastNanoTime = -1;
			public void handle (long currentNanoTime) {
				if (lastNanoTime >= 0) {  // Necessary for first clock-tick.
					GameState state;
					
					if (gameLosses == 5) {
						state = GameState.LOST;
						stop();
						restartGame(state);
					}
					
					// collisions between ball and paddle are done here so that
					// they are not relying on mouse movement
					if(isBallPaddleColliding() == true) {
						handleBallPaddleCollision();
					} 
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
	 * Updates the state of the game at each timestep. In particular, this method should
	 * move the ball, check if the ball collided with any of the animals, walls, or the paddle, etc.
	 * @param deltaNanoTime how much time (in nanoseconds) has transpired since the last update
	 * @return the current game state
	 */
	public GameState runOneTimestep (long deltaNanoTime) {
		ball.updatePosition(deltaNanoTime);
		return GameState.ACTIVE;
	}
}
