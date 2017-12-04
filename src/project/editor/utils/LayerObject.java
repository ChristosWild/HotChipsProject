package project.editor.utils;

import java.awt.Rectangle;

public class LayerObject
{
	private final Layer layer;
	private final Rectangle rectangle;

	public LayerObject(final Layer layer, final Rectangle rectangle)
	{
		this.layer = layer;
		this.rectangle = rectangle;
	}
}
