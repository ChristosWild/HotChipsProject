package project.editor.control;

import javafx.scene.Group;
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
import project.editor.utils.EditorConstants;

public class CanvasControl
{
	private Pane canvasMetalOne;
	private Pane canvasMetalTwo;
	private Pane canvasMetalThree;
	private Pane canvasMetalFour;
	private Pane canvasMetalFive;
	private Pane canvasDiffusionN;
	private Pane canvasDiffusionP;
	private Pane canvasPolysilicon;
	private Pane canvasVia;
	private Pane canvasPin;
	private Pane[] canvasArray;
	private Pane selectionLayer;

	public void createPartControl(final BorderPane root)
	{
		final Group canvasGroup = new Group();
		final ScrollPane scrollPane = new ScrollPane(canvasGroup);

		final Pane gridLayer = new Pane();
		gridLayer.setBackground(new Background(new BackgroundImage(
				new Image(EditorConstants.FILE_PATH_DATA + EditorConstants.IMG_PATH_GRID), BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		gridLayer.setPrefSize(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);

		canvasMetalOne = new Pane();
		canvasMetalTwo = new Pane();
		canvasMetalThree = new Pane();
		canvasMetalFour = new Pane();
		canvasMetalFive = new Pane();
		canvasDiffusionN = new Pane();
		canvasDiffusionP = new Pane();
		canvasPolysilicon = new Pane();
		canvasVia = new Pane();
		canvasPin = new Pane();
		canvasArray = new Pane[] { canvasMetalOne, canvasMetalTwo, canvasMetalThree, canvasMetalFour,
				canvasMetalFive, canvasDiffusionN, canvasDiffusionP, canvasPolysilicon, canvasVia, canvasPin };

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

	public Color getCurrentPaneColor()
	{
		return SelectorControl.getInstance().getSelectedLayer().getColor();
	}

	public Pane getSelectionPane()
	{
		return selectionLayer;
	}
}