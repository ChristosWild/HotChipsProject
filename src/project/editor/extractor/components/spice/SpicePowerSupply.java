package project.editor.extractor.components.spice;

import project.editor.extractor.util.SpiceUtil;

public class SpicePowerSupply extends SpiceComponent
{
	private final int instanceId;
	private final String nodePositive;
	private final String nodeNegative;

	public SpicePowerSupply(final String nodePositive, final String nodeNegative, final int instanceId)
	{
		this.nodePositive = nodePositive;
		this.nodeNegative = nodeNegative;
		this.instanceId = instanceId;
	}

	@Override
	public String getSpiceString()
	{
		sb = new StringBuilder();
		sb.append(SpiceUtil.PIN_NAME_VDD);
		if (instanceId != 0)
		{
			sb.append(instanceId);
		}
		addSpacer(sb);
		sb.append(nodePositive);
		addSpacer(sb);
		sb.append(nodeNegative);
		addSpacer(sb);
		sb.append("DC"); // FIXME get from tech file
		addSpacer(sb);
		sb.append(5);

		return sb.toString();
	}
}
