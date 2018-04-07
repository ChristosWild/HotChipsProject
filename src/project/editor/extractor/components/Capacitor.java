package project.editor.extractor.components;

import javafx.scene.shape.Rectangle;
import project.editor.util.EditorConstants;

public class Capacitor implements CircuitComponent
{
	private static final double E0 = 8.854 * Math.pow(10, -12);
	private static final double DIELECTRIC_CONSTANT_SILICON_DIOXIDE = 3.8; // TODO confirm 3.7 - 3.9?
	private static final double LAYER_SEPARATION_METRES = 400 * Math.pow(10, -9); // TODO confirm 400nm thickness. 600? 300? 500?

	private final String node1;
	private final String node2;
	private final Rectangle bounds;

	public Capacitor(final String node1, final String node2, final Rectangle bounds)
	{
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
		final double areaMicrons = (bounds.getWidth() / EditorConstants.CANVAS_GRID_SIZE)
				* (bounds.getHeight() / EditorConstants.CANVAS_GRID_SIZE) * 2.5;// TODO * techFileLambda val;
		final double areaMetres = areaMicrons * Math.pow(10, -6);

		return (areaMetres * E0 * DIELECTRIC_CONSTANT_SILICON_DIOXIDE) / LAYER_SEPARATION_METRES;
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