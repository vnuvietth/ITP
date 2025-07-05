package utils.ITP4Java.ITPTestDriver;

import org.eclipse.jdt.core.dom.*;
import utils.ITP4Java.common.ITPUtils;
import utils.ITP4Java.common.constants;
import utils.autoUnitTestUtil.dataStructure.ParamTestData;
import utils.autoUnitTestUtil.dataStructure.TestData;
import utils.autoUnitTestUtil.parser.ASTHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ITP4JavaV0TestDriverGenerator {
    private ITP4JavaV0TestDriverGenerator() {
    }

    public static void generateTestDriver(MethodDeclaration method, ASTHelper.Coverage coverage) {
        StringBuilder result = new StringBuilder();

        String testDriverTemplateContent = readTestDriverTemplate();

        String clonedMethod = createCloneMethod(method, coverage);

        String templateContent = testDriverTemplateContent.replace(constants.INSTRUMENTED_TESTING_UNIT_PLACEHOLDER, clonedMethod)
                .replace(constants.ITP_V0_TEST_DATA_FILE_PATH_PLACEHOLDER, constants.ITP_V0_TEST_DATA_FILE_PATH_FOR_TEST_DRIVER)
                .replace(constants.EXECUTION_RESULT_PATH_PLACEHOLDER, constants.EXECUTION_RESULT_PATH);

        String testDataCallingString = generateTestDataReader(method);

        String templateContentWithTestDataReading = templateContent.replace(constants.TEST_DATA_READING_PLACEHOLDER,testDataCallingString);

//        String unitCalling = generateTestRunner(method.getName().toString(), testData);
//
//        String templateWithUnitCalling = templateContentWithTestDataReading.replace(constants.UNIT_CALLING_PLACEHOLDER, unitCalling);

        result.append(templateContentWithTestDataReading);

        ITPUtils.writeToFile(String.valueOf(result), constants.ITP_V0_TEST_DRIVER_PATH, false);

    }

    private static String readTestDriverTemplate() {
        try
        {
            return Files.readString(Path.of(constants.ITP_V0_TEST_DRIVER_TEMPLATE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateTestDataReader(MethodDeclaration method) {
        StringBuilder testDataReader = new StringBuilder();
        StringBuilder unitCaller = new StringBuilder();
        unitCaller.append("Object output = ").append(method.getName().toString()).append("(");


        List paramList = method.parameters();

        for(int i = 0; i < method.parameters().size(); i++) {
            String param = "String param" + i + " = (String) jsonObject.get(\"" + ((SingleVariableDeclaration)(method.parameters().get(i))).getName() + "\");";

            if (i == 0) {
                testDataReader.append(param + "\n");
            }
            else {
                testDataReader.append("        " + param + "\n");
            }

            testDataReader.append("        System.out.println(\"" + ((SingleVariableDeclaration)(method.parameters().get(i))).getName() + " = \" " + " + param" + i + ");\n");

            SingleVariableDeclaration paramData = (SingleVariableDeclaration)paramList.get(i);

            if(paramData.getType().toString().equals("char")) {
                unitCaller.append("param" + i).append(".charAt(0)");
            } else if(paramData.getType().toString().equals("String")) {
                unitCaller.append("param" + i);
            } else if (paramData.getType().toString().equals("int")) {
                unitCaller.append("Integer.parseInt(param" + i + ")");
            } else if (paramData.getType().toString().equals("double")) {
                unitCaller.append("Double.parseDouble(param" + i + ")");
            } else if (paramData.getType().toString().equals("boolean")) {
                unitCaller.append("Boolean.parseBoolean(param" + i + ")");
            } else if (paramData.getType().toString().equals("long")) {
                unitCaller.append("Long.parseLong(param" + i + ")");
            } else if (paramData.getType().toString().equals("float")) {
                unitCaller.append("Float.parseFloat(param" + i + ")");
            }
            if(i != paramList.size() - 1) unitCaller.append(", ");
        }
        testDataReader.append("\n");

        unitCaller.append(");\n");

        return testDataReader + "        " + unitCaller.toString();
    }

    private static String generateTestRunner(String methodName, TestData testData) {
        StringBuilder result = new StringBuilder();
        result.append("Object output = ").append(methodName).append("(");

        List<ParamTestData> paramList = testData.getParamList();

        for(int i = 0; i < paramList.size(); i++) {
            ParamTestData param = paramList.get(i);

            if(param.getValue() instanceof Character) {
                result.append("'").append(param.getValue()).append("'");
            } else {
                result.append(param.getValue());
            }
            if(i != paramList.size() - 1) result.append(", ");
        }
        result.append(");\n");
        return result.toString();
    }

    private static String generateUtilities(MethodDeclaration method, ASTHelper.Coverage coverage) {
        StringBuilder result = new StringBuilder();

        // Generate mark method
        result.append(
                "    private static boolean mark(String statement, boolean isTrueCondition, boolean isFalseCondition) {\n" +
                        "       StringBuilder markResult = new StringBuilder();\n\n" +
                        "       markResult.append(statement).append(\"===\");\n\n" +
                        "       markResult.append(isTrueCondition).append(\"===\");\n\n" +
                        "       markResult.append(isFalseCondition).append(\"---end---\");\n\n" +
                        "       writeDataToFile(markResult.toString(), \"src/main/java/utils/autoUnitTestUtil/concreteExecuteResult.txt\", true);\n\n" +
                        "       if (!isTrueCondition && !isFalseCondition)\n" +
                        "          return true;\n\n" +
                        "       return !isFalseCondition;\n\n" +
                        "    }\n"
        );

        // Generate writeDataToFile method
        result.append(
                "    private static void writeDataToFile(String data, String path, boolean append) {\n" +
                        "       try {\n" +
                        "          FileWriter writer = new FileWriter(path, append);\n" +
                        "          writer.write(data);\n" +
                        "          writer.close();\n" +
                        "       }\n" +
                        "       catch(Exception e) {\n" +
                        "          e.printStackTrace();\n" +
                        "       }\n" +
                        "    }\n"
        );

        // Generate testing method with instruments
        result.append(createCloneMethod(method, coverage));

        return result.toString();
    }

    private static String createCloneMethod(MethodDeclaration method, ASTHelper.Coverage coverage) {
        StringBuilder cloneMethod = new StringBuilder();

        cloneMethod.append("public static ").append(method.getReturnType2()).append(" ").append(method.getName()).append("(");
        List<ASTNode> parameters = method.parameters();
        for (int i = 0; i < parameters.size(); i++) {
            cloneMethod.append(parameters.get(i));
            if (i != parameters.size() - 1) cloneMethod.append(", ");
        }
        cloneMethod.append(")\n");

        cloneMethod.append(generateCodeForBlock(method.getBody(), coverage)).append("\n");

        return cloneMethod.toString();
    }

    private static String generateCodeForOneStatement(ASTNode statement, String markMethodSeparator, ASTHelper.Coverage coverage) {
        if (statement == null) {
            return "";
        }

        if (statement instanceof Block) {
            return generateCodeForBlock((Block) statement, coverage);
        } else if (statement instanceof IfStatement) {
            return generateCodeForIfStatement((IfStatement) statement, coverage);
        } else if (statement instanceof ForStatement) {
            return generateCodeForForStatement((ForStatement) statement, coverage);
        } else if (statement instanceof WhileStatement) {
            return generateCodeForWhileStatement((WhileStatement) statement, coverage);
        } else if (statement instanceof DoStatement) {
            return generateCodeForDoStatement((DoStatement) statement, coverage);
        } else {
            return generateCodeForNormalStatement(statement, markMethodSeparator);
        }

    }

    private static String generateCodeForBlock(Block block, ASTHelper.Coverage coverage) {
        StringBuilder result = new StringBuilder();
        List<ASTNode> statements = block.statements();

        result.append("    {\n");
        for (int i = 0; i < statements.size(); i++) {
            result.append("        " + generateCodeForOneStatement(statements.get(i), ";", coverage));
        }
        result.append("    }\n");

        return result.toString();
    }

    private static String generateCodeForIfStatement(IfStatement ifStatement, ASTHelper.Coverage coverage) {
        StringBuilder result = new StringBuilder();

        result.append("    if (").append(generateCodeForCondition(ifStatement.getExpression(), coverage)).append(")\n");
        result.append("    {\n");
        result.append("        " + generateCodeForOneStatement(ifStatement.getThenStatement(), ";", coverage));
        result.append("    }\n");


        String elseCode = generateCodeForOneStatement(ifStatement.getElseStatement(), ";", coverage);
        if (!elseCode.equals("")) {
            result.append("    else {\n").append(elseCode).append("}\n");
        }

        return result.toString();
    }

    private static String generateCodeForForStatement(ForStatement forStatement, ASTHelper.Coverage coverage) {
        StringBuilder result = new StringBuilder();

        // Initializers
        List<ASTNode> initializers = forStatement.initializers();
        for (ASTNode initializer : initializers) {
            result.append(generateCodeForMarkMethod(initializer, ";"));
        }
        result.append("    for (");
        for (int i = 0; i < initializers.size(); i++) {
            result.append(initializers.get(i));
            if (i != initializers.size() - 1) result.append(", ");
        }

        // Condition
        result.append("; ");
        result.append("" + generateCodeForCondition(forStatement.getExpression(), coverage));

        // Updaters
        result.append("; ");
        List<ASTNode> updaters = forStatement.updaters();
        for (int i = 0; i < updaters.size(); i++) {
            result.append(generateCodeForOneStatement(updaters.get(i), ",", coverage));
            if (i != updaters.size() - 1) result.append(", ");
        }

        // Body
        result.append("    ) {\n\n");
        result.append("        " + generateCodeForOneStatement(forStatement.getBody(), ";", coverage));
        result.append("    }\n\n");

        return result.toString();
    }

    private static String generateCodeForWhileStatement(WhileStatement whileStatement, ASTHelper.Coverage coverage) {
        StringBuilder result = new StringBuilder();

        // Condition
        result.append("while (");
        result.append(generateCodeForCondition(whileStatement.getExpression(), coverage));
        result.append(") {\n\n");

        result.append("        " + generateCodeForOneStatement(whileStatement.getBody(), ";", coverage));
        result.append("    }\n\n");

        return result.toString();
    }

    private static String generateCodeForDoStatement(DoStatement doStatement, ASTHelper.Coverage coverage) {
        StringBuilder result = new StringBuilder();

        // Do body
        result.append("do {");
        result.append(generateCodeForOneStatement(doStatement.getBody(), ";", coverage));
        result.append("}\n");

        // Condition
        result.append("while (");
        result.append(generateCodeForCondition(doStatement.getExpression(), coverage));
        result.append(");\n");

        return result.toString();
    }

    private static String generateCodeForNormalStatement(ASTNode statement, String markMethodSeparator) {
        StringBuilder result = new StringBuilder();

        result.append("        " + generateCodeForMarkMethod(statement, markMethodSeparator));
        result.append(statement);

        return result.toString();
    }

    private static String generateCodeForMarkMethod(ASTNode statement, String markMethodSeparator) {
        StringBuilder result = new StringBuilder();

        String stringStatement = statement.toString();
        StringBuilder newStatement = new StringBuilder();

        // Rewrite Statement for mark method
        for (int i = 0; i < stringStatement.length(); i++) {
            char charAt = stringStatement.charAt(i);

            if (charAt == '\n') {
                newStatement.append("\\n");
                continue;
            } else if (charAt == '"') {
                newStatement.append("\\").append('"');
                continue;
            } else if (i != stringStatement.length() - 1 && charAt == '\\' && stringStatement.charAt(i + 1) == 'n') {
                newStatement.append("\" + \"").append("\\n").append("\" + \"");
                i++;
                continue;
            }

            newStatement.append(charAt);
        }

        result.append("mark(\"").append(newStatement).append("\", false, false)").append(markMethodSeparator).append("\n");

        return result.toString();
    }

    private static String generateCodeForCondition(Expression condition, ASTHelper.Coverage coverage) {
        if(coverage == ASTHelper.Coverage.MCDC) {
            return generateCodeForConditionForMCDCCoverage(condition);
        } else if(coverage == ASTHelper.Coverage.BRANCH || coverage == ASTHelper.Coverage.STATEMENT) {
            return generateCodeForConditionForBranchAndStatementCoverage(condition);
        } else {
            throw new RuntimeException("Invalid coverage!");
        }
    }

    private static String generateCodeForConditionForBranchAndStatementCoverage(Expression condition) {
        return "((" + condition + ") && mark(\"" + condition + "\", true, false))" +
                " || mark(\"" + condition + "\", false, true)";
    }

    private static String generateCodeForConditionForMCDCCoverage(Expression condition) {
        StringBuilder result = new StringBuilder();

        if (condition instanceof InfixExpression && isSeparableOperator(((InfixExpression) condition).getOperator())) {
            InfixExpression infixCondition = (InfixExpression) condition;

            result.append("(").append(generateCodeForConditionForMCDCCoverage(infixCondition.getLeftOperand())).append(") ").append(infixCondition.getOperator()).append(" (");
            result.append(generateCodeForConditionForMCDCCoverage(infixCondition.getRightOperand())).append(")");

            List<ASTNode> extendedOperands = infixCondition.extendedOperands();
            for (ASTNode operand : extendedOperands) {
                result.append(" ").append(infixCondition.getOperator()).append(" ");
                result.append("(").append(generateCodeForConditionForMCDCCoverage((Expression) operand)).append(")");
            }
        } else {
            result.append("((").append(condition).append(") && mark(\"").append(condition).append("\", true, false))");
            result.append(" || mark(\"").append(condition).append("\", false, true)");
        }

        return result.toString();
    }

    private static boolean isSeparableOperator(InfixExpression.Operator operator) {
        return operator.equals(InfixExpression.Operator.CONDITIONAL_OR) ||
                operator.equals(InfixExpression.Operator.OR) ||
                operator.equals(InfixExpression.Operator.CONDITIONAL_AND) ||
                operator.equals(InfixExpression.Operator.AND);
    }
}
