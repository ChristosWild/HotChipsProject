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
	private static int MAX_ID = 0;

	private ExtractorUtil() {};

	/**
	 * Extracts the drawn LayerRectangles to SPICE format
	 *
	 * @param editorController
	 */
	public static void extractSpice(final EditorController editorController)
	{
		MAX_ID = setInitialIds(editorController);
		connectAdjacentRects(editorController);

		final List<CircuitComponent> components = extractComponents(editorController);
		final List<SpiceComponent> spiceComponents = SpiceUtil.componentsToSpice(components);

		// editorController.getCanvasController().getAllLayerRectangles().forEach(layer
		// -> {
		// layer.forEach(rect -> {
		// System.out.println(rect.getId());
		// });
		// });

		//////////
		spiceComponents.forEach(e -> System.out.println(e.getSpiceString()));
		//////////
	}

	private static int setInitialIds(final EditorController editorController)
	{
		// Give each LayerRectangle a unique id, returns highest id val used later
		int id = 1;
		for (final ArrayList<LayerRectangle> layer : editorController.getCanvasController().getAllLayerRectangles())
		{
			for (final LayerRectangle layerRect : layer)
			{
				layerRect.setId(id + "");
				id++;
			}
		}
		return id;
	}

	/**
	 * Connects LayerRectangles that are on the same Layer if they touch or overlap
	 *
	 * @param editorController
	 */
	private static void connectAdjacentRects(final EditorController editorController)
	{
		final List<ArrayList<LayerRectangle>> allLayerRects = editorController.getCanvasController()
				.getAllLayerRectangles();

		LAYER: for (int layerIndex = 0 ; layerIndex < allLayerRects.size() ; layerIndex++) // List<LayerRectangle> layerRects : allLayerRects)
		{
			for (int rectIndex = 0; rectIndex < allLayerRects.get(layerIndex).size() - 1; rectIndex++)
			{
				if (allLayerRects.get(layerIndex).get(rectIndex).getLayer() == Layer.VIA
						|| allLayerRects.get(layerIndex).get(rectIndex).getLayer() == Layer.PIN)
				{
					break LAYER;
				}

				final LayerRectangle rectOne = allLayerRects.get(layerIndex).get(rectIndex);

				for (int rectTwoIndex = rectIndex + 1; rectTwoIndex < allLayerRects.get(layerIndex).size(); rectTwoIndex++)
				{
					final LayerRectangle rectTwo = allLayerRects.get(layerIndex).get(rectTwoIndex);

					if (rectOne.isAdjacentTo(rectTwo))
					{
						// Update ids to be the same as now considered same object
						MAX_ID++;
						updateIds(editorController, rectOne.getId(), MAX_ID + "");
						updateIds(editorController, rectTwo.getId(), MAX_ID + "");
					}
				}
			}
		}
	}

	private static List<CircuitComponent> extractComponents(final EditorController editorController)
	{
		final List<CircuitComponent> components = new ArrayList<CircuitComponent>();

		components.addAll(extractMetalViasAndCapacitors(editorController)); // TODO Check if capacitor when layer 1 and layer 5 overlap etc
		components.addAll(extractTransistors(editorController));

		return components;
	}

	private static List<Capacitor> extractMetalViasAndCapacitors(final EditorController editorController)
	{
		final List<Capacitor> capList = new ArrayList<Capacitor>();

		for (int layerIndex = 0; layerIndex < Layer.getMetalLayers().size() - 1; layerIndex++)
		{
			final Layer layerOne = Layer.getMetalLayers().get(layerIndex);
			final Layer layerTwo = Layer.getMetalLayers().get(layerIndex + 1);

			final List<LayerRectangle> layerOneRects = editorController.getCanvasController()
					.getLayerRectangles(layerOne);
			final List<LayerRectangle> layerTwoRects = editorController.getCanvasController()
					.getLayerRectangles(layerTwo);
			final List<LayerRectangle> viaRects = editorController.getCanvasController().getLayerRectangles(Layer.VIA);

			for (final LayerRectangle layerOneRect : layerOneRects)
			{
				for (final LayerRectangle layerTwoRect : layerTwoRects)
				{
					// Check if the two rectangles intersect
					final Rectangle intersection = layerOneRect.getIntersection(layerTwoRect);
					if (intersection.getWidth() > 0 && intersection.getHeight() > 0)
					{
						boolean isVia = false;

						// If overlap contains a via, it is a connection, else it is a capacitor
						for(final LayerRectangle via : viaRects)
						{
							if (via.isContainedBy(layerOneRect) && via.isContainedBy(layerTwoRect))
							{
								isVia = true;
							}
						}

						if (isVia)
						{
							// Remove capacitors connecting layerOneRect and layerTwoRect and update IDs to
							// connect the layerRects
							for (final Capacitor capacitor : capList)
							{
								if (capacitor.getNode1().equals(layerOneRect.getId())
										&& capacitor.getNode2().equals(layerTwoRect.getId()))
								{
									capList.remove(capacitor);
								}
							}
							updateIds(editorController, layerTwoRect.getId(), layerOneRect.getId());
						}
						else
						{
							final Capacitor capacitor = new Capacitor(layerOneRect.getId(), layerTwoRect.getId(),
									intersection);
							if (!capList.contains(capacitor) && !layerOneRect.getId().equals(layerTwoRect.getId()))
							{
								capList.add(capacitor);
							}
						}
					}
				}
			}
		}

		return capList;
	}

	private static List<CircuitComponent> extractTransistors(final EditorController editorController)
	{
		final List<CircuitComponent> transistors = new ArrayList<CircuitComponent>();

		return transistors;
	}

	/**
	 * Updates all LayerRectangles with ID = oldId to have ID = newId
	 *
	 * @param editorController
	 * @param oldId
	 * @param newId
	 */
	private static void updateIds(final EditorController editorController, final String oldId, final String newId)
	{
		final List<ArrayList<LayerRectangle>> allLayerRects = editorController.getCanvasController()
				.getAllLayerRectangles();

		for (final List<LayerRectangle> layerRects : allLayerRects)
		{
			for (final LayerRectangle layerRect : layerRects)
			{
				if (layerRect.getId().equals(oldId))
				{
					layerRect.setId(newId);
				}
			}
		}
	}
}
