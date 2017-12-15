package project.editor.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import project.editor.utils.EditorUtils.Delta;

public class LayerRectangle extends Rectangle
{
	private Pane parentPane;
	private BooleanProperty isSelected;
	private final Paint fill;
	private final Paint fillSelected;
	private Delta offset;

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

		offset = new Delta(0, 0);
	}

	public LayerRectangle(final double x, final double y, final double width, final double height, final Color color)
	{
		this(x, y, width, height, Color.web(color.toString(), EditorConstants.COLOR_OPACITY),
				Color.web(color.toString(), EditorConstants.COLOR_OPACITY_SELECTED));
	}

	@Override
	public LayerRectangle clone()
	{
		final LayerRectangle newLayerRect = new LayerRectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight(), fill,
				fillSelected); // TODO check if x and y are correct after dragging and moving
		newLayerRect.parentPane = this.parentPane == null ? (Pane) this.getParent() : parentPane;
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

	public Pane getParentPane()
	{
		return parentPane;
	}
}
