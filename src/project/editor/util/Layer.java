package project.editor.util;

import static project.editor.util.EditorConstants.COLOR_DIFFUSION_N;
import static project.editor.util.EditorConstants.COLOR_DIFFUSION_P;
import static project.editor.util.EditorConstants.COLOR_INVALID;
import static project.editor.util.EditorConstants.COLOR_METAL_FIVE;
import static project.editor.util.EditorConstants.COLOR_METAL_FOUR;
import static project.editor.util.EditorConstants.COLOR_METAL_ONE;
import static project.editor.util.EditorConstants.COLOR_METAL_THREE;
import static project.editor.util.EditorConstants.COLOR_METAL_TWO;
import static project.editor.util.EditorConstants.COLOR_PIN;
import static project.editor.util.EditorConstants.COLOR_POLYSILICON;
import static project.editor.util.EditorConstants.COLOR_VIA;
import static project.editor.util.EditorConstants.STRING_DIFFUSION_N;
import static project.editor.util.EditorConstants.STRING_DIFFUSION_P;
import static project.editor.util.EditorConstants.STRING_INVALID;
import static project.editor.util.EditorConstants.STRING_METAL_FIVE;
import static project.editor.util.EditorConstants.STRING_METAL_FOUR;
import static project.editor.util.EditorConstants.STRING_METAL_ONE;
import static project.editor.util.EditorConstants.STRING_METAL_THREE;
import static project.editor.util.EditorConstants.STRING_METAL_TWO;
import static project.editor.util.EditorConstants.STRING_PIN;
import static project.editor.util.EditorConstants.STRING_POLYSILICON;
import static project.editor.util.EditorConstants.STRING_VIA;

import javafx.scene.paint.Color;

public enum Layer
{
	METAL_ONE(0, STRING_METAL_ONE, COLOR_METAL_ONE),
	METAL_TWO(1, STRING_METAL_TWO, COLOR_METAL_TWO),
	METAL_THREE(2, STRING_METAL_THREE, COLOR_METAL_THREE),
	METAL_FOUR(3, STRING_METAL_FOUR, COLOR_METAL_FOUR),
	METAL_FIVE(4, STRING_METAL_FIVE, COLOR_METAL_FIVE),
	DIFFUSION_N(5, STRING_DIFFUSION_N, COLOR_DIFFUSION_N),
	DIFFUSION_P(6, STRING_DIFFUSION_P, COLOR_DIFFUSION_P),
	POLYSILICON(7, STRING_POLYSILICON, COLOR_POLYSILICON),
	VIA(8, STRING_VIA, COLOR_VIA),
	PIN(9, STRING_PIN, COLOR_PIN),
	INVALID_LAYER(10, STRING_INVALID, COLOR_INVALID);

	private final int layerIndex;
	private final String displayName;
	private final Color color;

	Layer(final int layerIndex, final String displayName, final Color color)
	{
		this.layerIndex=layerIndex;
		this.displayName = displayName;
		this.color = color;
	}

	public int getLayerIndex()
	{
		return layerIndex;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public Color getColor()
	{
		return color;
	}

	public static Layer getLayerFromName(final String layerName)
	{
		switch (layerName)
		{
			case EditorConstants.STRING_METAL_ONE:
				return METAL_ONE;

			case EditorConstants.STRING_METAL_TWO:
				return METAL_TWO;

			case EditorConstants.STRING_METAL_THREE:
				return METAL_THREE;

			case EditorConstants.STRING_METAL_FOUR:
				return METAL_FOUR;

			case EditorConstants.STRING_METAL_FIVE:
				return METAL_FIVE;

			case EditorConstants.STRING_DIFFUSION_N:
				return DIFFUSION_N;

			case EditorConstants.STRING_DIFFUSION_P:
				return DIFFUSION_P;

			case EditorConstants.STRING_POLYSILICON:
				return POLYSILICON;

			case EditorConstants.STRING_VIA:
				return VIA;

			case EditorConstants.STRING_PIN:
				return PIN;

			default:
				return INVALID_LAYER;
		}
	}

	public static Layer getLayerFromIndex(final int index)
	{
		for (final Layer layer : Layer.values())
		{
			if (index == layer.getLayerIndex())
			{
				return layer;
			}
		}
		return Layer.INVALID_LAYER;
	}

	// public static Image getLayerImageFromName(final String name)
	// {
	// return getImageFromLayer(getLayerFromName(name));
	// }
	//
	// public static Image getImageFromLayer(final Layer layer)
	// {
	// String imgPath = EditorConstants.FILE_PATH_DATA;
	//
	// switch (layer)
	// {
	// case METAL_ONE:
	// imgPath += EditorConstants.IMG_PATH_METAL_ONE;
	// break;
	//
	// case METAL_TWO:
	// imgPath += EditorConstants.IMG_PATH_METAL_TWO;
	// break;
	//
	// case METAL_THREE:
	// imgPath += EditorConstants.IMG_PATH_METAL_THREE;
	// break;
	//
	// case METAL_FOUR:
	// imgPath += EditorConstants.IMG_PATH_METAL_FOUR;
	// break;
	//
	// case METAL_FIVE:
	// imgPath += EditorConstants.IMG_PATH_METAL_FIVE;
	// break;
	//
	// case DIFFUSION_N:
	// imgPath += EditorConstants.IMG_PATH_DIFFUSION_N;
	// break;
	//
	// case DIFFUSION_P:
	// imgPath += EditorConstants.IMG_PATH_DIFFUSION_P;
	// break;
	//
	// case POLYSILICON:
	// imgPath += EditorConstants.IMG_PATH_POLYSILICON;
	// break;
	//
	// case VIA:
	// imgPath += EditorConstants.IMG_PATH_VIA;
	// break;
	//
	// case PIN:
	// imgPath += EditorConstants.IMG_PATH_PIN;
	// break;
	//
	// default:
	// imgPath = null;
	// }
	// return new Image(imgPath);
	// }
}
