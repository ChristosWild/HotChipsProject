package project.editor.control;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import project.editor.util.EditorConstants;
import project.editor.util.Layer;
import project.editor.util.LayerRectangle;

public class CanvasControl
{
	private Pane canvasDiffusionN;
	private Pane canvasDiffusionP;
	private Pane canvasPolysilicon;
	private Pane canvasMetalOne;
	private Pane canvasMetalTwo;
	private Pane canvasMetalThree;
	private Pane canvasMetalFour;
	private Pane canvasMetalFive;
	private Pane canvasVia;
	private Pane canvasPin;
	private Pane[] canvasArray;
	private Pane gridLayer;
	private Pane selectionLayer;

	private List<LayerRectangle> selectedObjects = new ArrayList<LayerRectangle>();

	public void createPartControl(final BorderPane root)
	{
		final Group canvasGroup = new Group();
		final ScrollPane scrollPane = new ScrollPane(canvasGroup);

		gridLayer = new Pane();
		gridLayer.setBackground(new Background(new BackgroundImage(
				new Image(EditorConstants.PATH_FILE_DATA + EditorConstants.PATH_IMG_GRID), BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		gridLayer.setPrefSize(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);

		canvasDiffusionN = new Pane();
		canvasDiffusionP = new Pane();
		canvasPolysilicon = new Pane();
		canvasMetalOne = new Pane();
		canvasMetalTwo = new Pane();
		canvasMetalThree = new Pane();
		canvasMetalFour = new Pane();
		canvasMetalFive = new Pane();
		canvasVia = new Pane();
		canvasPin = new Pane();
		canvasArray = new Pane[] { canvasDiffusionN, canvasDiffusionP, canvasPolysilicon, canvasMetalOne,
				canvasMetalTwo, canvasMetalThree, canvasMetalFour, canvasMetalFive, canvasVia, canvasPin };

		for (final Pane pane : canvasArray)
		{
			pane.setPrefSize(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);
		}

		selectionLayer = new Pane();
		selectionLayer.setPrefSize(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);

		canvasGroup.getChildren().add(gridLayer);
		canvasGroup.getChildren().addAll(canvasArray);
		canvasGroup.getChildren().add(selectionLayer);
		root.setCenter(scrollPane);
	}

	public Pane getCurrentPane()
	{
		return canvasArray[SelectorControl.getInstance().getSelectedLayer().getLayerIndex()];
	}

	public Layer getCurrentPaneLayer()
	{
		return SelectorControl.getInstance().getSelectedLayer();
	}

	public Color getCurrentPaneColor()
	{
		return SelectorControl.getInstance().getSelectedLayer().getColor();
	}

	public Pane getSelectionPane()
	{
		return selectionLayer;
	}

	public void clearAll()
	{
		deselectAll();
		for (final Pane pane : canvasArray)
		{
			pane.getChildren().clear();
		}
	}

	public void deleteSelected() // TODO iterate through list of selected?
	{
		final List<LayerRectangle> toDelete = new ArrayList<LayerRectangle>();
		for (final Pane pane : canvasArray)
		{
			for (final Node layerRect : pane.getChildren())
			{
				if (layerRect instanceof LayerRectangle && ((LayerRectangle) layerRect).isSelected())
				{
					toDelete.add((LayerRectangle) layerRect);
				}
			}
			pane.getChildren().removeAll(toDelete);
			selectedObjects.removeAll(toDelete);
			toDelete.clear();
		}

	}

	public void deselectAll()
	{
		for (final LayerRectangle layerRect : selectedObjects)
		{
			layerRect.setSelected(false);
		}
		selectedObjects.clear();
	}

	public void selectAll()
	{
		for (final ArrayList<LayerRectangle> layerRects : getAllLayerRectangles())
		{
			for (final LayerRectangle layerRect : layerRects)
			{
				layerRect.setSelected(true);
				selectedObjects.add(layerRect);
			}
		}
	}

	public void selectSingle(final double x, final double y)
	{
		for (final Pane pane : canvasArray)
		{
			for (final Node layerRect : pane.getChildren())
			{
				if (layerRect instanceof LayerRectangle
						&& ((LayerRectangle) layerRect).getBoundsInParent().contains(x, y))
				{
					((LayerRectangle) layerRect).setSelected(true);
					selectedObjects.add((LayerRectangle) layerRect);
				}
			}
		}
	}

	public void selectArea(final Rectangle area)
	{
		for (final Pane pane : canvasArray)
		{
			for (final Node layerRect : pane.getChildren())
			{
				if (layerRect instanceof LayerRectangle
						&& area.contains(((LayerRectangle) layerRect).getBoundsInParent().getMinX(),
								((LayerRectangle) layerRect).getBoundsInParent().getMinY()))
				{
					((LayerRectangle) layerRect).setSelected(true);
					selectedObjects.add((LayerRectangle) layerRect);
				}
			}
		}
	}

	public void addSelectedObject(final LayerRectangle selectedObject)
	{
		selectedObjects.add(selectedObject);
	}

	public List<LayerRectangle> getSelectedObjects()
	{
		return selectedObjects;
	}

	public void zoom(final double zoomScale)
	{
		gridLayer.setScaleX(zoomScale);
		gridLayer.setScaleY(zoomScale);

		selectionLayer.setScaleX(zoomScale);
		selectionLayer.setScaleY(zoomScale);

		for (final Pane pane : canvasArray)
		{
			pane.setScaleX(zoomScale);
			pane.setScaleY(zoomScale);
		}
	}

	public void addLayerRectangle(final LayerRectangle layerRect)
	{
		canvasArray[layerRect.getLayer().getLayerIndex()].getChildren().add(layerRect);
	}

	public List<ArrayList<LayerRectangle>> getAllLayerRectangles()
	{

		final List<ArrayList<LayerRectangle>> layerRects = new ArrayList<ArrayList<LayerRectangle>>();

		for (final Pane canvas : canvasArray)
		{
			final ArrayList<LayerRectangle> layerChildren = new ArrayList<LayerRectangle>();
			for (final Node node : canvas.getChildren())
			{
				if (node instanceof LayerRectangle)
				{
					layerChildren.add((LayerRectangle) node);
				}
			}

			layerRects.add(layerChildren);
		}

		return layerRects;
	}

	public List<LayerRectangle> getLayerRectangles(final Layer layer)
	{
		final List<LayerRectangle> layerRects = new ArrayList<LayerRectangle>();

		for (final Pane canvas : canvasArray)
		{
			for (final Node node : canvas.getChildren())
			{
				if (node instanceof LayerRectangle)
				{
					final LayerRectangle layerRect = (LayerRectangle) node;
					if (layerRect.getLayer() == layer)
					{
						layerRects.add(layerRect);
					}
				}
			}
		}

		return layerRects;
	}
}