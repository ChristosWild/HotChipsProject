package project.editor.extractor.components.spice;

import project.editor.controller.EditorController;
import project.editor.extractor.util.SpiceUtil;

public class SpicePulsePowerSupply extends SpiceComponent
{
	private static final String POWER_PULSE = "PULSE";

	private static final int VIN_MIN_VOLTAGE = 0;
	private static final String T_RISE = "1E-10";
	private static final String T_FALL = "1E-10";

	private static final String BRACKET_OPEN = "(";
	private static final String BRACKET_CLOSE = ")";

	private final EditorController editorController;
	private final int instanceId;
	private final String nodePositive;
	private final String nodeNegative;

	public SpicePulsePowerSupply(final EditorController editorController, final String nodePositive,
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
		final double period = editorController.getTechnologyControl().getPeriod();

		sb = new StringBuilder();
		sb.append(SpiceUtil.PIN_NAME_VIN);
		if (instanceId != 0)
		{
			sb.append(instanceId);
		}
		addSpacer(sb);
		sb.append(nodePositive);
		addSpacer(sb);
		sb.append(nodeNegative);
		addSpacer(sb);
		sb.append(POWER_PULSE);
		addSpacer(sb);
		sb.append(BRACKET_OPEN);
		sb.append(VIN_MIN_VOLTAGE);
		addSpacer(sb);
		sb.append(editorController.getTechnologyControl().getVinVoltage());
		addSpacer(sb);
		sb.append(period / 2);
		addSpacer(sb);
		sb.append(T_RISE);
		addSpacer(sb);
		sb.append(T_FALL);
		addSpacer(sb);
		sb.append(period / 2);
		addSpacer(sb);
		sb.append(period);
		sb.append(BRACKET_CLOSE);

		return sb.toString();
	}
}
