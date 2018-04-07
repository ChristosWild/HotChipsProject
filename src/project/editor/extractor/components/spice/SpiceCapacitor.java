package project.editor.extractor.components.spice;

public class SpiceCapacitor implements SpiceComponent
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
		final StringBuilder sb = new StringBuilder();

		if (name.charAt(0) != 'C')
		{
			sb.append("C");
		}
		sb.append(name);
		sb.append(" "); // TODO better way of spacing
		sb.append(node1);
		sb.append(" ");
		sb.append(node2);
		sb.append(" ");
		sb.append(capacitance);

		return sb.toString();
	}
}
