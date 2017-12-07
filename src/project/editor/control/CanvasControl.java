package project.editor.control;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
	private Canvas canvasMetalOne;
	private Canvas canvasMetalTwo;
	private Canvas canvasMetalThree;
	private Canvas canvasMetalFour;
	private Canvas canvasMetalFive;
	private Canvas canvasDiffusionN;
	private Canvas canvasDiffusionP;
	private Canvas canvasPolysilicon;
	private Canvas canvasVia;
	private Canvas canvasPin;
	private Canvas[] canvasArray;

	private GraphicsContext gcMetalOne;
	private GraphicsContext gcMetalTwo;
	private GraphicsContext gcMetalThree;
	private GraphicsContext gcMetalFour;
	private GraphicsContext gcMetalFive;
	private GraphicsContext gcDiffusionN;
	private GraphicsContext gcDiffusionP;
	private GraphicsContext gcPolysilicon;
	private GraphicsContext gcVia;
	private GraphicsContext gcPin;
	private GraphicsContext[] gcArray;

	public void createPartControl(final BorderPane root)
	{
		final Group canvasGroup = new Group();
		final ScrollPane scrollPane = new ScrollPane(canvasGroup);

		final Pane grid = new Pane();
		grid.setBackground(new Background(new BackgroundImage(new Image("file:data/grid.png"), BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		grid.setPrefSize(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);

		canvasMetalOne = new Canvas(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);
		canvasMetalTwo = new Canvas(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);
		canvasMetalThree = new Canvas(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);
		canvasMetalFour = new Canvas(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);
		canvasMetalFive = new Canvas(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);
		canvasDiffusionN = new Canvas(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);
		canvasDiffusionP = new Canvas(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);
		canvasPolysilicon = new Canvas(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);
		canvasVia = new Canvas(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);
		canvasPin = new Canvas(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);
		canvasArray = new Canvas[] { canvasMetalOne, canvasMetalTwo, canvasMetalThree, canvasMetalFour,
				canvasMetalFive, canvasDiffusionN, canvasDiffusionP, canvasPolysilicon, canvasVia, canvasPin };

		gcMetalOne = canvasMetalOne.getGraphicsContext2D();
		gcMetalTwo = canvasMetalTwo.getGraphicsContext2D();
		gcMetalThree = canvasMetalThree.getGraphicsContext2D();
		gcMetalFour = canvasMetalFour.getGraphicsContext2D();
		gcMetalFive = canvasMetalFive.getGraphicsContext2D();
		gcDiffusionN = canvasDiffusionN.getGraphicsContext2D();
		gcDiffusionP = canvasDiffusionP.getGraphicsContext2D();
		gcPolysilicon = canvasPolysilicon.getGraphicsContext2D();
		gcVia = canvasVia.getGraphicsContext2D();
		gcPin = canvasPin.getGraphicsContext2D();
		gcArray = new GraphicsContext[] { gcMetalOne, gcMetalTwo, gcMetalThree, gcMetalFour, gcMetalFive, gcDiffusionN,
				gcDiffusionP, gcPolysilicon, gcVia, gcPin };

		gcMetalOne.setFill(Color.RED); // TODO colours + caching colours
		gcMetalTwo.setFill(Color.ORANGE);
		gcMetalThree.setFill(Color.YELLOW);
		gcMetalFour.setFill(Color.CHARTREUSE);
		gcMetalFive.setFill(Color.GREEN);
		gcDiffusionN.setFill(Color.AQUA);
		gcDiffusionP.setFill(Color.BLUE);
		gcPolysilicon.setFill(Color.INDIGO);
		gcVia.setFill(Color.VIOLET);
		gcPin.setFill(Color.BLACK);

		gcMetalOne.setStroke(Color.RED);
		gcMetalOne.setLineWidth(5);

		canvasGroup.getChildren().add(grid);
		canvasGroup.getChildren().addAll(canvasArray);
		root.setCenter(scrollPane);
	}

	public GraphicsContext getGcMetalOne()
	{
		return gcMetalOne;
	}
}
