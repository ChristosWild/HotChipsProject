package project.editor.controller;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import project.editor.control.CanvasControl;

public class CanvasController
{
	private CanvasControl canvasControl;

	public CanvasController()
	{
		canvasControl = new CanvasControl();
	}

	public void createPartControl(final BorderPane root)
	{
		canvasControl.createPartControl(root);
		setUpListeners(root);
	}

	private void setUpListeners(final BorderPane root)
	{
		root.getCenter().addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			canvasControl.getGcMetalOne().beginPath();
			canvasControl.getGcMetalOne().moveTo(event.getX(), event.getY());
			canvasControl.getGcMetalOne().stroke();
		});

		root.getCenter().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
			canvasControl.getGcMetalOne().lineTo(event.getX(), event.getY());
			canvasControl.getGcMetalOne().stroke();
		});

		root.getCenter().addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
		});
	}
}
