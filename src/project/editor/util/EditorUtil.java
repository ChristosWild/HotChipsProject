package project.editor.util;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.stage.Window;

public final class EditorUtil
{
	private EditorUtil() {};
	
	public static class Delta
	{
		public double x, y;

		public Delta()
		{
		}

		public Delta(final double x, final double y)
		{
			this.x = x;
			this.y = y;
		}
	}

	public static void makeWindowDraggableByNode(final Window window, final Node node)
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

	public static Delta snapToGrid(final Delta delta)
	{
		final int gridSize = EditorConstants.CANVAS_GRID_SIZE;
		final int halfGridSize = EditorConstants.CANVAS_GRID_SIZE / 2;

		if(delta.x >= 0)
		{
			delta.x = (long) ((delta.x + halfGridSize) / gridSize) * gridSize;
		}
		else
		{
			delta.x = (long) ((delta.x - halfGridSize) / gridSize) * gridSize;
		}

		if(delta.y >= 0)
		{
			delta.y = (long) ((delta.y + halfGridSize) / gridSize) * gridSize;
		}
		else
		{
			delta.y = (long) ((delta.y - halfGridSize) / gridSize) * gridSize;
		}

		return delta;
	}
}