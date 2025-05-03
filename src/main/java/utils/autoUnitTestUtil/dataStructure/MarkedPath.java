package utils.autoUnitTestUtil.dataStructure;

import utils.autoUnitTestUtil.ast.additionalNodes.Node;
import utils.autoUnitTestUtil.cfg.CfgBoolExprNode;
import utils.autoUnitTestUtil.cfg.CfgNode;
import utils.autoUnitTestUtil.concolicResult.CoveredStatement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MarkedPath {

    private static List<MarkedStatement> markedStatements = new ArrayList<>();
    private static Set<CoveredStatement> fullTestSuiteCoveredStatements;
    private static Set<CoveredStatement> totalCoveredStatement;
    private static Set<CoveredStatement> totalCoveredBranch;

    private MarkedPath() {
    }

    public static boolean markOneStatement(String statement, boolean isTrueCondition, boolean isFalseCondition) {
        addNewStatementToPath(statement, isTrueCondition, isFalseCondition);
        if (!isTrueCondition && !isFalseCondition) return true;
        return !isFalseCondition;
    }

    private static void addNewStatementToPath(String statement, boolean isTrueCondition, boolean isFalseCondition) {
        MarkedStatement markedStatement = new MarkedStatement(statement, isTrueCondition, isFalseCondition);
        markedStatements.add(markedStatement);
    }

    public static void setMarkedStatements(List<MarkedStatement> markedStatements) {
        MarkedPath.markedStatements = markedStatements;
    }

    private static void reset() {
        markedStatements = new ArrayList<>();
    }

    public static void markPathToCFGV2(CfgNode rootNode, List<MarkedStatement> markedStatements) {
        totalCoveredBranch = new HashSet<>();
        totalCoveredStatement = new HashSet<>();

        int i = 0;
        while (rootNode != null && i < markedStatements.size()) {
            // Kiểm tra những CfgNode không có content
            if (rootNode.getContent().equals("")) {
                rootNode.setMarked(true);
                rootNode = rootNode.getAfterStatementNode();
                continue;
            }

            MarkedStatement markedStatement = markedStatements.get(i);
            if (rootNode.getContent().equals(markedStatement.getStatement())) {
//                if (!rootNode.isMarked()) {
//                }
                fullTestSuiteCoveredStatements.add(new CoveredStatement(rootNode.getContent(), rootNode.getLineNumber(), ""));
                totalCoveredStatement.add(new CoveredStatement(rootNode.getContent(), rootNode.getLineNumber(), ""));
                rootNode.setMarked(true);
                markedStatement.setCfgNode(rootNode);
            } else {
                return;
            }

            if (rootNode instanceof CfgBoolExprNode) {
                CfgBoolExprNode boolExprNode = (CfgBoolExprNode) rootNode;
                if (markedStatement.isFalseConditionalStatement()) {
//                    if (!boolExprNode.isFalseMarked()) {
//                    }
                    totalCoveredBranch.add(new CoveredStatement(boolExprNode.getContent(), boolExprNode.getLineNumber(), "false"));
                    boolExprNode.setFalseMarked(true);
                    rootNode = boolExprNode.getFalseNode();
                } else if (markedStatement.isTrueConditionalStatement()) {
//                    if (!boolExprNode.isTrueMarked()) {
//                    }
                    totalCoveredBranch.add(new CoveredStatement(boolExprNode.getContent(), boolExprNode.getLineNumber(), "true"));
                    boolExprNode.setTrueMarked(true);
                    rootNode = boolExprNode.getTrueNode();
                }
                i++;
                continue;
            }

            // Updater
            i++;
            rootNode = rootNode.getAfterStatementNode();
        }
        while (rootNode != null) {
            if (rootNode.getContent().equals("")) {
                rootNode.setMarked(true);
                rootNode = rootNode.getAfterStatementNode();
            }
        }
    }
    public static int getTotalCoveredStatement() {
        return totalCoveredStatement.size();
    }

    public static int getTotalCoveredBranch() {
        return totalCoveredBranch.size();
    }

    public static void resetFullTestSuiteCoveredStatements() {
        fullTestSuiteCoveredStatements = new HashSet<>();
    }

    public static int getFullTestSuiteTotalCoveredStatements() {
        return fullTestSuiteCoveredStatements.size();
    }

    private static List<CfgNode> coveredNodeInPath;

    public static CfgNode findUncoveredStatement(CfgNode rootNode) {
        coveredNodeInPath = new ArrayList<>();
        return findUncoveredStatement(rootNode, null);
    }

    private static CfgNode findUncoveredStatement(CfgNode rootNode, CfgNode duplicateNode) {
        if (rootNode == null) {
            return null;
        }
        if (!coveredNodeInPath.contains(rootNode)) {
            coveredNodeInPath.add(rootNode);
            if(!rootNode.isMarked()) return rootNode;
            if (rootNode instanceof CfgBoolExprNode) {
                CfgBoolExprNode boolExprNode = (CfgBoolExprNode) rootNode;
                CfgNode falseBranchUncoveredNode = findUncoveredStatement(boolExprNode.getFalseNode(), duplicateNode);
                CfgNode trueBranchUncoveredNode = findUncoveredStatement(boolExprNode.getTrueNode(), duplicateNode);
                return falseBranchUncoveredNode == null ? trueBranchUncoveredNode : falseBranchUncoveredNode;
            } else {
                return findUncoveredStatement(rootNode.getAfterStatementNode(), duplicateNode);
            }
        } else {
            return null;
        }
    }

    public static CfgNode findUncoveredBranch(CfgNode rootNode) {
        coveredNodeInPath = new ArrayList<>();
        return findUncoveredBranch(rootNode, null);
    }
//    private static CfgNode findUncoveredBranch(CfgNode rootNode, CfgNode duplicateNode) {
//        if (rootNode == null) {
//            return null;
//        }
//        if (rootNode instanceof CfgBoolExprNode) {
//            CfgBoolExprNode boolExprNode = (CfgBoolExprNode) rootNode;
//
//            if (!boolExprNode.isTrueMarked()) {
//                return boolExprNode.getTrueNode();
//            }
//            if (!boolExprNode.isFalseMarked()) {
//                return boolExprNode.getFalseNode();
//            }
//
////            if (boolExprNode != duplicateNode) {
////                duplicateNode = boolExprNode;
////                return findUncoveredBranch(boolExprNode.getTrueNode(), duplicateNode);
////            } else {
////                return findUncoveredBranch(boolExprNode.getFalseNode(), duplicateNode);
////            }
//            if(coveredNodeInPath.contains(boolExprNode)) {
//                return findUncoveredBranch(boolExprNode.getFalseNode(), duplicateNode);
//            } else {
//                coveredNodeInPath.add(boolExprNode);
//                CfgNode falseBranchUncoveredNode = findUncoveredBranch(boolExprNode.getFalseNode(), duplicateNode);
//                CfgNode trueBranchUncoveredNode = findUncoveredBranch(boolExprNode.getTrueNode(), duplicateNode);
//                return falseBranchUncoveredNode == null ? trueBranchUncoveredNode : falseBranchUncoveredNode;
//            }
//        }
//
//        coveredNodeInPath.add(rootNode);
//        return findUncoveredBranch(rootNode.getAfterStatementNode(), duplicateNode);
//    }

    private static CfgNode findUncoveredBranch(CfgNode rootNode, CfgNode duplicateNode) {
        if (rootNode == null) {
            return null;
        }
        if (!coveredNodeInPath.contains(rootNode)) {
            coveredNodeInPath.add(rootNode);
            if (rootNode instanceof CfgBoolExprNode) {
                CfgBoolExprNode boolExprNode = (CfgBoolExprNode) rootNode;

                if (!boolExprNode.isTrueMarked()) {
                    return boolExprNode.getTrueNode();
                }
                if (!boolExprNode.isFalseMarked()) {
                    return boolExprNode.getFalseNode();
                }

                CfgNode falseBranchUncoveredNode = findUncoveredBranch(boolExprNode.getFalseNode(), duplicateNode);
                CfgNode trueBranchUncoveredNode = findUncoveredBranch(boolExprNode.getTrueNode(), duplicateNode);
                return falseBranchUncoveredNode == null ? trueBranchUncoveredNode : falseBranchUncoveredNode;
            } else {
                return findUncoveredBranch(rootNode.getAfterStatementNode(), duplicateNode);
            }
        } else {
            return null;
        }
    }
}
