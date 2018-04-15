package project.editor.extractor.components;

import javafx.scene.shape.Rectangle;
import project.editor.controller.EditorController;
import project.editor.util.EditorConstants;

public class Capacitor implements CircuitComponent
{
	private static final double E0 = 8.854 * Math.pow(10, -12);
	private static final double DIELECTRIC_CONSTANT_SILICON_DIOXIDE = 3.8; // FIXME confirm 3.7 - 3.9?

	private final EditorController editorController;
	private final String node1;
	private final String node2;
	private final Rectangle bounds;

	public Capacitor(final EditorController editorController, final String node1, final String node2,
			final Rectangle bounds)
	{
		this.editorController = editorController;
		this.node1 = node1;
		this.node2 = node2;
		this.bounds = bounds;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (!(obj instanceof Capacitor))
		{
			return false;
		}
		else
		{
			final Capacitor cap = (Capacitor) obj;

			// Equal if same top left coordinates, width, and height
			if (this.bounds.getX() == cap.bounds.getX()
					&& this.bounds.getY() == cap.bounds.getY()
					&& this.bounds.getWidth() == cap.bounds.getWidth()
					&& this.bounds.getHeight() == cap.bounds.getHeight())
			{
				return true;
			}
		}
		return false;
	}

	public double getCapacitance()
	{
		// C = k*E0*A / d
		final double lambdaUm = editorController.getTechnologyControl().getLambda();
		final double layerSeparationMetres = editorController.getTechnologyControl().getOxideThicknessNm()
				* Math.pow(10, -9);

		final double widthMetres = (bounds.getWidth() / EditorConstants.CANVAS_GRID_SIZE) * lambdaUm * Math.pow(10, -6);
		final double heightMetres = (bounds.getHeight() / EditorConstants.CANVAS_GRID_SIZE) * lambdaUm * Math.pow(10, -6);
		final double areaMetres = widthMetres * heightMetres;

		return (areaMetres * E0 * DIELECTRIC_CONSTANT_SILICON_DIOXIDE) / layerSeparationMetres;
	}

	public String getName()
	{
		return (bounds.hashCode() + "").substring(0, 5);
	}

	public String getNode1()
	{
		return node1;
	}

	public String getNode2()
	{
		return node2;
	}
}
