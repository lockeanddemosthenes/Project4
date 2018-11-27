import java.io.FileNotFoundException;


import javafx.scene.layout.*; 
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;

//Entity is collidable so that the ball can hit it
public class Entity implements Collidable {
	// Constants
	/**
	 * The width of the paddle.
	 */
	public static final int ENTITY_WIDTH = 50;
	/**
	 * The height of the paddle.
	 */
	public static final int ENTITY_HEIGHT = 50;
	
	/**
	 * @return the x coordinate of the center of the paddle.
	 */
	public double getX () {
		return rectangle.getLayoutX() + rectangle.getTranslateX() + ENTITY_WIDTH/2;
	}

	/**
	 * @return the y coordinate of the center of the paddle.
	 */
	public double getY () {
		return rectangle.getLayoutY() + rectangle.getTranslateY() + ENTITY_HEIGHT/2;
	}
	
	//rectangle that helps contain the image
	private Rectangle rectangle;
	//label that helps display the image
	private Label imageLabel;
	
	
	/**
	 * removes the entity from the pane and gets the rectangle out of the way
	 * @param pane the game pane
	 */
	public void removeImage(Pane pane) {
		pane.getChildren().remove(this.imageLabel);
		rectangle.setLayoutX(-50);
		rectangle.setLayoutY(-50);
	}
	
	/**
	 * adds the image to the game pane, displaying it
	 * @param pane the game pane
	 */
	public void addToPane(Pane pane) {
		pane.getChildren().add(imageLabel);
	}

	/**
	 * sets both the image label and the rectangle to be centered at x,y 
	 * sets the image of the label
	 * 
	 * @param fileName the relative (or absolute) path of the image
	 * @param x the center x coordinate
	 * @param y the center y coordinate
	 * @throws FileNotFoundException if the file is not found
	 */
	public Entity(String fileName, double x, double y) throws FileNotFoundException {
		final Image image = new Image(fileName);
		imageLabel = new Label("", new ImageView(image));
		imageLabel.setLayoutX(x - image.getWidth()/2);
		imageLabel.setLayoutY(y - image.getHeight()/2);
		rectangle = new Rectangle(0, 0, ENTITY_WIDTH, ENTITY_HEIGHT);
		rectangle.setLayoutX(x-ENTITY_WIDTH/2);
		rectangle.setLayoutY(y-ENTITY_HEIGHT/2);
	}
	
	/**
	 * gets the width of the enitity
	 * @see Collidable
	 */
	@Override
	public double getWidth() {
		return ENTITY_WIDTH;
	}

	/**
	 * gets the height of the enitity
	 * @see Collidable
	 */
	@Override
	public double getHeight() {
		return ENTITY_HEIGHT;
	}
	
	
}
