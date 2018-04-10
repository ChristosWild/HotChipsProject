package project.editor.extractor.components;

public class Transistor implements CircuitComponent
{
	public enum TransistorType
	{
		NMOS, PMOS;
	}

	public static final String NAME_SOURCE = "Source";
	public static final String NAME_DRAIN = "Drain";

	private final TransistorType type;
	private final String nodeSource;
	private final String nodeDrain;
	private final String nodeGate;

	public Transistor(final TransistorType type, final String nodeSource, final String nodeDrain, final String nodeGate)
	{
		this.type = type;
		this.nodeSource = nodeSource;
		this.nodeDrain = nodeDrain;
		this.nodeGate = nodeGate;
	}
}