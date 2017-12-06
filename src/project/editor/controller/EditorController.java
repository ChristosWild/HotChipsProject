package project.editor.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.editor.control.CanvasControl;
import project.editor.control.SelectorControl;

public class EditorController
{
	private static int editorInstanceCount = -1;
	private static List<EditorController> editorInstances = new ArrayList<EditorController>();

	private static final String EDITOR_TITLE = "VLSI Editor";
	private static final int EDITOR_WIDTH = 700;
	private static final int EDITOR_HEIGHT = 400;

	private Stage stage;
	private BorderPane root;

	private ToolbarController toolbarController;

	public EditorController(final Stage stage)
	{
		this.stage = stage;
		editorInstanceCount++;
		editorInstances.add(this);

		toolbarController = new ToolbarController();
		// canvasController = new CanvasController();

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
		// canvasController.createPartControl(root);

		CanvasControl cc = new CanvasControl();
		cc.createPartControl(root);

		stage.setScene(new Scene(root, EDITOR_WIDTH, EDITOR_HEIGHT));
		stage.setTitle(editorInstanceCount > 0 ? EDITOR_TITLE + " (" + editorInstanceCount + ")" : EDITOR_TITLE);
		stage.show();

		SelectorControl.getInstance().show();
	}
}
