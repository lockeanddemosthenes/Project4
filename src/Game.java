import javafx.scene.layout.*;
/**
 * Interface for a game that fits into a Pane.
 */
interface Game {
	/**
	 * Returns the Pane containing the game.
	 * @return the Pane containing the game.
	 */
	Pane getPane ();

	/**
	 * Returns the name of the game.
	 * @return the name of the game.
	 */
	String getName ();
}
