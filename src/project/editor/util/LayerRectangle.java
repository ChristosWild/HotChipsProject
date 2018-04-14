package project.editor.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import project.editor.util.EditorUtil.Delta;

public class LayerRectangle extends Rectangle
{
	// Only used for vias and contacts during circuit extraction
	private String name = "";

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

	/**
	 * Gets the rectangle of the intersecting area. Width > 0 AND height > 0 =
	 * overlap, else not overlapping
	 *
	 * @param rectTwo
	 * @return A rectangle of the intersecting area
	 */
	public Rectangle getIntersection(final LayerRectangle rectTwo)
	{
		// Algorithm from java.awt.Rectangle intersection method used

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

		double width = rectOneX2 - rectOneX1;

		double height = rectOneY2 - rectOneY1;

		if (width < Integer.MIN_VALUE)
		{
			width = Integer.MIN_VALUE;
		}
		if (height < Integer.MIN_VALUE)
		{
			height = Integer.MIN_VALUE;
		}

		// if (width > 0 && height > 0)
		// {
		// intersection = new Rectangle(rectOneX1, rectOneY1, width, height);
		// }
		//
		// return intersection;

		return new Rectangle(rectOneX1, rectOneY1, width, height);
	}

	/**
	 * Checks if this rect is contained by another
	 *
	 * @param layerRect
	 * @return true if this LayerRectangle is entirely contained by layerRect
	 */
	public boolean isContainedBy(final LayerRectangle layerRect)
	{
		final double rectOneX1 = this.getX() + this.getOffset().x;
		final double rectOneY1 = this.getY() + this.getOffset().y;
		final double rectOneX2 = rectOneX1 + this.getWidth();
		final double rectOneY2 = rectOneY1 + this.getHeight();

		final double rectTwoX1 = layerRect.getX() + layerRect.getOffset().x;
		final double rectTwoY1 = layerRect.getY() + layerRect.getOffset().y;
		final double rectTwoX2 = rectTwoX1 + layerRect.getWidth();
		final double rectTwoY2 = rectTwoY1 + layerRect.getHeight();

		if (rectOneX1 >= rectTwoX1
				&& rectOneY1 >= rectTwoY1
				&& rectOneX2 <= rectTwoX2
				&& rectOneY2 <= rectTwoY2)
		{
			return true;
		}

		return false;
	}

	public boolean isAdjacentTo(final LayerRectangle layerRect)
	{
		boolean isAdjacent = false;

		final Rectangle overlap = this.getIntersection(layerRect);
		if (overlap.getWidth() > 0 && overlap.getHeight() > 0)
		{
			isAdjacent = true;
		}
		else if (overlap.getWidth() == 0 && overlap.getHeight() > 0)
		{
			isAdjacent = true;
		}
		else if (overlap.getHeight() == 0 && overlap.getWidth() > 0)
		{
			isAdjacent = true;
		}

		return isAdjacent;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
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
