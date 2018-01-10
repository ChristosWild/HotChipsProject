package project.editor.utils;

import javafx.scene.paint.Color;

public interface EditorConstants
{
	// _Metal_One
	// _Metal_Two
	// _Metal_Three
	// _Metal_Four
	// _Metal_Five
	// _Diffusion_N
	// _Diffusion_P
	// _Polysilicon
	// _Via
	// _Pin

	public static final int CANVAS_WIDTH = 1000; // Max = 8192
	public static final int CANVAS_HEIGHT = 6000; // Max = 8192
	public static final int CANVAS_GRID_SIZE = 10;

	public static final String STRING_METAL_ONE = "Metal 1";
	public static final String STRING_METAL_TWO = "Metal 2";
	public static final String STRING_METAL_THREE = "Metal 3";
	public static final String STRING_METAL_FOUR = "Metal 4";
	public static final String STRING_METAL_FIVE = "Metal 5";
	public static final String STRING_DIFFUSION_N = "N Diffusion";
	public static final String STRING_DIFFUSION_P = "P Diffusion";
	public static final String STRING_POLYSILICON = "Polysilicon";
	public static final String STRING_VIA = "Via";
	public static final String STRING_PIN = "Pin";
	public static final String STRING_INVALID = "Invalid Layer";

	public static final double COLOR_OPACITY = 0.5;
	public static final double COLOR_OPACITY_SELECTED = 0.8;

	public static final Color COLOR_METAL_ONE = Color.RED;
	public static final Color COLOR_METAL_TWO = Color.ORANGE;
	public static final Color COLOR_METAL_THREE = Color.YELLOW;
	public static final Color COLOR_METAL_FOUR = Color.CHARTREUSE;
	public static final Color COLOR_METAL_FIVE = Color.GREEN;
	public static final Color COLOR_DIFFUSION_N = Color.AQUA;
	public static final Color COLOR_DIFFUSION_P = Color.BLUE;
	public static final Color COLOR_POLYSILICON = Color.INDIGO;
	public static final Color COLOR_VIA = Color.TRANSPARENT; // Uses image instead
	public static final Color COLOR_PIN = Color.TRANSPARENT; // Uses image instead
	public static final Color COLOR_INVALID = Color.TRANSPARENT;

	public static final String PATH_FILE_DATA = "file:data/";
	public static final String PATH_FILE_SRC = "file:src/";

	public static final String PATH_IMG_VIA = "via.png";
	public static final String PATH_IMG_VIA_SELECTED = "via_selected.png";
	public static final String PATH_IMG_VIA_16 = "via_16.png";
	public static final String PATH_IMG_PIN = "pin.png";
	public static final String PATH_IMG_PIN_SELECTED = "pin_selected.png";
	public static final String PATH_IMG_PIN_16 = "pin_16.png";
	public static final String PATH_IMG_GRID = "grid.png";

	public static final String PATH_IMG_CLOSE = "close.png";

	public static final String PATH_CSS_MAIN = "project/application/Main.css";
}
