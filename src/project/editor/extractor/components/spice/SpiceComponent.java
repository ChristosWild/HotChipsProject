package project.editor.extractor.components.spice;

public abstract class SpiceComponent // TODO abstract so can implement addSpacer(StringBuilder)
{
	protected StringBuilder sb;

	public abstract String getSpiceString();

	protected void addSpacer(final StringBuilder sb)
	{
		sb.append(" ");
	}
}
