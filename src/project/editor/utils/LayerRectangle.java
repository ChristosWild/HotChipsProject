package project.editor.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import project.editor.utils.EditorUtils.Delta;

public class LayerRectangle extends Rectangle
{
	private BooleanProperty isSelected;
	private final Paint fill;
	private final Paint fillSelected;
	private Delta latestPosition;

	public LayerRectangle(final double x, final double y, final double width, final double height, final Paint fill,
			final Paint fillSelected)
	{
		super(x, y, width, height);

		this.fill = fill;
		this.fillSelected = fillSelected;

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

		latestPosition = new Delta(x, y);
	}

	public LayerRectangle(final double x, final double y, final double width, final double height, final Color color)
	{
		this(x, y, width, height, Color.web(color.toString(), EditorConstants.COLOR_OPACITY),
				Color.web(color.toString(), EditorConstants.COLOR_OPACITY_SELECTED));
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

	public void setLatestPosition()
	{
		latestPosition.x = this.getBoundsInParent().getMinX();
		latestPosition.y = this.getBoundsInParent().getMinY();

		System.out.println(latestPosition.x);
		System.out.println(latestPosition.y);
	}

	public Delta getLatestPosition()
	{
		return latestPosition;
	}
}
