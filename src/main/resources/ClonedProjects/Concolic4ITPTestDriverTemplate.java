import java.io.FileWriter;
//import ClonedProjects.*;

public class TestDriverTemplate {

    public static final String PATH_RESULT = "concreteExecuteResult.txt";
    public static final String PATH_DATA = "runTestDriverData.txt";
    //Start utilities
    private static boolean mark(String statement, boolean isTrueCondition, boolean isFalseCondition) {
       StringBuilder markResult = new StringBuilder();

       markResult.append(statement).append("===");

       markResult.append(isTrueCondition).append("===");

       markResult.append(isFalseCondition).append("---end---");

       writeDataToFile(markResult.toString(), PATH_RESULT, true);

       if (!isTrueCondition && !isFalseCondition)
          return true;

       return !isFalseCondition;

    }
    private static void writeDataToFile(String data, String path, boolean append) {
       try {
          FileWriter writer = new FileWriter(path, append);
          writer.write(data);
          writer.close();
       }
       catch(Exception e) {
          e.printStackTrace();
       }
    }

    //End utilities

    //Start testing method

    %%INSTRUMENTED_TESTING_UNIT_PLACEHOLDER%%

    //End testing method

    public static void main(String[] args) {

        writeDataToFile("", PATH_DATA, false);

        long startRunTestTime = System.nanoTime();

        //Start function calling

        //Object output = getNthPowerSum(8, 8);

        %%UNIT_CALLING_PLACEHOLDER%%

        //Start function calling

        long endRunTestTime = System.nanoTime();

        double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;

        writeDataToFile(runTestDuration + "===" + output, PATH_RESULT, true);

    }

}
