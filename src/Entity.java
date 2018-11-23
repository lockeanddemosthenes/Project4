import java.awt.*;
import javafx.scene.media.*;            
import javafx.scene.layout.*; 
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;


public class Entity {
	// Constants
	/**
	 * The width of the paddle.
	 */
	public static final int PADDLE_WIDTH = 50;
	/**
	 * The height of the paddle.
	 */
	public static final int PADDLE_HEIGHT = 50;
	
	/**
	 * @return the x coordinate of the center of the paddle.
	 */
	public double getX () {
		return rectangle.getLayoutX() + rectangle.getTranslateX() + PADDLE_WIDTH/2;
	}

	/**
	 * @return the y coordinate of the center of the paddle.
	 */
	public double getY () {
		return rectangle.getLayoutY() + rectangle.getTranslateY() + PADDLE_HEIGHT/2;
	}

	private Rectangle rectangle;
	
	public Entity(String fileName, double x, double y) {
		final Image image = new Image(getClass().getResourceAsStream(fileName));
		Label imageLabel = new Label("", new ImageView(image));
		imageLabel.setLayoutX(x - image.getWidth()/2);
		imageLabel.setLayoutY(y - image.getHeight()/2);
		
		// TODO Add the image to the game board
		pane.getChildren().add(imageLabel);  // pane is of type Pane
	}
	
	
}
