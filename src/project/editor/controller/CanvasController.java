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
import project.editor.utils.LayerRectangle;

public class CanvasController
{
	public enum CanvasMode
	{
		SELECT, DRAW;
	}

	private EditorController editorController;
	private CanvasControl canvasControl;
	private CanvasMode canvasMode;
	private Pane selectedPane;
	private LayerRectangle rectangle;
	private Delta startPos;
	private boolean isDragging;
	private boolean isSelected;
	private boolean isMoving;

	public CanvasController(final EditorController editorController)
	{
		this.editorController = editorController;
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

			Paint fill = null;
			Paint fillSelected = null;

			if (canvasMode == CanvasMode.DRAW)
			{
				canvasControl.deselectAll();

				selectedPane = canvasControl.getCurrentPane();

				if (SelectorControl.getInstance().getSelectedLayer() == Layer.VIA)
				{
					fill = new ImagePattern(new Image(EditorConstants.PATH_FILE_DATA + EditorConstants.PATH_IMG_VIA)); // TODO creates new images every time
					fillSelected = new ImagePattern(new Image(EditorConstants.PATH_FILE_DATA + EditorConstants.PATH_IMG_VIA_SELECTED));
				}
				else if (SelectorControl.getInstance().getSelectedLayer() == Layer.PIN)
				{
					fill = new ImagePattern(new Image(EditorConstants.PATH_FILE_DATA + EditorConstants.PATH_IMG_PIN));
					fillSelected = new ImagePattern(new Image(EditorConstants.PATH_FILE_DATA + EditorConstants.PATH_IMG_PIN_SELECTED));
				}
				else
				{
					fill = canvasControl.getCurrentPaneColor();
				}

			}
			else // SELECT
			{
				selectedPane = canvasControl.getSelectionPane();
				fill = Color.web(Color.GREY.toString(), 0.4); // TODO pick selection box colour

				isSelected = canvasControl.getSelectedObjects().size() > 0;
				isMoving = isSelected && selectedContainsMouse(event);
			}

			if(fill instanceof Color)
			{
				rectangle = new LayerRectangle(startPos.x, startPos.y, 0, 0, (Color) fill);
			}
			else
			{
				rectangle = new LayerRectangle(startPos.x, startPos.y, 0, 0, fill, fillSelected);
			}
			selectedPane.getChildren().add(rectangle);
		});

		canvasControl.getSelectionPane().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {

			if (isDragging)
			{
				if (isMoving)
				{
					for (final LayerRectangle layerRect : canvasControl.getSelectedObjects())
					{
						// TODO
					}
				}
				else
				{
					final Delta dragDelta = new Delta(event.getX(), event.getY());
					EditorUtils.snapToGrid(dragDelta);
					double width = dragDelta.x - startPos.x;
					double height = dragDelta.y - startPos.y;

					if (width >= 0)
					{
						if (rectangle.getX() + width <= EditorConstants.CANVAS_WIDTH)
						{
							rectangle.setTranslateX(0);
							rectangle.setWidth(width);
						}
					}
					else if (rectangle.getX() + width >= 0)
					{
						rectangle.setTranslateX(width);
						rectangle.setWidth(-width);
					}

					if (height >= 0)
					{
						if (rectangle.getY() + height <= EditorConstants.CANVAS_HEIGHT)
						{
							rectangle.setTranslateY(0);// TODO change to layout
							rectangle.setHeight(height);
						}
					}
					else if (rectangle.getY() + height >= 0)
					{
						rectangle.setTranslateY(height);
						rectangle.setHeight(-height);
					}
				}
			}
		});

		canvasControl.getSelectionPane().addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {

			final Delta endPos = new Delta(event.getX(), event.getY());
			EditorUtils.snapToGrid(endPos);

			if (canvasMode == CanvasMode.DRAW && (rectangle.getWidth() == 0 || rectangle.getHeight() == 0))
			{
				selectedPane.getChildren().remove(rectangle);
			}
			else if (canvasMode == CanvasMode.SELECT)
			{
				if (isMoving)
				{
					// System.out.println("set latest [moving]");
					//					rectangle.setLatestPosition();
				}
				else
				{
					if (startPos.x == endPos.x && startPos.y == endPos.y)
					{
						selectPoint(event.getX(), event.getY(), event.isControlDown());
					}
					else
					{
						selectArea(startPos.x, startPos.y, endPos.x, endPos.y, event.isControlDown());
					}

					selectedPane.getChildren().remove(rectangle);
				}
			}
			//			else
			//			{
			//				// System.out.println("set latest [else]");
			//				rectangle.setLatestPosition();
			//			}

			isDragging = false;
			rectangle = null;

			editorController.getToolbarController().focusToolbarButton();
		});
	}

	private void selectPoint(final double x, final double y, final boolean ctrlPressed)
	{
		if (!ctrlPressed)
		{
			canvasControl.deselectAll();
		}
		canvasControl.selectSingle(x, y);
	}

	private void selectArea(final double x1, final double y1, final double x2, final double y2,
			final boolean ctrlPressed)
	{
		final double topLeftX = Math.min(x1, x2);
		final double topLeftY = Math.min(y1, y2);
		final double width = Math.abs(x1 - x2);
		final double height = Math.abs(y1 - y2);

		final Rectangle area = new Rectangle(topLeftX, topLeftY, width, height);

		if (!ctrlPressed)
		{
			canvasControl.deselectAll();
		}
		canvasControl.selectArea(area);
	}

	private boolean selectedContainsMouse(final MouseEvent event)
	{
		for (final LayerRectangle layerRect : canvasControl.getSelectedObjects())
		{
			if (layerRect.getBoundsInParent().contains(event.getX(), event.getY()))
			{
				return true;
			}
		}
		return false;
	}

	public void setCanvasMode(final CanvasMode canvasMode)
	{
		this.canvasMode = canvasMode;
	}

	public void clearAll()
	{
		canvasControl.clearAll();
	}

	public void deleteSelected()
	{
		canvasControl.deleteSelected();
	}
}
