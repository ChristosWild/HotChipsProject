package project.editor.extractor.util;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Rectangle;
import project.editor.controller.EditorController;
import project.editor.extractor.components.Capacitor;
import project.editor.extractor.components.CircuitComponent;
import project.editor.extractor.components.spice.SpiceComponent;
import project.editor.util.Layer;
import project.editor.util.LayerRectangle;

public final class ExtractorUtil
{
	private ExtractorUtil() {};

	public static void extractSpice(final EditorController editorController)
	{
		setIds(editorController);
		final List<CircuitComponent> components = extractComponents(editorController); // TODO return list components
		final List<SpiceComponent> spiceComponents = SpiceUtil.componentsToSpice(components);

		//////////
		spiceComponents.forEach(e -> System.out.println(e.getSpiceString()));
		//////////
	}

	private static void setIds(final EditorController editorController)
	{
		// Give each LayerRectangle a unique id
		int id = 1;
		for (final ArrayList<LayerRectangle> layer : editorController.getCanvasController().getAllLayerRectangles())
		{
			for (final LayerRectangle layerRect : layer)
			{
				layerRect.setId(id + "");
				id++;
			}
		}
	}

	private static List<CircuitComponent> extractComponents(final EditorController editorController)
	{
		final List<CircuitComponent> components = new ArrayList<CircuitComponent>();

		final List<Capacitor> cap1_2 = extractCapacitor(editorController, Layer.METAL_ONE, Layer.METAL_TWO); // TODO Check if capacitor when layer 1 and layer 5 overlap etc
		final List<Capacitor> cap2_3 = extractCapacitor(editorController, Layer.METAL_TWO, Layer.METAL_THREE);
		final List<Capacitor> cap3_4 = extractCapacitor(editorController, Layer.METAL_THREE, Layer.METAL_FOUR);
		final List<Capacitor> cap4_5 = extractCapacitor(editorController, Layer.METAL_FOUR, Layer.METAL_FIVE);

		components.addAll(cap1_2);
		components.addAll(cap2_3);
		components.addAll(cap3_4);
		components.addAll(cap4_5);
		return components;
	}

	private static List<Capacitor> extractCapacitor(final EditorController editorController, final Layer layerOne,
			final Layer layerTwo)
	{
		final List<Capacitor> capList = new ArrayList<Capacitor>();

		final List<LayerRectangle> layerOneRects = editorController.getCanvasController().getLayerRectangles(layerOne);
		final List<LayerRectangle> layerTwoRects = editorController.getCanvasController().getLayerRectangles(layerTwo);

		for (final LayerRectangle layerOneRect : layerOneRects)
		{
			for (final LayerRectangle layerTwoRect : layerTwoRects)
			{
				// If two rectangles intersect, create capacitor and add to list
				final Rectangle intersection = layerOneRect.getIntersection(layerTwoRect);
				if (!(intersection.getWidth() <= 0 || intersection.getHeight() <= 0))
				{
					final Capacitor capacitor = new Capacitor(layerOneRect.getId(), layerTwoRect.getId(), intersection);
					if (!capList.contains(capacitor))
					{
						capList.add(capacitor);
					}
				}
			}
		}

		return capList;
	}
}
