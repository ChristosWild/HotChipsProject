package project.editor.control;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import project.editor.utils.EditorConstants;

public class CanvasControl
{
	public void createPartControl(final BorderPane root)
	{
		final Group canvasGroup = new Group();
		final ScrollPane scrollPane = new ScrollPane(canvasGroup);

		final Canvas canvas1 = new Canvas(EditorConstants.CANVAS_WIDTH, EditorConstants.CANVAS_HEIGHT);
		//		final Canvas canvas2 = new Canvas(300, 300);
		//		final Canvas canvas3 = new Canvas(300, 300);

		final GraphicsContext gc1 = canvas1.getGraphicsContext2D();
		//		final GraphicsContext gc2 = canvas2.getGraphicsContext2D();
		// final GraphicsContext gc3 = canvas3.getGraphicsContext2D();

		gc1.setFill(Color.RED);
		//		gc2.setFill(Color.GREEN);
		// gc3.setFill(Color.BLUE);

		gc1.fillRect(0, 0, 300, 300);
		//		gc2.fillRect(0, 0, 200, 100);
		// gc3.fillRect(0, 0, 100, 100);

		canvasGroup.getChildren().addAll(canvas1);// , canvas2, canvas3);
		root.setCenter(scrollPane);
	}
}
