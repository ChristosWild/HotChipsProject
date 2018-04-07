package project.editor.extractor.util;

import java.util.ArrayList;
import java.util.List;

import project.editor.extractor.components.Capacitor;
import project.editor.extractor.components.CircuitComponent;
import project.editor.extractor.components.spice.SpiceCapacitor;
import project.editor.extractor.components.spice.SpiceComponent;

public final class SpiceUtil // TODO util classes all final with private constructors
{
	private SpiceUtil() {}

	public static List<SpiceComponent> componentsToSpice(final List<CircuitComponent> components)
	{
		final List<SpiceComponent> spiceComponents = new ArrayList<SpiceComponent>();

		for (final CircuitComponent component : components)
		{
			if (component instanceof Capacitor)
			{
				final Capacitor cap = (Capacitor) component;
				final SpiceCapacitor sCap = new SpiceCapacitor(cap.getName(), cap.getNode1(), cap.getNode2(),
						cap.getCapacitance());
				spiceComponents.add(sCap);
			}
		}

		return spiceComponents;
	}
}
