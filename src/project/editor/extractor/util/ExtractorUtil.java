package project.editor.extractor.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.naming.ConfigurationException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;
import project.editor.controller.CanvasController;
import project.editor.controller.EditorController;
import project.editor.extractor.components.Capacitor;
import project.editor.extractor.components.CircuitComponent;
import project.editor.extractor.components.PowerSupply;
import project.editor.extractor.components.Transistor;
import project.editor.extractor.components.Transistor.TransistorType;
import project.editor.extractor.components.spice.SpiceComponent;
import project.editor.util.FileUtil;
import project.editor.util.Layer;
import project.editor.util.LayerRectangle;

public final class ExtractorUtil
{
	private static int MAX_ID = 1;
	private static String GND_ID = "0";
	private static final String TRANSISTOR_MISSING_VIA_EXCEPTION_MESSAGE = "Extraction failed.\nEnsure all vias"
			+ " connecting transistors are labelled as either a source or drain.";
	private static final String TRANSISTOR_SAME_SOURCE_DRAIN_EXCEPTION_MESSAGE = "Extraction failed.\nTransistor has 2"
			+ " sources, or 2 drains.";
	private static final String SAVE_BEFORE_EXTRACTION_MESSAGE = "Circuit must be saved before extraction.";
	private static final String EXTRACTION_SUCCESSFUL_MESSAGE = "Circuit extraction successful.\n";

	private ExtractorUtil() {};

	/**
	 * Extracts the drawn LayerRectangles to SPICE format
	 *
	 * @param editorController
	 */
	public static void extractSpice(final EditorController editorController, final Window window)
	{
		boolean isSaved = saveBeforeExtraction(editorController, window);

		final Alert infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setHeaderText(null);
		infoAlert.setGraphic(null);

		if (isSaved)
		{
			try
			{
				final CanvasController canvasController = editorController.getCanvasController();

				MAX_ID = setInitialIds(canvasController);
				connectAdjacentRects(canvasController);

				final List<CircuitComponent> components = extractComponents(canvasController);

				final List<SpiceComponent> spiceComponents = SpiceUtil.componentsToSpice(components);

				final Path filePath = SpiceUtil.writeToFile(editorController, spiceComponents);

				infoAlert.setContentText(EXTRACTION_SUCCESSFUL_MESSAGE + filePath.toString());
				infoAlert.show();

			} catch (ConfigurationException | IOException e)
			{
				// TODO log properly
				System.out.println(e.getMessage());
				e.printStackTrace();

				infoAlert.setContentText(e.getMessage());
				infoAlert.show();
			}
		}
	}

	private static boolean saveBeforeExtraction(final EditorController editorController, final Window window)
	{
		boolean isSaved = false;

		final Alert saveAlert = new Alert(AlertType.CONFIRMATION);
		saveAlert.setContentText(SAVE_BEFORE_EXTRACTION_MESSAGE);
		saveAlert.setHeaderText(null);
		saveAlert.setGraphic(null);

		final ButtonType save = new ButtonType("Save", ButtonData.OK_DONE);
		final ButtonType saveAs = new ButtonType("Save As", ButtonData.OK_DONE);
		saveAlert.getButtonTypes().remove(0);
		saveAlert.getButtonTypes().addAll(save, saveAs);

		final Optional<ButtonType> result = saveAlert.showAndWait();
		if (result.get().equals(save))
		{
			FileUtil.saveFile(editorController, window);
			isSaved = true;
		}
		else if (result.get().equals(saveAs))
		{
			FileUtil.saveFileAs(editorController, window);
			isSaved = true;
		}

		return isSaved;
	}

	private static int setInitialIds(final CanvasController canvasController)
	{
		// Give each LayerRectangle a unique id, returns highest id val used later
		int id = 1;
		for (final ArrayList<LayerRectangle> layer : canvasController.getAllLayerRectangles())
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
	 * @param canvasController
	 */
	private static void connectAdjacentRects(final CanvasController canvasController)
	{
		final List<ArrayList<LayerRectangle>> allLayerRects = canvasController.getAllLayerRectangles();

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
						updateIds(canvasController, rectOne.getId(), MAX_ID + "");
						updateIds(canvasController, rectTwo.getId(), MAX_ID + "");
					}
				}
			}
		}
	}

	private static List<CircuitComponent> extractComponents(final CanvasController canvasController)
			throws ConfigurationException
	{
		final List<CircuitComponent> components = new ArrayList<CircuitComponent>();

		extractGnd(canvasController);
		extractPolysiliconVias(canvasController);
		components.addAll(extractMetalViasAndCapacitors(canvasController)); // TODO Check if capacitor when layer 1 and layer 5 overlap etc
		components.addAll(extractTransistors(canvasController, TransistorType.NMOS));
		components.addAll(extractTransistors(canvasController, TransistorType.PMOS));
		components.addAll(extractVdd(canvasController));

		return components;
	}

	private static void extractGnd(final CanvasController canvasController)
	{
		for (final LayerRectangle pin : canvasController.getLayerRectangles(Layer.PIN))
		{
			LAYER: for (final List<LayerRectangle> layer : canvasController.getAllLayerRectangles())
			{
				for (final LayerRectangle layerRect : layer)
				{
					if (layerRect.getLayer() == Layer.VIA || layerRect.getLayer() == Layer.PIN)
					{
						break LAYER;
					}
					else if (pin.isContainedBy(layerRect) && pin.getName().equals(SpiceUtil.PIN_NAME_GND))
					{
						updateIds(canvasController, layerRect.getId(), GND_ID);
					}
				}
			}
		}
	}

	private static List<PowerSupply> extractVdd(final CanvasController canvasController)
	{
		final List<PowerSupply> vdd = new ArrayList<PowerSupply>();

		for (final LayerRectangle pin : canvasController.getLayerRectangles(Layer.PIN))
		{
			LAYER: for (final List<LayerRectangle> layer : canvasController.getAllLayerRectangles())
			{
				for (final LayerRectangle layerRect : layer)
				{
					if (layerRect.getLayer() == Layer.VIA || layerRect.getLayer() == Layer.PIN)
					{
						break LAYER;
					}
					else if (pin.isContainedBy(layerRect) && pin.getName().equals(SpiceUtil.PIN_NAME_VDD))
					{
						vdd.add(new PowerSupply(layerRect.getId(), GND_ID, vdd.size()));
					}
				}
			}
		}
		return vdd;
	}

	/**
	 * Connects polysilicon and metal one layers
	 *
	 * @param canvasController
	 */
	private static void extractPolysiliconVias(final CanvasController canvasController)
	{
		for (final LayerRectangle poly : canvasController.getLayerRectangles(Layer.POLYSILICON))
		{
			for (final LayerRectangle via : canvasController.getLayerRectangles(Layer.VIA))
			{
				if (via.isContainedBy(poly))
				{
					for (LayerRectangle metalOne : canvasController.getLayerRectangles(Layer.METAL_ONE))
					{
						if (via.isContainedBy(metalOne))
						{
							updateIds(canvasController, poly.getId(), metalOne.getId());
						}
					}
				}
			}
		}
	}

	/**
	 * Connects metal layers to adjacent metal layers. Also detects and returns
	 * capacitors
	 *
	 * @param canvasController
	 * @return List of capacitors detected between adjacent metal layers
	 */
	private static List<Capacitor> extractMetalViasAndCapacitors(final CanvasController canvasController)
	{
		final List<Capacitor> capList = new ArrayList<Capacitor>();

		for (int layerIndex = 0; layerIndex < Layer.getMetalLayers().size() - 1; layerIndex++)
		{
			final Layer layerOne = Layer.getMetalLayers().get(layerIndex);
			final Layer layerTwo = Layer.getMetalLayers().get(layerIndex + 1);

			final List<LayerRectangle> layerOneRects = canvasController.getLayerRectangles(layerOne);
			final List<LayerRectangle> layerTwoRects = canvasController.getLayerRectangles(layerTwo);
			final List<LayerRectangle> viaRects = canvasController.getLayerRectangles(Layer.VIA);

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
							updateIds(canvasController, layerTwoRect.getId(), layerOneRect.getId());
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

	private static List<Transistor> extractTransistors(final CanvasController canvasController,
			final TransistorType type) throws ConfigurationException
	{
		// TODO get all diffusion with same ID and compare overlap with them too (in case diffusion is split into different rects)
		// TODO poly must cover diffusion
		final List<Transistor> transistors = new ArrayList<Transistor>();

		final List<LayerRectangle> diffusionRects = canvasController
				.getLayerRectangles(type == TransistorType.NMOS ? Layer.DIFFUSION_N : Layer.DIFFUSION_P);

		for (final LayerRectangle diffusion : diffusionRects)
		{
			String nodeSource = null;
			String nodeDrain = null;
			String nodeGate = null;

			VIA: for (final LayerRectangle via : canvasController.getLayerRectangles(Layer.VIA))
			{
				if (via.isContainedBy(diffusion))
				{
					for (final LayerRectangle metalOne : canvasController.getLayerRectangles(Layer.METAL_ONE))
					{
						if (via.isContainedBy(metalOne))
						{
							if (via.getName() == null)
							{
								throw new ConfigurationException(TRANSISTOR_MISSING_VIA_EXCEPTION_MESSAGE); // TODO stop programming by exception
							}
							else if (via.getName().equals(SpiceUtil.TRANSISTOR_NAME_SOURCE))
							{
								nodeSource = metalOne.getId();
							}
							else if (via.getName().equals(SpiceUtil.TRANSISTOR_NAME_DRAIN))
							{
								nodeDrain = metalOne.getId();
							}
							else
							{
								throw new ConfigurationException(TRANSISTOR_MISSING_VIA_EXCEPTION_MESSAGE);
							}

							if (nodeSource != null && nodeDrain != null)
							{
								break VIA;
							}
							else
							{
								continue VIA;
							}
						}
					}
				}
			}

			for (final LayerRectangle poly : canvasController.getLayerRectangles(Layer.POLYSILICON))
			{
				final Rectangle intersection = diffusion.getIntersection(poly);
				if (intersection.getWidth() > 0 && intersection.getHeight() > 0)
				{
					nodeGate = poly.getId();
					break;
				}
			}

			if (nodeSource != null && nodeDrain != null && nodeGate != null)
			{
				if (nodeSource.equals(nodeDrain))
				{
					throw new ConfigurationException(TRANSISTOR_SAME_SOURCE_DRAIN_EXCEPTION_MESSAGE);
				}
				else
				{
					final Transistor transistor = new Transistor(type, nodeSource, nodeDrain, nodeGate,
							transistors.size());
					transistors.add(transistor);
				}
			}
		}

		return transistors;
	}

	/**
	 * Updates all LayerRectangles with "ID = oldId" to "ID = newId"
	 *
	 * @param canvasController
	 * @param oldId
	 * @param newId
	 */
	private static void updateIds(final CanvasController canvasController, String oldId, String newId)
	{
		final List<ArrayList<LayerRectangle>> allLayerRects = canvasController.getAllLayerRectangles();

		// Keep Gnd as ID = 0
		if (oldId.equals(GND_ID))
		{
			oldId = newId;
			newId = GND_ID;
		}

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
