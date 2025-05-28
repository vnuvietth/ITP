package utils.ITP4Java.common;

public interface constants {
    //General info start
    String TEST_FOLDER = "\\sample\\Units-From-Leetcode-Java-Solutions.zip";

    String INSTRUMENTED_TESTING_UNIT_PLACEHOLDER = "%%INSTRUMENTED_TESTING_UNIT_PLACEHOLDER%%";
    String UNIT_CALLING_PLACEHOLDER = "%%UNIT_CALLING_PLACEHOLDER%%";
    String TEST_DATA_READING_PLACEHOLDER = "%%TEST_DATA_READING_PLACEHOLDER%%";

    String PROJECT_ROOT_DRIVE = "E:\\IdeaProjects\\NTD-Paper";

    //Test driver folder
    String TEST_DRIVER_FOLDER = "E:\\IdeaProjects\\testDriver";
    String TEST_DRIVER_CLASSPATH_FOLDER = TEST_DRIVER_FOLDER + "\\target";
    String TEST_DRIVER_LIBRARY_FOLDER = PROJECT_ROOT_DRIVE + "\\src\\main\\resources\\testDriverLibraries";

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


}
