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
	
	public boolean isPaddleColliding() {
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

	private void restartGame (GameState state) {
		getChildren().clear();  // remove all components from the game

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
					@Override
					public void handle(MouseEvent e) {
						double mouseX = e.getSceneX();
						double mouseY = e.getSceneY();
						if (mouseX < Paddle.PADDLE_WIDTH/2) {
							mouseX = e.getSceneX();
						} else if (mouseX > GameImpl.WIDTH - Paddle.PADDLE_WIDTH/2) {
							mouseX = e.getSceneX();
						}

						if (mouseY < Paddle.MIN_Y_LOCATION_FRAC * GameImpl.HEIGHT) {
							mouseY = e.getSceneY();
						} else if (mouseY > Paddle.MAX_Y_LOCATION_FRAC * GameImpl.HEIGHT) {
							mouseY = e.getSceneY();
						}
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
							if(isPaddleColliding() == true) {
								ball.reverseDirectionY();
							} 
						}
						//System.out.println(mouseX);
						//paddle.moveTo(mouseX, e.getSceneY());
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
					if(isPaddleColliding() == true) {
						ball.reverseDirectionY();
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
