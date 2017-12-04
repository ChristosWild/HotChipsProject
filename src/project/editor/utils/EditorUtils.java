package project.editor.utils;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.stage.Window;

public class EditorUtils
{
	private static class Delta
	{
		public double x, y;
	}

	public static void makeWindowDraggableByNode(final Window window, final Node node) // TODO needed as a util method?
	{
		final Delta delta = new Delta();

		node.setOnMousePressed(event ->
		{
			node.setCursor(Cursor.MOVE);
			delta.x = window.getX() - event.getScreenX();
			delta.y = window.getY() - event.getScreenY();
		});

		node.setOnMouseReleased(event ->
		{
			node.setCursor(Cursor.HAND);
		});

		node.setOnMouseDragged(event ->
		{
			window.setX(event.getScreenX() + delta.x);
			window.setY(event.getScreenY() + delta.y);
		});

		node.setOnMouseEntered(event ->
		{
			if (!event.isPrimaryButtonDown())
			{
				node.setCursor(Cursor.HAND);
			}
		});

		node.setOnMouseExited(event ->
		{
			if (!event.isPrimaryButtonDown())
			{
				node.setCursor(Cursor.DEFAULT);
			}
		});
	}
}
