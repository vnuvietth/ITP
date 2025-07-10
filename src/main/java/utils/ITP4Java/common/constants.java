package utils.ITP4Java.common;

public interface constants {
    //General info start
    //https://github.com/varunu28/LeetCode-Java-Solutions
//    String TEST_FOLDER = "\\sample\\Units-From-Leetcode-Java-Solutions.zip";

//    String TEST_FOLDER = "\\sample\\LeetCode-Java-Solutions-master.zip";

    //https://github.com/TheAlgorithms/Java
//    String TEST_FOLDER = "\\sample\\Refactored-TheAlgorithms-Java.zip";

    //https://github.com/kishanrajput23/Java-Projects-Collections
    //https://github.com/oussemamansouri/JAVA-Challenges
    //https://github.com/hbelmiro/100DaysOfJava

    String TEST_FOLDER = "\\sample\\Refactored-TheAlgorithms-Java.zip";

    String INSTRUMENTED_TESTING_UNIT_PLACEHOLDER = "%%INSTRUMENTED_TESTING_UNIT_PLACEHOLDER%%";
    String UNIT_CALLING_PLACEHOLDER = "%%UNIT_CALLING_PLACEHOLDER%%";
    String TEST_DATA_READING_PLACEHOLDER = "%%TEST_DATA_READING_PLACEHOLDER%%";

//    String PROJECT_ROOT_DRIVE = "E:\\IdeaProjects\\NTD-Paper"; // At company
    String PROJECT_ROOT_DRIVE = "D:\\QG25.09\\Code.VietTH\\NTD-Paper"; //At home

    //Test driver folder
    String TEST_DRIVER_FOLDER = "E:\\IdeaProjects\\testDriver";
    String TEST_DRIVER_CLASSPATH_FOLDER = TEST_DRIVER_FOLDER + "\\target\\classes";
    String TEST_DRIVER_LIBRARY_FOLDER = PROJECT_ROOT_DRIVE + "\\src\\main\\resources\\testDriverLibraries";
    String UPLOADED_PROJECT_CLASSPATH = TEST_DRIVER_FOLDER + "\\target\\classes";

    String EXECUTION_RESULT_PATH_PLACEHOLDER = "%%EXECUTION_RESULT_PATH_PLACEHOLDER%%";
    String EXECUTION_RESULT_PATH = TEST_DRIVER_FOLDER.replace("\\", "\\\\") + "\\\\concreteExecuteResult.txt";

    //General info end

    //Test driver template folder
    String TEST_DRIVER_TEMPLATE_FOLDER = PROJECT_ROOT_DRIVE + "\\src\\main\\resources\\DriverTemplates";

    //Concolic 4ITP
    String CONCOLIC_4ITP_TEST_DRIVER_TEMPLATE_PATH = TEST_DRIVER_TEMPLATE_FOLDER + "\\Concolic4ITPTestDriverTemplate.java";
    String CONCOLIC_4ITP_TEST_DRIVER_PATH = TEST_DRIVER_FOLDER + "\\concolic4ITPTestDriver.java";
    String CONCOLIC_4ITP_BUILT_TEST_DRIVER_PATH = "concolic4ITPTestDriver.java";
    String CONCOLIC_4ITP_BUILD_COMMAND = "javac " + " -d \"" + constants.TEST_DRIVER_CLASSPATH_FOLDER + "\" "
            + "\"" + constants.CONCOLIC_4ITP_TEST_DRIVER_PATH + "\"";
    String CONCOLIC_4ITP_RUN_COMMAND = "java " + " -cp \"" + constants.TEST_DRIVER_CLASSPATH_FOLDER + "\" " +
            constants.CONCOLIC_4ITP_BUILT_TEST_DRIVER_PATH;


//    String CONCOLIC_TEST_DRIVER_PATH = "concolic4ITPTestDriver.java";
//    String CONCOLIC_BUILT_TEST_DRIVER_PATH = "concolic4ITPTestDriver";

    //ITP v0
    String ITP_V0_TEST_DATA_FILE_PATH_PLACEHOLDER = "%%TEST_DATA_FILE_PATH%%";
    String ITP_V0_TEST_DATA_FILE_PATH = TEST_DRIVER_FOLDER + "\\TestData.json";
    String ITP_V0_TEST_DATA_FILE_PATH_FOR_TEST_DRIVER = TEST_DRIVER_FOLDER.replace("\\", "\\\\") + "\\\\TestData.json";

    String ITP_V0_TEST_DRIVER_TEMPLATE_PATH = TEST_DRIVER_TEMPLATE_FOLDER + "\\ITP_V0_TestDriverTemplate.java";
    String ITP_V0_TEST_DRIVER_PATH = TEST_DRIVER_FOLDER + "\\ITP_V0_TestDriver.java";
    String ITP_V0_BUILT_TEST_DRIVER_PATH = "ITP_V0_TestDriver.java";
    String ITP_V0_TEST_DRIVER_CLASSPATH_BUILD_PARAMETERS = " -cp \"" + constants.TEST_DRIVER_CLASSPATH_FOLDER + ";" +
            TEST_DRIVER_LIBRARY_FOLDER + "\\json-simple-1.1.1.jar;\" ";

    String ITP_V0_BUILD_COMMAND = "javac " + " -d \"" + constants.TEST_DRIVER_CLASSPATH_FOLDER + "\" "
            + constants.ITP_V0_TEST_DRIVER_CLASSPATH_BUILD_PARAMETERS + " \"" + constants.ITP_V0_TEST_DRIVER_PATH + "\"";

    String ITP_V0_TEST_DRIVER_CLASSPATH_RUN_PARAMETERS = " -cp \"" + constants.TEST_DRIVER_CLASSPATH_FOLDER + ";" +
            TEST_DRIVER_LIBRARY_FOLDER + "\\json-simple-1.1.1.jar;\" ";

    String ITP_V0_RUN_COMMAND = "java " + constants.ITP_V0_TEST_DRIVER_CLASSPATH_RUN_PARAMETERS +
            constants.ITP_V0_BUILT_TEST_DRIVER_PATH;

    int MAX_RANDOM_NUMBER = 1000;
    int MIN_RANDOM_NUMBER = 1;

    int MAX_RANDOM_CHAR = 127;
    int MIN_RANDOM_CHAR = 33;

    //ITP

    String UNIT_CALLING_BLOCK_PLACEHOLDER = "%%UNIT_CALLING_BLOCK_PLACEHOLDER%%";
    String PASSING_PARAMETER_PLACEHOLDER = "%%PASSING_PARAMETER_PLACEHOLDER%%";

    String ITP_TEST_DATA_FILE_PATH_PLACEHOLDER = "%%TEST_DATA_FILE_PATH%%";
    String ITP_TEST_DATA_FILE_PATH = TEST_DRIVER_FOLDER + "\\ITPTestData.json";
    String ITP_TEST_DATA_FILE_PATH_FOR_TEST_DRIVER = TEST_DRIVER_FOLDER.replace("\\", "\\\\") + "\\\\ITPTestData.json";

    String ITP_TEST_DRIVER_TEMPLATE_PATH = TEST_DRIVER_TEMPLATE_FOLDER + "\\ITP_TestDriverTemplate.java";
    String ITP_TEST_DRIVER_PATH = TEST_DRIVER_FOLDER + "\\ITP_TestDriver.java";
    String ITP_BUILT_TEST_DRIVER_PATH = "ITP_TestDriver";
    String ITP_TEST_DRIVER_CLASSPATH_BUILD_PARAMETERS = " -cp \"" + constants.TEST_DRIVER_CLASSPATH_FOLDER + ";" +
            UPLOADED_PROJECT_CLASSPATH + ";" +
            TEST_DRIVER_LIBRARY_FOLDER + "\\json-simple-1.1.1.jar;\" ";

    String ITP_BUILD_COMMAND = "javac " + " -d \"" + constants.TEST_DRIVER_CLASSPATH_FOLDER + "\" "
            + constants.ITP_TEST_DRIVER_CLASSPATH_BUILD_PARAMETERS + " \"" + constants.ITP_TEST_DRIVER_PATH + "\"";

    String ITP_TEST_DRIVER_CLASSPATH_RUN_PARAMETERS = " -cp \"" + constants.TEST_DRIVER_CLASSPATH_FOLDER + ";" +
            TEST_DRIVER_LIBRARY_FOLDER + "\\json-simple-1.1.1.jar;\" ";

    String ITP_RUN_COMMAND = "java " + constants.ITP_TEST_DRIVER_CLASSPATH_RUN_PARAMETERS +
            constants.ITP_BUILT_TEST_DRIVER_PATH;

    String ITP_IMPORT_PLACEHOLDER = "%%IMPORT_PLACEHOLDER%%";

    String ITP_CLONED_PROJECT_CLASSPATH =  TEST_DRIVER_CLASSPATH_FOLDER;

    String ITP_TEST_RESULT_FILEPATH = TEST_DRIVER_FOLDER + "\\ITP_TestResult.txt";

    String ITP_EXCEPTION_UNIT_FILEPATH = TEST_DRIVER_FOLDER + "\\ITP_ExceptionUnit.txt";
}
