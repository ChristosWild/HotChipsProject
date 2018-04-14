package project.editor.extractor.components;

public class PowerSupply implements CircuitComponent
{
	private final String posNodeId;
	private final String negNodeId;

	public PowerSupply(final String posNodeId, final String negNodeId)
	{
		this.posNodeId = posNodeId;
		this.negNodeId = negNodeId;
	}

	public String getPosNodeId()
	{
		return posNodeId;
	}

	public String getNegNodeId()
	{
		return negNodeId;
	}
}
