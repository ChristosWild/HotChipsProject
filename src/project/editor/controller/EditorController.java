package project.editor.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.editor.control.SelectorControl;
import project.editor.utils.EditorConstants;
import project.editor.utils.FileUtil;

public class EditorController
{
	private static int editorInstanceCount = -1;
	private static List<EditorController> editorInstances = new ArrayList<EditorController>();

	private static final String EDITOR_TITLE = "VLSI Editor"; // TODO rename stage when file saved or opened
	private static final int EDITOR_WIDTH = 900 / 2;
	private static final int EDITOR_HEIGHT = 600;

	private Stage stage;
	private BorderPane root;

	private ToolbarController toolbarController;
	private CanvasController canvasController;

	private String filePath;

	private EventHandler<KeyEvent> keyPressedHandler = new EventHandler<KeyEvent>()
	{
		@Override
		public void handle(KeyEvent event) // TODO refine var names etc, use same methods as toolbar
		{
			final KeyCode key = event.getCode();

			if (event.isShortcutDown())
			{
				switch (key)
				{
					// FILE
					case N:
						final EditorController newController = new EditorController(new Stage());
						newController.createAndOpen();
						break;

					case O:
						FileUtil.openFile(stage);
						break;

					case S:
						FileUtil.saveFile(EditorController.this, stage);
						break;

						// VIEW
					case T:
						SelectorControl.getInstance().setMeantToBeVisible(true);
						SelectorControl.getInstance().updateOwner(stage, true);
						break;

					case PLUS:
					case ADD:
					case EQUALS:
						canvasController.zoomIn();
						break;

					case MINUS:
					case SUBTRACT:
						canvasController.zoomOut();
						break;

					case R:
						canvasController.zoomReset();
						break;

						// EDIT
					case X:
						canvasController.cut();
						break;

					case C:
						canvasController.copy();
						break;

					case V:
						canvasController.paste();
						break;

					default:
						break;
				}
			}
			else
			{
				final ScrollPane scrollPane = ((ScrollPane) root.getCenter());
				double pageHeightPercentage = scrollPane.getViewportBounds().getHeight() / EditorConstants.CANVAS_HEIGHT;
				double pageWidthPercentage = scrollPane.getViewportBounds().getWidth() / EditorConstants.CANVAS_WIDTH;

				switch (key)
				{
					case DELETE:
						canvasController.deleteSelected();
						break;

					case S:
						toolbarController.selectModeSelect();
						break;

					case D:
						toolbarController.selectModeDraw();
						break;

					case UP:
						scrollPane.setVvalue(scrollPane.getVvalue() - pageHeightPercentage);
						break;

					case DOWN:
						scrollPane.setVvalue(scrollPane.getVvalue() + pageHeightPercentage);
						break;

					case LEFT:
						scrollPane.setHvalue(scrollPane.getHvalue() - pageWidthPercentage);
						break;

					case RIGHT:
						scrollPane.setHvalue(scrollPane.getHvalue() + pageWidthPercentage);
						break;

					case DIGIT1:
						SelectorControl.getInstance().selectLayer(0);
						break;

					case DIGIT2:
						SelectorControl.getInstance().selectLayer(1);
						break;

					case DIGIT3:
						SelectorControl.getInstance().selectLayer(2);
						break;

					case DIGIT4:
						SelectorControl.getInstance().selectLayer(3);
						break;

					case DIGIT5:
						SelectorControl.getInstance().selectLayer(4);
						break;

					case DIGIT6:
						SelectorControl.getInstance().selectLayer(5);
						break;

					case DIGIT7:
						SelectorControl.getInstance().selectLayer(6);
						break;

					case DIGIT8:
						SelectorControl.getInstance().selectLayer(7);
						break;

					case DIGIT9:
						SelectorControl.getInstance().selectLayer(8);
						break;

					case DIGIT0:
						SelectorControl.getInstance().selectLayer(9);
						break;

					default:
						break;
				}
			}
			event.consume();
		}
	};

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

		stage.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedHandler);

		stage.setScene(new Scene(root, EDITOR_WIDTH, EDITOR_HEIGHT));
		stage.setTitle(editorInstanceCount > 0 ? EDITOR_TITLE + " (" + editorInstanceCount + ")" : EDITOR_TITLE);
		stage.show();

		SelectorControl.getInstance().show();
	}

	public static EditorController getEditorControllerFromStage(final Stage stage)
	{
		for (final EditorController controller : editorInstances)
		{
			if (controller.stage == stage)
			{
				return controller;
			}
		}

		return null;
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

	public void setEditorTitle(final String title)
	{
		stage.setTitle(title);
	}

	public EventHandler<KeyEvent> getKeyPressedHandler()
	{
		return keyPressedHandler;
	}
}
