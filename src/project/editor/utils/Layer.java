package project.editor.utils;

public enum Layer
{
	POLYSILICON (EditorConstants.STRING_LAYER_POLYSILICON),
	DIFFUSION_P (EditorConstants.STRING_LAYER_DIFFUSION_P),
	DIFFUSION_N (EditorConstants.STRING_LAYER_DIFFUSION_N),
	METAL_ONE (EditorConstants.STRING_LAYER_METAL_ONE),
	METAL_TWO (EditorConstants.STRING_LAYER_METAL_TWO),
	METAL_THREE (EditorConstants.STRING_LAYER_METAL_THREE),
	METAL_FOUR (EditorConstants.STRING_LAYER_METAL_FOUR),
	METAL_FIVE (EditorConstants.STRING_LAYER_METAL_FIVE),
	VIA (EditorConstants.STRING_LAYER_VIA),
	PIN (EditorConstants.STRING_LAYER_PIN),
	INVALID_LAYER (EditorConstants.STRING_LAYER_INVALID);

	private final String name;

	Layer(final String stringName)
	{
		this.name = stringName;
	}

	public String getName()
	{
		return name;
	}

	public static Layer getLayerFromName(final String layerName)
	{
		switch (layerName)
		{
			case EditorConstants.STRING_LAYER_POLYSILICON:
				return POLYSILICON;

			case EditorConstants.STRING_LAYER_DIFFUSION_P:
				return DIFFUSION_P;

			case EditorConstants.STRING_LAYER_DIFFUSION_N:
				return DIFFUSION_N;

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

			case EditorConstants.STRING_LAYER_VIA:
				return VIA;

			case EditorConstants.STRING_LAYER_PIN:
				return PIN;

			default:
				return INVALID_LAYER;
		}
	}
}
