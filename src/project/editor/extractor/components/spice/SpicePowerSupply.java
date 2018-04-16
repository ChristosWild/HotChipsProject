package project.editor.extractor.components.spice;

import project.editor.controller.EditorController;
import project.editor.extractor.util.SpiceUtil;

public class SpicePowerSupply extends SpiceComponent
{
	private static final String POWER_DC = "DC";

	private final EditorController editorController;
	private final int instanceId;
	private final String nodePositive;
	private final String nodeNegative;

	public SpicePowerSupply(final EditorController editorController, final String nodePositive,
			final String nodeNegative, final int instanceId)
	{
		this.editorController = editorController;
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
		sb.append(POWER_DC);
		addSpacer(sb);
		sb.append(editorController.getTechnologyControl().getVddVoltage());

		return sb.toString();
	}
}
