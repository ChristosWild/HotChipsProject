package project.editor.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import project.editor.utils.EditorUtils.Delta;

public class LayerRectangle extends Rectangle
{
	private Layer layer;
	private BooleanProperty isSelected;
	private final Paint fill;
	private final Paint fillSelected;
	private Delta offset;

	public LayerRectangle(final double x, final double y, final double width, final double height, final Paint fill,
			final Paint fillSelected, final Layer layer)
	{
		super(x, y, width, height);

		this.fill = fill;
		this.fillSelected = fillSelected;
		this.layer = layer;

		setFill(this.fill);

		isSelected = new SimpleBooleanProperty(false);
		isSelected.addListener((ov, oldVal, newVal) -> {
			if (newVal)
			{
				this.setFill(this.fillSelected);
			}
			else
			{
				this.setFill(this.fill);
			}
		});

		offset = new Delta(0, 0);
	}

	public LayerRectangle(final double x, final double y, final double width, final double height, final Color color,
			final Layer layer)
	{
		this(x, y, width, height, Color.web(color.toString(), EditorConstants.COLOR_OPACITY),
				Color.web(color.toString(), EditorConstants.COLOR_OPACITY_SELECTED), layer);
	}

	@Override
	public LayerRectangle clone()
	{
		final LayerRectangle newLayerRect = new LayerRectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight(), fill,
				fillSelected, layer);
		newLayerRect.setSelected(true);
		newLayerRect.offset = new Delta(this.offset.x, this.offset.y);
		newLayerRect.setTranslateX(newLayerRect.offset.x);
		newLayerRect.setTranslateY(newLayerRect.offset.y);

		return newLayerRect;
	}

	public boolean isSelected()
	{
		return isSelected.get();
	}

	public void setSelected(final boolean isSelected)
	{
		this.isSelected.set(isSelected);
	}

	public BooleanProperty getSelectedProperty()
	{
		return isSelected;
	}

	public void setOffset(final double x, final double y)
	{
		offset.x = x;
		offset.y = y;
	}

	public Delta getOffset()
	{
		return offset;
	}

	public Layer getLayer()
	{
		return layer;
	}
}
