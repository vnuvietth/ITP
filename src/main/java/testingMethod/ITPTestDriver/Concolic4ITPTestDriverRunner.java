package testingMethod.ITPTestDriver;

import utils.common.constants;
import utils.autoUnitTestUtil.dataStructure.MarkedStatement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class Concolic4ITPTestDriverRunner {
    private static double runtime;
    private static String output;
    private Concolic4ITPTestDriverRunner() {
    }


    public static void buildTestDriverConcolic4ITP() throws IOException, InterruptedException {
        String buildCommand = constants.CONCOLIC_4ITP_BUILD_COMMAND;

        System.out.println(buildCommand);

        executeCommand(buildCommand);
    }

    public static void runTestDriverConcolic4ITP() throws IOException, InterruptedException {
        String runCommand = constants.CONCOLIC_4ITP_RUN_COMMAND;

        System.out.println(runCommand);

        executeCommand(runCommand);
    }



    public static List<MarkedStatement> getMarkedStatementConcolic4ITP() throws IOException, InterruptedException {
        return getMarkedStatement();
    }

    public static List<MarkedStatement> runTestDriver() throws IOException, InterruptedException {

//        executeCommand("cd " + constants.CONCOLIC_TEST_DRIVER_FOLDER);
//        executeCommand(constants.CONCOLIC_TEST_DRIVER_ROOT_DRIVE);

        String buildCommand = constants.CONCOLIC_4ITP_BUILD_COMMAND;

//        System.out.println(buildCommand);

        executeCommand(buildCommand);

        String runCommand = constants.CONCOLIC_4ITP_RUN_COMMAND;

//        System.out.println(runCommand);

        executeCommand(runCommand);

        return getMarkedStatement();
    }

    private static void executeCommand(String command) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(command, null, new File(constants.TEST_DRIVER_FOLDER));

        p.waitFor();

        if (p.exitValue() != 0)
        {
            String result = new String(p.getErrorStream().readAllBytes());
            System.out.println("Executing test driver, result = " + result);
        }

    }

    private static List<MarkedStatement> getMarkedStatement() {
        List<MarkedStatement> result = new ArrayList<>();

        String markedData = getDataFromFile(constants.EXECUTION_RESULT_PATH);
        String[] markedStatements = markedData.split("---end---");
        for (int i = 0; i < markedStatements.length; i++) {
            String[] markedStatementData = markedStatements[i].split("===");
            if(i == markedStatements.length - 1) {
                runtime = Double.parseDouble(markedStatementData[0]);
                output = markedStatementData[1];
            } else {
                String statement = markedStatementData[0];
                boolean isTrueConditionalStatement = Boolean.parseBoolean(markedStatementData[1]);
                boolean isFalseConditionalStatement = Boolean.parseBoolean(markedStatementData[2]);
                result.add(new MarkedStatement(statement, isTrueConditionalStatement, isFalseConditionalStatement));
            }
        }
        return result;
    }

    public static double getRuntime() {
        return runtime;
    }

    public static String getOutput() {
        return output;
    }

    private static String getDataFromFile(String path) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            if((line = br.readLine()) != null) {
                result.append(line);
            }
            while ((line = br.readLine()) != null) {
                result.append("\n").append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private static void writeDataToFile(String data, String path) {
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
