package project.editor.controller;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import project.editor.control.CanvasControl;
import project.editor.control.SelectorControl;
import project.editor.utils.EditorConstants;
import project.editor.utils.EditorUtils;
import project.editor.utils.EditorUtils.Delta;
import project.editor.utils.Layer;

public class CanvasController
{
	public enum CanvasMode
	{
		SELECT, DRAW;
	}

	private CanvasControl canvasControl;
	private CanvasMode canvasMode;
	private Pane selectedPane;
	private Rectangle rectangle;
	private Delta startPos;
	private boolean isDragging;

	public CanvasController()
	{
		canvasControl = new CanvasControl();
		setCanvasMode(CanvasMode.SELECT);
	}

	public void createPartControl(final BorderPane root)
	{
		canvasControl.createPartControl(root);
		setUpListeners(root);
	}

	private void setUpListeners(final BorderPane root)
	{
		canvasControl.getSelectionPane().addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {

			isDragging = true;

			startPos = new Delta();
			startPos.x = event.getX();
			startPos.y = event.getY();
			EditorUtils.snapToGrid(startPos);
			rectangle = new Rectangle(startPos.x, startPos.y, 0, 0);

			Paint fill;

			if (canvasMode == CanvasMode.DRAW)
			{
				selectedPane = canvasControl.getCurrentPane();

				if (SelectorControl.getInstance().getSelectedLayer() == Layer.VIA)
				{
					fill = new ImagePattern(new Image("file:data/via.png")); // TODO creates new images every time
				}
				else if (SelectorControl.getInstance().getSelectedLayer() == Layer.PIN)
				{
					fill = new ImagePattern(new Image("file:data/pin.png"));
				}
				else
				{
					fill = Color.web(canvasControl.getCurrentPaneColor().toString(), 0.4);
				}

			}
			else// if (canvasMode == CanvasMode.SELECT)
			{
				selectedPane = canvasControl.getSelectionPane();
				fill = Color.web(Color.GREY.toString(), 0.4);
			}

			rectangle.setFill(fill);
			selectedPane.getChildren().add(rectangle);
		});

		canvasControl.getSelectionPane().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {

			if (isDragging)
			{
				final Delta dragDelta = new Delta(event.getX() - startPos.x, event.getY() - startPos.y); // DONT MINUS BEFORE SNAPPING TO GRID
				EditorUtils.snapToGrid(dragDelta);
				double width = dragDelta.x;
				double height = dragDelta.y;

				System.out.println(width + ", " + height);

				if (width > 0)
				{
					if (rectangle.getX() + width <= EditorConstants.CANVAS_WIDTH)
					{
						rectangle.setTranslateX(0);
						rectangle.setWidth(width);
						System.out.println("WIDTH CHANGED");
					}
				}
				else if (rectangle.getX() + width > 0)
				{
					rectangle.setTranslateX(width);
					rectangle.setWidth(-width);
				}

				if (height > 0)
				{
					if (rectangle.getY() + height <= EditorConstants.CANVAS_HEIGHT)
					{
						rectangle.setTranslateY(0);
						rectangle.setHeight(height);
						System.out.println("HEIGHT CHANGED");
					}
				}
				else if (rectangle.getY() + height > 0)
				{
					rectangle.setTranslateY(height);
					rectangle.setHeight(-height);
				}
			}
		});

		canvasControl.getSelectionPane().addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {

			final Delta endPos = new Delta(event.getX(), event.getY());
			EditorUtils.snapToGrid(endPos);

			// System.out.println(startPos.x + " : " + endPos.x);
			// System.out.println(startPos.y + " : " + endPos.y + "\n");

			if (endPos.x == startPos.x && endPos.y == startPos.y)
			{
				System.out.println("NOT MOVED");
			}

			if (canvasMode == CanvasMode.SELECT)
			{
				// select here
				selectedPane.getChildren().remove(rectangle);
			}

			isDragging = false;
			rectangle = null;
		});
	}

	public void setCanvasMode(final CanvasMode canvasMode)
	{
		this.canvasMode = canvasMode;
	}
}
