package project.editor.utils;

import javafx.scene.image.Image;

public enum Layer
{
	METAL_ONE (EditorConstants.STRING_LAYER_METAL_ONE),
	METAL_TWO (EditorConstants.STRING_LAYER_METAL_TWO),
	METAL_THREE (EditorConstants.STRING_LAYER_METAL_THREE),
	METAL_FOUR (EditorConstants.STRING_LAYER_METAL_FOUR),
	METAL_FIVE (EditorConstants.STRING_LAYER_METAL_FIVE),
	DIFFUSION_N (EditorConstants.STRING_LAYER_DIFFUSION_N),
	DIFFUSION_P (EditorConstants.STRING_LAYER_DIFFUSION_P),
	POLYSILICON (EditorConstants.STRING_LAYER_POLYSILICON),
	VIA (EditorConstants.STRING_LAYER_VIA),
	PIN (EditorConstants.STRING_LAYER_PIN),
	INVALID_LAYER (EditorConstants.STRING_LAYER_INVALID);

	private final String displayName;

	Layer(final String displayName)
	{
		this.displayName = displayName;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public static Layer getLayerFromName(final String layerName)
	{
		switch (layerName)
		{
			case EditorConstants.STRING_LAYER_METAL_ONE:
				return METAL_ONE;

			case EditorConstants.STRING_LAYER_METAL_TWO:
				return METAL_TWO;

			case EditorConstants.STRING_LAYER_METAL_THREE:
				return METAL_THREE;

			case EditorConstants.STRING_LAYER_METAL_FOUR:
				return METAL_FOUR;

			case EditorConstants.STRING_LAYER_METAL_FIVE:
				return METAL_FIVE;

			case EditorConstants.STRING_LAYER_DIFFUSION_N:
				return DIFFUSION_N;

			case EditorConstants.STRING_LAYER_DIFFUSION_P:
				return DIFFUSION_P;

			case EditorConstants.STRING_LAYER_POLYSILICON:
				return POLYSILICON;

			case EditorConstants.STRING_LAYER_VIA:
				return VIA;

			case EditorConstants.STRING_LAYER_PIN:
				return PIN;

			default:
				return INVALID_LAYER;
		}
	}

	public static Image getLayerImageFromName(final String name)
	{
		return getImageFromLayer(getLayerFromName(name));
	}

	public static Image getImageFromLayer(final Layer layer)
	{
		String imgPath = EditorConstants.FILE_PATH_DATA;

		switch (layer)
		{
			case METAL_ONE:
				imgPath += EditorConstants.IMG_PATH_METAL_ONE;
				break;

			case METAL_TWO:
				imgPath += EditorConstants.IMG_PATH_METAL_TWO;
				break;

			case METAL_THREE:
				imgPath += EditorConstants.IMG_PATH_METAL_THREE;
				break;

			case METAL_FOUR:
				imgPath += EditorConstants.IMG_PATH_METAL_FOUR;
				break;

			case METAL_FIVE:
				imgPath += EditorConstants.IMG_PATH_METAL_FIVE;
				break;

			case DIFFUSION_N:
				imgPath += EditorConstants.IMG_PATH_DIFFUSION_N;
				break;

			case DIFFUSION_P:
				imgPath += EditorConstants.IMG_PATH_DIFFUSION_P;
				break;

			case POLYSILICON:
				imgPath += EditorConstants.IMG_PATH_POLYSILICON;
				break;

			case VIA:
				imgPath += EditorConstants.IMG_PATH_VIA;
				break;

			case PIN:
				imgPath += EditorConstants.IMG_PATH_PIN;
				break;

			default:
				imgPath = null;
		}
		return new Image(imgPath);
	}
}
