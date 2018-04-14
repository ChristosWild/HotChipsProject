package project.editor.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
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
import project.editor.extractor.util.SpiceUtil;
import project.editor.util.EditorConstants;
import project.editor.util.EditorUtil;
import project.editor.util.EditorUtil.Delta;
import project.editor.util.Layer;
import project.editor.util.LayerRectangle;

public class CanvasController
{
	public enum CanvasMode
	{
		SELECT, DRAW;
	}

	private static final double ZOOM_INCREMENT = 1.2;
	private static final String LAMBDA = "\u03BB";

	private EditorController editorController;
	private CanvasControl canvasControl;

	private CanvasMode canvasMode;
	private Layer layer;
	private Pane selectedPane;
	private LayerRectangle rectangle;
	private Tooltip tooltip;

	private ContextMenu menuVia;
	private ContextMenu menuPin;
	private MenuItem setSource;
	private MenuItem setDrain;
	private MenuItem setPinName;
	private TextInputDialog pinNameDialog;

	private Delta startPos;
	private boolean isDragging;
	private boolean isCtrlPressed;
	private boolean isPrimary;
	private boolean isSelected;
	private boolean isMoving;

	private double minSelectedX;
	private double minSelectedY;
	private double maxSelectedX;
	private double maxSelectedY;

	private double zoomScale = 1;

	public CanvasController(final EditorController editorController)
	{
		this.editorController = editorController;
		canvasControl = new CanvasControl();
		setCanvasMode(CanvasMode.SELECT);
	}

	public void createPartControl(final BorderPane root)
	{
		canvasControl.createPartControl(root);

		setSource = new MenuItem("Set as transistor source");
		setDrain = new MenuItem("Set as transistor drain");
		setPinName = new MenuItem("Set pin name");

		menuVia = new ContextMenu(setSource, setDrain);
		menuPin = new ContextMenu(setPinName);// TODO name as vdd and ground

		pinNameDialog = new TextInputDialog();
		pinNameDialog.setTitle("Pin");
		pinNameDialog.setContentText("Name:");
		pinNameDialog.getEditor().setPromptText("Name...");
		pinNameDialog.setHeaderText(null);
		pinNameDialog.setGraphic(null);

		setUpListeners(root);
	}

	private void setUpListeners(final BorderPane root)
	{
		setSource.setOnAction(e -> {
			final LayerRectangle via = (LayerRectangle) ((MenuItem) e.getSource()).getParentPopup().getOwnerNode();
			via.setName(SpiceUtil.TRANSISTOR_NAME_SOURCE);
		});
		setDrain.setOnAction(e -> {
			final LayerRectangle via = (LayerRectangle) ((MenuItem) e.getSource()).getParentPopup().getOwnerNode();
			via.setName(SpiceUtil.TRANSISTOR_NAME_DRAIN);
		});
		setPinName.setOnAction(e -> {
			final LayerRectangle pin = (LayerRectangle) ((MenuItem) e.getSource()).getParentPopup().getOwnerNode();
			pinNameDialog.getEditor().setText(pin.getName());
			pinNameDialog.getEditor().selectAll();
			pinNameDialog.showAndWait().ifPresent(name -> pin.setName(name));
		});

		canvasControl.getSelectionPane().addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {

			isDragging = true;
			isCtrlPressed = event.isShortcutDown();
			isPrimary = event.isPrimaryButtonDown();

			startPos = new Delta();
			startPos.x = event.getX();
			startPos.y = event.getY();
			EditorUtil.snapToGrid(startPos);

			Paint fill = null;
			Paint fillSelected = null;

			if (isPrimary)
			{
				if (canvasMode == CanvasMode.DRAW)
				{
					canvasControl.deselectAll();
					isSelected = false;
					isMoving = false;

					layer = canvasControl.getCurrentPaneLayer();
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
					layer = Layer.INVALID_LAYER;
					selectedPane = canvasControl.getSelectionPane();
					fill = Color.web(Color.GREY.toString(), 0.4); // TODO pick selection box colour

					isSelected = canvasControl.getSelectedObjects().size() > 0;
					isMoving = isSelected && selectedContainsMouse(event);

					updateSelectedBounds();
				}

				if(fill instanceof Color)
				{
					rectangle = new LayerRectangle(startPos.x, startPos.y, 0, 0, (Color) fill, layer);
				}
				else
				{
					rectangle = new LayerRectangle(startPos.x, startPos.y, 0, 0, fill, fillSelected, layer);
				}

				selectedPane.getChildren().add(rectangle);

				tooltip = new Tooltip();
				Tooltip.install(rectangle, tooltip);
			}
		});

		canvasControl.getSelectionPane().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {

			if (isDragging)
			{
				if (isPrimary)
				{
					final Delta dragPos = new Delta(event.getX(), event.getY());
					EditorUtil.snapToGrid(dragPos);
					double deltaX = dragPos.x - startPos.x;
					double deltaY = dragPos.y - startPos.y;

					if (isMoving && !isCtrlPressed)
					{
						if (movementPossibleX(deltaX))
						{
							for (final LayerRectangle layerRect : canvasControl.getSelectedObjects())
							{
								layerRect.setTranslateX(layerRect.getOffset().x + deltaX);
							}
						}

						if (movementPossibleY(deltaY))
						{
							for (final LayerRectangle layerRect : canvasControl.getSelectedObjects())
							{
								layerRect.setTranslateY(layerRect.getOffset().y + deltaY);
							}
						}
					}
					else
					{
						if (deltaX >= 0) // TODO check if move possible to fix out of bounds tooltip issue
						{
							if (rectangle.getX() + deltaX <= EditorConstants.CANVAS_WIDTH)
							{
								rectangle.setTranslateX(0);
								rectangle.setWidth(deltaX);
							}
						}
						else if (rectangle.getX() + deltaX >= 0)
						{
							rectangle.setTranslateX(deltaX);
							rectangle.setWidth(-deltaX);
						}

						if (deltaY >= 0)
						{
							if (rectangle.getY() + deltaY <= EditorConstants.CANVAS_HEIGHT)
							{
								rectangle.setTranslateY(0);
								rectangle.setHeight(deltaY);
							}
						}
						else if (rectangle.getY() + deltaY >= 0)
						{
							rectangle.setTranslateY(deltaY);
							rectangle.setHeight(-deltaY);
						}

						final Scene scene = rectangle.getScene();
						final Point2D point = rectangle.localToScene(0, 0);

						tooltip.setText((int) (Math.abs(deltaX) / EditorConstants.CANVAS_GRID_SIZE) + LAMBDA + " x "
								+ (int) (Math.abs(deltaY) / EditorConstants.CANVAS_GRID_SIZE) + LAMBDA); // TODO fix tooltip for backwards rects
						tooltip.setX(scene.getWindow().getX() + scene.getX() + point.getX() + dragPos.x);
						tooltip.setY(scene.getWindow().getY() + scene.getY() + point.getY() + dragPos.y);
						tooltip.show(rectangle.getScene().getWindow());
					}
				}
			}
		});

		canvasControl.getSelectionPane().addEventHandler(MouseEvent.MOUSE_RELEASED, event -> { // TODO investigate swapping to select mode by pressing S key while dragging in draw mode

			final Delta endPos = new Delta(event.getX(), event.getY());
			EditorUtil.snapToGrid(endPos);

			if (isPrimary)
			{
				tooltip.hide();
				Tooltip.uninstall(rectangle, tooltip);

				if (canvasMode == CanvasMode.DRAW)
				{
					if (rectangle.getWidth() == 0 || rectangle.getHeight() == 0)
					{
						selectedPane.getChildren().remove(rectangle);
					}
					else
					{
						rectangle.setOffset(rectangle.getTranslateX(), rectangle.getTranslateY());

						// Check if selected pane already has the same rectangle added
						if (selectedPane.getChildren().indexOf(rectangle) != selectedPane.getChildren()
								.lastIndexOf(rectangle))
						{
							selectedPane.getChildren().remove(rectangle);
						}
					}
				}
				else
				{
					if (isMoving)
					{
						for (final LayerRectangle layerRect : canvasControl.getSelectedObjects())
						{
							layerRect.setOffset(layerRect.getTranslateX(), layerRect.getTranslateY());
						}
					}

					if (!isMoving || isCtrlPressed)
					{
						if (startPos.x == endPos.x && startPos.y == endPos.y)
						{
							selectPoint(event.getX(), event.getY(), isCtrlPressed);
						}
						else
						{
							selectArea(startPos.x, startPos.y, endPos.x, endPos.y, isCtrlPressed);
						}
					}
					selectedPane.getChildren().remove(rectangle);
				}
			}
			else
			{
				// Brings up right click menu for top via or pin
				for (final LayerRectangle via : getLayerRectangles(Layer.VIA))
				{
					if (via.getBoundsInParent().contains(event.getX(), event.getY()))
					{
						menuVia.show(via, event.getScreenX(), event.getScreenY());
					}
				}

				for (final LayerRectangle pin : getLayerRectangles(Layer.PIN))
				{
					if (pin.getBoundsInParent().contains(event.getX(), event.getY()))
					{
						menuPin.show(pin, event.getScreenX(), event.getScreenY());
					}
				}
			}

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

	private void updateSelectedBounds()
	{
		minSelectedX = EditorConstants.CANVAS_WIDTH;
		minSelectedY = EditorConstants.CANVAS_HEIGHT;
		maxSelectedX = 0;
		maxSelectedY = 0;

		for (final LayerRectangle layerRect : canvasControl.getSelectedObjects())
		{
			minSelectedX = Math.min(minSelectedX, layerRect.getBoundsInParent().getMinX());
			minSelectedY = Math.min(minSelectedY, layerRect.getBoundsInParent().getMinY());
			maxSelectedX = Math.max(maxSelectedX, layerRect.getBoundsInParent().getMaxX());
			maxSelectedY = Math.max(maxSelectedY, layerRect.getBoundsInParent().getMaxY());
		}
	}

	private boolean movementPossibleX(final double deltaX)
	{
		boolean movementPossible = true;

		if (minSelectedX + deltaX < 0 || maxSelectedX + deltaX > EditorConstants.CANVAS_WIDTH)
		{
			movementPossible = false;
		}

		return movementPossible;
	}

	private boolean movementPossibleY(final double deltaY)
	{
		boolean movementPossible = true;

		if (minSelectedY + deltaY < 0 || maxSelectedY + deltaY > EditorConstants.CANVAS_HEIGHT)
		{
			movementPossible = false;
		}

		return movementPossible;
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

	public void cut()
	{
		copy();
		deleteSelected();
	}

	public void copy()
	{
		ClipboardController.getInstance().setCurrentItems(canvasControl.getSelectedObjects());
	}

	public void paste() // TODO Display nothing to copy / paste message?
	{
		final List<LayerRectangle> rectsToPaste = ClipboardController.getInstance().getCurrentItems();
		final boolean pasteValid = rectsToPaste.size() > 0;

		if (pasteValid)
		{
			canvasControl.deselectAll();

			for (final LayerRectangle layerRect : rectsToPaste)
			{
				final LayerRectangle clonedRect = layerRect.clone();
				canvasControl.addLayerRectangle(clonedRect);
				canvasControl.addSelectedObject(clonedRect);
			}

			editorController.getToolbarController().selectModeSelect();
		}

		isSelected = pasteValid ? true : isSelected;
	}

	public void zoomIn()
	{
		zoomScale *= ZOOM_INCREMENT;
		canvasControl.zoom(zoomScale);
	}

	public void zoomOut()
	{
		zoomScale *= 1.0 / ZOOM_INCREMENT;
		canvasControl.zoom(zoomScale);
	}

	public void zoomReset()
	{
		zoomScale = 1;
		canvasControl.zoom(zoomScale);
	}

	public List<ArrayList<LayerRectangle>> getAllLayerRectangles()
	{
		return canvasControl.getAllLayerRectangles();
	}

	public List<LayerRectangle> getLayerRectangles(final Layer layer)
	{
		return canvasControl.getLayerRectangles(layer);
	}

	public void addNewRectangle(final LayerRectangle layerRectangle)
	{
		canvasControl.addLayerRectangle(layerRectangle);
	}
}
