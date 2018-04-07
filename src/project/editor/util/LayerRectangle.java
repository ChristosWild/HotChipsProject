package project.editor.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import project.editor.util.EditorUtil.Delta;

public class LayerRectangle extends Rectangle
{
	private Layer layer;
	private BooleanProperty isSelected;
	private final Paint fill;
	private final Paint fillSelected;
	private Delta offset;

	public LayerRectangle(final double x, final double y, final double width, final double height, final Color color,
			final Layer layer)
	{
		this(x, y, width, height, Color.web(color.toString(), EditorConstants.COLOR_OPACITY),
				Color.web(color.toString(), EditorConstants.COLOR_OPACITY_SELECTED), layer);
	}

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

	@Override
	public boolean equals(final Object obj)
	{
		if (!(obj instanceof LayerRectangle))
		{
			return false;
		}
		else
		{
			final LayerRectangle layerRect = (LayerRectangle) obj;

			// Equal if same top left coordinates, width, and height
			if (this.getX() + this.getOffset().x == layerRect.getX() + layerRect.getOffset().x
					&& this.getY() + this.getOffset().y == layerRect.getY() + layerRect.getOffset().y
					&& this.getWidth() == layerRect.getWidth()
					&& this.getHeight() == layerRect.getHeight())
			{
				return true;
			}
		}

		return false;
	}

	public Rectangle getIntersection(final LayerRectangle rectTwo)
	{
		// Algorithm from java.awt.Rectangle intersection used

		// Top left = (x1,y1), bottom right = (x2, y2)
		double rectOneX1 = this.getX() + this.getOffset().x;
		double rectOneY1 = this.getY() + this.getOffset().y;
		double rectTwoX1 = rectTwo.getX() + rectTwo.getOffset().x;
		double rectTwoY1 = rectTwo.getY() + rectTwo.getOffset().y;

		double rectOneX2 = rectOneX1;
		rectOneX2 += this.getWidth();

		double rectOneY2 = rectOneY1;
		rectOneY2 += this.getHeight();

		double rectTwoX2 = rectTwoX1;
		rectTwoX2 += rectTwo.getWidth();

		double rectTwoY2 = rectTwoY1;
		rectTwoY2 += rectTwo.getHeight();

		if (rectOneX1 < rectTwoX1)
		{
			rectOneX1 = rectTwoX1;
		}
		if (rectOneY1 < rectTwoY1)
		{
			rectOneY1 = rectTwoY1;
		}
		if (rectOneX2 > rectTwoX2)
		{
			rectOneX2 = rectTwoX2;
		}
		if (rectOneY2 > rectTwoY2)
		{
			rectOneY2 = rectTwoY2;
		}

		double width = rectOneX2 - rectOneX1; // width

		double height = rectOneY2 - rectOneY1;

		if (width < Integer.MIN_VALUE)
		{
			width = Integer.MIN_VALUE;
		}
		if (height < Integer.MIN_VALUE)
		{
			height = Integer.MIN_VALUE;
		}

		if (width < 0)
		{
			width = 0;
		}

		if (height < 0)
		{
			height = 0;
		}

		return new Rectangle(rectOneX1, rectOneY1, width, height);
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
