package project.editor.controller;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.editor.control.SelectorControl;
import project.editor.control.TechnologyControl;
import project.editor.control.ToolbarControl;
import project.editor.controller.CanvasController.CanvasMode;
import project.editor.utils.FileUtil;

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
			FileUtil.openFile(editorController, toolbarControl.getRoot().getScene().getWindow());
		});

		toolbarControl.getMenuItemSave().setOnAction(event -> {
			FileUtil.saveFile(editorController, toolbarControl.getRoot().getScene().getWindow());
		});

		toolbarControl.getMenuItemSaveAs().setOnAction(event -> {
			FileUtil.saveFileAs(editorController, toolbarControl.getRoot().getScene().getWindow());
		});

		toolbarControl.getMenuItemClose().setOnAction(event -> toolbarControl.getRoot().getScene().getWindow().hide());

		// VIEW
		toolbarControl.getMenuItemShowToolbar().setOnAction(event -> {
			SelectorControl.getInstance().setMeantToBeVisible(true);
			SelectorControl.getInstance().updateOwner((Stage) toolbarControl.getRoot().getScene().getWindow(), true);
		});

		toolbarControl.getMenuItemZoomIn().setOnAction(event -> editorController.getCanvasController().zoomIn());
		toolbarControl.getMenuItemZoomOut().setOnAction(event -> editorController.getCanvasController().zoomOut());
		toolbarControl.getMenuItemZoomReset().setOnAction(event -> editorController.getCanvasController().zoomReset());

		// EDIT
		toolbarControl.getMenuItemCut().setOnAction(event -> editorController.getCanvasController().cut());
		toolbarControl.getMenuItemCopy().setOnAction(event -> editorController.getCanvasController().copy());
		toolbarControl.getMenuItemPaste().setOnAction(event -> editorController.getCanvasController().paste());
		toolbarControl.getMenuItemTechnologyFile().setOnAction(event -> TechnologyControl.getInstance().show());

		// HELP

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

		toolbarControl.getBtnDelete().setOnAction(evet -> editorController.getCanvasController().deleteSelected());
		toolbarControl.getBtnClearAll().setOnAction(evet -> editorController.getCanvasController().clearAll());
	}

	public void focusToolbarButton() // TODO temp fix for bug where 1x1 rectangles not appearing until something else focused
	{
		((ToggleButton) toolbarControl.getToggleGroup().getSelectedToggle()).requestFocus();
	}
}
