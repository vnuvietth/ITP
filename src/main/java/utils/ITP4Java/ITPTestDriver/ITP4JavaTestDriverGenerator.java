package utils.ITP4Java.ITPTestDriver;

import controller.ITP4JavaController;
import org.eclipse.jdt.core.dom.*;
import utils.ITP4Java.common.ITPUtils;
import utils.ITP4Java.common.constants;
import utils.autoUnitTestUtil.cfg.*;
import utils.autoUnitTestUtil.dataStructure.ParamTestData;
import utils.autoUnitTestUtil.dataStructure.TestData;
import utils.autoUnitTestUtil.parser.ASTHelper;
import utils.autoUnitTestUtil.utils.Utils;
import utils.cloneProjectUtil.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ITP4JavaTestDriverGenerator {
    private ITP4JavaTestDriverGenerator() {
    }

    public static void generateITPTestDriver(String clonedJavaDirPath, ITP4JavaController.Coverage coverage, StringBuilder importStatement) {
        StringBuilder result = new StringBuilder();

        String testDriverTemplateContent = readTestDriverTemplate();

        String templateContent = testDriverTemplateContent.replace(constants.ITP_TEST_DATA_FILE_PATH_PLACEHOLDER, constants.ITP_TEST_DATA_FILE_PATH_FOR_TEST_DRIVER)
                .replace(constants.EXECUTION_RESULT_PATH_PLACEHOLDER, constants.EXECUTION_RESULT_PATH);

        List<File> files = Utils.getJavaFiles(clonedJavaDirPath);

        StringBuilder allUnitCallingBlocks = new StringBuilder();


//        String importStatement = "";

        for (File file : files) {

//            importStatement += "import " + getClassName(file.getAbsolutePath()) + ";\n";

//            System.out.println("importStatement: " + importStatement);

            if (file.getAbsolutePath().equals("E:\\IdeaProjects\\testDriver\\uploadedProject\\Refactored-TheAlgorithms-Java\\src\\main\\java\\com\\thealgorithms\\ciphers\\Blowfish.java") ||
                    file.getAbsolutePath().equals("E:\\IdeaProjects\\testDriver\\uploadedProject\\Refactored-TheAlgorithms-Java\\src\\main\\java\\com\\thealgorithms\\ciphers\\AffineCipher.java"))
            {
                int i = 0;
                System.out.println(file.getAbsolutePath());
            }

            allUnitCallingBlocks.append("        //All units of file: " + file.getAbsolutePath().replace("\\", "\\\\") + "\n");
            System.out.println("ITP4JavaTestDriverGenerator:        //All units of file: " + file.getAbsolutePath().replace("\\", "\\\\") + "\n");

            String absolutePath = file.getAbsolutePath();

            List<ASTNode> methodList = getMethodList(absolutePath);

            for (ASTNode method : methodList) {

                System.out.println("method name: " + ((MethodDeclaration) method).getName().getIdentifier());

                if (((MethodDeclaration) method).isConstructor() || ((MethodDeclaration) method).getName().getIdentifier().equals("main") ||
                        getMethodSignature((MethodDeclaration) method).equals("static void writeDataToFile(String,String,boolean)") ||
                        (getMethodSignature((MethodDeclaration) method).equals("static boolean mark(String,boolean,boolean)")) ||
                        !(getMethodAccessModifier((MethodDeclaration) method).equals("public"))
                )
                    continue;

                boolean isSimpleUnit = ITP4JavaTestDriverGenerator.isSimpleUnit((MethodDeclaration) method);

                if (isSimpleUnit) {
                    String unitCallingBlock = generateTestDataReader((MethodDeclaration) method, file);

                    allUnitCallingBlocks.append(unitCallingBlock + "\n\n");
                }

            }

//            break;//xet 1 file trước rồi làm những file khác sau
        }

        templateContent = templateContent.replace(constants.UNIT_CALLING_BLOCK_PLACEHOLDER, allUnitCallingBlocks);


        templateContent = templateContent.replace(constants.ITP_IMPORT_PLACEHOLDER, importStatement);

        ITPUtils.writeToFile(String.valueOf(templateContent), constants.ITP_TEST_DRIVER_PATH, false);

    }

    public static boolean isSimpleUnit(MethodDeclaration method) {
        System.out.println(method.toString());

        List<ASTNode> statements = method.getBody().statements();

        boolean isSimpleUnit = true;
        for (ASTNode statement : statements) {
            boolean isSimpleStatement = isSimpleStatement(statement);

            if (!isSimpleStatement) {
                isSimpleUnit = false;
                return isSimpleUnit;
            }
        }

        boolean isSimpleParameter = true;

        if (((MethodDeclaration) method).parameters().isEmpty())
        {
            return false;
        }
        else
        {
            List<ASTNode> parameters = ((MethodDeclaration) method).parameters();

            for (ASTNode parameter : parameters) {
                System.out.println(parameter.toString());

                if (!((SingleVariableDeclaration) parameter).getType().isPrimitiveType())
                {
                    if (!((SingleVariableDeclaration) parameter).getType().toString().equals("String"))
                    {
                        isSimpleParameter = false;
                        return isSimpleParameter;
                    }
                }
            }
        }

        return true;
    }

    private static Type getType(SingleVariableDeclaration parameter) {
        return parameter.getType();
    }

    public static boolean isSimpleStatement(ASTNode statement) {
        boolean isSimpleStatement = true;

        if (statement instanceof SwitchStatement) {
            Expression switchExpression = statement.getAST().newConditionalExpression();

            isSimpleStatement = isSimpleStatement(switchExpression);

            return isSimpleStatement;

        } else if (statement instanceof IfStatement) {

            Expression ifConditionAST = ((IfStatement) statement).getExpression();

            boolean isIfConditionSimpleStatement = isSimpleStatement(ifConditionAST);

            Statement thenAST = ((IfStatement) statement).getThenStatement();

            boolean isThenSimpleStatement = isSimpleStatement(thenAST);

            Statement elseAST = ((IfStatement) statement).getElseStatement();

            boolean isElseSimpleStatement = isSimpleStatement(elseAST);

            isSimpleStatement = isIfConditionSimpleStatement && isThenSimpleStatement && isElseSimpleStatement;

            return isSimpleStatement;

        } else if (statement instanceof ForStatement) {

            List initializers = ((ForStatement) statement).initializers();

            boolean isInitializerSimpleStatement = true;
            for (int i = 0; i < initializers.size(); i++) {
                if (initializers.get(i) instanceof VariableDeclarationExpression) {
                    isInitializerSimpleStatement = isSimpleStatement((VariableDeclarationExpression) initializers.get(i));
                } else if (initializers.get(i) instanceof Assignment) {
                    isInitializerSimpleStatement = isSimpleStatement((Assignment) initializers.get(i));
                }

                if (isInitializerSimpleStatement == false) {
                    break;
                }
            }

            //Dieu kien
            Expression forConditionAST = ((ForStatement) statement).getExpression();

            boolean isForConditionSimpleStatement = isSimpleStatement(forConditionAST);

            //Khoi body
            Statement bodyStatementBlock = ((ForStatement) statement).getBody();

            boolean isBodySimpleStatement = isSimpleStatement(bodyStatementBlock);

            //Updater
            List updaters = ((ForStatement) statement).updaters();

            boolean isUpdaterSimpleStatement = true;

            for (int i = 0; i < updaters.size(); i++) {
                CfgNormalNode normalNode = new CfgNormalNode();

                if (updaters.get(i) instanceof PostfixExpression) {
                    isUpdaterSimpleStatement = true;
                } else if (updaters.get(i) instanceof Assignment) {
                    isUpdaterSimpleStatement = isSimpleStatement((Assignment) updaters.get(i));
                }

                if (isUpdaterSimpleStatement == false) {
                    break;
                }
            }

            isSimpleStatement = isInitializerSimpleStatement && isForConditionSimpleStatement && isUpdaterSimpleStatement;
            return isSimpleStatement;

        } else if (statement instanceof EnhancedForStatement) {

            //Khoi expression
            Expression expressionAST = ((EnhancedForStatement) statement).getExpression();

            boolean isExpressionASTSimpleStatement = isSimpleStatement(expressionAST);

            //Khoi parameter
            SingleVariableDeclaration parameterAST = ((EnhancedForStatement) statement).getParameter();

            boolean isParameterASTSimpleStatement = isSimpleStatement(parameterAST);

            //Khoi body
            Statement bodyStatementBlock = ((EnhancedForStatement) statement).getBody();

            boolean isBodySimpleStatement = isSimpleStatement(bodyStatementBlock);

            isSimpleStatement = isExpressionASTSimpleStatement && isParameterASTSimpleStatement && isBodySimpleStatement;

            return isSimpleStatement;

        } else if (statement instanceof WhileStatement) {
            //Dieu kien
            Expression whileConditionAST = ((WhileStatement) statement).getExpression();

            boolean isWhileConditionSimpleStatement = isSimpleStatement(whileConditionAST);

            //Khoi body
            Statement bodyStatementBlock = ((WhileStatement) statement).getBody();

            boolean isBodySimpleStatement = isSimpleStatement(bodyStatementBlock);

            isSimpleStatement = isWhileConditionSimpleStatement && isBodySimpleStatement;

            return isSimpleStatement;
        } else if (statement instanceof DoStatement) {
            //Khoi body
            Statement bodyStatementBlock = ((DoStatement) statement).getBody();

            boolean isBodySimpleStatement = isSimpleStatement(bodyStatementBlock);

            //Dieu kien
            Expression doConditionAST = ((DoStatement) statement).getExpression();

            boolean isDoConditionASTSimpleStatement = isSimpleStatement(doConditionAST);

            isSimpleStatement = isBodySimpleStatement && isDoConditionASTSimpleStatement;

            return isSimpleStatement;
        } else if (statement instanceof Block) {

            isSimpleStatement = false;

            List childStatements = ((Block) statement).statements();

            for (int i = 0; i < childStatements.size(); i++) {
                isSimpleStatement = isSimpleStatement((Statement)childStatements.get(i));

                if (!isSimpleStatement) {
                    break;
                }
            }

            return isSimpleStatement;

        } else if (statement instanceof ExpressionStatement) {

            if (((ExpressionStatement) statement).getExpression() instanceof Assignment) {
                boolean isLeftHandSideSimpleStatement = isSimpleStatement(((Assignment) ((ExpressionStatement) statement).getExpression()).getLeftHandSide());
                boolean isRightHandSideSimpleStatement = isSimpleStatement(((Assignment) ((ExpressionStatement) statement).getExpression()).getRightHandSide());

                return isLeftHandSideSimpleStatement && isRightHandSideSimpleStatement;
            }
            else {
                isSimpleStatement = isSimpleStatement(((ExpressionStatement) statement).getExpression());

                return isSimpleStatement;
            }
        }
        else if (statement instanceof InfixExpression) {
            boolean isLeftHandSideSimpleStatement = isSimpleStatement(((InfixExpression) statement).getLeftOperand());
            boolean isRightHandSideSimpleStatement = isSimpleStatement(((InfixExpression) statement).getRightOperand());

            return isLeftHandSideSimpleStatement && isRightHandSideSimpleStatement;

        }
        else if (statement instanceof PostfixExpression) {
            boolean isOperandSimpleStatement = isSimpleStatement(((PostfixExpression)statement).getOperand());

            return isOperandSimpleStatement;

        }
        else if (statement instanceof PrefixExpression) {
            boolean isOperandSimpleStatement = isSimpleStatement(((PrefixExpression)statement).getOperand());

            return isOperandSimpleStatement;

        }
        else if (statement instanceof ReturnStatement) {

//            isSimpleStatement = isSimpleStatement(statement);

            boolean isExpressionASTSimpleStatement = isSimpleStatement(((ReturnStatement) statement).getExpression());

            return isExpressionASTSimpleStatement;

        } else if (statement instanceof VariableDeclarationStatement) {

            List fragments = ((VariableDeclarationStatement)statement).fragments();

            isSimpleStatement = true;

            for (int i = 0; i < fragments.size(); i++) {
                isSimpleStatement = isSimpleStatement(((VariableDeclarationFragment)(ASTNode) fragments.get(i)).getInitializer());

                if (!isSimpleStatement) {
                    break;
                }
            }

            return isSimpleStatement;

        }
        if (statement instanceof VariableDeclarationExpression) {

            List fragments = ((VariableDeclarationExpression)statement).fragments();

            isSimpleStatement = true;

            for (int i = 0; i < fragments.size(); i++) {
                isSimpleStatement = isSimpleStatement(((VariableDeclarationFragment)(ASTNode) fragments.get(i)).getInitializer());

                if (!isSimpleStatement) {
                    break;
                }
            }

            return isSimpleStatement;

        }
        else if (statement instanceof MethodInvocation) {
            return false;
        } else if (statement instanceof BreakStatement) {

            isSimpleStatement = true;

            return isSimpleStatement;

        } else if (statement instanceof ContinueStatement) {

            isSimpleStatement = true;

            return isSimpleStatement;

        }
        else {

            isSimpleStatement = true;

            return isSimpleStatement;

        }
//        return false;
    }

    public static String getMethodSignature(MethodDeclaration method) {

        StringBuilder signature = new StringBuilder();
        String methodName = method.getName().getIdentifier();

        List<ASTNode> parameterTypes = method.parameters();

        if (method.getReturnType2() == null)
        {
            return methodName;
        }

        String returnType = method.getReturnType2().toString();
        String staticStr = "";

        if (method.modifiers().size() > 1 && method.modifiers().get(1).toString().contains("static")) {
            staticStr = "static ";
        }

        if (method.modifiers().size() == 1)
        {
            signature.append(method.modifiers().get(0) + " ").append(staticStr).append(returnType).append(" ").append(methodName).append("(");
        }
        else
        {
            signature.append(staticStr).append(returnType).append(" ").append(methodName).append("(");
        }

        for (int i = 0; i < parameterTypes.size(); i++) {
            signature.append(((SingleVariableDeclaration) parameterTypes.get(i)).getType() + " " + ((SingleVariableDeclaration) parameterTypes.get(i)).getName());
            if (i < parameterTypes.size() - 1) {
                signature.append(",");
            }
        }
        signature.append(")");

        return signature.toString();
    }

    public static String getMethodAccessModifier(MethodDeclaration method) {

        if (method.modifiers().size() > 1)
        {
            return method.modifiers().get(0).toString();
        }
        if (method.modifiers().size() == 1)
        {
            if (!method.modifiers().get(0).equals("static"))
            {
                return method.modifiers().get(0).toString();
            }
            else
            {
                return "";
            }
        }
        else
        {
            return "";
        }
    }

    public static String getClassName(String fileName) {
        CompilationUnit compilationUnit = null;
        try {
            compilationUnit = Parser.parseFileToCompilationUnit(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final String[] className = {""};
        ASTVisitor methodsVisitor = new ASTVisitor() {
            @Override
            public boolean visit(TypeDeclaration node) {
                className[0] = node.getName().toString();
                return true;
            }
        };
        compilationUnit.accept(methodsVisitor);

        return className[0];
    }

    public static List<ASTNode> getMethodList(String fileName) {
        CompilationUnit compilationUnit = null;
        try {
            compilationUnit = Parser.parseFileToCompilationUnit(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<ASTNode> methods = new ArrayList<>();
        ASTVisitor methodsVisitor = new ASTVisitor() {
            @Override
            public boolean visit(TypeDeclaration node) {
                for (MethodDeclaration method : node.getMethods()) {
//                    if (!method.isConstructor()) {
                    methods.add(method);
//                    }
                }
                return true;
            }
        };
        compilationUnit.accept(methodsVisitor);

        return methods;
    }

//    public static void generateTestDriver(MethodDeclaration method, TestData testData, ITP4JavaController.Coverage coverage) {
//        StringBuilder result = new StringBuilder();
//
//        String testDriverTemplateContent = readTestDriverTemplate();
//
//        String clonedMethod = createCloneMethod(method, coverage);
//
//        String templateContent = testDriverTemplateContent.replace(constants.INSTRUMENTED_TESTING_UNIT_PLACEHOLDER, clonedMethod)
//                .replace(constants.ITP_TEST_DATA_FILE_PATH_PLACEHOLDER, constants.ITP_TEST_DATA_FILE_PATH_FOR_TEST_DRIVER)
//                .replace(constants.EXECUTION_RESULT_PATH_PLACEHOLDER, constants.EXECUTION_RESULT_PATH);
//
//        String testDataCallingString = generateTestDataReader(method);
//
//        String templateContentWithTestDataReading = templateContent.replace(constants.UNIT_CALLING_BLOCK_PLACEHOLDER,testDataCallingString);
//
//        String fileName = "E:\\IdeaProjects\\NTD-Paper\\src\\main\\uploadedProject\\Units-From-Leetcode-Java-Solutions\\src\\main\\java\\ArmstrongNumber.java";
//        String methodIdentifier = "abc";
//
//
////        String templateWithUniCalling = templateContentWithTestDataReading.replace(constants.PASSING_PARAMETER_PLACEHOLDER, "\"" + fileName + "\", \"" + methodIdentifier + "\"");
//

    /// /        String unitCalling = generateTestRunner(method.getName().toString(), testData);
    /// /
    /// /        String templateWithUnitCalling = templateContentWithTestDataReading.replace(constants.UNIT_CALLING_PLACEHOLDER, unitCalling);
//
//        result.append(templateContentWithTestDataReading);
//
//        ITPUtils.writeToFile(String.valueOf(result), constants.ITP_TEST_DRIVER_PATH, false);
//
//    }
    private static String readTestDriverTemplate() {
        try {
            return Files.readString(Path.of(constants.ITP_TEST_DRIVER_TEMPLATE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateTestDataReader(MethodDeclaration method, File file) {
        StringBuilder testDataReader = new StringBuilder();
        StringBuilder unitCaller = new StringBuilder();

        Boolean isStatic = false;
        String className = ((TypeDeclaration) method.getParent()).getName().getIdentifier();

//        System.out.println("className: " + className);

        if (method.modifiers().size() > 1 && method.modifiers().get(1).toString().contains("static")) {
            isStatic = true;
        }
        else if (method.modifiers().size() == 1)
        {
            if (method.modifiers().get(0).toString().equals("static"))
            {
                isStatic = true;
            }
        }

        if (method.getReturnType2().toString().contains("void")) {
            if (isStatic) {
                unitCaller.append("        ").append(className + "." + method.getName().toString()).append("(");
            } else {
                unitCaller.append("        ").append(className + " object = new " + className + "();\n");//need to get constructor
                unitCaller.append("        ").append("object." + method.getName().toString()).append("(");
            }
        } else {
            if (isStatic) {
                unitCaller.append("        Object output = ").append(className + "." + method.getName().toString()).append("(");
            } else {
                unitCaller.append("        ").append(className + " object = new " + className + "();\n");
                unitCaller.append("            Object output = ").append("object." + method.getName().toString()).append("(");
            }

        }

        //List<ParamTestData> paramList = testData.getParamList();

        for (int i = 0; i < method.parameters().size(); i++) {
            String param = "String param" + i + " = (String) jsonObject.get(\"" + ((SingleVariableDeclaration) (method.parameters().get(i))).getName() + "\");";

            if (i == 0) {
                testDataReader.append("            " + param + "\n");
            } else {
                testDataReader.append("            " + param + "\n");
            }

            testDataReader.append("            System.out.println(\"" + ((SingleVariableDeclaration) (method.parameters().get(i))).getName() + " = \" " + " + param" + i + ");\n");

            SingleVariableDeclaration paramData = (SingleVariableDeclaration) method.parameters().get(i);

//            System.out.println("paramData.getName() = " + paramData.getName() + "; paramData.getType() = " + paramData.getType());

//            System.out.println("paramData.getType() = " + paramData.getType().toString());

            if (paramData.getType().toString().equals("char")) {
                unitCaller.append("'").append("param" + i).append("'");
            } else if (paramData.getType().toString().equals("String")) {
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
            if (i != method.parameters().size() - 1) unitCaller.append(", ");
        }
        testDataReader.append("\n");

        unitCaller.append(");\n\n");

        if (!method.getReturnType2().toString().contains("void")) {
            unitCaller.append("\t\t\t\t\t\tSystem.out.println(\"output = \"  + output.toString());\n\n");

            unitCaller.append("\t\t\t\t\t\treturn output.toString();\n");
        } else {
            unitCaller.append("\t\t\t\t\t\treturn \"0\";\n");
        }

        String methodSignature = getMethodSignature(method);

        StringBuilder unitcallingBlock = new StringBuilder();
        unitcallingBlock.append("        if (\"" + file.getAbsolutePath().replace("\\", "\\\\") + "\".equals(fileName) && \"" + methodSignature + "\".equals(functionName)) {\n");

        unitcallingBlock.append(
                "\t\t\t\t\t\n" +
                        "            System.out.println(\"Executing unit: " + getMethodSignature(method) + " ...\");\n\n");

        String unitBlock = testDataReader + "    " + unitCaller.toString();

        unitcallingBlock.append(unitBlock);
        unitcallingBlock.append("        }\n\n");

        return unitcallingBlock.toString();
    }

    private static String generateTestRunner(String methodName, TestData testData) {
        StringBuilder result = new StringBuilder();
        result.append("Object output = ").append(methodName).append("(");

        List<ParamTestData> paramList = testData.getParamList();

        for (int i = 0; i < paramList.size(); i++) {
            ParamTestData param = paramList.get(i);

            if (param.getValue() instanceof Character) {
                result.append("'").append(param.getValue()).append("'");
            } else {
                result.append(param.getValue());
            }
            if (i != paramList.size() - 1) result.append(", ");
        }
        result.append(");\n");
        return result.toString();
    }

    private static String generateUtilities(MethodDeclaration method, ITP4JavaController.Coverage coverage) {
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

    private static String createCloneMethod(MethodDeclaration method, ITP4JavaController.Coverage coverage) {
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

    private static String generateCodeForOneStatement(ASTNode statement, String markMethodSeparator, ITP4JavaController.Coverage coverage) {
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

    private static String generateCodeForBlock(Block block, ITP4JavaController.Coverage coverage) {
        StringBuilder result = new StringBuilder();
        List<ASTNode> statements = block.statements();

        result.append("    {\n");
        for (int i = 0; i < statements.size(); i++) {
            result.append("        " + generateCodeForOneStatement(statements.get(i), ";", coverage));
        }
        result.append("    }\n");

        return result.toString();
    }

    private static String generateCodeForIfStatement(IfStatement ifStatement, ITP4JavaController.Coverage coverage) {
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

    private static String generateCodeForForStatement(ForStatement forStatement, ITP4JavaController.Coverage coverage) {
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

    private static String generateCodeForWhileStatement(WhileStatement whileStatement, ITP4JavaController.Coverage coverage) {
        StringBuilder result = new StringBuilder();

        // Condition
        result.append("while (");
        result.append(generateCodeForCondition(whileStatement.getExpression(), coverage));
        result.append(") {\n\n");

        result.append("        " + generateCodeForOneStatement(whileStatement.getBody(), ";", coverage));
        result.append("    }\n\n");

        return result.toString();
    }

    private static String generateCodeForDoStatement(DoStatement doStatement, ITP4JavaController.Coverage coverage) {
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

    private static String generateCodeForCondition(Expression condition, ITP4JavaController.Coverage coverage) {
//        if(coverage == ITP4JavaController.Coverage.MCDC) {
//            return generateCodeForConditionForMCDCCoverage(condition);
//        } else
        if (coverage == ITP4JavaController.Coverage.BRANCH || coverage == ITP4JavaController.Coverage.STATEMENT) {
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
