package project.editor.extractor.components;

public class Transistor implements CircuitComponent
{
	public enum TransistorType
	{
		NMOS, PMOS;
	}

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

	public TransistorType getType()
	{
		return type;
	}

	public String getNodeSource()
	{
		return nodeSource;
	}

	public String getNodeDrain()
	{
		return nodeDrain;
	}

	public String getNodeGate()
	{
		return nodeGate;
	}
}
