package project.application;

import javafx.application.Application;
import javafx.stage.Stage;
import project.editor.controller.EditorController;

/**
 * Main class to launch application
 *
 * @author Christos
 *
 */
public class Main extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		final EditorController editorController = new EditorController(primaryStage);
		editorController.createAndOpen();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}