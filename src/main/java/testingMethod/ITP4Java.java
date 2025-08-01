package testingMethod;

import controller.ITP4JavaController;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import testingMethod.ITPTestDriver.ITP4JavaTestDriverGenerator;
import testingMethod.ITPTestDriver.ITP4JavaTestDriverRunner;
import utils.common.ITPUtils;
import utils.common.constants;
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
import java.util.*;

import static testingMethod.ITPTestDriver.ITP4JavaTestDriverGenerator.*;
import static testingMethod.ITPTestDriver.ITP4JavaTestDriverRunner.runTestDriver;

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

        writeDataToFile("ITP: Test result for the selected project: " + projectName + "\n", constants.ITP_TEST_RESULT_FILEPATH, false);
        writeDataToFile("Coverage: " + coverage.name() + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);

        generateTestDataForProject(path, coverage, importStatement);

        long endRunTestTime = System.nanoTime();

        double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;
        float usedMem = ((float) totalUsedMem) / tickCount / 1024 / 1024;

        writeDataToFile("***************** o0o *****************\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("runTestDuration: " + (runTestDuration/(double)constants.NUMBER_OF_RUNTIMES) + " (ms)\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("usedMem: " + usedMem + " (MB)\n", constants.ITP_TEST_RESULT_FILEPATH, true);

//        result.setTestingTime(runTestDuration);
//        result.setUsedMemory(usedMem);

//        return result;
        return null;
    }

    //javac  -d "E:\IdeaProjects\testDriver\target"  -cp "C:\Users\HP\Downloads\UploadedCode\1\target\classes;E:\IdeaProjects\NTD-Paper\src\main\resources\testDriverLibraries\json-simple-1.1.1.jar;"  "E:\IdeaProjects\testDriver\ITP_TestDriver.java"
    // Đường dẫn tới class path thì chứa thư mục clonedProject, nhưng không được bao gồm thư mục clonedProject.

    static long totalUsedMemForUnit = 0;
    static long tickCountForUnit = 0;

    private static void generateTestDataForProject(String path, ITP4JavaController.Coverage coverage, StringBuilder importStatement) throws IOException, NoSuchFieldException, ClassNotFoundException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ITP4JavaTestDriverGenerator.generateITPTestDriver(path, coverage, importStatement);

//        System.out.println("Finish generating the project test driver.");

        ITP4JavaTestDriverRunner.buildTestDriver();

//        System.out.println("Finish building the project test driver.");

        List<File> files = Utils.getJavaFiles(path);

        int unitCountForProject = 0;
        int simpleUnitCountForProject = 0;
        int simpleUnitCountForProjectWithException = 0;
        int testDataCountForProject = 0;

        double totalCoverage = 0;

        StringBuilder resultString = new StringBuilder();
        StringBuilder exceptionUnitList = new StringBuilder();

        ArrayList<String> bypassMethodList = ITPUtils.getByPassMethod();

        writeDataToFile("", constants.ITP_EXCEPTION_UNIT_FILEPATH, false);

        for (File file : files) {
            resultString.setLength(0);
            System.out.println("ITP4Java:        //All units of file: " + file.getAbsolutePath().replace("\\", "\\\\") + "\n");


//            if (file.getAbsolutePath().equals("E:\\IdeaProjects\\testDriver\\uploadedProject\\Refactored-TheAlgorithms-Java\\src\\main\\java\\com\\thealgorithms\\audiofilters\\IIRFilter.java") //||
////                    file.getAbsolutePath().equals("E:\\IdeaProjects\\testDriver\\uploadedProject\\Refactored-TheAlgorithms-Java\\src\\main\\java\\com\\thealgorithms\\ciphers\\AffineCipher.java")
//            )
//            {
//                int i = 0;
//                System.out.println(file.getAbsolutePath());
//            }

            //String clonedMethod = createCloneMethod(method, coverage);
            double totalCoverageForFile = 0;
            int simpleUnitCountForFile = 0;
            int unitCountForFile = 0;
            int simpleUnitCountForFileWithException = 0;

            List<ASTNode> methodList = ITP4JavaTestDriverGenerator.getMethodList(file.getAbsolutePath());

            //unitCountForProject += methodList.size();

//            writeDataToFile("======================== o0o ========================\n", constants.ITP_TEST_RESULT_FILEPATH, true);
//            writeDataToFile("Test result for file: " + file.getAbsolutePath() + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);

            resultString.append("======================== o0o ========================\n");
            resultString.append("Test result for file: " + file.getAbsolutePath() + "\n");

            for (ASTNode method : methodList) {

                if (((MethodDeclaration) method).isConstructor() || ((MethodDeclaration) method).getName().getIdentifier().equals("main") ||
                        ITP4JavaTestDriverGenerator.getMethodSignature((MethodDeclaration)method).equals("static void writeDataToFile(String,String,boolean)") ||
                        (ITP4JavaTestDriverGenerator.getMethodSignature((MethodDeclaration)method).equals("static boolean mark(String,boolean,boolean)")) ||
                        !(getMethodAccessModifier((MethodDeclaration)method).equals("public"))
                ) {
                    continue;
                }

                String methodName = ((MethodDeclaration)method).getName().getIdentifier();

//                if (methodName.equals("isThree"))
//                {
//                    System.out.println("methodName = " + methodName);
//                }

                if (bypassMethodList.contains(methodName)) {
                    continue;
                }

                unitCountForFile += 1;
                unitCountForProject += 1;

                System.out.println("Processing method: " + ((MethodDeclaration)method).toString());

                boolean isSimpleUnit = ITP4JavaTestDriverGenerator.isSimpleUnit((MethodDeclaration)method);

                if (isSimpleUnit) {
                    simpleUnitCountForProject += 1;
                    simpleUnitCountForFile += 1;

                    writeDataToFile("", constants.EXECUTION_RESULT_PATH, false);//clear file

                    try {

                        totalUsedMemForUnit = 0;
                        tickCountForUnit = 0;

                        Timer T4Unit = new Timer(true);

                        TimerTask memoryTaskForUnit = new TimerTask() {
                            @Override
                            public void run() {
                                totalUsedMemForUnit += (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
                                tickCountForUnit += 1;
                            }
                        };

                        T4Unit.scheduleAtFixedRate(memoryTaskForUnit, 0, 1); //0 delay and 5 ms tick

                        long startRunTestTimeForUnit = System.nanoTime();

//                        if (methodName.equals("isThree")
////                                ||
////                                methodName.equals("calculatePi") ||
////                                methodName.equals("isOperator")
//                        )
//                        {
//                            System.out.println("Method name = " + methodName);
//                        }

//                        System.out.println("simpleUnitCountForProject = " + simpleUnitCountForProject);
//                        System.out.println("simpleUnitCountForFile = " + simpleUnitCountForFile);

                        ConcolicTestResult[] testResult = new ConcolicTestResult[constants.NUMBER_OF_RUNTIMES];
                        int testDataCountForUnit = 0;

                        resultString.append("\n**********************\n");
                        resultString.append("Test result for unit: " + getMethodSignature((MethodDeclaration) method) + "\n\n");

                        boolean isException = true;

                        double tempTotalCoverage = 0;
                        double tempTotalCoverageForFile = 0;
                        int tempTestDataCountForUnit = 0;

                        for (int i = 0; i < constants.NUMBER_OF_RUNTIMES; i++)
                        {
                            resultString.append("i = " + i + "\n");

                            testResult[i] = startGeneratingForOneUnit(file.getAbsolutePath(), (MethodDeclaration) method, coverage);

                            tempTotalCoverage += testResult[i].getFullCoverage();

                            tempTotalCoverageForFile += testResult[i].getFullCoverage();

                            tempTestDataCountForUnit += testResult[i].getFullTestData().size();

                            resultString.append(testResult[i].getStringResult()).append("\n");
                        }

                        isException = false;

                        if (!isException)
                        {
                            totalCoverage += tempTotalCoverage;

                            totalCoverageForFile += tempTotalCoverageForFile;

                            testDataCountForUnit += tempTestDataCountForUnit;

                            resultString.append("totalCoverage = ").append(totalCoverage).append("\n");
                        }

                        long endRunTestTimeForUnit = System.nanoTime();

                        double runTestDurationForUnit = (endRunTestTimeForUnit - startRunTestTimeForUnit) / 1000000.0/(double)constants.NUMBER_OF_RUNTIMES;
                        float usedMemForUnit = ((float) totalUsedMemForUnit) / tickCountForUnit / 1024 / 1024/(float)constants.NUMBER_OF_RUNTIMES;


//                        System.out.println("testDataCountForUnit = " + testDataCountForUnit/constants.NUMBER_OF_RUNTIMES);

                        testDataCountForProject += testDataCountForUnit/constants.NUMBER_OF_RUNTIMES;

//                        System.out.println("testDataCountForProject = " + testDataCountForProject);

                        resultString.append("testDataCountForUnit: " + (testDataCountForUnit/constants.NUMBER_OF_RUNTIMES) + "\n");
                        resultString.append("runTestDurationForUnit: " + runTestDurationForUnit + " (ms)\n");
                        resultString.append("usedMemForUnit: " + usedMemForUnit + " (MB)\n");
                        resultString.append("***************** o0o *****************\n");
                    }
                    catch (Exception e) {
                        exceptionUnitList.append(methodName).append("\n");
                        System.out.println("exceptionUnitList: ");
                        System.out.println(exceptionUnitList.toString());

                        simpleUnitCountForFileWithException += 1;
                        simpleUnitCountForProjectWithException += 1;

                        System.out.println(e.getMessage());
                        System.out.println(Arrays.toString(e.getStackTrace()));

                        writeDataToFile(methodName + "\n", constants.ITP_EXCEPTION_UNIT_FILEPATH, true);
                    }
                }
                else
                {
                    System.out.println("Not simple unit.");
                }
            }

            if (simpleUnitCountForFile > 0 && simpleUnitCountForFileWithException <= 0)
            {
                writeDataToFile(resultString.toString(), constants.ITP_TEST_RESULT_FILEPATH, true);

//                writeDataToFile("\n=========== o0o ===========\n", constants.ITP_TEST_RESULT_FILEPATH, true);
//
//                writeDataToFile("Test result for unit: " + getMethodSignature(method) + "\n\n", constants.ITP_TEST_RESULT_FILEPATH, true);
//
//                writeTestResultToFile(testResult, constants.ITP_TEST_RESULT_FILEPATH, true);


                writeDataToFile("unitCountForFile: " + unitCountForFile  + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
                writeDataToFile("simpleUnitCountForFile: " + simpleUnitCountForFile  + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
                writeDataToFile("simpleUnitCountForFileWithException: " + simpleUnitCountForFileWithException  + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
                writeDataToFile("totalCoverageForFile: " + (totalCoverageForFile/(double)constants.NUMBER_OF_RUNTIMES)  + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
                writeDataToFile("simpleUnitCountForFile - simpleUnitCountForFileWithException: " + (simpleUnitCountForFile - simpleUnitCountForFileWithException)  + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);

                writeDataToFile("averageCoverageForFile: " + file.getName() + ": " + (totalCoverageForFile /(simpleUnitCountForFile - simpleUnitCountForFileWithException)/(double)constants.NUMBER_OF_RUNTIMES)  + "%\n", constants.ITP_TEST_RESULT_FILEPATH, true);
            }
//            break;

        }

        writeDataToFile("\n ****************** o0o ******************" + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("unitCountForProject: " + unitCountForProject + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("simpleUnitCountForProject: " + simpleUnitCountForProject + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("simpleUnitCountForProjectWithException: " + simpleUnitCountForProjectWithException + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("totalCoverage: " + (totalCoverage/(double)constants.NUMBER_OF_RUNTIMES) + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("simpleUnitCountForProject - simpleUnitCountForProjectWithException: " + (simpleUnitCountForProject - simpleUnitCountForProjectWithException) + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("testDataCountForProject: " + testDataCountForProject + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("Test data/(simpleUnitCountForProject - simpleUnitCountForProjectWithException): " + ((double)(testDataCountForProject/((double)(simpleUnitCountForProject - simpleUnitCountForProjectWithException)))) + "\n", constants.ITP_TEST_RESULT_FILEPATH, true);
        writeDataToFile("totalAverageCoverage: " + (totalCoverage/(double)constants.NUMBER_OF_RUNTIMES /(simpleUnitCountForProject - simpleUnitCountForProjectWithException ))  + "%\n", constants.ITP_TEST_RESULT_FILEPATH, true);


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

        CfgNode uncoveredNode = findUncoverNode(cfgBeginNode, coverage);

        while (uncoveredNode != null) {

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

//        writeDataToFile("\n=========== o0o ===========\n", constants.ITP_TEST_RESULT_FILEPATH, true);
//
//        writeDataToFile("Test result for unit: " + getMethodSignature(method) + "\n\n", constants.ITP_TEST_RESULT_FILEPATH, true);
//
//        writeTestResultToFile(testResult, constants.ITP_TEST_RESULT_FILEPATH, true);

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
