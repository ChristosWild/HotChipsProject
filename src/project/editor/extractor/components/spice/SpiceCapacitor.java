package project.editor.extractor.components.spice;

public class SpiceCapacitor extends SpiceComponent
{
	private String name;
	private String node1;
	private String node2;
	private double capacitance;

	public SpiceCapacitor(final String name, final String node1, final String node2, final double capacitance)
	{
		this.name = name;
		this.node1 = node1;
		this.node2 = node2;
		this.capacitance = capacitance;
	}

	@Override
	public String getSpiceString()
	{
		sb = new StringBuilder();

		if (name.charAt(0) != 'C')
		{
			sb.append("C");
		}
		sb.append(name);
		addSpacer(sb);
		sb.append(node1);
		addSpacer(sb);
		sb.append(node2);
		addSpacer(sb);
		sb.append(capacitance);

		return sb.toString();
	}
}
