package utils.autoUnitTestUtil.cfg;

import utils.autoUnitTestUtil.utils.Utils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IfStatement;

public class CfgContinueStatementNode extends CfgNode
{
    public CfgContinueStatementNode() {}

    @Override
    public String markContent(String testPath) {
        StringBuilder content = new StringBuilder("");
        content.append(getStartPosition()).append(getClass().getSimpleName()).append("{StartAt:" + getStartPosition()+ ",").append("EndAt:" + getEndPosition());
        return Utils.getWriteToTestPathContent(String.valueOf(content), testPath);
    }

    @Override
    public String getContentReport() {
        ASTNode ast = getAst();
        if (ast instanceof IfStatement) return ((IfStatement) ast).getExpression().toString();
        return getContent();
    }
}
