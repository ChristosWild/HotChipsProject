package project.editor.controller;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.editor.control.SelectorControl;
import project.editor.control.ToolbarControl;

public class ToolbarController
{
	private ToolbarControl toolbarControl;

	public ToolbarController()
	{
		toolbarControl = new ToolbarControl();
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
	}
}
