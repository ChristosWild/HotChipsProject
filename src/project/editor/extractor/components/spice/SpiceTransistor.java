package project.editor.extractor.components.spice;

import project.editor.extractor.components.Transistor.TransistorType;

public class SpiceTransistor extends SpiceComponent
{
	private final TransistorType type;
	private final String nodeSource;
	private final String nodeDrain;
	private final String nodeGate;

	public SpiceTransistor(final TransistorType type, final String nodeSource, final String nodeDrain,
			final String nodeGate)
	{
		this.type = type;
		this.nodeSource = nodeSource;
		this.nodeDrain = nodeDrain;
		this.nodeGate = nodeGate;
	}
	public String getTransistorModel()
	{
		final String n = ".MODEL modelNMOS NMOS ()";
		final String p = ".MODEL modelPMOS PMOS ()";
		return null;
	}

	@Override
	public String getSpiceString()
	{
		// TODO Auto-generated method stub
		return "transistor";
	}
}
