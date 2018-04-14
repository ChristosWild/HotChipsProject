package project.editor.extractor.components.spice;

import project.editor.extractor.components.Transistor.TransistorType;

public class SpiceTransistor extends SpiceComponent
{
	private final int instanceId;
	private final TransistorType type;
	private final String nodeSource;
	private final String nodeDrain;
	private final String nodeGate;

	public SpiceTransistor(final TransistorType type, final String nodeSource, final String nodeDrain,
			final String nodeGate, final int instanceId)
	{
		this.type = type;
		this.nodeSource = nodeSource;
		this.nodeDrain = nodeDrain;
		this.nodeGate = nodeGate;
		this.instanceId = instanceId;
	}

	@Override
	public String getSpiceString()
	{
		sb = new StringBuilder();
		sb.append("M");
		sb.append(instanceId);
		addSpacer(sb);
		sb.append(nodeDrain);
		addSpacer(sb);
		sb.append(nodeGate);
		addSpacer(sb);
		sb.append(nodeSource);
		addSpacer(sb);
		sb.append(type);
		sb.append("MODEL");

		return sb.toString();
	}
}
