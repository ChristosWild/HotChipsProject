package project.editor.extractor.components;

public class PowerSupply implements CircuitComponent
{
	private final String posNodeId;
	private final String negNodeId;
	private final int instanceId;

	public PowerSupply(final String posNodeId, final String negNodeId, final int instanceId)
	{
		this.posNodeId = posNodeId;
		this.negNodeId = negNodeId;
		this.instanceId = instanceId;
	}

	public String getPosNodeId()
	{
		return posNodeId;
	}

	public String getNegNodeId()
	{
		return negNodeId;
	}

	public int getInstanceId()
	{
		return instanceId;
	}
}
