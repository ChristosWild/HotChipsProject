package project.editor.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.editor.control.SelectorControl;

public class EditorController
{
	private static int editorInstanceCount = -1;
	private static List<EditorController> editorInstances = new ArrayList<EditorController>();

	private static final String EDITOR_TITLE = "VLSI Editor";
	private static final int EDITOR_WIDTH = 900;
	private static final int EDITOR_HEIGHT = 600;

	private Stage stage;
	private BorderPane root;

	private ToolbarController toolbarController;
	private CanvasController canvasController;

	private String filePath;

	public EditorController(final Stage stage)
	{
		this.stage = stage;
		editorInstanceCount++;
		editorInstances.add(this);

		toolbarController = new ToolbarController(this);
		canvasController = new CanvasController(this);

		stage.focusedProperty().addListener((ov, oldVal, newVal) -> {
			if (oldVal)
			{
				SelectorControl.getInstance().hide();
			} else if (newVal)
			{
				SelectorControl.getInstance().updateOwner(stage, true);
			}
		});

		stage.setOnCloseRequest(event -> {

			editorInstances.remove(this);

			// If current owner closes AND if there is another editor open, change popup
			// owner
			if (SelectorControl.getInstance().hasOwner(stage) && editorInstances.size() > 0)
			{
				SelectorControl.getInstance().updateOwner(editorInstances.get(0).stage, true);
			}
		});
	}

	public void createAndOpen()
	{
		root = new BorderPane();

		toolbarController.createPartControl(root);
		canvasController.createPartControl(root);

		stage.setScene(new Scene(root, EDITOR_WIDTH, EDITOR_HEIGHT));
		stage.setTitle(editorInstanceCount > 0 ? EDITOR_TITLE + " (" + editorInstanceCount + ")" : EDITOR_TITLE);
		stage.show();

		SelectorControl.getInstance().show();
	}

	public CanvasController getCanvasController()
	{
		return canvasController;
	}

	public ToolbarController getToolbarController()
	{
		return toolbarController;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(final String filePath)
	{
		this.filePath = filePath;
	}
}
