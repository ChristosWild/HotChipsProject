package project.editor.controller;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import project.editor.control.CanvasControl;
import project.editor.utils.EditorUtils.Delta;

public class CanvasController
{
	private enum CanvasMode
	{
		SELECT, DRAW;
	}

	private CanvasControl canvasControl;
	private CanvasMode canvasMode;
	private GraphicsContext gc;
	private Delta dragDelta;
	private Rectangle rectangle;

	public CanvasController()
	{
		canvasControl = new CanvasControl();
		canvasMode = CanvasMode.DRAW; // TODO default select?
	}

	public void createPartControl(final BorderPane root)
	{
		canvasControl.createPartControl(root);
		setUpListeners(root);
	}

	private void setUpListeners(final BorderPane root)
	{
		root.getCenter().addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {

			if (canvasMode == CanvasMode.DRAW)
			{
				gc = canvasControl.getSelectedGc();
				dragDelta = new Delta();
				dragDelta.x = event.getX();
				dragDelta.y = event.getY();

				rectangle = new Rectangle(dragDelta.x, dragDelta.y, 0, 0);

			} else if (canvasMode == CanvasMode.SELECT)
			{
			}
		});

		root.getCenter().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {

		});

		root.getCenter().addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
		});
	}
}
