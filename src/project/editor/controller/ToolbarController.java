package project.editor.controller;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.editor.control.SelectorControl;
import project.editor.control.ToolbarControl;
import project.editor.controller.CanvasController.CanvasMode;

public class ToolbarController
{
	private EditorController editorController;
	private ToolbarControl toolbarControl;

	public ToolbarController(final EditorController editorController)
	{
		toolbarControl = new ToolbarControl();
		this.editorController = editorController;
	}

	public void createPartControl(final BorderPane root)
	{
		toolbarControl.createPartControl(root);
		setUpListeners();
	}

	private void setUpListeners()
	{
		// FILE
		toolbarControl.getMenuItemNew().setOnAction(event -> {
			final EditorController newEditor = new EditorController(new Stage());
			newEditor.createAndOpen();
		});

		toolbarControl.getMenuItemOpen().setOnAction(event -> {

		});

		toolbarControl.getMenuItemClose().setOnAction(event -> {
			toolbarControl.getRoot().getScene().getWindow().hide();
		});

		// VIEW
		toolbarControl.getMenuItemShowToolbar().setOnAction(event -> {
			SelectorControl.getInstance().setMeantToBeVisible(true);
			SelectorControl.getInstance().updateOwner((Stage) toolbarControl.getRoot().getScene().getWindow(), true);
		});

		// HELP

		// ABOUT

		// BUTTONS
		toolbarControl.getToggleGroup().selectedToggleProperty().addListener((obs, oldVal, newVal) -> {

			if (newVal == null)
			{
				oldVal.setSelected(true);
			}
			else if (newVal == toolbarControl.getBtnSelect())
			{
				editorController.getCanvasController().setCanvasMode(CanvasMode.SELECT);
			}
			else if (newVal == toolbarControl.getBtnDraw())
			{
				editorController.getCanvasController().setCanvasMode(CanvasMode.DRAW);
			}
		});

		toolbarControl.getBtnDelete().setOnAction(evet -> {
			editorController.getCanvasController().deleteSelected();
		});

		toolbarControl.getBtnClearAll().setOnAction(evet -> {
			editorController.getCanvasController().clearAll();
		});
	}

	public void focusToolbarButton() // TODO temp fix for weird not drawing bug 1x1 rectangles not appearing until something else focused
	{
		((ToggleButton) toolbarControl.getToggleGroup().getSelectedToggle()).requestFocus();
	}
}
