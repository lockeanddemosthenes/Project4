import java.awt.*;

import java.io.FileNotFoundException;


import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javafx.scene.media.*;            
import javafx.scene.layout.*; 
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Parent;
import javafx.scene.control.Label;


public class Entity extends Object{
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

	private Rectangle rectangle;
	private Label imageLabel;
	
	public void removeImage() {
		
	}
	
	public void addToPane(Pane pane) {
		pane.getChildren().add(imageLabel);
	}


	public Entity(String fileName, double x, double y) throws FileNotFoundException {
		final Image image = new Image(fileName);
		imageLabel = new Label("", new ImageView(image));
		imageLabel.setLayoutX(x - image.getWidth()/2);
		imageLabel.setLayoutY(y - image.getHeight()/2);
	}
	
	
}
