package project.editor.extractor.components.spice;

import project.editor.extractor.util.SpiceUtil;

public class SpicePowerSupply extends SpiceComponent
{
	private final String posNodeId;
	private final String negNodeId;

	public SpicePowerSupply(final String posNodeId, final String negNodeId)
	{
		this.posNodeId = posNodeId;
		this.negNodeId = negNodeId;
	}

	@Override
	public String getSpiceString()
	{
		sb = new StringBuilder();
		sb.append(SpiceUtil.PIN_NAME_VDD);
		addSpacer(sb);
		sb.append(posNodeId);
		addSpacer(sb);
		sb.append(negNodeId);
		addSpacer(sb);
		sb.append("DC"); // TODO get friom tech file
		addSpacer(sb);
		sb.append(5);

		return sb.toString();
	}
}
