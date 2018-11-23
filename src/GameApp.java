import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.*;

public class GameApp extends Application {
	public GameApp () {
	}

	@Override
	public void start (Stage primaryStage) {
		Game game = new GameImpl();
		primaryStage.setTitle(game.getName());
		primaryStage.setScene(new Scene(game.getPane(), GameImpl.WIDTH, GameImpl.HEIGHT));
                primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
