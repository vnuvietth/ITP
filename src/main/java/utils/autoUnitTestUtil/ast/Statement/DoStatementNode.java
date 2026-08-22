package utils.autoUnitTestUtil.ast.Statement;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import utils.autoUnitTestUtil.cfg.CfgNode;
import utils.autoUnitTestUtil.cfg.utils.CfgUtils;
import utils.autoUnitTestUtil.algorithms.SymbolicExecution;

public class DoStatementNode extends StatementNode {
    public static void replaceMethodInvocationWithStub(DoStatement originDoStatement, MethodInvocation originMethodInvocation, ASTNode replacement) {
        if (originDoStatement.getExpression() == originMethodInvocation){
            originDoStatement.setExpression((Expression) replacement);

            CfgNode currentCfgNode = SymbolicExecution.getCurrentCfgNode();
            if (currentCfgNode != null) {
                currentCfgNode.setAst(replacement);
            } else {
                CfgUtils.getCurrentCfgNode().setAst(replacement);
            }
        }
        // body
    }
}
