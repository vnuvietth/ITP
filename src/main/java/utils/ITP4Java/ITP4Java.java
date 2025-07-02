package utils.ITP4Java;

import controller.ITP4JavaController;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import utils.FilePath;
import utils.ITP4Java.ITPTestDriver.ITP4JavaTestDriverGenerator;
import utils.ITP4Java.ITPTestDriver.ITP4JavaTestDriverRunner;
import utils.ITP4Java.ITPTestDriver.ITP4JavaV0TestDriverRunner;
import utils.ITP4Java.common.constants;
import utils.autoUnitTestUtil.algorithms.FindPath;
import utils.autoUnitTestUtil.algorithms.SymbolicExecution;
import utils.autoUnitTestUtil.cfg.CfgBlockNode;
import utils.autoUnitTestUtil.cfg.CfgEndBlockNode;
import utils.autoUnitTestUtil.cfg.CfgNode;
import utils.autoUnitTestUtil.concolicResult.ConcolicTestData;
import utils.autoUnitTestUtil.concolicResult.ConcolicTestResult;
import utils.autoUnitTestUtil.concolicResult.CoveredStatement;
import utils.autoUnitTestUtil.dataStructure.*;
import utils.autoUnitTestUtil.parser.ASTHelper;
import utils.autoUnitTestUtil.parser.ProjectParser;
import utils.autoUnitTestUtil.testDriver.TestDriverRunner;
import utils.autoUnitTestUtil.testDriver.Utils4TestDriver;
import utils.autoUnitTestUtil.utils.Utils;
import utils.uploadUtil.ConcolicUploadUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static utils.ITP4Java.ITPTestDriver.ITP4JavaTestDriverGenerator.*;
import static utils.ITP4Java.ITPTestDriver.ITP4JavaTestDriverRunner.runTestDriver;

public class ITP4Java {
    private static CompilationUnit compilationUnit;
    private static ArrayList<ASTNode> funcAstNodeList;
    private static CfgNode cfgBeginNode;
    private static CfgEndBlockNode cfgEndNode;
    private static List<ASTNode> parameters;
    private static Class<?>[] parameterClasses;
    private static List<String> parameterNames;
    private static ASTNode testFunc;
    private static String classKey;

    private ITP4Java() {
    }

    private static long totalUsedMem = 0;
    private static long tickCount = 0;


    public static ConcolicTestResult runITP4Project(String path, ITP4JavaController.Coverage coverage, StringBuilder importStatement)
            throws IOException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, ClassNotFoundException, NoSuchFieldException,
            InterruptedException {

//        setup(path, className, methodName);
//        setupCfgTree(coverage);
//        setupParameters(methodName);

        totalUsedMem = 0;
        tickCount = 0;
        Timer T = new Timer(true);

        TimerTask memoryTask = new TimerTask() {
            @Override
            public void run() {
                totalUsedMem += (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
                tickCount += 1;
            }
        };

        T.scheduleAtFixedRate(memoryTask, 0, 1); //0 delay and 5 ms tick

        long startRunTestTime = System.nanoTime();
//        ConcolicTestResult result = startGenerating(coverage);

        String projectName = path;// path.substring(path.lastIndexOf("\\"));

        writeDataToFile("Test result for the selected project: " + projectName + "\n", constants.ITP_TEST_RESULT_FILEPATH, false);

        generateTestDataForProject(path, coverage, importStatement);

        long endRunTestTime = System.nanoTime();

        double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;
        float usedMem = ((float) totalUsedMem) / tickCount / 1024 / 1024;

        writeDataToFile("***************** o0o *****************\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("runTestDuration: " + runTestDuration + " (ms)\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("usedMem: " + usedMem + " (MB)\n", constants.ITP_TEST_RESULT_FILEPATH, true);

//        result.setTestingTime(runTestDuration);
//        result.setUsedMemory(usedMem);

//        return result;
        return null;
    }

    //javac  -d "E:\IdeaProjects\testDriver\target"  -cp "C:\Users\HP\Downloads\UploadedCode\1\target\classes;E:\IdeaProjects\NTD-Paper\src\main\resources\testDriverLibraries\json-simple-1.1.1.jar;"  "E:\IdeaProjects\testDriver\ITP_TestDriver.java"
    // Đường dẫn tới class path thì chứa thư mục clonedProject, nhưng không được bao gồm thư mục clonedProject.


    private static void generateTestDataForProject(String path, ITP4JavaController.Coverage coverage, StringBuilder importStatement) throws IOException, NoSuchFieldException, ClassNotFoundException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ITP4JavaTestDriverGenerator.generateITPTestDriver(path, coverage, importStatement);

        ITP4JavaTestDriverRunner.buildTestDriver();

        List<File> files = Utils.getJavaFiles(path);

        int unitCountForProject = 0;
        int simpleUnitCountForProject = 0;

        double totalCoverage = 0;

        for (File file : files) {
            System.out.println("ITP4Java:        //All units of file: " + file.getAbsolutePath().replace("\\", "\\\\") + "\n");


            if (file.getAbsolutePath().equals("E:\\IdeaProjects\\testDriver\\uploadedProject\\Refactored-TheAlgorithms-Java\\src\\main\\java\\com\\thealgorithms\\audiofilters\\IIRFilter.java") //||
//                    file.getAbsolutePath().equals("E:\\IdeaProjects\\testDriver\\uploadedProject\\Refactored-TheAlgorithms-Java\\src\\main\\java\\com\\thealgorithms\\ciphers\\AffineCipher.java")
            )
            {
                int i = 0;
                System.out.println(file.getAbsolutePath());
            }

            //String clonedMethod = createCloneMethod(method, coverage);
            double totalCoverageForFile = 0;
            int simpleUnitCountForFile = 0;
            int unitCountForFile = 0;

            List<ASTNode> methodList = ITP4JavaTestDriverGenerator.getMethodList(file.getAbsolutePath());

            unitCountForProject += methodList.size();

            writeDataToFile("======================== o0o ========================\n", constants.ITP_TEST_RESULT_FILEPATH, true);
            writeDataToFile("Test result for file: " + file.getAbsolutePath() + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);

            for (ASTNode method : methodList) {

                if (((MethodDeclaration) method).isConstructor() || ((MethodDeclaration) method).getName().getIdentifier().equals("main") ||
                        ITP4JavaTestDriverGenerator.getMethodSignature((MethodDeclaration)method).equals("static void writeDataToFile(String,String,boolean)") ||
                        (ITP4JavaTestDriverGenerator.getMethodSignature((MethodDeclaration)method).equals("static boolean mark(String,boolean,boolean)")) ||
                        !(getMethodAccessModifier((MethodDeclaration)method).equals("public"))
                ) {
                    continue;
                }

                String methodName = ((MethodDeclaration)method).getName().getIdentifier();

                unitCountForFile += 1;

                if (methodName.equals("indexOfRightMostSetBit") ||
                        methodName.equals("bruteforce") ||
                        methodName.equals("isEven") ||
                        methodName.equals("isPowerTwo") ||
                        methodName.equals("differentSigns") ||
                        methodName.equals("reverseBits") ||
                        methodName.equals("flipBit") ||
                        methodName.equals("setBit") ||
                        methodName.equals("clearBit") ||
                        methodName.equals("getBit") ||
                        methodName.equals("binaryToDecimal") ||
                        methodName.equals("convertBinaryToOctal") ||
                        methodName.equals("decimal2octal") ||
                        methodName.equals("convertOctalDigitToBinary") ||
                        methodName.equals("convertOctalToDecimal") ||
                        methodName.equals("minTrials") ||
                        methodName.equals("uniquePaths2") ||
                        methodName.equals("getMaxValue") ||
                        methodName.equals("surfaceAreaCube") ||
                        methodName.equals("surfaceAreaSphere") ||
                        methodName.equals("bpR") ||
                        methodName.equals("surfaceAreaRectangle") ||
                        methodName.equals("surfaceAreaCylinder") ||
                        methodName.equals("surfaceAreaSquare") ||
                        methodName.equals("surfaceAreaTriangle") ||
                        methodName.equals("surfaceAreaParallelogram") ||
                        methodName.equals("surfaceAreaTrapezium") ||
                        methodName.equals("surfaceAreaCircle") ||
                        methodName.equals("surfaceAreaHemisphere") ||
                        methodName.equals("surfaceAreaCone") ||
                        methodName.equals("binomialCoefficient") ||
                        methodName.equals("ceil") ||
                        methodName.equals("factorial") ||
                        methodName.equals("combinationsOptimized") ||
                        methodName.equals("isDudeney") ||
                        methodName.equals("floor") ||
                        methodName.equals("isHarshad") ||
                        methodName.equals("lcm") ||
                        methodName.equals("valOfChar")
                ) //
                {
                    continue;
                }

                boolean isSimpleUnit = ITP4JavaTestDriverGenerator.isSimpleUnit((MethodDeclaration)method);

                if (isSimpleUnit) {
                    simpleUnitCountForProject += 1;
                    simpleUnitCountForFile += 1;

                    writeDataToFile("", constants.EXECUTION_RESULT_PATH, false);//clear file

                    ConcolicTestResult testResult = startGeneratingForOneUnit(file.getAbsolutePath(), (MethodDeclaration) method, coverage);

                    totalCoverage += testResult.getFullCoverage();

                    totalCoverageForFile += testResult.getFullCoverage();
                }
            }

            writeDataToFile("unitCountForFile: " + file.getName() + ": " + unitCountForFile  + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
            writeDataToFile("simpleUnitCountForFile: " + file.getName() + ": " + simpleUnitCountForFile  + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
            writeDataToFile("averageCoverageForFile: " + file.getName() + ": " + (totalCoverageForFile /simpleUnitCountForFile)  + "%\n", constants.ITP_TEST_RESULT_FILEPATH, true);
//            break;

        }

        writeDataToFile("unitCountForProject: " + unitCountForProject + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("simpleUnitCountForProject: " + simpleUnitCountForProject + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);

        writeDataToFile("totalAverageCoverage: " + (totalCoverage /simpleUnitCountForProject)  + "%\n", constants.ITP_TEST_RESULT_FILEPATH, true);


    }

//    public static ConcolicTestResult runFullConcolic(String path, String methodName, String className,
//                                                     ITP4JavaController.Coverage coverage)
//            throws IOException, NoSuchMethodException, InvocationTargetException,
//            IllegalAccessException, ClassNotFoundException, NoSuchFieldException,
//            InterruptedException {
//
//        setup(path, className, methodName);
//        setupCfgTree(coverage);
//        setupParameters(methodName);
//
//        totalUsedMem = 0;
//        tickCount = 0;
//        Timer T = new Timer(true);
//
//        TimerTask memoryTask = new TimerTask() {
//            @Override
//            public void run() {
//                totalUsedMem += (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
//                tickCount += 1;
//            }
//        };
//
//        T.scheduleAtFixedRate(memoryTask, 0, 1); //0 delay and 5 ms tick
//
//        long startRunTestTime = System.nanoTime();
//        ConcolicTestResult result = startGenerating(coverage);
//        long endRunTestTime = System.nanoTime();
//
//        double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;
//        float usedMem = ((float) totalUsedMem) / tickCount / 1024 / 1024;
//
//        result.setTestingTime(runTestDuration);
//        result.setUsedMemory(usedMem);
//
//        return result;
//    }

    private static ConcolicTestResult startGeneratingForOneUnit(String filePath, MethodDeclaration method, ITP4JavaController.Coverage coverage)
            throws InvocationTargetException, IllegalAccessException, ClassNotFoundException,
            NoSuchFieldException, IOException, InterruptedException, NoSuchMethodException {

        setup(filePath, getClassName(filePath), method.getName().toString());
        setupCfgTree(coverage);
        setupParameters(method.getName().toString());

        setUpTestFunc(method.getName().toString());
        setupParameters(method.getName().toString());

        Boolean isTestDriverBuilt = false;
        ConcolicTestResult testResult = new ConcolicTestResult();
        int testCaseID = 1;
//        Object[] evaluatedValues = Utils4TestDriver.createRandomTestData(parameterClasses);
        ITPTestData testData = Utils4TestDriver.createRandomTestData4ITP(parameters, filePath, method);

        writeDataToFile(testData.toJSONString(), constants.ITP_TEST_DATA_FILE_PATH, false);

//        ITP4JavaTestDriverRunner.buildTestDriver();
        runTestDriver();

        List<MarkedStatement> markedStatements = ITP4JavaTestDriverRunner.getCoveredStatement();

        MarkedPath.markPathToCFGV2(cfgBeginNode, markedStatements);

        List<CoveredStatement> coveredStatements = CoveredStatement.switchToCoveredStatementList(markedStatements);

        testResult.addToFullTestData(new ConcolicTestData(testData, coveredStatements,
                TestDriverRunner.getOutput(), TestDriverRunner.getRuntime(), calculateRequiredCoverage(coverage),
                calculateFunctionCoverage(), calculateSourceCodeCoverage(), testCaseID++));

        boolean isTestedSuccessfully = true;
        int i = 5;

        for (CfgNode uncoveredNode = findUncoverNode(cfgBeginNode, coverage); uncoveredNode != null; ) {

            Path newPath = (new FindPath(cfgBeginNode, uncoveredNode, cfgEndNode)).getPath();

            SymbolicExecution solution = new SymbolicExecution(newPath, parameters);

            if (solution.getModel() == null) {
                isTestedSuccessfully = false;
                break;
            }

            testData = Utils4TestDriver.getParameterValue4ITP(parameters, filePath, method);

            writeDataToFile(testData.toJSONString(), constants.ITP_TEST_DATA_FILE_PATH, false);

            runTestDriver();

            markedStatements = ITP4JavaTestDriverRunner.getCoveredStatement();

            MarkedPath.markPathToCFGV2(cfgBeginNode, markedStatements); // CHANGE SOMETHING ELSE

            coveredStatements = CoveredStatement.switchToCoveredStatementList(markedStatements);

            testResult.addToFullTestData(new ConcolicTestData(testData, coveredStatements,
                    TestDriverRunner.getOutput(), TestDriverRunner.getRuntime(), calculateRequiredCoverage(coverage),
                    calculateFunctionCoverage(), calculateSourceCodeCoverage(), testCaseID++));

            uncoveredNode = findUncoverNode(cfgBeginNode, coverage);
            System.out.println("Uncovered Node: " + uncoveredNode);
        }

        if (isTestedSuccessfully) System.out.println("Tested successfully with 100% coverage");
        else System.out.println("Test fail due to UNSATISFIABLE constraint");

        testResult.setFullCoverage(calculateFullTestSuiteCoverage());

        writeDataToFile("\n=========== o0o ===========\n", constants.ITP_TEST_RESULT_FILEPATH, true);

        writeDataToFile("Test result for file: " + filePath.substring(filePath.lastIndexOf("\\") + 1) + ", method: " + getMethodSignature(method) + "\n\n", constants.ITP_TEST_RESULT_FILEPATH, true);

        writeTestResultToFile(testResult, constants.ITP_TEST_RESULT_FILEPATH, true);

        return testResult;
    }

    private static void writeTestResultToFile(ConcolicTestResult testResult, String path, boolean append) {
        try {
            FileWriter writer = new FileWriter(path, append);

            StringBuilder result = new StringBuilder();

            List<ConcolicTestData> testDataList = testResult.getFullTestData();

            for (int i = 0; i < testDataList.size(); i++) {
                result.append("Test no " + i + ": " + testDataList.get(i) + " \n");
            }

            result.append("Full coverage: " + testResult.getFullCoverage() + " \n");
            result.append("Testing time: " + testResult.getTestingTime() + " \n");
            result.append("Memory: " + testResult.getUsedMemory() + " \n");

            writer.write(result.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeDataToFile(String data, String path, boolean append) {
        try {
            if (!append)
            {
                Files.deleteIfExists(Paths.get(path));
            }
            FileWriter writer = new FileWriter(path, append);
            writer.write(data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static CfgNode findUncoverNode(CfgNode cfgNode, ITP4JavaController.Coverage coverage) {
        switch (coverage) {
            case STATEMENT:
                return MarkedPath.findUncoveredStatement(cfgNode);
            case BRANCH:
                return MarkedPath.findUncoveredBranch(cfgNode);
            default:
                throw new RuntimeException("Invalid coverage type");
        }
    }


    private static void setup(String path, String className, String methodName) throws IOException {
        funcAstNodeList = ProjectParser.parseFile(path);
        compilationUnit = ProjectParser.parseFileToCompilationUnit(path);
        classKey = (compilationUnit.getPackage() != null ? compilationUnit.getPackage().getName().toString() : "") + className.replace(".java", "") + "totalStatement";
        setUpTestFunc(methodName);
        MarkedPath.resetFullTestSuiteCoveredStatements();
    }

    private static double calculateFullTestSuiteCoverage() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        String key = getTotalFunctionCoverageVariableName((MethodDeclaration) testFunc, ITP4JavaController.Coverage.STATEMENT);
        int totalFunctionStatement = ConcolicUploadUtil.totalStatementsInUnits.get(key);
        int totalCovered = MarkedPath.getFullTestSuiteTotalCoveredStatements();
        return (totalCovered * 100.0) / totalFunctionStatement;
    }

    private static double calculateRequiredCoverage(ITP4JavaController.Coverage coverage) {
        String key = getTotalFunctionCoverageVariableName((MethodDeclaration) testFunc, coverage);
        int totalFunctionCoverage = 1;
        int totalCovered = 0;
        if (coverage == ITP4JavaController.Coverage.STATEMENT) {
            totalCovered = MarkedPath.getTotalCoveredStatement();
            totalFunctionCoverage = ConcolicUploadUtil.totalStatementsInUnits.get(key);
        } else if (coverage == ITP4JavaController.Coverage.BRANCH) {
            totalCovered = MarkedPath.getTotalCoveredBranch();
            totalFunctionCoverage = ConcolicUploadUtil.totalBranchesInUnits.get(key);
        }
        return (totalCovered * 100.0) / totalFunctionCoverage;
    }

    private static double calculateFunctionCoverage() {
        String key = getTotalFunctionCoverageVariableName((MethodDeclaration) testFunc, ITP4JavaController.Coverage.STATEMENT);
        int totalFunctionStatement = ConcolicUploadUtil.totalStatementsInUnits.get(key);
        int totalCoveredStatement = MarkedPath.getTotalCoveredStatement();
        return (totalCoveredStatement * 100.0) / (totalFunctionStatement * 1.0);
    }

    private static double calculateSourceCodeCoverage() {
        int totalClassStatement = ConcolicUploadUtil.totalStatementsInJavaFile.get(classKey);
        int totalCoveredStatement = MarkedPath.getTotalCoveredStatement();
        return (totalCoveredStatement * 100.0) / (totalClassStatement * 1.0);
    }

    private static void setUpTestFunc(String methodName) {
        for (ASTNode func : funcAstNodeList) {
            if (((MethodDeclaration) func).getName().getIdentifier().equals(methodName)) {
                testFunc = func;
            }
        }
    }

    private static void setupParameters(String methodName) throws ClassNotFoundException, NoSuchMethodException {
        parameters = ((MethodDeclaration) testFunc).parameters();
        parameterClasses = Utils4TestDriver.getParameterClasses(parameters);
        parameterNames = Utils4TestDriver.getParameterNames(parameters);
//        method = Class.forName(fullyClonedClassName).getDeclaredMethod(methodName, parameterClasses);
    }

    private static void setupCfgTree(ITP4JavaController.Coverage coverage) {
        Block functionBlock = Utils.getFunctionBlock(testFunc);

        cfgBeginNode = new CfgNode();
        cfgBeginNode.setIsBeginCfgNode(true);

        cfgEndNode = new CfgEndBlockNode();
        cfgEndNode.setIsEndCfgNode(true);

        CfgNode block = new CfgBlockNode();
        block.setAst(functionBlock);

        int firstLine = compilationUnit.getLineNumber(functionBlock.getStartPosition());
        block.setLineNumber(1);

        block.setBeforeStatementNode(cfgBeginNode);
        block.setAfterStatementNode(cfgEndNode);

        ASTHelper.generateCFG(block, compilationUnit, firstLine, getCoverageType(coverage));
    }

    private static ASTHelper.Coverage getCoverageType(ITP4JavaController.Coverage coverage) {
        switch (coverage) {
            case STATEMENT:
                return ASTHelper.Coverage.STATEMENT;
            case BRANCH:
                return ASTHelper.Coverage.BRANCH;
            default:
                throw new RuntimeException("Invalid coverage");
        }
    }

    private static String getTotalFunctionCoverageVariableName(MethodDeclaration methodDeclaration, ITP4JavaController.Coverage coverage) {
        StringBuilder result = new StringBuilder(classKey);
        result.append(methodDeclaration.getReturnType2());
        result.append(methodDeclaration.getName());
        for (int i = 0; i < methodDeclaration.parameters().size(); i++) {
            result.append(methodDeclaration.parameters().get(i));
        }
        if (coverage == ITP4JavaController.Coverage.STATEMENT) {
            result.append("TotalStatement");
        } else if (coverage == ITP4JavaController.Coverage.BRANCH) {
            result.append("TotalBranch");
        } else {
            throw new RuntimeException("Invalid Coverage");
        }

        return reformatVariableName(result.toString());
    }

    private static String reformatVariableName(String name) {
        return name.replace(" ", "").replace(".", "")
                .replace("[", "").replace("]", "")
                .replace("<", "").replace(">", "")
                .replace(",", "");
    }
}
