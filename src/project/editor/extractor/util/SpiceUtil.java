package project.editor.extractor.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import project.editor.control.TechnologyControl;
import project.editor.controller.EditorController;
import project.editor.extractor.components.Capacitor;
import project.editor.extractor.components.CircuitComponent;
import project.editor.extractor.components.DCPowerSupply;
import project.editor.extractor.components.PulsePowerSupply;
import project.editor.extractor.components.Transistor;
import project.editor.extractor.components.spice.SpiceCapacitor;
import project.editor.extractor.components.spice.SpiceComponent;
import project.editor.extractor.components.spice.SpiceDCPowerSupply;
import project.editor.extractor.components.spice.SpicePulsePowerSupply;
import project.editor.extractor.components.spice.SpiceTransistor;
import project.editor.util.FileUtil;

public final class SpiceUtil
{
	public static final String PIN_NAME_VDD = "Vdd";
	public static final String PIN_NAME_VIN = "Vin";
	public static final String PIN_NAME_GND = "Gnd";

	public static final String TRANSISTOR_NAME_SOURCE = "Source";
	public static final String TRANSISTOR_NAME_DRAIN = "Drain";
	private static final String TRANSISTOR_MODEL_NMOS = ".MODEL NMOSMODEL NMOS (LEVEL=1 VTO=%S TOX=%S LAMBDA=%S)"; // FIXME VTO=threshold, TOX=oxide thickness, LAMBDA=lambda
	private static final String TRANSISTOR_MODEL_PMOS = ".MODEL PMOSMODEL PMOS (LEVEL=1 VTO=%S TOX=%S LAMBDA=%S)";

	private static final String TRAN = ".TRAN %S %S";
	private static final String OPTIONS = ".OPTIONS LIST NODE"; // FIXME POST opition?
	private static final String PROBE = ".PROBE";
	private static final String FILE_END = ".END";

	private static final String FILE_EXTENSION = ".cir";

	private SpiceUtil() {}

	public static List<SpiceComponent> componentsToSpice(final EditorController editorController,
			final List<CircuitComponent> components)
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
			else if (component instanceof Transistor)
			{
				final Transistor tran = (Transistor) component;
				final SpiceTransistor sTran = new SpiceTransistor(tran.getType(), tran.getNodeSource(),
						tran.getNodeDrain(), tran.getNodeGate(), tran.getInstanceId());
				spiceComponents.add(sTran);
			}
			else if (component instanceof DCPowerSupply)
			{
				final DCPowerSupply power = (DCPowerSupply) component;
				final SpiceDCPowerSupply sPower = new SpiceDCPowerSupply(editorController, power.getPosNodeId(),
						power.getNegNodeId(), power.getInstanceId());
				spiceComponents.add(sPower);
			}
			else if (component instanceof PulsePowerSupply)
			{
				final PulsePowerSupply power = (PulsePowerSupply) component;
				final SpicePulsePowerSupply sPower = new SpicePulsePowerSupply(editorController, power.getPosNodeId(),
						power.getNegNodeId(), power.getInstanceId());
				spiceComponents.add(sPower);
			}
		}

		return spiceComponents;
	}

	public static Path writeToFile(final EditorController editorController, final List<SpiceComponent> components)
			throws IOException
	{
		try
		{
			final List<String> data = new ArrayList<String>();
			final String filePath = editorController.getFilePath();
			final String fileName = FileUtil.getFileNameFromPath(filePath);
			final String fileNameTitle = FileUtil.removeFileExtension(fileName);

			data.add(fileNameTitle); // Spice file title

			for (final SpiceComponent component : components)
			{
				data.add(component.getSpiceString()); // Components
			}

			final TechnologyControl techControl = editorController.getTechnologyControl();
			data.add(String.format(TRANSISTOR_MODEL_NMOS, techControl.getThresholdVoltage(), // Transistor models
					techControl.getOxideThickness(), techControl.getLambda()));
			data.add(String.format(TRANSISTOR_MODEL_PMOS, techControl.getThresholdVoltage(),
					techControl.getOxideThickness(), techControl.getLambda()));

			data.add(String.format(TRAN, techControl.getPeriod() / 10, techControl.getPeriod() * 10));
			data.add(OPTIONS);
			data.add(PROBE);

			data.add(FILE_END); // File end marker

			final Path file = Paths.get(FileUtil.getDirFromPath(filePath) + fileNameTitle + FILE_EXTENSION);
			return Files.write(file, data, Charset.forName("UTF-8"));

		} catch (IOException e)
		{
			throw new IOException("Writing to file failed.\n" + e.getMessage(), e);
		}
	}
}
