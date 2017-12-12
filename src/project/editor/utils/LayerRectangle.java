package project.editor.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class LayerRectangle extends Rectangle
{
	private BooleanProperty isSelected;
	private final Paint fill;
	private final Paint fillSelected;

	public LayerRectangle(final double x, final double y, final double width, final double height, final Paint fill)
	{
		super(x, y, width, height);

		if (fill instanceof ImagePattern)
		{
			this.fill = fill;// TODO different image for selected
			this.fillSelected = fill;
		}
		else
		{
			this.fill = Color.web(fill.toString(), EditorConstants.COLOR_OPACITY);
			this.fillSelected = Color.web(fill.toString(), 0.8);
		}

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
}
