package utils.autoUnitTestUtil.cfg;

public class CfgForEachExpressionNode extends CfgNode
{
    public CfgForEachExpressionNode(){}

    private CfgNode hasElementAfterNode = null;
    private CfgNode noMoreElementAfterNode = null;
    private CfgNode parameterNode = null;

    public CfgNode getHasElementAfterNode()
    {
        return hasElementAfterNode;
    }

    public void setHasElementAfterNode(CfgNode hasElementAfterNode)
    {
        this.hasElementAfterNode = hasElementAfterNode;
    }

    public CfgNode getNoMoreElementAfterNode()
    {
        return noMoreElementAfterNode;
    }

    public void setNoMoreElementAfterNode(CfgNode noMoreElementAfterNode)
    {
        this.noMoreElementAfterNode = noMoreElementAfterNode;
    }

    public CfgNode getParameterNode()
    {
        return parameterNode;
    }

    public void setParameterNode(CfgNode parameterNode)
    {
        this.parameterNode = parameterNode;
    }
}
